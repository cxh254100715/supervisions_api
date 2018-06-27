package com.supervisions.modules.service.impl;

import com.supervisions.common.constant.Constant;
import com.supervisions.common.utils.StringUtils;
import com.supervisions.modules.dao.ITenUserDao;
import com.supervisions.modules.mapper.TenDevice;
import com.supervisions.modules.mapper.TenUser;
import com.supervisions.modules.service.ITenUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * user 业务层处理
 */
@Service("tenUserService")
@CacheConfig(cacheNames="tenUser")
public class TenUserServiceImpl implements ITenUserService
{

    @Autowired
    private ITenUserDao tenUserDao;


    @Override
    public TenUser selectUserByOpenId(String openId)
    {
        return tenUserDao.selectUserByOpenId(openId);
    }

    @Override
    @Cacheable(key = "'userId:'+#id")
    public TenUser selectUserById(Long id)
    {
        return tenUserDao.selectUserById(id);
    }

    @Override
    @CachePut(key = "'userId:'+#user.getId()")
    public TenUser save(TenUser user)
    {
        if(StringUtils.isNotNull(user.getId())){
            user.setUpdateTime(new Date());
            user.setVersion(user.getVersion()+1);
            tenUserDao.update(user);
        }else{
            user.setCreateTime(new Date());
            user.setStatus("0");
            user.setVersion(1);
            tenUserDao.insert(user);
        }
        return user;
    }
}
