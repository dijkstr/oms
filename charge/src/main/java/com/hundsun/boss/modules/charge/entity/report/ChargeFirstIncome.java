package com.hundsun.boss.modules.charge.entity.report;

/**
 * 第一次收入
 */
public class ChargeFirstIncome {
    // 所属部门
    private String office_id;
    // 协同合同编号
    private String contract_id;
    // 收入开始日期
    private String income_begin_date;
    // 收入结束日期
    private String income_end_date;
    // 第一次发生日期
    private String charge_begin_date;
    // 第一次发生收入金额
    private Double change_income;

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

    public String getIncome_begin_date() {
        return income_begin_date;
    }

    public void setIncome_begin_date(String income_begin_date) {
        this.income_begin_date = income_begin_date;
    }

    public String getIncome_end_date() {
        return income_end_date;
    }

    public void setIncome_end_date(String income_end_date) {
        this.income_end_date = income_end_date;
    }

    public String getCharge_begin_date() {
        return charge_begin_date;
    }

    public void setCharge_begin_date(String charge_begin_date) {
        this.charge_begin_date = charge_begin_date;
    }

    public Double getChange_income() {
        return change_income;
    }

    public void setChange_income(Double change_income) {
        this.change_income = change_income;
    }

}
