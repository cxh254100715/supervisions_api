package com.supervisions.modules.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.supervisions.common.constant.WeixinConstant;
import com.supervisions.common.utils.CheckoutUtils;
import com.supervisions.common.utils.RequestUtils;
import com.supervisions.common.utils.WechatMessageUtils;
import com.supervisions.framework.web.mapper.TextMessage;
import com.supervisions.modules.mapper.TenDevice;
import com.supervisions.modules.mapper.TenScanlog;
import com.supervisions.modules.mapper.TenUser;
import com.supervisions.modules.service.ITenDeviceService;
import com.supervisions.modules.service.ITenScanlogService;
import com.supervisions.modules.service.ITenUserService;
import com.supervisions.modules.utils.WeixinAccessTokenTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信接口
 */
@Controller
@RequestMapping(value = "/wx/")
public class WeixinController {

    private static final Logger log = LoggerFactory.getLogger(WeixinController.class);

    @Autowired
    private ITenDeviceService tenDeviceService;
    @Autowired
    private ITenUserService tenUserService;
    @Autowired
    private ITenScanlogService tenScanlogService;

    /**
     * 公众号token验证
     */
    @RequestMapping(value = "checkoutToken", method = { RequestMethod.GET,RequestMethod.POST })
    @ResponseBody
    public void checkoutToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        PrintWriter print = null;
        System.out.println(isGet);
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
    @RequestMapping(value = "loginqr", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> loginqr(String deviceid) {
        Map<String,Object> map = new HashMap<>();
        try {
            // 获取临时二维码url
            String qrcodeUrl = WeixinConstant.QRCODE.replace("TOKEN", WeixinAccessTokenTask.WXAccessToken);
            String json = "{\"expire_seconds\": 604800, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+deviceid+"\"}}}";
            String result = RequestUtils.httpsRequest(qrcodeUrl, "POST", json);
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            String ticket = jsonObject.get("ticket").getAsString();
            System.out.println(ticket);
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
                int count = tenDeviceService.save(tenDevice);
                if(count<1){
                    map.put("qr", "服务器开小差啦，请稍后再试！");
                    map.put("code",2);
                    return map;
                }
            }
            // 返回数据
            map.put("qr", ticketUrl);
            map.put("code",0);
            log.info(map.toString());
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
    @RequestMapping(value = "userinfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> userinfo(String deviceid) {
        try {
            Map<String,Object> map = new HashMap<>();
            TenDevice tenDevice = tenDeviceService.selectDeviceByDeviceId(deviceid);
            if(tenDevice!=null){
                if(tenDevice.getLeftId()!=null){
                    String openId = tenUserService.selectUserById(tenDevice.getLeftId()).getOpenId();
                    String userInfoUrl = WeixinConstant.USERINFO.replace("TOKEN", WeixinAccessTokenTask.WXAccessToken).replace("OPENID", openId);
                    String userInfoResult = RequestUtils.httpsRequest(userInfoUrl, "GET", null);
                    JsonObject jsonObject1 = new JsonParser().parse(userInfoResult).getAsJsonObject();
                    String headimgurl = jsonObject1.get("headimgurl").getAsString();
                    String nickname = jsonObject1.get("nickname").getAsString();
                    map.put("aFace", headimgurl);
                    map.put("aNickName", nickname);
                }
                if(tenDevice.getRightId()!=null){
                    String openId = tenUserService.selectUserById(tenDevice.getRightId()).getOpenId();
                    String userInfoUrl = WeixinConstant.USERINFO.replace("TOKEN", WeixinAccessTokenTask.WXAccessToken).replace("OPENID", openId);
                    String userInfoResult = RequestUtils.httpsRequest(userInfoUrl, "GET", null);
                    JsonObject jsonObject1 = new JsonParser().parse(userInfoResult).getAsJsonObject();
                    String headimgurl = jsonObject1.get("headimgurl").getAsString();
                    String nickname = jsonObject1.get("nickname").getAsString();
                    map.put("bFace", headimgurl);
                    map.put("bNickName", nickname);
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
        String userInfoUrl = WeixinConstant.USERINFO.replace("TOKEN", WeixinAccessTokenTask.WXAccessToken).replace("OPENID", fromUserName);
        String userInfoResult = RequestUtils.httpsRequest(userInfoUrl, "GET", null);
        JsonObject jsonObject1 = new JsonParser().parse(userInfoResult).getAsJsonObject();
        String nickname = jsonObject1.get("nickname").getAsString();
        System.out.println(nickname);


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
        if(tenDevice.getLeftId()!=null&&tenDevice.getRightId()!=null){
            content = "服务器开小差啦，请稍后再试!";
            System.out.println("服务器开小差啦，请稍后再试!");
        }
        switch (type) {
            case "A":
                String BOpenId = "";
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
                    int count = tenDeviceService.save(tenDevice);
                    if (count < 1)
                    {
                        content = "服务器开小差啦，请稍后再试！";
                    }
                    // 扫码记录
                    TenScanlog tenScanlog = new TenScanlog();
                    tenScanlog.setDeviceId(deviceidInput);
                    tenScanlog.setLeftId(tenUserId);
                    tenScanlogService.save(tenScanlog);
                }
                break;
            case "B":
                String AOpenId = "";
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
                    int count = tenDeviceService.save(tenDevice);
                    if (count < 1)
                    {
                        content = "服务器开小差啦，请稍后再试！";
                    }
                    // 扫码记录
                    TenScanlog tenScanlog = new TenScanlog();
                    tenScanlog.setDeviceId(deviceidInput);
                    tenScanlog.setRightId(tenUserId);
                    tenScanlogService.save(tenScanlog);
                }
                break;
            default:
                content = "参数错误!";
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

}
