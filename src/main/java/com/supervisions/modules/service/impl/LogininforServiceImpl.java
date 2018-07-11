package com.supervisions.modules.service.impl;

import com.supervisions.common.utils.StringUtils;
import com.supervisions.modules.dao.IDeviceinfoDao;
import com.supervisions.modules.dao.ILogininforDao;
import com.supervisions.modules.mapper.Logininfor;
import com.supervisions.modules.mapper.TenDevice;
import com.supervisions.modules.service.ILogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("logininforService")
//@CacheConfig(cacheNames="logininfor")
public class LogininforServiceImpl implements ILogininforService
{

    @Autowired
    private ILogininforDao logininforDao;

    @Override
    //@Cacheable(key = "'logininforId:'+#type+':'+#deviceId")
    public Logininfor selectLogininforByTypeAndDeviceId(Integer type, Long deviceId)
    {
        return logininforDao.selectLogininforByTypeAndDeviceId(type, deviceId);
    }

    @Override
    //@CachePut(key = "'logininforId:'+#logininfor.getType()+':'+#logininfor.getDeviceId()")
    public Logininfor save(Logininfor logininfor)
    {
        Long id = logininfor.getId();
        if (StringUtils.isNotNull(id))
        {
            logininfor.setUpdateTime(new Date());
            logininfor.setVersion(logininfor.getVersion()+1);
            logininforDao.update(logininfor);
        }
        else
        {
            logininfor.setCreateTime(new Date());
            logininfor.setVersion(1);
            logininforDao.insert(logininfor);
        }
        return logininfor;
    }

    @Override
    public Logininfor selectLogininforByTokenAndDeviceSN(String token,String deviceSN)
    {
        return logininforDao.selectLogininforByTokenAndDeviceSN(token, deviceSN);
    }

}
