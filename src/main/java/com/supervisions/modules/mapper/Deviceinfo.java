package com.supervisions.modules.mapper;

import com.supervisions.framework.web.mapper.DataEntity;

/**
 * deviceinfo
 * @author cxh
 */
public class Deviceinfo extends DataEntity
{

	/** 序列号 */
    private String serialNumber;
	/** 类型 0:box 1:tv 2:camera */
    private Integer type;
	/** 设备id */
    private String deviceId;
	/** 商户id */
    private String merchantId;
	/** 商户地址id */
    private String merchantAddressId;
	/** 经纬度（逗号隔开） */
    private String lonLat;
	/** 是否激活 0:否 1:是 */
    private Integer isActivated;
	/** 备注 */
    private String remark;
    /** 设备名 */
    private String deviceName;
    /** 商户名 */
    private String merchantName;
    /** 商户地址名 */
    private String merchantAddressName;

    public String getSerialNumber(){
       return serialNumber ;
    }
	
    public void setSerialNumber(String serialNumber){
       this.serialNumber = serialNumber;
    }
    public Integer getType(){
       return type ;
    }
	
    public void setType(Integer type){
       this.type = type;
    }
    public String getDeviceId(){
       return deviceId ;
    }
	
    public void setDeviceId(String deviceId){
       this.deviceId = deviceId;
    }
    public String getMerchantId(){
       return merchantId ;
    }
	
    public void setMerchantId(String merchantId){
       this.merchantId = merchantId;
    }
    public String getMerchantAddressId(){
       return merchantAddressId ;
    }
	
    public void setMerchantAddressId(String merchantAddressId){
       this.merchantAddressId = merchantAddressId;
    }
    public String getLonLat(){
       return lonLat ;
    }
	
    public void setLonLat(String lonLat){
       this.lonLat = lonLat;
    }
    public Integer getIsActivated(){
       return isActivated ;
    }
	
    public void setIsActivated(Integer isActivated){
       this.isActivated = isActivated;
    }
    public String getRemark(){
       return remark ;
    }
	
    public void setRemark(String remark){
       this.remark = remark;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public String getMerchantName()
    {
        return merchantName;
    }

    public void setMerchantName(String merchantName)
    {
        this.merchantName = merchantName;
    }

    public String getMerchantAddressName()
    {
        return merchantAddressName;
    }

    public void setMerchantAddressName(String merchantAddressName)
    {
        this.merchantAddressName = merchantAddressName;
    }
}
