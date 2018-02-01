package com.hundsun.boss.modules.charge.entity.order;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.hundsun.boss.base.hibernate.IdEntity;

/**
 * 框架合同结算信息Entity
 */
@Entity
@Table(name = "order_frame_price")
public class OrderFramePrice extends IdEntity<OrderFramePrice> {

    private static final long serialVersionUID = 1L;

    public OrderFramePrice() {
        super();
    }

    public OrderFramePrice(String id) {
        this();
        this.id = id;
    }

    /**
     *合同编号
     */
    private String contract_id;
    /**
     * 子合同编号
     */
    private String sub_contract_id;
    /**
     * 开始日期
     */
    private String charge_begin_date;
    /**
     * 结束日期
     */
    private String charge_end_date;
    /**
     * 计费来源
     */
    private String charge_source;
    /**
     * 显示名称.
     */
    private String option_name;
    /**
     * 价格.
     */
    private Double price;
    /**
     * 结算比例.
     */
    private Double fee_ratio;
    /**
     * 结算比例拆分天数.
     */
    private Double fee_ratio_division;
    /**
     * 按固定金额结束
     */
    private Double fixed_charge;
    /**
     * 最高消费.
     */
    private Double max_consume;
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

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getSub_contract_id() {
        return sub_contract_id;
    }

    public void setSub_contract_id(String sub_contract_id) {
        this.sub_contract_id = sub_contract_id;
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

    public String getCharge_source() {
        return charge_source;
    }

    public void setCharge_source(String charge_source) {
        this.charge_source = charge_source;
    }

    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
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
