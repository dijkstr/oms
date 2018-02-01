package com.hundsun.boss.modules.charge.entity.setting;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.hundsun.boss.base.hibernate.IdEntity;

/**
 * 计费模式选项Entity
 */
@Entity
@Table(name = "charge_price")
public class ChargePrice extends IdEntity<ChargePrice> {

    private static final long serialVersionUID = 1L;
    private String feemodel_id;
    private Double price;
    private Double fee_ratio;
    private Double fee_ratio_division;
    private String fix_charge_type;
    private Double fixed_charge;
    private String max_type;
    private Double max_consume;
    private String option_name;
    private String step_interval;
    private Double step_begin;
    private Double step_end;
    private String step_unit;

    public ChargePrice() {
        super();
    }

    public ChargePrice(String id) {
        this();
        this.id = id;
    }

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

    public String getFix_charge_type() {
        return fix_charge_type;
    }

    public void setFix_charge_type(String fix_charge_type) {
        this.fix_charge_type = fix_charge_type;
    }

    public Double getFixed_charge() {
        return fixed_charge;
    }

    public void setFixed_charge(Double fixed_charge) {
        this.fixed_charge = fixed_charge;
    }

    public String getMax_type() {
        return max_type;
    }

    public void setMax_type(String max_type) {
        this.max_type = max_type;
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
