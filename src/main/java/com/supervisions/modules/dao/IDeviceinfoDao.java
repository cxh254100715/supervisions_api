package com.supervisions.modules.dao;

import com.supervisions.modules.mapper.Deviceinfo;
import com.supervisions.modules.mapper.TenDevice;

import java.util.List;

public interface IDeviceinfoDao
{
    public List<Deviceinfo> selectDeviceinfoBydeviceSn(String deviceSn);

    public int updateDeviceinfo(Deviceinfo deviceinfo); // 暂只更新isActivated字段
}
