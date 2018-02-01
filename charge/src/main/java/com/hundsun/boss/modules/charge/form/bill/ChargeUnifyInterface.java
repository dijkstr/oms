package com.hundsun.boss.modules.charge.form.bill;

import com.hundsun.boss.base.page.Page;

/**
 * 计费统一接口
 *
 */
public class ChargeUnifyInterface extends Page<ChargeUnifyInterface> {

    private String batch_no;
    private String contract_id;
    private String oc_date;
    private String customer_id;
    private String office_id;
    private String product_id;
    private String fee_type;
    private String data1;
    private String data2;
    private String data3;
    private String data4;
    private String data5;
    private String comment1;
    private String comment2;
    private String comment3;
    private String comment4;
    private String comment5;
    private String customer_name;
    private String product_name;
    private String department;
    private String fee_type_name;
    private String charge_begin_month;
    private String charge_end_month;
    /** 是否是最新批次的数据 */
    private String is_newest;
    /** 所属部门 */
    private String dept;

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getOc_date() {
        return oc_date;
    }

    public void setOc_date(String oc_date) {
        this.oc_date = oc_date;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
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

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    public String getData4() {
        return data4;
    }

    public void setData4(String data4) {
        this.data4 = data4;
    }

    public String getData5() {
        return data5;
    }

    public void setData5(String data5) {
        this.data5 = data5;
    }

    public String getComment1() {
        return comment1;
    }

    public void setComment1(String comment1) {
        this.comment1 = comment1;
    }

    public String getComment2() {
        return comment2;
    }

    public void setComment2(String comment2) {
        this.comment2 = comment2;
    }

    public String getComment3() {
        return comment3;
    }

    public void setComment3(String comment3) {
        this.comment3 = comment3;
    }

    public String getComment4() {
        return comment4;
    }

    public void setComment4(String comment4) {
        this.comment4 = comment4;
    }

    public String getComment5() {
        return comment5;
    }

    public void setComment5(String comment5) {
        this.comment5 = comment5;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFee_type_name() {
        return fee_type_name;
    }

    public void setFee_type_name(String fee_type_name) {
        this.fee_type_name = fee_type_name;
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

    public String getIs_newest() {
        return is_newest;
    }

    public void setIs_newest(String is_newest) {
        this.is_newest = is_newest;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

}
