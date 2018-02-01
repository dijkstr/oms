package com.hundsun.boss.modules.charge.form.finance;

import com.hundsun.boss.base.page.Page;

/**
 * 财务汇总表Entity
 */
@SuppressWarnings("rawtypes")
public class FinSummaryForm extends Page {
    private String office_id;

    private String contract_id;

    private String customer_id;

    private String user_name;

    private String charge_begin_date;

    private String charge_end_date;

    private String finance_total_income;

    private String finance_current_income;

    private String change_income;

    private String product_id;

    private String product_name;

    private String payment_type;

    private String display_name;

    private String income_begin_date;

    private String income_end_date;

    private String detailid;

    private String dept;

    private String charge_month;

    private String batch_no;

    private String update_flag;

    private String audit_employee_name;

    private String audit_branch_name;

    private String income_source;
    //实际使用客户
    private String customername;

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
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

    public String getCharge_begin_date() {
        return charge_begin_date;
    }

    public void setCharge_begin_date(String charge_begin_date) {
        this.charge_begin_date = charge_begin_date;
    }

    public String getCharge_end_date() {
        return charge_end_date;
    }

    public void setCharge_end_date(String charge_end_date) {
        this.charge_end_date = charge_end_date;
    }

    public String getFinance_total_income() {
        return finance_total_income;
    }

    public void setFinance_total_income(String finance_total_income) {
        this.finance_total_income = finance_total_income;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getIncome_begin_date() {
        return income_begin_date;
    }

    public void setIncome_begin_date(String income_begin_date) {
        this.income_begin_date = income_begin_date;
    }

    public String getUpdate_flag() {
        return update_flag;
    }

    public void setUpdate_flag(String update_flag) {
        this.update_flag = update_flag;
    }

    public String getIncome_end_date() {
        return income_end_date;
    }

    public void setIncome_end_date(String income_end_date) {
        this.income_end_date = income_end_date;
    }

    public String getDetailid() {
        return detailid;
    }

    public void setDetailid(String detailid) {
        this.detailid = detailid;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getCharge_month() {
        return charge_month;
    }

    public void setCharge_month(String charge_month) {
        this.charge_month = charge_month;
    }

    public String getFinance_current_income() {
        return finance_current_income;
    }

    public void setFinance_current_income(String finance_current_income) {
        this.finance_current_income = finance_current_income;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getAudit_employee_name() {
        return audit_employee_name;
    }

    public void setAudit_employee_name(String audit_employee_name) {
        this.audit_employee_name = audit_employee_name;
    }

    public String getAudit_branch_name() {
        return audit_branch_name;
    }

    public void setAudit_branch_name(String audit_branch_name) {
        this.audit_branch_name = audit_branch_name;
    }

    public String getChange_income() {
        return change_income;
    }

    public void setChange_income(String change_income) {
        this.change_income = change_income;
    }

    public String getIncome_source() {
        return income_source;
    }

    public void setIncome_source(String income_source) {
        this.income_source = income_source;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

}
