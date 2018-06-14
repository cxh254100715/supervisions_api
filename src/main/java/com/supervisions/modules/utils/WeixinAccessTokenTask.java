package com.supervisions.modules.utils;

import com.supervisions.common.constant.WeixinConstant;
import com.supervisions.framework.web.mapper.AccessToken;
import com.supervisions.framework.web.service.RedisService;
import com.supervisions.modules.controller.WeixinController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时获取微信accessToken（7100s）保存到内存中
 */
@Component
public class WeixinAccessTokenTask {

    private static final Logger logger = LoggerFactory.getLogger(WeixinController.class);

    @Autowired
    private WeixinCommenUtil weixinCommenUtil;

    @Autowired
    private RedisService redisService;

    //public static String WXAccessToken = null;

    // 第一次延迟1秒执行，当执行完后7100秒再执行7000*1000
    @Scheduled(fixedDelay = 3600*1000)
    public void getWeixinAccessToken(){
        try {
            AccessToken accessToken = weixinCommenUtil.getToken(WeixinConstant.APPID, WeixinConstant.APPSECRET);
            redisService.set("accessToken", accessToken.getToken(),3660l);
            logger.info("获取到的微信accessToken为"+accessToken.getToken());
        } catch (Exception e) {
            logger.error("获取微信accessToken出错，信息如下");
            e.printStackTrace();
            // 此处可能陷入死循环
            //this.getWeixinAccessToken();
        }
    }
}
