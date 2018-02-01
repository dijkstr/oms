package com.hundsun.boss.modules.charge.form.order;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.hundsun.boss.base.form.BaseForm;
import com.hundsun.boss.modules.charge.entity.order.OrderFrameSub;

/**
 * 框架子合同
 */
public class OrderFrameSubForm extends BaseForm {
    public OrderFrameSubForm() {
        bindClass = OrderFrameSub.class.getName();
    }

    /**
     * 框架协同合同编号.
     */
    private String frame_contract_id;
    /**
     * 协同合同编号.
     */
    private String contract_id;
    
    /**
     * 开始时间.
     */
    private String order_begin_date;
    /**
     * 结束时间.
     */
    private String order_end_date;

    private String jsonListOrderFramePrice;

    private List<OrderFramePriceForm> orderFramePrices = new ArrayList<OrderFramePriceForm>();

    public String getFrame_contract_id() {
        return frame_contract_id;
    }

    public void setFrame_contract_id(String frame_contract_id) {
        this.frame_contract_id = frame_contract_id;
    }

    @NotBlank(message = "子合同编号不允许为空")
    @Length(min = 1, max = 40, message = "子合同编号最大长度不能超过40位")
    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
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

    public String getJsonListOrderFramePrice() {
        return jsonListOrderFramePrice;
    }

    public void setJsonListOrderFramePrice(String jsonListOrderFramePrice) {
        this.jsonListOrderFramePrice = jsonListOrderFramePrice;
    }

    public List<OrderFramePriceForm> getOrderFramePrices() {
        return orderFramePrices;
    }

    public void setOrderFramePrices(List<OrderFramePriceForm> orderFramePrices) {
        this.orderFramePrices = orderFramePrices;
    }

}
