package com.hundsun.boss.modules.charge.form.order;

import com.hundsun.boss.base.form.BaseForm;

public class ProductLineInfoForm extends BaseForm {

    /**
     * 协同合同编号.
     */
    private String contract_id;
    /**
     * 协同用户id.
     */
    private String customer_id;
    /**
     * 协调产品id
     */
    private String product_id;
    /**
     * 订单开始日期.
     */
    private String product_line_date;

    /**
     * 状态
     */
    private String online_status;

    /**
     * 协同客户名称。
     */
    private String hs_customername;

    private String dept;
    
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

    public String getProduct_line_date() {
        return product_line_date;
    }

    public void setProduct_line_date(String product_line_date) {
        this.product_line_date = product_line_date;
    }

    public String getOnline_status() {
        return online_status;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }

    public String getHs_customername() {
        return hs_customername;
    }

    public void setHs_customername(String hs_customername) {
        this.hs_customername = hs_customername;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

}
