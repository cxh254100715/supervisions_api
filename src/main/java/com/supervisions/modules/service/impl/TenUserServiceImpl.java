package com.supervisions.modules.service.impl;

import com.supervisions.common.constant.Constant;
import com.supervisions.common.utils.StringUtils;
import com.supervisions.modules.dao.ITenUserDao;
import com.supervisions.modules.mapper.TenDevice;
import com.supervisions.modules.mapper.TenUser;
import com.supervisions.modules.service.ITenUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * user 业务层处理
 */
@Service("tenUserService")
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
    public TenUser selectUserById(Long id)
    {
        return tenUserDao.selectUserById(id);
    }

    @Override
    public int save(TenUser user)
    {
        int count = 0;
        user.setCreateTime(new Date());
        count = tenUserDao.insert(user);
        return count;
    }
}
