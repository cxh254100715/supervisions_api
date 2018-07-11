package com.supervisions.modules.controller;

import com.supervisions.common.utils.MD5Util;
import com.supervisions.common.utils.RSAUtils;
import com.supervisions.framework.token.*;
import com.supervisions.framework.web.mapper.AccessToken;
import com.supervisions.framework.web.mapper.Result;
import com.supervisions.framework.web.service.RedisService;
import com.supervisions.modules.dao.ILogininforDao;
import com.supervisions.modules.mapper.Deviceinfo;
import com.supervisions.modules.mapper.Logininfor;
import com.supervisions.modules.mapper.TenUser;
import com.supervisions.modules.service.IDeviceinfoService;
import com.supervisions.modules.service.ILogininforService;
import com.supervisions.modules.service.ITenDeviceService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

/**
 * 访问令牌 token
 */
@Slf4j
@RestController
@RequestMapping("/token")
public class TokenController {

    //private static final Logger log = LoggerFactory.getLogger(TokenController.class);
    @Autowired
    private IDeviceinfoService deviceinfoService;
    @Autowired
    private ILogininforService logininforService;

    /**
     * API Token
     */
    /*@ApiOperation(value="获取token", notes="获取token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceSN", value = "序列号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "timestamp", value = "时间戳", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "sign", value = "签名", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping("/api_token")
    public Result apiToken(String deviceSN, @RequestHeader("timestamp") String timestamp, @RequestHeader("sign") String sign) throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        try
        {
            Assert.isTrue(!StringUtils.isEmpty(deviceSN) && !StringUtils.isEmpty(timestamp) && !StringUtils.isEmpty(sign), "参数错误");

            long reqeustInterval = System.currentTimeMillis() - Long.valueOf(timestamp);
            Assert.isTrue(reqeustInterval < 30 * 60 * 1000, "请求过期，请重新请求");

            // 根据appId查询数据库获取appSecret
            List<Deviceinfo> deviceinfos = deviceinfoService.selectDeviceinfoBydeviceSn(deviceSN);
            Assert.isTrue(deviceinfos.size()==1&&deviceinfos.get(0)!=null, "序列号不存在");
            Deviceinfo deviceinfo = deviceinfos.get(0);

            // 校验签名
            String signString = timestamp + deviceSN + deviceinfo.getDeviceUnique();
            String signature = MD5Util.encode(signString);
            //log.info(signature);
            Assert.isTrue(signature.equals(sign), "签名错误");
            // 如果正确生成一个token保存到redis中，如果错误返回错误信息
            String token = this.saveToken(0,deviceinfo.getId());
            Map<String, Object> map = new HashMap<>();
            map.put("token",token);
            map.put("sign",MD5Util.encode("token="+token+"&deviceID="+deviceinfo.getDeviceUnique()));
            return Result.successResult(map);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            return Result.errorResult(e.getMessage());
        }

    }*/

    /*@Transactional(rollbackFor = Exception.class)
    public String saveToken(Integer tokenType,Long deviceId) {
        String token = UUID.randomUUID().toString();

        // 保存token
        Logininfor logininfor = logininforService.selectLogininforByTypeAndDeviceId(tokenType,deviceId);
        if(null == logininfor){
            Logininfor logininfor1 = new Logininfor();
            logininfor1.setType(tokenType);
            logininfor1.setToken(token);
            logininfor1.setDeviceId(deviceId);
            logininforService.save(logininfor1);
        }else{
            logininfor.setToken(token);
            logininforService.save(logininfor);
        }
        return token;
    }*/

}
