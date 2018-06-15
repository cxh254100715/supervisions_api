package com.supervisions.modules.service;

import com.supervisions.modules.mapper.TenDevice;
import com.supervisions.modules.mapper.TenUser;

import java.util.List;

/**
 * user 业务层
 */
public interface ITenUserService
{
    public TenUser selectUserByOpenId(String openId);

    public TenUser selectUserById(Long id);

    public TenUser save(TenUser user);
}
