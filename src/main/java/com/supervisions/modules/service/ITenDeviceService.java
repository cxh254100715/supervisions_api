package com.supervisions.modules.service;


import com.supervisions.modules.mapper.TenDevice;

import java.util.List;

/**
 * device 业务层
 */
public interface ITenDeviceService
{

    public TenDevice selectDeviceByDeviceId(String deviceId);

    public TenDevice save(TenDevice device);

}
