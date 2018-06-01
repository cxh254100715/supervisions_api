package com.supervisions.modules.service.impl;

import com.supervisions.common.constant.Constant;
import com.supervisions.common.utils.StringUtils;
import com.supervisions.modules.dao.ITenDeviceDao;
import com.supervisions.modules.mapper.TenDevice;
import com.supervisions.modules.service.ITenDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * device 业务层处理
 */
@Service("tenDeviceService")
public class TenDeviceServiceImpl implements ITenDeviceService
{

    @Autowired
    private ITenDeviceDao tenDeviceDao;

    @Override
    public TenDevice selectDeviceByDeviceId(String deviceId)
    {
        return tenDeviceDao.selectDeviceByDeviceId(deviceId);
    }

    @Override
    public int save(TenDevice device)
    {
        int count = 0;
        Long id = device.getId();
        if (StringUtils.isNotNull(id))
        {
            device.setUpdateTime(new Date());
            count = tenDeviceDao.update(device);
        }
        else
        {
            device.setCreateTime(new Date());
            count = tenDeviceDao.insert(device);
        }
        return count;
    }
}
