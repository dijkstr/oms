package com.hundsun.boss.modules.charge.entity.report;

/**
 * 第一次超保底
 */
public class ChargeOverMin {
    // 所属部门
    private String office_id;
    // 客户名称
    private String customer_name;
    // 账单编号
    private String bill_id;
    // 协同合同编号
    private String contract_id;
    // 组合下属产品
    private String product_names;
    // 本期开始日期
    private String charge_begin_date;
    // 本期结束日期
    private String charge_end_date;
    // 保底金额
    private String min_consume;
    // 累计技术服务费
    private Double yearly_service_charge;

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getProduct_names() {
        return product_names;
    }

    public void setProduct_names(String product_names) {
        this.product_names = product_names;
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

    public String getMin_consume() {
        return min_consume;
    }

    public void setMin_consume(String min_consume) {
        this.min_consume = min_consume;
    }

    public Double getYearly_service_charge() {
        return yearly_service_charge;
    }

    public void setYearly_service_charge(Double yearly_service_charge) {
        this.yearly_service_charge = yearly_service_charge;
    }

}
