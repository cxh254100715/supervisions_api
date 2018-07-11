package com.supervisions.modules.service.impl;

import com.supervisions.modules.dao.IDeviceinfoDao;
import com.supervisions.modules.mapper.Deviceinfo;
import com.supervisions.modules.mapper.TenUser;
import com.supervisions.modules.service.IDeviceinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("deviceinfoService")
public class DeviceinfoServiceImpl implements IDeviceinfoService
{
    @Autowired
    private IDeviceinfoDao deviceinfoDao;

    @Override
    public List<Deviceinfo> selectDeviceinfoBydeviceSn(String deviceSn)
    {
        return deviceinfoDao.selectDeviceinfoBydeviceSn(deviceSn);
    }


}
