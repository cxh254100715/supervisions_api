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
import org.springframework.web.bind.annotation.*;

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
    private RedisService redisService;
    @Autowired
    private IBoxService boxService;

    @ApiOperation(value = "激活设备", notes = "激活设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "格式{deviceCode:abcd,type:0}（type 0:检测设备是否存在 1:激活该设备）,成功返回字符串success", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/activateDevice")
    public Result activateDevice(@RequestParam(required = true) String param)
    {
        try
        {
            String privateKey = redisService.get("privateKey").toString();
            String json = RSAUtils.privateDecrypt(param, RSAUtils.getPrivateKey(redisService.get("privateKey").toString()));
            if (!isGoodJson(json))
            {
                return Result.errorResult("参数错误");
            }
            if (json.indexOf("deviceCode")==-1||json.indexOf("type")==-1)
            {
                return Result.errorResult("参数错误");
            }
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            String deviceCode = jsonObject.get("deviceCode").getAsString();
            String type = jsonObject.get("type").getAsString();
            List<Deviceinfo> deviceinfos = boxService.activateDevice(deviceCode);
            if (deviceinfos.size() == 1)
            {
                Deviceinfo deviceinfo = deviceinfos.get(0);
                if (deviceinfo != null)
                {
                    if (!deviceinfo.getIsActivated().equals(0))
                    {
                        return Result.errorResult("序列号已激活，请勿重复激活");
                    }
                    if(type.equals("0")){
                        return Result.successResult(RSAUtils.publicEncrypt("success", RSAUtils.getPublicKey(redisService.get("publicKey").toString())));
                    }else if(type.equals("1")){
                        deviceinfo.setIsActivated(1); // 激活
                        boxService.save(deviceinfo);
                        return Result.successResult(RSAUtils.publicEncrypt("success", RSAUtils.getPublicKey(redisService.get("publicKey").toString())));
                    }
                    return Result.errorResult("参数错误");
                }
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


    public static boolean isGoodJson(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            log.error("bad json: " + json);
            return false;
        }
    }

}
