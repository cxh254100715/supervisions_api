package com.supervisions.framework.web.mapper;

import java.util.Date;

/**
 * 基础信息
 */
public class DataEntity
{

    //private static final long serialVersionUID = 1L;

    /** id */
    private Long id;
    /** 记录状态标志位 */
    private String status; // 状态 0:正常 1:锁定 2:删除
    /** 操作版本(乐观锁,用于并发控制) */
    private Integer version;
    /** 记录创建者用户登录名 */
    private String createUser;
    /** 记录创建时间 */
    private Date createTime;
    /** 记录更新用户 用户登录名 */
    private String updateUser;
    /** 记录更新时间 */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
