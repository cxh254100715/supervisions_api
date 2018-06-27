package com.supervisions.modules.dao;

import com.supervisions.modules.mapper.TenPicture;

import java.util.List;
import java.util.Map;

/**
 * TenPicture 数据层
 * @author cxh
 */
public interface ITenPictureDao
{
    /**
     * 获取所有头像
     */
    public List<Map<String,Object>> selectTenPictureByType(Integer type);

}
