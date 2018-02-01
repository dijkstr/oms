package com.hundsun.boss.modules.charge.entity.order;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.hundsun.boss.base.hibernate.IdEntity;

/***
 * 计费模式选项.
 */
@Entity
@Table(name = "order_price")
public class OrderPrice extends IdEntity<OrderPrice> {

    private static final long serialVersionUID = 1L;

    /**
     * 计费模式代码.
     */
    private String feemodel_id;
    /**
     * 价格.
     */
    private Double price;
    /**
     * 费率.
     */
    private Double fee_ratio;
    /**
     * 费率拆分天数.
     */
    private Double fee_ratio_division;
    /**
     * 阶梯固定收费.
     */
    private Double fixed_charge;
    /**
     * 最高消费.
     */
    private Double max_consume;
    /**
     * 规格选项名称.
     */
    private String option_name;
    /**
     * 阶梯区间.
     */
    private String step_interval;
    /**
     * 区间开始.
     */
    private Double step_begin;
    /**
     * 区间结束.
     */
    private Double step_end;
    /**
     * 阶梯区间单位.
     */
    private String step_unit;

    public String getFeemodel_id() {
        return feemodel_id;
    }

    public void setFeemodel_id(String feemodel_id) {
        this.feemodel_id = feemodel_id;
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getFee_ratio() {
        return fee_ratio;
    }

    public void setFee_ratio(Double fee_ratio) {
        this.fee_ratio = fee_ratio;
    }

    public Double getFee_ratio_division() {
        return fee_ratio_division;
    }

    public void setFee_ratio_division(Double fee_ratio_division) {
        this.fee_ratio_division = fee_ratio_division;
    }

    public Double getFixed_charge() {
        return fixed_charge;
    }

    public void setFixed_charge(Double fixed_charge) {
        this.fixed_charge = fixed_charge;
    }

    public Double getMax_consume() {
        return max_consume;
    }

    public void setMax_consume(Double max_consume) {
        this.max_consume = max_consume;
    }

    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
    }

    public String getStep_interval() {
        return step_interval;
    }

    public void setStep_interval(String step_interval) {
        this.step_interval = step_interval;
    }

    public Double getStep_begin() {
        return step_begin;
    }

    public void setStep_begin(Double step_begin) {
        this.step_begin = step_begin;
    }

    public Double getStep_end() {
        return step_end;
    }

    public void setStep_end(Double step_end) {
        this.step_end = step_end;
    }

    public String getStep_unit() {
        return step_unit;
    }

    public void setStep_unit(String step_unit) {
        this.step_unit = step_unit;
    }

}
