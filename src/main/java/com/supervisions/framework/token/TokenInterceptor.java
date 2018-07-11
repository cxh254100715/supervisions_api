package com.supervisions.framework.token;

import com.supervisions.common.utils.ApiUtils;
import com.supervisions.common.utils.MD5Util;
import com.supervisions.framework.web.service.RedisService;
import com.supervisions.modules.mapper.Logininfor;
import com.supervisions.modules.service.ILogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TokenInterceptor
 */
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisService redisService;
    @Autowired
    private ILogininforService logininforService;

    /**
     *
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        String deviceSN = request.getParameter("deviceSN");
        //String timestamp = request.getHeader("timestamp");
        // 随机字符串
        //String nonce = request.getHeader("nonce");
        //String sign = request.getHeader("sign");
        //Assert.isTrue(!StringUtils.isEmpty(token) && !StringUtils.isEmpty(timestamp) && !StringUtils.isEmpty(sign), "参数错误");
        Assert.isTrue(!StringUtils.isEmpty(token)||!StringUtils.isEmpty(deviceSN), "缺少参数");

        // 获取超时时间
        //NotRepeatSubmit notRepeatSubmit = ApiUtils.getNotRepeatSubmit(handler);
        //long expireTime = notRepeatSubmit == null ? 30 * 60 * 1000 : notRepeatSubmit.value();

        // 请求时间间隔
        //long reqeustInterval = System.currentTimeMillis() - Long.valueOf(timestamp);
        //Assert.isTrue(reqeustInterval < expireTime, "请求超时，请重新请求");

        // 校验Token是否存在
        Logininfor logininfor = logininforService.selectLogininforByTokenAndDeviceSN(token,deviceSN);
        Assert.notNull(logininfor, "参数错误");
        Assert.isTrue(logininfor.getToken().equals(token), "token错误");
        // 校验签名(将所有的参数加进来，防止别人篡改参数) 所有参数看参数名升续排序拼接成url
        // 请求参数 + token + timestamp + nonce
        //String signString = ApiUtils.concatSignString(request) + logininfor.getToken() + token + timestamp + nonce;
        //String signature = MD5Util.encode(signString);
        //boolean flag = signature.equals(sign);
        //Assert.isTrue(flag, "签名错误");

        // 拒绝重复调用(第一次访问时存储，过期时间和请求超时时间保持一致), 只有标注不允许重复提交注解的才会校验
        //if (notRepeatSubmit != null) {
        //    boolean exists = redisService.exists("sign:"+sign);
        //    Assert.isTrue(!exists, "请勿重复提交");
        //    redisService.set("sign:"+sign, 0, expireTime/1000);
        //}

        return super.preHandle(request, response, handler);
    }
}
