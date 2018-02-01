package com.hundsun.boss.modules.charge.entity.finance;

import com.hundsun.boss.base.page.Page;

/**
 * 调账管理Entity
 */
@SuppressWarnings("rawtypes")
public class AdjustBillForm extends Page {

    /** 账单日期 */
    private String adjust_date;
    /** 协同合同编号 */
    private String contract_id;
    /** 协同客户编号 */
    private String customer_id;
    /** 客户名称 */
    private String user_name;
    /** 调整金额 */
    private String adjust_balance;
    /** 调账状态 */
    private String bill_adjust_status;
    /** 操作员 */
    private String operater_no;
    /** 开始日期 */
    private String begin_date;
    /** 结束日期 */
    private String end_date;
    /** 客户经理1 */
    private String customermanagername;
    /** 客户经理2 */
    private String customermanager2name;
    private String dept;
    private String office_id;
    private String id;
    private String remark;

    public AdjustBillForm() {
        super();
    }

    public String getAdjust_date() {
        return adjust_date;
    }

    public void setAdjust_date(String adjust_date) {
        this.adjust_date = adjust_date;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAdjust_balance() {
        return adjust_balance;
    }

    public void setAdjust_balance(String adjust_balance) {
        this.adjust_balance = adjust_balance;
    }

    public String getBill_adjust_status() {
        return bill_adjust_status;
    }

    public void setBill_adjust_status(String bill_adjust_status) {
        this.bill_adjust_status = bill_adjust_status;
    }

    public String getOperater_no() {
        return operater_no;
    }

    public void setOperater_no(String operater_no) {
        this.operater_no = operater_no;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public String getCustomermanagername() {
        return customermanagername;
    }

    public void setCustomermanagername(String customermanagername) {
        this.customermanagername = customermanagername;
    }

    public String getCustomermanager2name() {
        return customermanager2name;
    }

    public void setCustomermanager2name(String customermanager2name) {
        this.customermanager2name = customermanager2name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
