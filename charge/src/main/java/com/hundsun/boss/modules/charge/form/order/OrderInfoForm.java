package com.hundsun.boss.modules.charge.form.order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.util.HtmlUtils;

import com.hundsun.boss.base.form.BaseForm;
import com.hundsun.boss.modules.charge.entity.order.OrderInfo;
import com.hundsun.boss.modules.charge.entity.ordervalidate.OrderValidate;

public class OrderInfoForm extends BaseForm {
    public OrderInfoForm() {
        bindClass = OrderInfo.class.getName();
    }

    private String id;

    /*
     * 协同合同编号.
     */
    private String contract_id;
    /*
     * 框架合同号
     */
    private String frame_contract_id;
    /*
     * 所属部门.
     */
    private String office_id;
    /*
     * 订单开始日期.
     */
    private String order_begin_date;
    /*
     * 订单结束日期.
     */
    private String order_end_date;
    /*
     * 协同用户id.
     */
    private String customer_id;
    /*
     * 结算周期.
     */
    private String payment_cycle;
    /*
     * 最迟付款日.
     */
    private String pay_deadline;
    /*
     * 收入设置状态(默认0：未设置)
     */
    private String income_status;
    /*
     * 合同终止状态
     */
    private String termination_status;
    /*
     * 是否发送邮件
     */
    private String is_send;
    /*
     * 是否已出账(0:未出账)
     */
    private String order_status;
    /*
     * 协同客户名称。
     */
    private String hs_customername;
    /*
     * 协同经理1。
     */
    private String customermanagername;
    /*
     * 协同经理2。
     */
    private String customermanager2name;
    /*
     * 实际使用客户。
     */
    private String customername;
    /*
     * 上报类型
     */
    private String reporttype_id;
    /*
     * 合同签订日期
     */
    private String signeddate;
    /*
     * 操作权限
     */
    private String shiroPermission;

    /*
     * 即将到期月份
     */
    private String expire_month;
    /*
     * 费用类型
     */
    private String payment_type;

    /*
     * 审核
     */
    private String is_verify;

    private String jsonListOrderCombie;

    private List<OrderCombineForm> orderCombines = new ArrayList<OrderCombineForm>();

    private Set<OrderValidate> orderValidates = new HashSet<OrderValidate>();

    private String cc_flag;
    /*
     * 收入来源 nc:由NC系统计费、charge:由计费系统计费
     */
    private String income_source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    @NotBlank(message = "所属部门不允许为空")
    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
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

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getHs_customername() {
        return hs_customername;
    }

    public void setHs_customername(String hs_customername) {
        this.hs_customername = hs_customername;
    }

    public String getCustomermanagername() {
        return customermanagername;
    }

    public void setCustomermanagername(String customermanagername) {
        this.customermanagername = customermanagername;
    }

    public String getCustomermanager2name() {
        return customermanager2name;
    }

    public void setCustomermanager2name(String customermanager2name) {
        this.customermanager2name = customermanager2name;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getSigneddate() {
        return signeddate;
    }

    public void setSigneddate(String signeddate) {
        this.signeddate = signeddate;
    }

    public String getShiroPermission() {
        return shiroPermission;
    }

    public void setShiroPermission(String shiroPermission) {
        this.shiroPermission = shiroPermission;
    }

    public String getExpire_month() {
        return expire_month;
    }

    public void setExpire_month(String expire_month) {
        this.expire_month = expire_month;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
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

    public Set<OrderValidate> getOrderValidates() {
        return orderValidates;
    }

    public void setOrderValidates(Set<OrderValidate> orderValidates) {
        this.orderValidates = orderValidates;
    }

    public String getTermination_status() {
        return termination_status;
    }

    public void setTermination_status(String termination_status) {
        this.termination_status = termination_status;
    }

    public String getIs_verify() {
        return is_verify;
    }

    public void setIs_verify(String is_verify) {
        this.is_verify = is_verify;
    }

    public String getCc_flag() {
        return cc_flag;
    }

    public void setCc_flag(String cc_flag) {
        this.cc_flag = cc_flag;
    }

    public String getIncome_source() {
        return income_source;
    }

    public void setIncome_source(String income_source) {
        this.income_source = income_source;
    }

    public String getReporttype_id() {
        return reporttype_id;
    }

    public void setReporttype_id(String reporttype_id) {
        this.reporttype_id = reporttype_id;
    }

}
