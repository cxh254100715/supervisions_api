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
    /** 昵称 */
    private String nickname;
    /** 头像 */
    private String headimgurl;
    /** 性别 0:未知 1:男 2:女 */
    private Integer sex;
    /** 地址（例：{"country": "中国", "province": "广东", "city": "广州"}） */
    private String address;

    public String getOpenId(){
       return openId ;
    }
	
    public void setOpenId(String openId){
       this.openId = openId;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getHeadimgurl()
    {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl)
    {
        this.headimgurl = headimgurl;
    }

    public Integer getSex()
    {
        return sex;
    }

    public void setSex(Integer sex)
    {
        this.sex = sex;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
}
