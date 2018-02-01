package com.hundsun.boss.modules.charge.form.order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.hundsun.boss.base.form.BaseForm;
import com.hundsun.boss.modules.charge.entity.order.OrderProduct;
import com.hundsun.boss.modules.charge.entity.ordervalidate.OrderValidate;

public class OrderProductForm extends BaseForm {
    public OrderProductForm() {
        bindClass = OrderProduct.class.getName();
    }

    /**
     * 协同合同号。
     */
    private String contract_id;
    /**
     * 组合编号.
     */
    private String combine_id;
    /**
     * 协同产品编号.(32位)
     */
    private String product_id;
    /**
     * 产品名称。
     */
    private String product_name;
    /**
     * 协同产品编号.(5位)
     */
    private String ex_product_id;
    /**
     * 产品开始日期.
     */
    private String prod_begin_date;
    /**
     * 产品结束日期.
     */
    private String prod_end_date;

    private String jsonListOrderModel;

    private List<OrderModelForm> orderModels = new ArrayList<OrderModelForm>();

    private Set<OrderValidate> orderValidates = new HashSet<OrderValidate>();
    
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

    @NotBlank(message = "协同销售产品编码不允许为空")
    @Length(min = 1, max = 40, message = "协同销售产品编码最大长度不能超过40位")
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    @Length(min = 0, max = 64, message = "协同销售产品编码最大长度不能超过64位")
    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getEx_product_id() {
        return ex_product_id;
    }

    public void setEx_product_id(String ex_product_id) {
        this.ex_product_id = ex_product_id;
    }

    public String getProd_begin_date() {
        return prod_begin_date;
    }

    public void setProd_begin_date(String prod_begin_date) {
        this.prod_begin_date = prod_begin_date;
    }

    public String getProd_end_date() {
        return prod_end_date;
    }

    public void setProd_end_date(String prod_end_date) {
        this.prod_end_date = prod_end_date;
    }

    public String getJsonListOrderModel() {
        return jsonListOrderModel;
    }

    public void setJsonListOrderModel(String jsonListOrderModel) {
        this.jsonListOrderModel = jsonListOrderModel;
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

}
