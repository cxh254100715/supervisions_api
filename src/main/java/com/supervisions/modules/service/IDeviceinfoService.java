package com.supervisions.modules.service;

import com.supervisions.modules.mapper.Deviceinfo;

import java.util.List;

public interface IDeviceinfoService
{

    public List<Deviceinfo> selectDeviceinfoBydeviceSn(String deviceSn);

}
