package com.hundsun.boss.modules.forecast.form.income;


public class IncomeForecastForm {

    public IncomeForecastForm() {
    }

    private String office_id;
    private String contract_id;
    private String customer_id;
    private String product_id;
    private String begin_month;
    private String end_month;
    private String dept;

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

    public String getBegin_month() {
        return begin_month;
    }

    public void setBegin_month(String begin_month) {
        this.begin_month = begin_month;
    }

    public String getEnd_month() {
        return end_month;
    }

    public void setEnd_month(String end_month) {
        this.end_month = end_month;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

}
