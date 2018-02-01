package com.hundsun.boss.modules.charge.form.bill;

import com.hundsun.boss.base.page.Page;

/**
 * 账单Entity
 */
@SuppressWarnings("rawtypes")
public class ChargeBillSearchForm extends Page {

    private String charge_begin_month;
    private String charge_end_month;
    private String office_id;
    private String contract_id;
    private String customer_id;
    private String user_name;
    private String charge_off;
    private String send_status;
    private String bill_id;
    private String dept;
    private String charge_begin_date;
    private String charge_end_date;

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

    public String getCharge_off() {
        return charge_off;
    }

    public void setCharge_off(String charge_off) {
        this.charge_off = charge_off;
    }

    public String getSend_status() {
        return send_status;
    }

    public void setSend_status(String send_status) {
        this.send_status = send_status;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
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

}
