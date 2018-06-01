package com.supervisions.modules.dao;


import com.supervisions.modules.mapper.TenScanlog;

import java.util.List;

/**
 * scanlog 数据层
 * @author cxh
 */
public interface ITenScanlogDao
{
    /**
     * 新增对象
     * @param scanlog
     * @return
     */
    public int insert(TenScanlog scanlog);
    
}
