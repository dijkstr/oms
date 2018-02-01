package com.hundsun.boss.modules.charge.form.receipt;

public class ExportReceiveForm {

    // 业务单元
    private String order_source;
    // 合同编号
    private String hs_contract_id;

    //销售产品名称
    private String saleprdname;
    //到款金额
    private Double bankreceipt_amount;
    //到款日期
    private String bankreceipt_month;
  
    //创建时间
    private String create_datetime;
  
    //协同客户号
    private String hs_user_id;
    //协同客户名称
    private String user_name;
    //销售产品编号
    private String saleprdid;
    //考核部门
    private String audit_branch_name;
    //考核人
    private String audit_employee_name;
    public String getOrder_source() {
        return order_source;
    }
    public void setOrder_source(String order_source) {
        this.order_source = order_source;
    }
    public String getHs_contract_id() {
        return hs_contract_id;
    }
    public void setHs_contract_id(String hs_contract_id) {
        this.hs_contract_id = hs_contract_id;
    }
    public String getSaleprdname() {
        return saleprdname;
    }
    public void setSaleprdname(String saleprdname) {
        this.saleprdname = saleprdname;
    }
    public Double getBankreceipt_amount() {
        return bankreceipt_amount;
    }
    public void setBankreceipt_amount(Double bankreceipt_amount) {
        this.bankreceipt_amount = bankreceipt_amount;
    }
    public String getBankreceipt_month() {
        return bankreceipt_month;
    }
    public void setBankreceipt_month(String bankreceipt_month) {
        this.bankreceipt_month = bankreceipt_month;
    }
    public String getCreate_datetime() {
        return create_datetime;
    }
    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }
    public String getHs_user_id() {
        return hs_user_id;
    }
    public void setHs_user_id(String hs_user_id) {
        this.hs_user_id = hs_user_id;
    }
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getSaleprdid() {
        return saleprdid;
    }
    public void setSaleprdid(String saleprdid) {
        this.saleprdid = saleprdid;
    }
    public String getAudit_branch_name() {
        return audit_branch_name;
    }
    public void setAudit_branch_name(String audit_branch_name) {
        this.audit_branch_name = audit_branch_name;
    }
    public String getAudit_employee_name() {
        return audit_employee_name;
    }
    public void setAudit_employee_name(String audit_employee_name) {
        this.audit_employee_name = audit_employee_name;
    }
     
    
}
