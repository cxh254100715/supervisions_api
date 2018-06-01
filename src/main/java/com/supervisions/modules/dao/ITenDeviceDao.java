package com.supervisions.modules.dao;

import com.supervisions.modules.mapper.TenDevice;

import java.util.List;

/**
 * device 数据层
 * @author cxh
 */
public interface ITenDeviceDao
{
    public TenDevice selectDeviceByDeviceId(String deviceId);

    public int insert(TenDevice device);

    public int update(TenDevice device);
}
