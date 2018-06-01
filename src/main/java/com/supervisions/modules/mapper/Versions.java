package com.supervisions.modules.mapper;

import com.supervisions.framework.web.mapper.DataEntity;

/**
 * app版本
 */
public class Versions extends DataEntity
{

    /** 名称 */
    private String name;
    /** 版本名 */
    private String versionName;
    /** 版本号 */
    private String versionCode;
    /** 备注 */
    private String remark;
    /** 类型 0:Android 1:box */
    private Integer type;
    /** 包名 */
    private String packageName;
    /** url */
    private String url;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersionName()
    {
        return versionName;
    }

    public void setVersionName(String versionName)
    {
        this.versionName = versionName;
    }

    public String getVersionCode()
    {
        return versionCode;
    }

    public void setVersionCode(String versionCode)
    {
        this.versionCode = versionCode;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}
