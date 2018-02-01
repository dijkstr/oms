package com.hundsun.boss.modules.charge.form.order;

import javax.persistence.Column;

import org.hibernate.validator.constraints.Length;

import com.hundsun.boss.base.form.BaseForm;
import com.hundsun.boss.common.beanvalidator.Numberic;
import com.hundsun.boss.modules.charge.entity.order.OrderPrice;

/***
 * 计费模式选项.
 */
public class OrderPriceForm extends BaseForm {
    public OrderPriceForm() {
        bindClass = OrderPrice.class.getName();
    }

    /**
     * 计费模式代码.
     */
    private String feemodel_id;
    /**
     * 价格.
     */
    private String price;
    /**
     * 费率.
     */
    private String fee_ratio;
    /**
     * 费率拆分天数.
     */
    private String fee_ratio_division;
    /**
     * 阶梯固定收费单位.
     */
    private String fixed_charge;
    /**
     * 最高消费.
     */
    private String max_consume;
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
    private String step_begin;
    /**
     * 区间结束.
     */
    private String step_end;
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

    @Numberic(message = "单价必须输入数字类型")
    @Column(nullable = true)
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Numberic(message = "费率必须输入数字类型")
    @Column(nullable = true)
    public String getFee_ratio() {
        return fee_ratio;
    }

    public void setFee_ratio(String fee_ratio) {
        this.fee_ratio = fee_ratio;
    }

    @Numberic(message = "费率拆分规则必须输入数字类型")
    @Column(nullable = true)
    public String getFee_ratio_division() {
        return fee_ratio_division;
    }

    public void setFee_ratio_division(String fee_ratio_division) {
        this.fee_ratio_division = fee_ratio_division;
    }

    @Numberic(message = "固定费用必须输入数字类型")
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

    @Length(min = 0, max = 100, message = "选项名称最大长度不能超过100位")
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
