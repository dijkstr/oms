package com.hundsun.boss.modules.forecast.bean;

import org.apache.commons.beanutils.BeanUtils;

import com.hundsun.boss.common.utils.CommonUtil;

/**
 * 收入预测明细行
 */
public class IncomeForecastDataBean {
    public IncomeForecastDataBean() {
    }

    public IncomeForecastDataBean(IncomeForecastDataBean orgbBean) throws Exception {
        BeanUtils.copyProperties(this, orgbBean);
    }

    // 作为主键，内涵为协同合同号+收入类型
    private String id;
    private String office_id;
    private String contract_id;
    private String combine_id;
    private String product_id;
    private String payment_type;
    private String income;
    private String change_income;
    private String service_charge_weight;
    private String charge_begin_date;
    private String charge_end_date;
    private String income_begin_date;
    private String income_end_date;
    private String forecast_flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCombine_id() {
        return combine_id;
    }

    public void setCombine_id(String combine_id) {
        this.combine_id = combine_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getIncome() {
        return income;
    }

    public Double getIncomeDouble() {
        if (CommonUtil.isNullorEmpty(income)) {
            return 0.0;
        } else {
            return Double.valueOf(income);
        }
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public void setIncome(Double income) {
        this.income = String.valueOf(income);
    }

    public String getChange_income() {
        return change_income;
    }

    public Double getChangeIncomeDouble() {
        if (CommonUtil.isNullorEmpty(change_income)) {
            return 0.0;
        } else {
            return Double.valueOf(change_income);
        }
    }

    public void setChange_income(String change_income) {
        this.change_income = change_income;
    }

    public void setChange_income(Double change_income) {
        this.change_income = String.valueOf(change_income);
    }

    public String getService_charge_weight() {
        return service_charge_weight;
    }

    public Double getService_charge_weightDouble() {
        return Double.valueOf(service_charge_weight);
    }

    public void setService_charge_weight(String service_charge_weight) {
        this.service_charge_weight = service_charge_weight;
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

    public String getForecast_flag() {
        return forecast_flag;
    }

    public void setForecast_flag(String forecast_flag) {
        this.forecast_flag = forecast_flag;
    }

}
