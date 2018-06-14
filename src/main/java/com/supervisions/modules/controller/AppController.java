package com.supervisions.modules.controller;

import com.supervisions.framework.web.mapper.Result;
import com.supervisions.modules.mapper.Versions;
import com.supervisions.modules.service.IAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 接口
 */
@RestController
@RequestMapping("/app")
public class AppController
{
    @Autowired
    private IAppService appService;

    /**
     * app接口获取版本
     */
    @ApiOperation(value="获取带参数二维码", notes="根据设备id获取二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型（0:Android 1:box）", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "versionCode", value = "版本号", required = true, dataType = "string", paramType = "query")
    })
    @GetMapping("/checkVersionUpdate")
    public Result checkVersionUpdate(@RequestParam(required = true) Integer type, @RequestParam(required = true) String versionCode)
    {
        try
        {
            Versions versions = appService.getMaxCode(type);
            if (versions!=null && Double.valueOf(versions.getVersionCode()) > Double.valueOf(versionCode))
            {
                return Result.successResult(versions.getUrl());
            }else{
                return  Result.errorResult("暂无版本更新");
            }
        }catch (Exception e){
            return  Result.errorResult("服务器开小差啦，请稍后再试！");
        }

    }

}
