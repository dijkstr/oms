package com.hundsun.boss.base.hibernate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.DateUtils;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 数据Entity类
 */
@MappedSuperclass
public abstract class DataEntity<T> extends BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String remarks;	// 备注
    protected User createBy;	// 创建者
    protected String createDate;// 创建日期
    protected User updateBy;	// 更新者
    protected String updateDate;// 更新日期
    protected String delFlag; // 删除标记（0：正常；1：删除；2：审核）

    //    protected Date createDateStart;
    //    protected Date createDateEnd;
    //    protected Date updateDateStart;
    //    protected Date updateDateEnd;

    public DataEntity() {
        super();
        this.delFlag = DEL_FLAG_NORMAL;
    }

    @PrePersist
    public void prePersist() {
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId())) {
            this.updateBy = user;
            this.createBy = user;
        }
        this.updateDate = DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
        this.createDate = this.updateDate;
    }

    @PreUpdate
    public void preUpdate() {
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId())) {
            this.updateBy = user;
        }
        this.updateDate = DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
        if(CommonUtil.isNullorEmpty(this.createBy)){
        	this.createBy = user;
        	this.createDate = this.updateDate;
        }
    }

    @Length(min = 0, max = 255)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    public User getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(User updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    @Length(min = 1, max = 1)
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    //    @Temporal(TemporalType.DATE)
    //    @Transient
    //    public Date getCreateDateStart() {
    //        return DateUtils.getDateStart(createDateStart);
    //    }
    //
    //    public void setCreateDateStart(Date createDateStart) {
    //        this.createDateStart = createDateStart;
    //    }
    //
    //    @Temporal(TemporalType.DATE)
    //    @Transient
    //    public Date getCreateDateEnd() {
    //        return DateUtils.getDateEnd(createDateEnd);
    //    }
    //
    //    public void setCreateDateEnd(Date createDateEnd) {
    //        this.createDateEnd = createDateEnd;
    //    }
    //
    //    @Temporal(TemporalType.DATE)
    //    @Transient
    //    public Date getUpdateDateStart() {
    //        return DateUtils.getDateStart(updateDateStart);
    //    }
    //
    //    public void setUpdateDateStart(Date updateDateStart) {
    //        this.updateDateStart = updateDateStart;
    //    }
    //
    //    @Temporal(TemporalType.DATE)
    //    @Transient
    //    public Date getUpdateDateEnd() {
    //        return DateUtils.getDateEnd(updateDateEnd);
    //    }
    //
    //    public void setUpdateDateEnd(Date updateDateEnd) {
    //        this.updateDateEnd = updateDateEnd;
    //    }
}
