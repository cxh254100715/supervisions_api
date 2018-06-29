package com.supervisions.modules.service.impl;

import com.supervisions.modules.dao.ITenPictureDao;
import com.supervisions.modules.service.ITenPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * TenPicture 业务层处理
 */
@Service("tenPictureService")
@CacheConfig(cacheNames="tenPicture")
public class TenPictureServiceImpl implements ITenPictureService
{
    @Autowired
    private ITenPictureDao tenPictureDao;

    @Override
    @Cacheable(key = "'pictureList:list'")
    public List<Map<String, Object>> selectTenPictureByType(Integer type)
    {
        return tenPictureDao.selectTenPictureByType(type);
    }
}
