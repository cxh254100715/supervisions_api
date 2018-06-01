package com.supervisions.modules.utils;

import com.supervisions.common.constant.WeixinConstant;
import com.supervisions.common.utils.RequestUtils;
import com.supervisions.framework.web.mapper.AccessToken;
import com.supervisions.modules.controller.WeixinController;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 微信公用和常用的操作类
 */
@Component
public class WeixinCommenUtil
{

    private static final Logger logger = LoggerFactory.getLogger(WeixinController.class);

    // 获取access_token的接口地址（GET） 限2000（次/天）
    private static String url = WeixinConstant.ACCESS_TOKEN;

    public AccessToken getToken(String appid, String appsecret)
    {
        AccessToken token = null;
        //访问微信服务器的地址
        String requestUrl = url.replace("APPID", appid).replace("APPSECRET", appsecret);
        //创建一个json对象
        String result = RequestUtils.httpsRequest(requestUrl, "GET", null);
        JSONObject json = JSONObject.fromObject(result);
        System.out.println("获取到的json格式的Token为:" + json);
        //判断json是否为空
        if (json != null)
        {
            try
            {
                token = new AccessToken();
                //将获取的access_token放入accessToken对象中
                token.setToken(json.getString("access_token"));
                //将获取的expires_in时间放入accessToken对象中
                token.setExpiresIn(json.getInt("expires_in"));
            }
            catch (Exception e)
            {
                token = null;
                e.printStackTrace();
                System.out.println("系统出错了！");
            }
        }
        else
        {
            token = null;
            // 获取token失败
            logger.error("获取token失败 errcode:{} errmsg:{}");
        }
        return token;
    }
}
