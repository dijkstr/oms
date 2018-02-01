package com.hundsun.boss.modules.charge.form.order;

import java.util.List;

/**
 * 订单收入设置对象
 * 
 * @author ligc
 *
 */
public class OrderIncomeSettingForm {
    /**
     * 主键id
     */
    private String id;
    /**
     * 产品id
     */
    private String product_id;
    /**
     * 拆分比例
     */
    private String split_ratio;
    /**
     * 收入开始时间
     */
    private String income_begin_date;
    /**
     * 收入结束时间
     */
    private String income_end_date;
    /**
     * 费用类型
     */
    private String payment_type;
    /**
     * 产品名称
     */
    private String product_name;
    /**
     * 所属部门
     */
    private String office_id;
    /**
     * 协同合同号
     */
    private String contract_id;
    /**
     * 拆分金额
     */
    private String split_amount;
    /**
     * 收入设置json串.
     */
    private String jsonOrderIncomeSetting;
    /**
     * 收入设置列表.
     */
    private List<OrderIncomeSettingForm> listOrderIncomeSetting;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSplit_ratio() {
        return split_ratio;
    }

    public void setSplit_ratio(String split_ratio) {
        this.split_ratio = split_ratio;
    }

    public String getIncome_begin_date() {
        return income_begin_date;
    }

    public void setIncome_begin_date(String income_begin_date) {
        this.income_begin_date = income_begin_date;
    }

    public String getIncome_end_date() {
        return income_end_date;
    }

    public void setIncome_end_date(String income_end_date) {
        this.income_end_date = income_end_date;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getSplit_amount() {
        return split_amount;
    }

    public void setSplit_amount(String split_amount) {
        this.split_amount = split_amount;
    }

    public String getJsonOrderIncomeSetting() {
        return jsonOrderIncomeSetting;
    }

    public void setJsonOrderIncomeSetting(String jsonOrderIncomeSetting) {
        this.jsonOrderIncomeSetting = jsonOrderIncomeSetting;
    }

    public List<OrderIncomeSettingForm> getListOrderIncomeSetting() {
        return listOrderIncomeSetting;
    }

    public void setListOrderIncomeSetting(List<OrderIncomeSettingForm> listOrderIncomeSetting) {
        this.listOrderIncomeSetting = listOrderIncomeSetting;
    }

}
