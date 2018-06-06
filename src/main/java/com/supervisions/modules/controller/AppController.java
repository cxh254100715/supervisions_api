package com.supervisions.modules.controller;

import com.supervisions.framework.web.mapper.Result;
import com.supervisions.modules.mapper.Versions;
import com.supervisions.modules.service.IAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 接口
 */
@Controller
@RequestMapping("/app")
public class AppController
{
    @Autowired
    private IAppService appService;

    /**
     * app接口获取版本
     */
    @GetMapping("/checkVersionUpdate")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Result checkVersionUpdate(Integer type, String versionCode)
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
