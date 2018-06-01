package com.supervisions.modules.service;


import com.supervisions.modules.mapper.TenScanlog;

import java.util.List;

/**
 * scanlog 业务层
 */
public interface ITenScanlogService
{
    /**
     * 保存对象
     * @param scanlog
     * @return
     */
    public int save(TenScanlog scanlog);

}
