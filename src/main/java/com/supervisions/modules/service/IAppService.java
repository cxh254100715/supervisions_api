package com.supervisions.modules.service;

import com.supervisions.modules.mapper.Versions;

public interface IAppService
{

    /**
     * 获取最大版本号
     * @param type
     * @return
     */
    public Versions getMaxCode(Integer type);
}
