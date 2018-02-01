package com.hundsun.boss.modules.charge.form.bill;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.util.HtmlUtils;

import com.hundsun.boss.base.form.BaseForm;
import com.hundsun.boss.modules.charge.entity.order.OrderInfo;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillInfo;
import com.hundsun.boss.modules.charge.form.bill.ChargeOrderInfo;

public class OrderInfoForm extends BaseForm {
    public OrderInfoForm() {
        bindClass = OrderInfo.class.getName();
    }

    /**
     * 协同合同编号.
     */
    private String contract_id;
    /**
     * 框架合同号
     */
    private String frame_contract_id;
    /**
     * 订单开始日期.
     */
    private String order_begin_date;
    /**
     * 订单结束日期.
     */
    private String order_end_date;
    /**
     * 协同用户id.
     */
    private String customer_id;
    /**
     * 结算周期.
     */
    private String payment_cycle;
    /**
     * 最迟付款日.
     */
    private String pay_deadline;
    /**
     * 收入设置状态
     */
    private String income_status;
    /**
     * 是否发送邮件
     */
    private String is_send;
    /**
     * 协同客户名称。
     */
    private String hs_customername;
    /**
     * 合同签订日期
     */
    private String signeddate;

    private String jsonListOrderCombie;

    private List<OrderCombineForm> orderCombines = new ArrayList<OrderCombineForm>();

    private String bill_id;

    private OrderModelForm ssOrderModel;

    private ChargeOrderInfo chargeOrderInfo;

    private ChargeBillInfo chargeBillInfo;

    private String office_id;

    @NotBlank(message = "协同合同编号不允许为空")
    @Length(min = 1, max = 40, message = "协同合同编号最大长度不能超过40位")
    @Column(unique = true)
    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getFrame_contract_id() {
        return frame_contract_id;
    }

    public void setFrame_contract_id(String frame_contract_id) {
        this.frame_contract_id = frame_contract_id;
    }

    public String getOrder_begin_date() {
        return order_begin_date;
    }

    public void setOrder_begin_date(String order_begin_date) {
        this.order_begin_date = order_begin_date;
    }

    public String getOrder_end_date() {
        return order_end_date;
    }

    public void setOrder_end_date(String order_end_date) {
        this.order_end_date = order_end_date;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getPayment_cycle() {
        return payment_cycle;
    }

    public void setPayment_cycle(String payment_cycle) {
        this.payment_cycle = payment_cycle;
    }

    public String getPay_deadline() {
        return pay_deadline;
    }

    public void setPay_deadline(String pay_deadline) {
        this.pay_deadline = pay_deadline;
    }

    public String getIncome_status() {
        return income_status;
    }

    public void setIncome_status(String income_status) {
        this.income_status = income_status;
    }

    public String getIs_send() {
        return is_send;
    }

    public void setIs_send(String is_send) {
        this.is_send = is_send;
    }

    public String getHs_customername() {
        return hs_customername;
    }

    public void setHs_customername(String hs_customername) {
        this.hs_customername = hs_customername;
    }

    public String getSigneddate() {
        return signeddate;
    }

    public void setSigneddate(String signeddate) {
        this.signeddate = signeddate;
    }

    public String getJsonListOrderCombie() {
        return HtmlUtils.htmlUnescape(jsonListOrderCombie);
    }

    public void setJsonListOrderCombie(String jsonListOrderCombie) {
        this.jsonListOrderCombie = jsonListOrderCombie;
    }

    public List<OrderCombineForm> getOrderCombines() {
        return orderCombines;
    }

    public void setOrderCombines(List<OrderCombineForm> orderCombines) {
        this.orderCombines = orderCombines;
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

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

}
