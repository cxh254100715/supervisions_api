package com.supervisions.modules.mapper;

import com.supervisions.framework.web.mapper.DataEntity;

/**
 * scanlog
 * @author cxh
 */
public class TenScanlog extends DataEntity
{

	/** 设备id */
    private Long deviceId;
	/** 左边用户id */
    private Long leftId;
	/** 右边用户id */
    private Long rightId;

    public Long getDeviceId(){
       return deviceId ;
    }
	
    public void setDeviceId(Long deviceId){
       this.deviceId = deviceId;
    }
    public Long getLeftId(){
       return leftId ;
    }
	
    public void setLeftId(Long leftId){
       this.leftId = leftId;
    }
    public Long getRightId(){
       return rightId ;
    }
	
    public void setRightId(Long rightId){
       this.rightId = rightId;
    }

}
