package com.hundsun.boss.modules.charge.form.bill;

import com.hundsun.boss.base.page.Page;

/**
 * 详单管理Entity
 */
@SuppressWarnings("rawtypes")
public class ChargeReceiptForm extends Page {
    private String bill_id;
    private String batch_no;
    private String charge_begin_date;
    private String contract_id;
    private String customer_id;
    private String user_name;
    private String charge_begin_month;
    private String charge_end_month;
    private String fix_charge_type;
    private String id;
    private String flag;

    private String office_id;
    private String dept;

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getCharge_begin_date() {
        return charge_begin_date;
    }

    public void setCharge_begin_date(String charge_begin_date) {
        this.charge_begin_date = charge_begin_date;
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

    public String getCharge_begin_month() {
        return charge_begin_month;
    }

    public void setCharge_begin_month(String charge_begin_month) {
        this.charge_begin_month = charge_begin_month;
    }

    public String getCharge_end_month() {
        return charge_end_month;
    }

    public void setCharge_end_month(String charge_end_month) {
        this.charge_end_month = charge_end_month;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getFix_charge_type() {
        return fix_charge_type;
    }

    public void setFix_charge_type(String fix_charge_type) {
        this.fix_charge_type = fix_charge_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
