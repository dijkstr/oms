package com.hundsun.boss.modules.charge.entity.order;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.hundsun.boss.base.hibernate.IdEntity;

/**
 * 订单费用预付
 */
@Entity
@Table(name = "order_adv_payment")
public class OrderAdvPayment extends IdEntity<OrderAdvPayment> {

    private static final long serialVersionUID = 1L;

    /**
     * 协同合同号.
     */
    private String contract_id;
    /**
     * 组合编号
     */
    private String combine_id;
    /**
     * 预付类型
     */
    private String payment_type;
    /**
     * 显示名称
     */
    private String display_name;
    /**
     * 支付金额。
     */
    private Double amount;
    /**
     * 预付日期.
     */
    private String advance_date;
    /**
     * 显示日期
     */
    private String display_date;
    /**
     * 支付单位
     */
    private String fee_unit;
    
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
    public String getPayment_type() {
        return payment_type;
    }
    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }
    public String getDisplay_name() {
        return display_name;
    }
    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getAdvance_date() {
        return advance_date;
    }
    public void setAdvance_date(String advance_date) {
        this.advance_date = advance_date;
    }
    public String getDisplay_date() {
        return display_date;
    }
    public void setDisplay_date(String display_date) {
        this.display_date = display_date;
    }
    public String getFee_unit() {
        return fee_unit;
    }
    public void setFee_unit(String fee_unit) {
        this.fee_unit = fee_unit;
    }

}
