package com.hundsun.boss.modules.charge.form.order;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.util.HtmlUtils;

import com.hundsun.boss.base.form.BaseForm;
import com.hundsun.boss.modules.charge.entity.order.OrderFrameInfo;

/**
 * 框架合同Form
 */
public class OrderFrameInfoForm extends BaseForm {

    public OrderFrameInfoForm() {
        bindClass = OrderFrameInfo.class.getName();
    }

    /**
     * 协同合同编号.
     */
    private String contract_id;
    /**
     * 所属部门.
     */
    private String office_id;
    /**
     * 协同用户id.
     */
    private String customer_id;
    /**
     * 结算周期.
     */
    private String payment_cycle;
    /**
     * 操作权限
     */
    private String shiroPermission;
    /**
     * 协同客户名称。
     */
    private String hs_customername;
    /**
     * 协同经理1。
     */
    private String customermanagername;
    /**
     * 协同经理1。
     */
    private String customermanager2name;

    private String jsonListOrderFrameSub;

    private List<OrderFrameSubForm> orderFrameSubs = new ArrayList<OrderFrameSubForm>();

    @NotBlank(message = "框架合同编号不允许为空")
    @Length(min = 1, max = 40, message = "框架合同编号最大长度不能超过40位")
    @Column(unique = true)
    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    @NotBlank(message = "所属部门不允许为空")
    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
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

    public String getShiroPermission() {
        return shiroPermission;
    }

    public void setShiroPermission(String shiroPermission) {
        this.shiroPermission = shiroPermission;
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

    public String getJsonListOrderFrameSub() {
        return HtmlUtils.htmlUnescape(jsonListOrderFrameSub);
    }

    public void setJsonListOrderFrameSub(String jsonListOrderFrameSub) {
        this.jsonListOrderFrameSub = jsonListOrderFrameSub;
    }

    public List<OrderFrameSubForm> getOrderFrameSubs() {
        return orderFrameSubs;
    }

    public void setOrderFrameSubs(List<OrderFrameSubForm> orderFrameSubs) {
        this.orderFrameSubs = orderFrameSubs;
    }

}
