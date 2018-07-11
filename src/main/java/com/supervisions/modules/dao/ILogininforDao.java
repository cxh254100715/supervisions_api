package com.supervisions.modules.dao;

import com.supervisions.modules.mapper.Logininfor;
import com.supervisions.modules.mapper.TenDevice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ILogininforDao
{

    public Logininfor selectLogininforByTypeAndDeviceId(@Param("type") Integer type, @Param("deviceId") Long deviceId);

    public int insert(Logininfor logininfor);

    public int update(Logininfor logininfor);

    public Logininfor selectLogininforByTokenAndDeviceSN(@Param("token") String token, @Param("deviceSN") String deviceSN);
}
