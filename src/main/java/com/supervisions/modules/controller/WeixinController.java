package com.supervisions.modules.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.supervisions.common.constant.WeixinConstant;
import com.supervisions.common.utils.CheckoutUtils;
import com.supervisions.common.utils.RequestUtils;
import com.supervisions.common.utils.StringUtils;
import com.supervisions.common.utils.WechatMessageUtils;
import com.supervisions.framework.web.mapper.AccessToken;
import com.supervisions.framework.web.mapper.Result;
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
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
/*    @ApiIgnore
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
    }*/

    /**
     * 获取带参数二维码
     */
/*    @ApiOperation(value="获取带参数二维码", notes="根据设备id获取二维码")
    @ApiImplicitParam(name = "deviceid", value = "设备id(末尾加-A或-B区分左右)", required = true, dataType = "String", paramType = "query")
    @GetMapping(value = "/loginqr")
    @Transactional(rollbackFor = Exception.class)
    public Result loginqr(@RequestParam(required = true) String deviceid) {
        Result result = null;
        try {
            if(!StringUtils.isEmpty(deviceid)&&deviceid.length()>2){
                String str = deviceid.substring(deviceid.length()-2);
                char[] ar = str.toCharArray();
                if(String.valueOf(ar[0]).equals("-")&&(String.valueOf(ar[1]).equals("A")||String.valueOf(ar[1]).equals("B"))){

                }else {
                    return result.errorResult("参数错误！");
                }
            }else {
                return result.errorResult("参数错误！");
            }

            // 获取临时二维码url
            String qrcodeUrl = WeixinConstant.QRCODE.replace("TOKEN", getNewAccessToken());
            String json = "{\"expire_seconds\": 86400, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+deviceid+"\"}}}";
            String res = RequestUtils.httpsRequest(qrcodeUrl, "POST", json);
            log.info("deviceId："+ deviceid + "  " + res);
            JsonObject jsonObject = new JsonParser().parse(res).getAsJsonObject();
            String ticket = jsonObject.get("ticket").getAsString();
            String ticketUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;

            String deviceidInput = deviceid.substring(0,deviceid.length()-2);
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
            return result.successResult("{\"qr\":\""+ticketUrl+"\"}");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return result.errorResult();
        }
    }*/

    /**
     * 返回用户信息
     */
/*    @ApiOperation(value="获取微信用户信息", notes="根据设备id获取登录的微信用户信息")
    @ApiImplicitParam(name = "deviceid", value = "设备id", required = true, dataType = "String", paramType = "query")
    @GetMapping(value = "/userinfo")
    public Result userinfo(@RequestParam(required = true) String deviceid) {
        Result result = null;
        Map<String,Object> map = new HashMap<>();
        try {
            TenDevice tenDevice = tenDeviceService.selectDeviceByDeviceId(deviceid);
            if(tenDevice!=null){
                if(tenDevice.getLeftId()!=null){
                    TenUser user = tenUserService.selectUserById(tenDevice.getLeftId());
                    map.put("aId",user.getId());
                    map.put("aFace", user.getHeadimgurl());
                    map.put("aNickName", user.getNickname());
                }
                if(tenDevice.getRightId()!=null){
                    TenUser user = tenUserService.selectUserById(tenDevice.getRightId());
                    map.put("bId",user.getId());
                    map.put("bFace", user.getHeadimgurl());
                    map.put("bNickName", user.getNickname());
                }
                return result.successResult(map);
            }
            return result.errorResult();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return result.errorResult();
        }
    }*/

    /**
     * 返回微信xml格式消息
     */
