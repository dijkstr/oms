package com.hundsun.boss.modules.forecast.bean;

/**
 * 保底封顶值
 */
public class MinMaxConsumeDataBean {
    private String contract_id;
    private String combine_id;
    private String product_id;
    private String prod_begin_date;
    private String prod_end_date;
    private Double min_consume;
    private String min_type;
    private Double max_consume;
    private String max_type;
    private Double split_ratio;

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

    public String getProd_begin_date() {
        return prod_begin_date;
    }

    public void setProd_begin_date(String prod_begin_date) {
        this.prod_begin_date = prod_begin_date;
    }

    public String getProd_end_date() {
        return prod_end_date;
    }

    public void setProd_end_date(String prod_end_date) {
        this.prod_end_date = prod_end_date;
    }

    public Double getMin_consume() {
        return min_consume;
    }

    public void setMin_consume(Double min_consume) {
        this.min_consume = min_consume;
    }

    public String getMin_type() {
        return min_type;
    }

    public void setMin_type(String min_type) {
        this.min_type = min_type;
    }

    public Double getMax_consume() {
        return max_consume;
    }

    public void setMax_consume(Double max_consume) {
        this.max_consume = max_consume;
    }

    public String getMax_type() {
        return max_type;
    }

    public void setMax_type(String max_type) {
        this.max_type = max_type;
    }

    public Double getSplit_ratio() {
        return split_ratio;
    }

    public void setSplit_ratio(Double split_ratio) {
        this.split_ratio = split_ratio;
    }

}
