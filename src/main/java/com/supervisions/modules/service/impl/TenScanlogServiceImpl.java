package com.supervisions.modules.service.impl;

import com.supervisions.common.constant.Constant;
import com.supervisions.common.utils.StringUtils;
import com.supervisions.modules.dao.ITenScanlogDao;
import com.supervisions.modules.mapper.TenScanlog;
import com.supervisions.modules.service.ITenScanlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * scanlog 业务层处理
 */
@Service("tenScanlogService")
public class TenScanlogServiceImpl implements ITenScanlogService
{

    @Autowired
    private ITenScanlogDao scanlogDao;

    @Override
    public int save(TenScanlog scanlog)
    {
        int count = 0;
        scanlog.setCreateTime(new Date());
        count = scanlogDao.insert(scanlog);
        return count;
    }
    
}
