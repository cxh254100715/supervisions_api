package com.supervisions.modules.dao;


import com.supervisions.modules.mapper.TenDevice;
import com.supervisions.modules.mapper.TenUser;

import java.util.List;

/**
 * user 数据层
 * @author cxh
 */
public interface ITenUserDao
{

    public TenUser selectUserByOpenId(String openId);

    public int insert(TenUser user);

    public TenUser selectUserById(Long id);
}
