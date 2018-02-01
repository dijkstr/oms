package com.hundsun.boss.modules.charge.form.order;

import javax.persistence.Column;

import org.hibernate.validator.constraints.Length;

import com.hundsun.boss.base.form.BaseForm;
import com.hundsun.boss.common.beanvalidator.Numberic;
import com.hundsun.boss.modules.charge.entity.order.OrderFramePrice;

/**
 * 框架合同结算信息
 */
public class OrderFramePriceForm extends BaseForm {
    public OrderFramePriceForm() {
        bindClass = OrderFramePrice.class.getName();
    }

    /**
     * 计费来源
     */
    private String contract_id;
    /**
     * 显示名称
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
    private String price;
    /**
     * 结算比例.
     */
    private String fee_ratio;
    /**
     * 结算比例拆分天数.
     */
    private String fee_ratio_division;
    /**
     * 按固定金额结束
     */
    private String fixed_charge;
    /**
     * 最高消费.
     */
    private String max_consume;
    /**
     * 阶梯区间.
     */
    private String step_interval;
    /**
     * 区间开始.
     */
    private String step_begin;
    /**
     * 区间结束.
     */
    private String step_end;
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

    @Length(min = 0, max = 100, message = "显示名称最大长度不能超过100位")
    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
    }

    @Numberic(message = "单价必须输入数字类型")
    @Column(nullable = true)
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Numberic(message = "结算比例必须输入数字类型")
    @Column(nullable = true)
    public String getFee_ratio() {
        return fee_ratio;
    }

    public void setFee_ratio(String fee_ratio) {
        this.fee_ratio = fee_ratio;
    }

    @Numberic(message = "结算比例规则必须输入数字类型")
    @Column(nullable = true)
    public String getFee_ratio_division() {
        return fee_ratio_division;
    }

    public void setFee_ratio_division(String fee_ratio_division) {
        this.fee_ratio_division = fee_ratio_division;
    }

    @Numberic(message = "按固定金额结算必须输入数字类型")
    @Column(nullable = true)
    public String getFixed_charge() {
        return fixed_charge;
    }

    public void setFixed_charge(String fixed_charge) {
        this.fixed_charge = fixed_charge;
    }

    @Numberic(message = "封顶费用必须输入数字类型")
    @Column(nullable = true)
    public String getMax_consume() {
        return max_consume;
    }

    public void setMax_consume(String max_consume) {
        this.max_consume = max_consume;
    }

    public String getStep_interval() {
        return step_interval;
    }

    public void setStep_interval(String step_interval) {
        this.step_interval = step_interval;
    }

    @Numberic(message = "阶梯开始必须输入数字类型")
    @Column(nullable = true)
    public String getStep_begin() {
        return step_begin;
    }

    public void setStep_begin(String step_begin) {
        this.step_begin = step_begin;
    }

    @Numberic(message = "阶梯结束必须输入数字类型")
    @Column(nullable = true)
    public String getStep_end() {
        return step_end;
    }

    public void setStep_end(String step_end) {
        this.step_end = step_end;
    }

    public String getStep_unit() {
        return step_unit;
    }

    public void setStep_unit(String step_unit) {
        this.step_unit = step_unit;
    }
}
