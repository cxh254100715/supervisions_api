package com.supervisions.modules.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.supervisions.common.constant.Constant;
import com.supervisions.common.utils.RSAUtils;
import com.supervisions.framework.web.mapper.Result;
import com.supervisions.framework.web.service.RedisService;
import com.supervisions.modules.mapper.Deviceinfo;
import com.supervisions.modules.mapper.Logininfor;
import com.supervisions.modules.service.IBoxService;
import com.supervisions.modules.service.IDeviceinfoService;
import com.supervisions.modules.service.ILogininforService;
import io.swagger.annotations.Api;
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
import java.util.UUID;

/**
 * box接口
 */
//@Api(value="盒子controller",tags={"工控机接口"})
@RestController
@RequestMapping("/box")
public class BoxController
{
    private static final Logger log = LoggerFactory.getLogger(BoxController.class);

    @Autowired
    private IBoxService boxService;
    @Autowired
    private IDeviceinfoService deviceinfoService;
    @Autowired
    private ILogininforService logininforService;

    @ApiOperation(value = "上线设备", notes = "上线设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceSN", value = "序列号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "json", value = "格式：{\"deviceID\":\"\"}", required = true, dataType = "json", paramType = "body")
    })
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/onlineDevice")
    public Result onlineDevice(@RequestBody(required = false) String json, @RequestParam(required = false) String deviceSN)
    {
        try
        {
            if(StringUtils.isEmpty(json)||StringUtils.isEmpty(deviceSN)){
                return Result.errorResult("缺少参数");
            }
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            String deviceUnique = jsonObject.get("deviceID").getAsString();
            List<Deviceinfo> deviceinfos = boxService.activateDevice(deviceSN);
            if (deviceinfos.size() == 1)
            {
                Deviceinfo deviceinfo = deviceinfos.get(0);
                //String publicKey = deviceinfo.getDevicePublickey();
                //if(RSAUtils.publicDecrypt(deviceUnique,RSAUtils.getPublicKey(deviceinfo.getDevicePublickey())).equals(deviceinfo.getDeviceUnique()))
                if(deviceUnique.equals(deviceinfo.getDeviceUnique()))
                {
                    /*if (!deviceinfo.getIsActivated().equals(0))
                    {
                        return Result.errorResult("序列号已激活，请勿重复激活");
                    }*/
                    deviceinfo.setStatus("0"); // 激活
                    deviceinfo.setIsActivated(1); // 激活
                    boxService.save(deviceinfo);
                    String token = this.saveToken(0,deviceinfo.getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("token",token);
                    return Result.successResult(map);
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

    @ApiOperation(value = "激活设备", notes = "激活设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "deviceSN", value = "序列号", required = true, dataType = "String", paramType = "query")
    })
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/activateDevice")
    public Result activateDevice(@RequestParam(required = false) String deviceSN)
    {
        try
        {
            if(StringUtils.isEmpty(deviceSN)){
                return Result.errorResult("缺少参数");
            }
            List<Deviceinfo> deviceinfos = boxService.activateDevice(deviceSN);
            if (deviceinfos.size() == 1)
            {
                Deviceinfo deviceinfo = deviceinfos.get(0);
                if(deviceinfo!=null)
                {
                    deviceinfo.setStatus("0");
                    boxService.save(deviceinfo);
                    return Result.successResult("0");
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

    @ApiOperation(value = "上报比赛结果", notes = "上报比赛结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "deviceSN", value = "序列号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "json", value = "格式：", required = true, dataType = "String", paramType = "body")
    })
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/reportedCompetition")
    public Result reportedCompetition(@RequestBody(required = false) String json, @RequestParam(required = false) String deviceSN)
    {
        try
        {
            if(StringUtils.isEmpty(json)||StringUtils.isEmpty(deviceSN)){
                return Result.errorResult("缺少参数");
            }
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            return Result.successResult("success");
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



    @Transactional(rollbackFor = Exception.class)
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
    }

}
