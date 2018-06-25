package com.supervisions.modules.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.supervisions.common.constant.WeixinConstant;
import com.supervisions.common.utils.CheckoutUtils;
import com.supervisions.common.utils.RequestUtils;
import com.supervisions.common.utils.StringUtils;
import com.supervisions.common.utils.WechatMessageUtils;
import com.supervisions.framework.web.mapper.AccessToken;
import com.supervisions.framework.web.mapper.TextMessage;
import com.supervisions.framework.web.service.RedisService;
import com.supervisions.modules.mapper.TenDevice;
import com.supervisions.modules.mapper.TenScanlog;
import com.supervisions.modules.mapper.TenUser;
import com.supervisions.modules.service.ITenDeviceService;
import com.supervisions.modules.service.ITenScanlogService;
import com.supervisions.modules.service.ITenUserService;
import com.supervisions.modules.utils.WeixinCommenUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信接口
 */
@RestController
@RequestMapping("/wx")
public class WeixinController {

    private static final Logger log = LoggerFactory.getLogger(WeixinController.class);

    @Autowired
    private ITenDeviceService tenDeviceService;
    @Autowired
    private ITenUserService tenUserService;
    @Autowired
    private ITenScanlogService tenScanlogService;
    @Autowired
    private RedisService redisService;

