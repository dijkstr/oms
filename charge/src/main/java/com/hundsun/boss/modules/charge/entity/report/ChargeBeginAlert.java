package com.hundsun.boss.modules.charge.entity.report;

/**
 * 合同启动
 */
public class ChargeBeginAlert {
    // 所属部门
    private String office_id;
    // 协同合同编号
    private String contract_id;
    // 合同开始日期
    private String order_begin_date;
    // 创建日期
    private String create_date;
    // 本期开始日期
    private String charge_begin_date;
    // 本期应付
    private Double payable;
    // 累计收入
    private Double income;
    // 本期收入
    private Double change_income;
    // 本期技术服务费
    private Double change_service_charge;
    // 客户名称
    private String customer_name;

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

    public String getOrder_begin_date() {
        return order_begin_date;
    }

    public void setOrder_begin_date(String order_begin_date) {
        this.order_begin_date = order_begin_date;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getCharge_begin_date() {
        return charge_begin_date;
    }

    public void setCharge_begin_date(String charge_begin_date) {
        this.charge_begin_date = charge_begin_date;
    }

    public Double getPayable() {
        return payable;
    }

    public void setPayable(Double payable) {
        this.payable = payable;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Double getChange_income() {
        return change_income;
    }

    public void setChange_income(Double change_income) {
        this.change_income = change_income;
    }

    public Double getChange_service_charge() {
        return change_service_charge;
    }

    public void setChange_service_charge(Double change_service_charge) {
        this.change_service_charge = change_service_charge;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

}
