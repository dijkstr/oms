package com.hundsun.boss.base.form;

public class BaseForm {
    protected String id;
    protected String remarks;  // 备注
    protected String createBy;    // 创建者
    protected String createDate;// 创建日期
    protected String updateBy;    // 更新者
    protected String updateDate;// 更新日期
    protected String bindClass;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getBindClass() {
        return bindClass;
    }

    public void setBindClass(String bindClass) {
        this.bindClass = bindClass;
    }

}
