package com.supervisions.modules.controller;

import com.supervisions.common.utils.StringUtils;
import com.supervisions.framework.web.mapper.Result;
import com.supervisions.modules.mapper.TenUser;
import com.supervisions.modules.mapper.Versions;
import com.supervisions.modules.service.IAppService;
import com.supervisions.modules.service.ITenPictureService;
import com.supervisions.modules.service.ITenUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * app接口
 */
@RestController
@RequestMapping("/app")
public class AppController
{
    @Autowired
    private IAppService appService;
    @Autowired
    private ITenPictureService tenPictureService;
    @Autowired
    private ITenUserService tenUserService;

    @ApiOperation(value="获取最新版本", notes="获取最新版本")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型（0:android 1:box）", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "versionCode", value = "版本号", required = true, dataType = "string", paramType = "query")
    })
    @GetMapping("/checkVersionUpdate")
    public Result checkVersionUpdate(@RequestParam(required = true) Integer type, @RequestParam(required = true) String versionCode)
    {
        try
        {
            Map<String,Object> map = new HashMap<>();
            Versions versions = appService.getMaxCode(type);
            if (versions!=null && Double.valueOf(versions.getVersionCode()) > Double.valueOf(versionCode))
            {
                map.put("type",type==0?"android":"box");
                map.put("versionCode",versions.getVersionCode());
                map.put("url",versions.getUrl());
                return Result.successResult(map);
            }else{
                return  Result.errorResult("暂无版本更新");
            }
        }catch (Exception e){
            return  Result.errorResult();
        }
    }

    @ApiOperation(value = "获取所有头像", notes="获取所有头像")
    @GetMapping("/getHeadimgurls")
    public Result getHeadimgurls(){
        Result result = null;
        try
        {
            List<Map<String,Object>> list = tenPictureService.selectTenPictureByType(0);
            return result.successResult(list);
        }catch (Exception e)
        {
            return  result.errorResult();
        }
    }

    @ApiOperation(value="修改用户信息", notes="修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "nickname", value = "昵称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "headimgurl", value = "头像", dataType = "string", paramType = "query")
    })
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/updateProfile")
    public Result updateProfile(@RequestParam(required = true) Long id, String nickname, String headimgurl)
    {
        Result result = null;
        Map<String,Object> map = new HashMap<String,Object>();
        try
        {
            /*TenUser user = tenUserService.selectUserById(id);
            if(user==null){
                return  result.errorResult("用户不存在！");
            }
            if(StringUtils.isEmpty(nickname)&&StringUtils.isEmpty(headimgurl)){
                return  result.errorResult("缺少参数！");
            }
            map.put("id",user.getId());
            if(StringUtils.isNotEmpty(nickname)){
                user.setNickname(nickname);
                map.put("nickname",user.getNickname());
            }
            if(StringUtils.isNotEmpty(headimgurl)){
                user.setHeadimgurl(headimgurl);
                map.put("headimgurl",user.getHeadimgurl());
            }
            tenUserService.save(user);
            return Result.successResult(map);*/
            return Result.successResult();
        }catch (Exception e)
        {
            return  result.errorResult();
        }
    }

}
