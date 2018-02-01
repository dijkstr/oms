package com.hundsun.boss.modules.charge.entity.report;

/**
 * 预付查询
 */
public class OrderAdvPayment {
    // 所属部门
    private String office_id;
    // 客户编号
    private String customer_id;
    // 客户名称
    private String customer_name;
    // 协同合同编号
    private String contract_id;
    // 销售产品编号
    private String product_ids;
    // 销售产品名称
    private String product_names;
    // 费用类型
    private String payment_type;
    // 预付日期
    private String display_date;
    // 预付金额
    private Double amount;

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

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getProduct_ids() {
        return product_ids;
    }

    public void setProduct_ids(String product_ids) {
        this.product_ids = product_ids;
    }

    public String getProduct_names() {
        return product_names;
    }

    public void setProduct_names(String product_names) {
        this.product_names = product_names;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getDisplay_date() {
        return display_date;
    }

    public void setDisplay_date(String display_date) {
        this.display_date = display_date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
