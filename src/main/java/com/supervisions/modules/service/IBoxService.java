package com.supervisions.modules.service;

import com.supervisions.modules.mapper.Deviceinfo;

import java.util.List;

public interface IBoxService
{

    public List<Deviceinfo> activateDevice(String deviceCode);

    public int save(Deviceinfo deviceinfo);
}
