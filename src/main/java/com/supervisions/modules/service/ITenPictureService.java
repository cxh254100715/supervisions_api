package com.supervisions.modules.service;

import java.util.List;
import java.util.Map;

public interface ITenPictureService
{
    /**
     * 获取所有头像
     */
    public List<Map<String,Object>> selectTenPictureByType(Integer type);

}
