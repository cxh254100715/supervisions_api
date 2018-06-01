package com.supervisions.modules.service.impl;

import com.supervisions.modules.dao.IVersionsDao;
import com.supervisions.modules.mapper.Versions;
import com.supervisions.modules.service.IApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("apiService")
public class ApiServiceImpl implements IApiService
{

    @Autowired
    private IVersionsDao versionsDao;

    /**
     * 获取最大版本号
     * @param type
     * @return
     */
    @Override
    public Versions getMaxCode(Integer type)
    {
        return versionsDao.getMaxCode(type);
    }
}