/*    @Transactional(rollbackFor = Exception.class)
    public String processRequest(HttpServletRequest request) {
        Map<String, String> map = WechatMessageUtils.xmlToMap(request);
        log.info(map.toString());
        // 默认回复一个"success"
        String responseMessage = "success";
        // 发送方帐号（一个OpenID）
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        // 消息类型
        String msgType = map.get("MsgType");
        if(!msgType.equals("event")){
            return responseMessage;
        }
        // 设备唯一编号
        String eventKey = map.get("EventKey").replace("qrscene_", "");

        String content = "";
        // 返回公众号消息
        TextMessage textMessage = new TextMessage();
        textMessage.setMsgType(WechatMessageUtils.MESSAGE_TEXT);
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(System.currentTimeMillis()/1000);

        if(!StringUtils.isEmpty(eventKey)&&eventKey.length()>2){
            String str = eventKey.substring(eventKey.length()-2);
            char[] ar = str.toCharArray();
            if(String.valueOf(ar[0]).equals("-")&&(String.valueOf(ar[1]).equals("A")||String.valueOf(ar[1]).equals("B"))){

            }else {
                content = "参数错误";
                textMessage.setContent(content);
                responseMessage = WechatMessageUtils.textMessageToXml(textMessage);
                log.info(responseMessage);
                return responseMessage;
            }
        }else {
            content = "参数错误";
            textMessage.setContent(content);
            responseMessage = WechatMessageUtils.textMessageToXml(textMessage);
            log.info(responseMessage);
            return responseMessage;
        }

        Long tenUserId = 0l;
        TenUser tenUser = tenUserService.selectUserByOpenId(fromUserName);
        if(tenUser == null){
            // 获取微信用户
            String userInfoUrl = WeixinConstant.USERINFO.replace("TOKEN", getNewAccessToken()).replace("OPENID", fromUserName);
            String userInfoResult = RequestUtils.httpsRequest(userInfoUrl, "GET", null);
            log.info(userInfoResult);
            JsonObject jsonObject = new JsonParser().parse(userInfoResult).getAsJsonObject();
            if(jsonObject.get("subscribe").toString().equals("0")){
                return responseMessage;
            }
            TenUser tenUser1 = new TenUser();
            tenUser1.setOpenId(fromUserName);
            tenUser1.setNickname(jsonObject.get("nickname").getAsString());
            tenUser1.setHeadimgurl(jsonObject.get("headimgurl").getAsString());
            tenUser1.setSex(Integer.valueOf(jsonObject.get("sex").getAsString()));
            String city = jsonObject.get("city").getAsString();
            String province = jsonObject.get("province").getAsString();
            String country = jsonObject.get("country").getAsString();
            if(StringUtils.isNotEmpty(city)&&StringUtils.isNotEmpty(province)&&StringUtils.isNotEmpty(country)){
                String address = "{\"country\": \""+jsonObject.get("country").getAsString()+"\", \"province\": \""+jsonObject.get("province").getAsString()+"\", \"city\": \""+jsonObject.get("city").getAsString()+"\"}";
                tenUser1.setAddress(address);
            }
            tenUserService.save(tenUser1);
            tenUserId = tenUser1.getId();
            content = ""+tenUser1.getNickname()+",您已成功登陆设备:"+eventKey+"";
        }else{
            tenUserId = tenUser.getId();
            content = ""+tenUser.getNickname()+",您已成功登陆设备:"+eventKey+"";
        }

        String deviceidInput = eventKey.substring(0,eventKey.length()-2);
        String type = eventKey.substring(eventKey.length()-1); // 值 A或B

        // 判断AB是否重复
        TenDevice tenDevice = tenDeviceService.selectDeviceByDeviceId(deviceidInput);
        if(tenDevice==null){
            content = "设备不存在!";
            textMessage.setContent(content);
            responseMessage = WechatMessageUtils.textMessageToXml(textMessage);
            log.info(responseMessage);
            return responseMessage;
        }
        switch (type) {
            case "A":
                if(tenDevice.getLeftId() != null){
                    content = "此设备已登录!";
                    break;
                }
                if (tenDevice.getRightId()!=null&&tenDevice.getRightId().equals(tenUserId)){
                    content = "您已扫码,请不要重复扫码!";
                }else{
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
                if(tenDevice.getRightId() != null){
                    content = "此设备已登录!";
                    break;
                }
                if (tenDevice.getLeftId()!=null&&tenDevice.getLeftId().equals(tenUserId)){
                    content = "您已扫码,请不要重复扫码!";
                }else{
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

        textMessage.setContent(content);
        responseMessage = WechatMessageUtils.textMessageToXml(textMessage);
        log.info(responseMessage);
        return responseMessage;
    }*/

    /**
     * 模拟扫码接口
     */
