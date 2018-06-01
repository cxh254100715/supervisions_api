package com.supervisions.modules.mapper;

import com.supervisions.framework.web.mapper.DataEntity;

/**
 * user
 * @author cxh
 */
public class TenUser extends DataEntity
{

	/** 微信open_id */
    private String openId;

    public String getOpenId(){
       return openId ;
    }
	
    public void setOpenId(String openId){
       this.openId = openId;
    }

}