    /**
     * 公众号token验证
     */
    @ApiIgnore//使用该注解忽略这个API
    @RequestMapping(value = "/checkoutToken", method = { RequestMethod.GET,RequestMethod.POST })
    public void checkoutToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        PrintWriter print = null;
        if (isGet) {
            // 微信加密签名
            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");
            // 随机字符串
            String echostr = request.getParameter("echostr");
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            if (signature != null && CheckoutUtils.checkSignature(signature, timestamp, nonce)) {
                try {
                    print = response.getWriter();
                    print.write(echostr);
                    print.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            String responseMessage = processRequest(request);
            print = response.getWriter();
            print.write(responseMessage);
            print.flush();
        }
    }

    /**
     * 获取带参数二维码
     */
    @ApiOperation(value="获取带参数二维码", notes="根据设备id获取二维码")
    @ApiImplicitParam(name = "deviceid", value = "设备id", required = true, dataType = "String", paramType = "query")
    @GetMapping(value = "/loginqr")
    public Map<String,Object> loginqr(@RequestParam(required = true) String deviceid) {
        Map<String,Object> map = new HashMap<>();
        try {
            if(!StringUtils.isEmpty(deviceid)&&deviceid.indexOf("-")==-1){
                map.put("qr", "参数错误！");
                map.put("code",2);
                return map;
            }

            // 获取临时二维码url
            String qrcodeUrl = WeixinConstant.QRCODE.replace("TOKEN", getNewAccessToken());
            String json = "{\"expire_seconds\": 86400, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+deviceid+"\"}}}";
            String result = RequestUtils.httpsRequest(qrcodeUrl, "POST", json);
            log.info("deviceId："+ deviceid + "  " + result);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            String ticket = jsonObject.get("ticket").getAsString();
            String ticketUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;

            String[] strs = deviceid.split("-");
            String deviceidInput = strs[0];
            // deviceid是否存在
            TenDevice tenDevice = tenDeviceService.selectDeviceByDeviceId(deviceidInput);
            if(tenDevice==null){
                TenDevice tenDevice1 = new TenDevice();
                tenDevice1.setDeviceId(deviceidInput);
                tenDeviceService.save(tenDevice1);
            }else{
                tenDevice.setLeftId(null);
                tenDevice.setRightId(null);
                tenDeviceService.save(tenDevice);
            }
            // 返回数据
            map.put("qr", ticketUrl);
            map.put("code",0);
            return map;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            map.put("qr", "服务器开小差啦，请稍后再试！");
            map.put("code",2);
            return map;
        }
    }

    /**
     * 返回用户信息
     */
    @ApiOperation(value="获取微信用户信息", notes="根据设备id获取登录的微信用户信息")
    @ApiImplicitParam(name = "deviceid", value = "设备id", required = true, dataType = "String", paramType = "query")
    @GetMapping(value = "/userinfo")
    public Map<String,Object> userinfo(@RequestParam(required = true) String deviceid) {
        try {
            Map<String,Object> map = new HashMap<>();
            TenDevice tenDevice = tenDeviceService.selectDeviceByDeviceId(deviceid);
            if(tenDevice!=null){
                if(tenDevice.getLeftId()!=null){
                    String openId = tenUserService.selectUserById(tenDevice.getLeftId()).getOpenId();
                    String headimgurl = "";
                    String nickname = "";
                    if(redisService.exists(openId)){
                        String json = redisService.get(openId).toString();
                        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                        headimgurl = jsonObject.get("headimgurl").getAsString();
                        nickname = jsonObject.get("nickname").getAsString();
                        map.put("aFace", headimgurl);
                        map.put("aNickName", nickname);
                    }else{
                        String userInfoUrl = WeixinConstant.USERINFO.replace("TOKEN", getNewAccessToken()).replace("OPENID", openId);
                        String userInfoResult = RequestUtils.httpsRequest(userInfoUrl, "GET", null);
                        log.info(userInfoResult);
                        JsonObject jsonObject = new JsonParser().parse(userInfoResult).getAsJsonObject();
                        if(!jsonObject.get("subscribe").toString().equals("0")){
                            headimgurl = jsonObject.get("headimgurl").getAsString();
                            nickname = jsonObject.get("nickname").getAsString();
                            String json = "{\"headimgurl\":\""+headimgurl+"\",\"nickname\":\""+nickname+"\"}";
                            redisService.set(openId,json,7200l);
                            map.put("aFace", headimgurl);
                            map.put("aNickName", nickname);
                        }
                    }
                }
                if(tenDevice.getRightId()!=null){
                    String openId = tenUserService.selectUserById(tenDevice.getRightId()).getOpenId();
                    String headimgurl = "";
                    String nickname = "";
                    if(redisService.exists(openId)){
                        String json = redisService.get(openId).toString();
                        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                        headimgurl = jsonObject.get("headimgurl").getAsString();
                        nickname = jsonObject.get("nickname").getAsString();
                        map.put("bFace", headimgurl);
                        map.put("bNickName", nickname);
                    }else{
                        String userInfoUrl = WeixinConstant.USERINFO.replace("TOKEN", getNewAccessToken()).replace("OPENID", openId);
                        String userInfoResult = RequestUtils.httpsRequest(userInfoUrl, "GET", null);
                        log.info(userInfoResult);
                        JsonObject jsonObject = new JsonParser().parse(userInfoResult).getAsJsonObject();
                        if(!jsonObject.get("subscribe").toString().equals("0")){
                            headimgurl = jsonObject.get("headimgurl").getAsString();
                            nickname = jsonObject.get("nickname").getAsString();
                            String json = "{\"headimgurl\":\"" + headimgurl + "\",\"nickname\":\"" + nickname + "\"}";
                            redisService.set(openId, json, 7200l);
                            map.put("bFace", headimgurl);
                            map.put("bNickName", nickname);
                        }
                    }
                }
                map.put("code",0);
                return map;
            }else{
                map.put("msg", "服务器开小差啦，请稍后再试！");
                map.put("code",2);
                return map;
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            Map<String,Object> map = new HashMap<>();
            map.put("msg", "服务器开小差啦，请稍后再试！");
            map.put("code",2);
            return map;
        }
    }

    /**
     * 返回微信xml格式消息
     */
    public String processRequest(HttpServletRequest request) {
        Map<String, String> map = WechatMessageUtils.xmlToMap(request);
        log.info(map.toString());
        // 发送方帐号（一个OpenID）
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        // 消息类型
        String msgType = map.get("MsgType");
        String eventKey = map.get("EventKey").replace("qrscene_", "");
        // 默认回复一个"success"
        String responseMessage = "success";

        // 获取微信昵称
        String userInfoUrl = WeixinConstant.USERINFO.replace("TOKEN", getNewAccessToken()).replace("OPENID", fromUserName);
        String userInfoResult = RequestUtils.httpsRequest(userInfoUrl, "GET", null);
        log.info(userInfoResult);
        JsonObject jsonObject1 = new JsonParser().parse(userInfoResult).getAsJsonObject();
        if(jsonObject1.get("subscribe").toString().equals("0")){
            return responseMessage;
        }
        String nickname = jsonObject1.get("nickname").getAsString();

        String[] strs = eventKey.split("-");
        String deviceidInput = strs[0];
        String type = strs[1]; // 值 A或B
        String openId = fromUserName; // 扫码者openid
        String content = ""+nickname+",您已成功登陆设备:"+eventKey+"";

        // 保存微信用户
        Long tenUserId = 0l;
        TenUser tenUser = tenUserService.selectUserByOpenId(openId);
        if(tenUser == null){
            TenUser tenUser1 = new TenUser();
            tenUser1.setOpenId(openId);
            tenUserService.save(tenUser1);
            tenUserId = tenUser1.getId();
        }else{
            tenUserId = tenUser.getId();
        }

        // 判断AB是否重复
        TenDevice tenDevice = tenDeviceService.selectDeviceByDeviceId(deviceidInput);
        if(tenDevice==null){
            content = "服务器开小差啦，请稍后再试!";
        }
        if(tenDevice.getLeftId()!=null && tenDevice.getRightId()!=null){
            content = "此设备已登录!";
        }else{
            switch (type) {
                case "A":
                    String BOpenId = "";
                    if(tenDevice.getLeftId() != null){
                        content = "此设备已登录!";
                        break;
                    }
                    if (tenDevice.getRightId() != null)
                    {
                        BOpenId = tenUserService.selectUserById(tenDevice.getRightId()).getOpenId();
                    }
                    if (openId.equals(BOpenId))
                    {
                        content = "您已扫码,请不要重复扫码!";
                    }
                    else
                    {
                        tenDevice.setLeftId(tenUserId);
                        tenDeviceService.save(tenDevice);
                        // 扫码记录
                        TenScanlog tenScanlog = new TenScanlog();
                        tenScanlog.setDeviceId(tenDevice.getId());
                        tenScanlog.setLeftId(tenUserId);
                        tenScanlogService.save(tenScanlog);
                    }
                    break;
                case "B":
                    String AOpenId = "";
                    if(tenDevice.getRightId() != null){
                        content = "此设备已登录!";
                        break;
                    }
                    if (tenDevice.getLeftId() != null)
                    {
                        AOpenId = tenUserService.selectUserById(tenDevice.getLeftId()).getOpenId();
                    }
                    if (openId.equals(AOpenId))
                    {
                        content = "您已扫码,请不要重复扫码!";
                    }
                    else
                    {
                        tenDevice.setRightId(tenUserId);
                        tenDeviceService.save(tenDevice);
                        // 扫码记录
                        TenScanlog tenScanlog = new TenScanlog();
                        tenScanlog.setDeviceId(tenDevice.getId());
                        tenScanlog.setRightId(tenUserId);
                        tenScanlogService.save(tenScanlog);
                    }
                    break;
                default:
                    content = "参数错误!";
            }
        }

        // 返回公众号消息
        TextMessage textMessage = new TextMessage();
        textMessage.setMsgType(WechatMessageUtils.MESSAGE_TEXT);
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(System.currentTimeMillis()/1000);
        textMessage.setContent(content);
        responseMessage = WechatMessageUtils.textMessageToXml(textMessage);
        log.info(responseMessage);
        return responseMessage;
    }

    @Autowired
    private WeixinCommenUtil weixinCommenUtil;

    /**
     * 获取token(调用微信一个无上限接口确保token不过期)
     */
    public String getNewAccessToken() {
        String access_token = "";
        if(redisService.exists("accessToken")){
            access_token = redisService.get("accessToken").toString();
        }else{
            access_token = weixinCommenUtil.getToken(WeixinConstant.APPID, WeixinConstant.APPSECRET).getToken();
            redisService.set("accessToken", access_token,3660l);
        }
        String url = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=" + access_token;  // 获取微信服务器IP地址接口
        String result = RequestUtils.httpsRequest(url, "GET", null);
        //log.info(result);
        if(result.indexOf("errcode")!=-1){
            AccessToken accessToken = weixinCommenUtil.getToken(WeixinConstant.APPID, WeixinConstant.APPSECRET);
            redisService.set("accessToken", accessToken.getToken(),3660l);
            log.info("获取到的微信accessToken为"+accessToken.getToken());
            access_token =accessToken.getToken();
        }
        return access_token;
    }

}
