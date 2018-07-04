package com.supervisions.modules.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.supervisions.common.constant.Constant;
import com.supervisions.common.utils.RSAUtils;
import com.supervisions.framework.web.mapper.Result;
import com.supervisions.framework.web.service.RedisService;
import com.supervisions.modules.mapper.Deviceinfo;
import com.supervisions.modules.service.IBoxService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * box接口
 */
@RestController
@RequestMapping("/box")
public class BoxController
{
    private static final Logger log = LoggerFactory.getLogger(BoxController.class);

    @Autowired
    private IBoxService boxService;

    @ApiOperation(value = "激活设备", notes = "激活设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceSN", value = "设备序列号", required = true, dataType = "String", paramType = "query")
    })
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/activateDevice")
    public Result activateDevice(@RequestBody(required = false) String data, @RequestParam(required = false) String deviceSN)
    {
        try
        {
            if(StringUtils.isEmpty(data)||StringUtils.isEmpty(deviceSN)){
                return Result.errorResult("缺少参数");
            }
            JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
            String deviceUnique = jsonObject.get("deviceID").getAsString();
            List<Deviceinfo> deviceinfos = boxService.activateDevice(deviceSN);
            if (deviceinfos.size() == 1)
            {
                Deviceinfo deviceinfo = deviceinfos.get(0);
                String publicKey = deviceinfo.getDevicePublickey();
                if(RSAUtils.publicDecrypt(deviceUnique,RSAUtils.getPublicKey(deviceinfo.getDevicePublickey())).equals(deviceinfo.getDeviceUnique()))
                {
                    if (!deviceinfo.getIsActivated().equals(0))
                    {
                        return Result.errorResult("序列号已激活，请勿重复激活");
                    }
                    deviceinfo.setIsActivated(1); // 激活
                    boxService.save(deviceinfo);
                    return Result.successResult(RSAUtils.publicEncrypt("success", RSAUtils.getPublicKey(publicKey)));
                }
                return Result.errorResult("参数错误");
            }
            return Result.errorResult("序列号不存在");
        }
        catch (RuntimeException e)
        {
            log.error(e.getMessage());
            return Result.errorResult("参数错误");
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            return Result.errorResult();
        }
    }

}
