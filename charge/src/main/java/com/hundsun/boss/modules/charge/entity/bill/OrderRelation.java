package com.hundsun.boss.modules.charge.entity.bill;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.hundsun.boss.base.hibernate.IdEntity;
import com.hundsun.boss.modules.sys.entity.Office;

/**
 * 账单联系人Entity
 */
@Entity
@Table(name = "order_relation")
public class OrderRelation extends IdEntity<OrderRelation> {

    private static final long serialVersionUID = 1L;
    private String contract_id;
    private String relation_name;
    private String mobile_tel;
    private String relation_tel;
    private String cc;
    private String bcc;
    private String email;
    private String duties;
    private String office_id;
    private String customer_id;
    private String user_name;
    private Office office;  // 归属部门

    public OrderRelation() {
        super();
    }

    public OrderRelation(String id) {
        this();
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "office_id", referencedColumnName = "code", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

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

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
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

    public static long getSerialversionuid() {
        return serialVersionUID;
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
