package com.hundsun.boss.modules.charge.form.bill;

import com.hundsun.boss.base.form.BaseForm;
import com.hundsun.boss.modules.charge.entity.bill.OrderRelation;

/**
 * 账单联系人Entity
 */
public class RelationForm extends BaseForm {
    public RelationForm() {
        bindClass = OrderRelation.class.getName();
    }

    private String contract_id;
    private String relation_name;
    private String mobile_tel;
    private String relation_tel;
    private String bcc;
    private String cc;
    private String email;
    private String duties;
    private String office_id;
    private String customer_id;
    private String user_name;

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getRelation_name() {
        return relation_name;
    }

    public void setRelation_name(String relation_name) {
        this.relation_name = relation_name;
    }

    public String getMobile_tel() {
        return mobile_tel;
    }

    public void setMobile_tel(String mobile_tel) {
        this.mobile_tel = mobile_tel;
    }

    public String getRelation_tel() {
        return relation_tel;
    }

    public void setRelation_tel(String relation_tel) {
        this.relation_tel = relation_tel;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
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

}
