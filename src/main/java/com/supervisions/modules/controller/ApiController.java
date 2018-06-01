package com.supervisions.modules.controller;

import com.supervisions.framework.web.mapper.Result;
import com.supervisions.modules.mapper.Versions;
import com.supervisions.modules.service.IApiService;
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
@RequestMapping("/api")
public class ApiController
{
    @Autowired
    private IApiService apiService;

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
            Versions versions = apiService.getMaxCode(type);
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
