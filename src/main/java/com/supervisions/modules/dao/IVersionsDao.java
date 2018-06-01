package com.supervisions.modules.dao;


import com.supervisions.modules.mapper.Versions;

import java.util.List;
import java.util.Map;

/**
 * app版本 数据层
 */
public interface IVersionsDao
{
    /**
     * 获取最大版本号
     * @return
     */
    public Versions getMaxCode(Integer type);

}
