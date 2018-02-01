package com.hundsun.boss.modules.charge.form.bill;

import com.hundsun.boss.base.page.Page;

/**
 * 错单管理Entity
 */
@SuppressWarnings("rawtypes")
public class WrongBillForm extends Page {
    private String id;
    private String batch_no;
    private String oc_date;
    private String contract_id;
    private String customer_id;
    private String product_id;
    private String fee_type;
    private String wrong_reason;
    private String wrong_status;
    private String create_datetime;
    private String office_id;
    private String dept;
    private String begin_date;
    private String end_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getOc_date() {
        return oc_date;
    }

    public void setOc_date(String oc_date) {
        this.oc_date = oc_date;
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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getWrong_reason() {
        return wrong_reason;
    }

    public void setWrong_reason(String wrong_reason) {
        this.wrong_reason = wrong_reason;
    }

    public String getWrong_status() {
        return wrong_status;
    }

    public void setWrong_status(String wrong_status) {
        this.wrong_status = wrong_status;
    }

    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
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

}
