package com.hundsun.boss.modules.charge.form.receipt;

import com.hundsun.boss.base.page.Page;
/**
 * 到款form
 * @author feigq
 *
 */
@SuppressWarnings("rawtypes")
public class ChargeReceiptForm extends Page{
    
    // 到款单号
    private String bankreceipt_serialno;
    // 业务单元
    private String department;
    // 合同编号
    private String contract_id;
    // 协同销售产品id
    private String ex_product_id;
    //销售产品名称
    private String product_name;
    //到款金额
    private Double bankreceipt_amount;
    //到款开始日期
    private String bankreceipt_month_begin;
    //到款结束日期
    private String bankreceipt_month_end;
    
    //到款行id
    private String receiveid;
    //创建时间
    private String bankreceipt_date;
    
    private String bankreceipt_month;
    
    //协同客户号
    private String customerid;
    //协同客户名称
    private String custname;
    //销售产品编号
    private String saleprdid;
    //考核部门
    private String audit_branch_name;
    //考核人
    private String audit_employee_name;
    public String getBankreceipt_serialno() {
        return bankreceipt_serialno;
    }
    public void setBankreceipt_serialno(String bankreceipt_serialno) {
        this.bankreceipt_serialno = bankreceipt_serialno;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getContract_id() {
        return contract_id;
    }
    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }
    public String getEx_product_id() {
        return ex_product_id;
    }
    public void setEx_product_id(String ex_product_id) {
        this.ex_product_id = ex_product_id;
    }
    public String getProduct_name() {
        return product_name;
    }
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
    public Double getBankreceipt_amount() {
        return bankreceipt_amount;
    }
    public void setBankreceipt_amount(Double bankreceipt_amount) {
        this.bankreceipt_amount = bankreceipt_amount;
    }
    public String getBankreceipt_month_begin() {
        return bankreceipt_month_begin;
    }
    public void setBankreceipt_month_begin(String bankreceipt_month_begin) {
        this.bankreceipt_month_begin = bankreceipt_month_begin;
    }
    public String getBankreceipt_month_end() {
        return bankreceipt_month_end;
    }
    public void setBankreceipt_month_end(String bankreceipt_month_end) {
        this.bankreceipt_month_end = bankreceipt_month_end;
    }
    public String getReceiveid() {
        return receiveid;
    }
    public void setReceiveid(String receiveid) {
        this.receiveid = receiveid;
    }
    public String getBankreceipt_date() {
        return bankreceipt_date;
    }
    public void setBankreceipt_date(String bankreceipt_date) {
        this.bankreceipt_date = bankreceipt_date;
    }
    public String getBankreceipt_month() {
        return bankreceipt_month;
    }
    public void setBankreceipt_month(String bankreceipt_month) {
        this.bankreceipt_month = bankreceipt_month;
    }
    public String getCustomerid() {
        return customerid;
    }
    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }
    public String getCustname() {
        return custname;
    }
    public void setCustname(String custname) {
        this.custname = custname;
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
