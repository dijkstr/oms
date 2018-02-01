package com.hundsun.boss.modules.charge.form.order;

import javax.persistence.Column;

import org.hibernate.validator.constraints.Length;

import com.hundsun.boss.base.form.BaseForm;
import com.hundsun.boss.common.beanvalidator.Numberic;
import com.hundsun.boss.modules.charge.entity.order.OrderAdvPayment;

/**
 * 订单费用预付
 */
public class OrderAdvPaymentForm extends BaseForm {
    public OrderAdvPaymentForm() {
        bindClass = OrderAdvPayment.class.getName();
    }

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
    private String amount;
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

    @Length(min = 0, max = 50, message = "显示名称最大长度不能超过50位")
    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    @Numberic(message = "预付金额必须输入数字类型")
    @Column(nullable = true)
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
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
