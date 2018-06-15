package com.supervisions.modules.service.impl;

import com.supervisions.common.constant.Constant;
import com.supervisions.common.utils.StringUtils;
import com.supervisions.modules.dao.ITenDeviceDao;
import com.supervisions.modules.mapper.TenDevice;
import com.supervisions.modules.service.ITenDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * device 业务层处理
 */
@Service("tenDeviceService")
@CacheConfig(cacheNames="tenDevice")
public class TenDeviceServiceImpl implements ITenDeviceService
{

    @Autowired
    private ITenDeviceDao tenDeviceDao;

    @Override
    @Cacheable(key = "'deviceId_'+#deviceId")
    public TenDevice selectDeviceByDeviceId(String deviceId)
    {
        return tenDeviceDao.selectDeviceByDeviceId(deviceId);
    }

    @Override
    @CachePut(key = "'deviceId_'+#device.getDeviceId()")
    public TenDevice save(TenDevice device)
    {
        Long id = device.getId();
        if (StringUtils.isNotNull(id))
        {
            device.setUpdateTime(new Date());
            device.setVersion(device.getVersion()+1);
            tenDeviceDao.update(device);
        }
        else
        {
            device.setCreateTime(new Date());
            device.setVersion(1);
            tenDeviceDao.insert(device);
        }
        return device;
    }
}
