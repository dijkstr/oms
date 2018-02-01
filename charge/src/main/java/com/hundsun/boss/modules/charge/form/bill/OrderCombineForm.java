package com.hundsun.boss.modules.charge.form.bill;

import java.util.ArrayList;
import java.util.List;

import com.hundsun.boss.base.form.BaseForm;
import com.hundsun.boss.modules.charge.entity.order.OrderCombine;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillInfo;
import com.hundsun.boss.modules.charge.form.bill.ChargeOrderInfo;
import com.hundsun.boss.modules.charge.form.bill.OrderProductForm;

public class OrderCombineForm extends BaseForm {
    public OrderCombineForm() {
        bindClass = OrderCombine.class.getName();
    }

    /**
     * 协同合同号.
     */
    private String contract_id;
    private String combine_id;
    /**
     * 组合开始时间。
     */
    private String combine_begin_date;
    /**
     * 组合结束时间.
     */
    private String combine_end_date;

    private String jsonListOrderProduct;

    private List<OrderProductForm> orderProducts = new ArrayList<OrderProductForm>();

    private String jsonListPayment;

    private List<OrderAdvPaymentForm> orderAdvPayments = new ArrayList<OrderAdvPaymentForm>();

    private String jsonListOrderModel;

    private List<OrderModelForm> orderModels = new ArrayList<OrderModelForm>();

    private String bill_id;

    private OrderModelForm ssOrderModel;

    private ChargeOrderInfo chargeOrderInfo;

    private ChargeBillInfo chargeBillInfo;

    private int view_flag;

    private String charge_begin_date;

    private String charge_end_date;

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

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public OrderModelForm getSsOrderModel() {
        return ssOrderModel;
    }

    public void setSsOrderModel(OrderModelForm ssOrderModel) {
        this.ssOrderModel = ssOrderModel;
    }

    public ChargeOrderInfo getChargeOrderInfo() {
        return chargeOrderInfo;
    }

    public void setChargeOrderInfo(ChargeOrderInfo chargeOrderInfo) {
        this.chargeOrderInfo = chargeOrderInfo;
    }

    public ChargeBillInfo getChargeBillInfo() {
        return chargeBillInfo;
    }

    public void setChargeBillInfo(ChargeBillInfo chargeBillInfo) {
        this.chargeBillInfo = chargeBillInfo;
    }

    public int getView_flag() {
        return view_flag;
    }

    public void setView_flag(int view_flag) {
        this.view_flag = view_flag;
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

}
