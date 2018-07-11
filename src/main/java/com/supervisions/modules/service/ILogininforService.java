package com.supervisions.modules.service;

import com.supervisions.modules.mapper.Logininfor;

import java.util.List;

public interface ILogininforService
{

    public Logininfor selectLogininforByTypeAndDeviceId( Integer type, Long deviceId);

    public Logininfor save(Logininfor logininfor);

    public Logininfor selectLogininforByTokenAndDeviceSN(String token, String deviceSN);
}
