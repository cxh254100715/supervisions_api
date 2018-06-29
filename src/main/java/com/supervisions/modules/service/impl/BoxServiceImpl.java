package com.supervisions.modules.service.impl;

import com.supervisions.modules.dao.IDeviceinfoDao;
import com.supervisions.modules.mapper.Deviceinfo;
import com.supervisions.modules.service.IBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("boxService")
public class BoxServiceImpl implements IBoxService
{
    @Autowired
    private IDeviceinfoDao deviceinfoDao;

    @Override
    public List<Deviceinfo> activateDevice(String deviceCode)
    {
        return deviceinfoDao.selectDeviceinfoBySerialNumber(deviceCode);
    }

    @Override
    public int save(Deviceinfo deviceinfo)
    {
        int count = 0;
        if(deviceinfo.getId()!=null){
            deviceinfoDao.updateDeviceinfo(deviceinfo);
        }
        return count;
    }
}
