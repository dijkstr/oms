package com.hundsun.boss.modules.charge.form.order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hundsun.boss.base.form.BaseForm;
import com.hundsun.boss.modules.charge.entity.order.OrderCombine;
import com.hundsun.boss.modules.charge.entity.ordervalidate.OrderValidate;

public class OrderCombineForm extends BaseForm {
    public OrderCombineForm() {
        bindClass = OrderCombine.class.getName();
    }

    /*
     * 协同合同号.
     */
    private String contract_id;
    /*
     * 组合开始时间。
     */
    private String combine_begin_date;
    /*
     * 组合结束时间.
     */
    private String combine_end_date;
    /*
     * 是否产生技术服务费收入.
     */
    private String tech_charge_income_flag;

    private String jsonListOrderProduct;

    private List<OrderProductForm> orderProducts = new ArrayList<OrderProductForm>();

    private String jsonListPayment;

    private List<OrderAdvPaymentForm> orderAdvPayments = new ArrayList<OrderAdvPaymentForm>();

    private String jsonListOrderModel;

    private List<OrderModelForm> orderModels = new ArrayList<OrderModelForm>();

    private Set<OrderValidate> orderValidates = new HashSet<OrderValidate>();

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getCombine_begin_date() {
        return combine_begin_date;
    }

    public void setCombine_begin_date(String combine_begin_date) {
        this.combine_begin_date = combine_begin_date;
    }

    public String getCombine_end_date() {
        return combine_end_date;
    }

    public void setCombine_end_date(String combine_end_date) {
        this.combine_end_date = combine_end_date;
    }

    public String getJsonListOrderProduct() {
        return jsonListOrderProduct;
    }

    public void setJsonListOrderProduct(String jsonListOrderProduct) {
        this.jsonListOrderProduct = jsonListOrderProduct;
    }

    public String getJsonListPayment() {
        return jsonListPayment;
    }

    public void setJsonListPayment(String jsonListPayment) {
        this.jsonListPayment = jsonListPayment;
    }

    public String getJsonListOrderModel() {
        return jsonListOrderModel;
    }

    public void setJsonListOrderModel(String jsonListOrderModel) {
        this.jsonListOrderModel = jsonListOrderModel;
    }

    public List<OrderProductForm> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProductForm> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public List<OrderAdvPaymentForm> getOrderAdvPayments() {
        return orderAdvPayments;
    }

    public void setOrderAdvPayments(List<OrderAdvPaymentForm> orderAdvPayments) {
        this.orderAdvPayments = orderAdvPayments;
    }

    public List<OrderModelForm> getOrderModels() {
        return orderModels;
    }

    public void setOrderModels(List<OrderModelForm> orderModels) {
        this.orderModels = orderModels;
    }

    public Set<OrderValidate> getOrderValidates() {
        return orderValidates;
    }

    public void setOrderValidates(Set<OrderValidate> orderValidates) {
        this.orderValidates = orderValidates;
    }

    public String getTech_charge_income_flag() {
        return tech_charge_income_flag;
    }

    public void setTech_charge_income_flag(String tech_charge_income_flag) {
        this.tech_charge_income_flag = tech_charge_income_flag;
    }

}
