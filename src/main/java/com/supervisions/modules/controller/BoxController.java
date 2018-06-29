package com.supervisions.modules.controller;

import com.supervisions.framework.web.mapper.Result;
import com.supervisions.modules.mapper.Deviceinfo;
import com.supervisions.modules.service.IBoxService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    private IBoxService boxService;

    @ApiOperation(value="激活设备", notes="激活设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceCode", value = "序列号", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/activateDevice")
    public Result activateDevice(@RequestParam(required = true) String deviceCode)
    {
        try
        {
            Map<String,Object> map = new HashMap<>();
            List<Deviceinfo> deviceinfos = boxService.activateDevice(deviceCode);
            if(deviceinfos.size()==1){
                Deviceinfo deviceinfo = deviceinfos.get(0);
                if(deviceinfo!=null){
                    if(!deviceinfo.getIsActivated().equals(0)){
                        return Result.errorResult("设备已激活");
                    }
                    deviceinfo.setIsActivated(1); // 激活
                    boxService.save(deviceinfo);
                    return Result.successResult(map);
                }
            }
            return Result.errorResult("序列号不存在");
        }catch (Exception e){
            return  Result.errorResult();
        }
    }

}
