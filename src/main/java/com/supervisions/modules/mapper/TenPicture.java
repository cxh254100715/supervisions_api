package com.supervisions.modules.mapper;

import com.supervisions.framework.web.mapper.DataEntity;

/**
 * TenHeadimgurl
 * @author cxh
 */
public class TenPicture extends DataEntity
{
    /** 排序 */
    private String orderNo;
    /** url */
    private String url;
    /** 类型 0:app */
    private Integer type;

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }
}
