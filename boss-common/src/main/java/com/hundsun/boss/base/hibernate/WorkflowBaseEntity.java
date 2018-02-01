package com.hundsun.boss.base.hibernate;

import java.sql.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * 工作流基本Entity
 * 
 * @author zhengzh20786
 *
 */
@MappedSuperclass
public abstract class WorkflowBaseEntity<T> extends IdEntity<T> {

    private static final long serialVersionUID = 1L;

    protected String process_instance_id; // 流程实例编号
    protected Date startTime;     // 请假开始日期
    protected Date endTime;       // 请假结束日期
    protected Date realityStartTime;      // 实际开始时间
    protected Date realityEndTime;        // 实际结束时间
    protected String processStatus; //流程状态

    protected Date createDateStart;
    protected Date createDateEnd;
    protected Date updateDateStart;
    protected Date updateDateEnd;

    public String getProcess_instance_id() {
        return process_instance_id;
    }

    public void setProcess_instance_id(String process_instance_id) {
        this.process_instance_id = process_instance_id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getRealityStartTime() {
        return realityStartTime;
    }

    public void setRealityStartTime(Date realityStartTime) {
        this.realityStartTime = realityStartTime;
    }

    public Date getRealityEndTime() {
        return realityEndTime;
    }

    public void setRealityEndTime(Date realityEndTime) {
        this.realityEndTime = realityEndTime;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }
    @Transient
    public Date getCreateDateStart() {
        return createDateStart;
    }
    @Transient
    public void setCreateDateStart(Date createDateStart) {
        this.createDateStart = createDateStart;
    }
    @Transient
    public Date getCreateDateEnd() {
        return createDateEnd;
    }
    @Transient
    public void setCreateDateEnd(Date createDateEnd) {
        this.createDateEnd = createDateEnd;
    }
    @Transient
    public Date getUpdateDateStart() {
        return updateDateStart;
    }
    @Transient
    public void setUpdateDateStart(Date updateDateStart) {
        this.updateDateStart = updateDateStart;
    }
    @Transient
    public Date getUpdateDateEnd() {
        return updateDateEnd;
    }
    @Transient
    public void setUpdateDateEnd(Date updateDateEnd) {
        this.updateDateEnd = updateDateEnd;
    }

}
