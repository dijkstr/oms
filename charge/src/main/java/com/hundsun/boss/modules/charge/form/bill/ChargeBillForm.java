package com.hundsun.boss.modules.charge.form.bill;

import com.hundsun.boss.base.page.Page;

/**
 * 账单Entity
 */
@SuppressWarnings("rawtypes")
public class ChargeBillForm extends Page {

    private String contract_id;
    private String customer_id;
    private String user_name;
    private String charge_begin_date;
    private String charge_end_date;
    private String bill_id;
    private String check_status;
    private String send_status;
    private String service_charge;
    private String receivable;
    private String adjust_amt;
    private String payable;
    private String is_send;
    private String office_id;
    private String mail_subject;
    private String cc_flag;
    private String cm_email;
    private String charge_amt;

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

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getCheck_status() {
        return check_status;
    }

    public void setCheck_status(String check_status) {
        this.check_status = check_status;
    }

    public String getSend_status() {
        return send_status;
    }

    public void setSend_status(String send_status) {
        this.send_status = send_status;
    }

    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    public String getReceivable() {
        return receivable;
    }

    public void setReceivable(String receivable) {
        this.receivable = receivable;
    }

    public String getAdjust_amt() {
        return adjust_amt;
    }

    public void setAdjust_amt(String adjust_amt) {
        this.adjust_amt = adjust_amt;
    }

    public String getPayable() {
        return payable;
    }

    public void setPayable(String payable) {
        this.payable = payable;
    }

    public String getIs_send() {
        return is_send;
    }

    public void setIs_send(String is_send) {
        this.is_send = is_send;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public String getMail_subject() {
        return mail_subject;
    }

    public void setMail_subject(String mail_subject) {
        this.mail_subject = mail_subject;
    }

    public String getCc_flag() {
        return cc_flag;
    }

    public void setCc_flag(String cc_flag) {
        this.cc_flag = cc_flag;
    }

    public String getCm_email() {
        return cm_email;
    }

    public void setCm_email(String cm_email) {
        this.cm_email = cm_email;
    }

    public String getCharge_amt() {
        return charge_amt;
    }

    public void setCharge_amt(String charge_amt) {
        this.charge_amt = charge_amt;
    }

}