/*    @ApiOperation(value="模拟扫码接口", notes="模拟扫码接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "FromUserName", value = "用户openId", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "EventKey", value = "设备id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "MsgType", value = "消息类型", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/processRequestTest")
    @Transactional(rollbackFor = Exception.class)
    public String processRequestTest(HttpServletRequest request,@RequestParam(required = true) String FromUserName,@RequestParam(required = true) String EventKey,@RequestParam(required = true) String MsgType) {
        //Map<String, String> map = WechatMessageUtils.xmlToMap(request);
        //log.info(map.toString());

        // 默认回复一个"success"
        String responseMessage = "success";
        // 发送方帐号（一个OpenID）
        String fromUserName = FromUserName;
        // 开发者微信号
        String toUserName = "gh_41a513215ab8";
        // 消息类型
        String msgType = MsgType;
        if(!msgType.equals("event")){
            return responseMessage;
        }
        // 设备唯一编号
        String eventKey = EventKey;

        String content = "";
        // 返回公众号消息
        TextMessage textMessage = new TextMessage();
        textMessage.setMsgType(WechatMessageUtils.MESSAGE_TEXT);
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(System.currentTimeMillis()/1000);

        if(!StringUtils.isEmpty(eventKey)&&eventKey.length()>2){
            String str = eventKey.substring(eventKey.length()-2);
            char[] ar = str.toCharArray();
            if(String.valueOf(ar[0]).equals("-")&&(String.valueOf(ar[1]).equals("A")||String.valueOf(ar[1]).equals("B"))){

            }else {
                content = "参数错误";
                textMessage.setContent(content);
                responseMessage = WechatMessageUtils.textMessageToXml(textMessage);
                log.info(responseMessage);
                return responseMessage;
            }
        }else {
            content = "参数错误";
            textMessage.setContent(content);
            responseMessage = WechatMessageUtils.textMessageToXml(textMessage);
            log.info(responseMessage);
            return responseMessage;
        }

        Long tenUserId = 0l;
        TenUser tenUser = tenUserService.selectUserByOpenId(fromUserName);
        if(tenUser == null){
            // 获取微信用户
            String userInfoUrl = WeixinConstant.USERINFO.replace("TOKEN", getNewAccessToken()).replace("OPENID", fromUserName);
            String userInfoResult = RequestUtils.httpsRequest(userInfoUrl, "GET", null);
            log.info(userInfoResult);
            JsonObject jsonObject = new JsonParser().parse(userInfoResult).getAsJsonObject();
            if(jsonObject.get("subscribe").toString().equals("0")){
                return responseMessage;
            }
            TenUser tenUser1 = new TenUser();
            tenUser1.setOpenId(fromUserName);
            tenUser1.setNickname(jsonObject.get("nickname").getAsString());
            tenUser1.setHeadimgurl(jsonObject.get("headimgurl").getAsString());
            tenUser1.setSex(Integer.valueOf(jsonObject.get("sex").getAsString()));
            String city = jsonObject.get("city").getAsString();
            String province = jsonObject.get("province").getAsString();
            String country = jsonObject.get("country").getAsString();
            if(StringUtils.isNotEmpty(city)&&StringUtils.isNotEmpty(province)&&StringUtils.isNotEmpty(country)){
                String address = "{\"country\": \""+jsonObject.get("country").getAsString()+"\", \"province\": \""+jsonObject.get("province").getAsString()+"\", \"city\": \""+jsonObject.get("city").getAsString()+"\"}";
                tenUser1.setAddress(address);
            }
            tenUserService.save(tenUser1);
            tenUserId = tenUser1.getId();
            content = ""+tenUser1.getNickname()+",您已成功登陆设备:"+eventKey+"";
        }else{
            tenUserId = tenUser.getId();
            content = ""+tenUser.getNickname()+",您已成功登陆设备:"+eventKey+"";
        }

        String deviceidInput = eventKey.substring(0,eventKey.length()-2);
        String type = eventKey.substring(eventKey.length()-1); // 值 A或B

        // 判断AB是否重复
        TenDevice tenDevice = tenDeviceService.selectDeviceByDeviceId(deviceidInput);
        if(tenDevice==null){
            content = "设备不存在!";
            textMessage.setContent(content);
            responseMessage = WechatMessageUtils.textMessageToXml(textMessage);
            log.info(responseMessage);
            return responseMessage;
        }
        switch (type) {
            case "A":
                if(tenDevice.getLeftId() != null){
                    content = "此设备已登录!";
                    break;
                }
                if (tenDevice.getRightId()!=null&&tenDevice.getRightId().equals(tenUserId)){
                    content = "您已扫码,请不要重复扫码!";
                }else{
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
                if(tenDevice.getRightId() != null){
                    content = "此设备已登录!";
                    break;
                }
                if (tenDevice.getLeftId()!=null&&tenDevice.getLeftId().equals(tenUserId)){
                    content = "您已扫码,请不要重复扫码!";
                }else{
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

        textMessage.setContent(content);
        responseMessage = WechatMessageUtils.textMessageToXml(textMessage);
        log.info(responseMessage);
        return responseMessage;
    }*/

    @Autowired
    private WeixinCommenUtil weixinCommenUtil;

    /**
     * 获取token(调用微信一个无上限接口确保token不过期)
     */
/*    public String getNewAccessToken() {
        String access_token = "";
        if(redisService.exists("accessToken")){
            access_token = redisService.get("accessToken").toString();
        }else{
            access_token = weixinCommenUtil.getToken(WeixinConstant.APPID, WeixinConstant.APPSECRET).getToken();
            redisService.set("accessToken", access_token,7260l);
        }
        String url = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=" + access_token;  // 获取微信服务器IP地址接口
        String result = RequestUtils.httpsRequest(url, "GET", null);
        if(result.indexOf("errcode")!=-1){
            AccessToken accessToken = weixinCommenUtil.getToken(WeixinConstant.APPID, WeixinConstant.APPSECRET);
            redisService.set("accessToken", accessToken.getToken(),7260l);
            log.info("获取到的微信accessToken为"+accessToken.getToken());
            access_token =accessToken.getToken();
        }
        return access_token;
    }*/

}
