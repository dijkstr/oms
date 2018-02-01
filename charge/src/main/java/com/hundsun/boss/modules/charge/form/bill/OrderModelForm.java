package com.hundsun.boss.modules.charge.form.bill;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import com.hundsun.boss.base.form.BaseForm;
import com.hundsun.boss.common.beanvalidator.Numberic;
import com.hundsun.boss.modules.charge.entity.order.OrderModel;

public class OrderModelForm extends BaseForm {
    public OrderModelForm() {
        bindClass = OrderModel.class.getName();
    }

    /**
     * 计费模式名称.
     */
    private String feemodel_name;
    /**
     * 产品编号.
     */
    private String ref_id;
    /**
     * 归属类型.
     */
    private String belong_type;
    /**
     * 计费公式.
     */
    private String fee_formula;
    /**
     * 收费类型.
     */
    private String fee_type;
    /**
     * 保底类型.
     */
    private String min_type;
    /**
     * 保底消费.
     */
    private String min_consume;
    /**
     * 封顶类型.
     */
    private String max_type;
    /**
     * 最高消费.
     */
    private String max_consume;
    /**
     * 折扣比例.
     */
    private String discount;
    /**
     * 固定收费周期.
     */
    private String fix_charge_type;
    /**
     * 是否乘以实际发生天数.
     */
    private String is_multiplied_actualdays;
    
    /**
     * 计费公式名称.
     */
    private String fee_formula_name;
    /**
     * 收费类型名称.
     */
    private String fee_type_name;
    

    private String jsonListOrderPrice;

    private List<OrderPriceForm> orderPrices = new ArrayList<OrderPriceForm>();

    private String bill_id;
    public String getFeemodel_name() {
        return feemodel_name;
    }

    public void setFeemodel_name(String feemodel_name) {
        this.feemodel_name = feemodel_name;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getBelong_type() {
        return belong_type;
    }

    public void setBelong_type(String belong_type) {
        this.belong_type = belong_type;
    }

    public String getFee_formula() {
        return fee_formula;
    }

    public void setFee_formula(String fee_formula) {
        this.fee_formula = fee_formula;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getMin_type() {
        return min_type;
    }

    public void setMin_type(String min_type) {
        this.min_type = min_type;
    }
    
    @Numberic(message = "保底金额必须输入数字类型")
    @Column(nullable = true)
    public String getMin_consume() {
        return min_consume;
    }

    public void setMin_consume(String min_consume) {
        this.min_consume = min_consume;
    }

    public String getMax_type() {
        return max_type;
    }

    public void setMax_type(String max_type) {
        this.max_type = max_type;
    }
    
    @Numberic(message = "封顶金额必须输入数字类型")
    @Column(nullable = true)
    public String getMax_consume() {
        return max_consume;
    }

    public void setMax_consume(String max_consume) {
        this.max_consume = max_consume;
    }

    @Numberic(message = "折扣比例必须输入数字类型")
    @Column(nullable = true)
    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFix_charge_type() {
        return fix_charge_type;
    }

    public void setFix_charge_type(String fix_charge_type) {
        this.fix_charge_type = fix_charge_type;
    }

    public String getIs_multiplied_actualdays() {
        return is_multiplied_actualdays;
    }

    public void setIs_multiplied_actualdays(String is_multiplied_actualdays) {
        this.is_multiplied_actualdays = is_multiplied_actualdays;
    }

    public String getJsonListOrderPrice() {
        return jsonListOrderPrice;
    }

    public void setJsonListOrderPrice(String jsonListOrderPrice) {
        this.jsonListOrderPrice = jsonListOrderPrice;
    }

    public List<OrderPriceForm> getOrderPrices() {
        return orderPrices;
    }

    public void setOrderPrices(List<OrderPriceForm> orderPrices) {
        this.orderPrices = orderPrices;
    }

    public String getFee_formula_name() {
        return fee_formula_name;
    }

    public void setFee_formula_name(String fee_formula_name) {
        this.fee_formula_name = fee_formula_name;
    }

    public String getFee_type_name() {
        return fee_type_name;
    }

    public void setFee_type_name(String fee_type_name) {
        this.fee_type_name = fee_type_name;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

}
