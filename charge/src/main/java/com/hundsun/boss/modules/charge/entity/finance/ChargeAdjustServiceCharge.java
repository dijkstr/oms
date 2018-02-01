package com.hundsun.boss.modules.charge.entity.finance;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.hundsun.boss.base.hibernate.IdEntity;
import com.hundsun.boss.modules.charge.entity.order.OrderCombine;
import com.hundsun.boss.modules.sys.entity.Office;

/**
 * 技术服务费调账Entity
 */
@Entity
@Table(name = "charge_adjust_service_charge")
public class ChargeAdjustServiceCharge extends IdEntity<ChargeAdjustServiceCharge> {

    private static final long serialVersionUID = 1L;
    // 所属部门
    private String office_id;
    // 归属部门
    private Office office;
    // 协同合同id
    private String contract_id;
    // 组合id
    private String combine_id;
    private OrderCombine orderCombine;
    // 收入开始日期
    private String combine_begin_date;
    // 收入结束日期
    private String combine_end_date;
    // 协同销售产品名称
    private String product_names;
    // 调账日期
    private String adjust_date;
    // 调账金额
    private Double adjust_amt;
    // 是否可用
    private String status;

    public ChargeAdjustServiceCharge() {
        super();
    }

    public ChargeAdjustServiceCharge(String id) {
        this();
        this.id = id;
    }

    public String getOffice_id() {
        return office_id;
    }

    @ManyToOne
    @JoinColumn(name = "office_id", referencedColumnName = "code", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
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

    public String getCombine_id() {
        return combine_id;
    }

    public void setCombine_id(String combine_id) {
        this.combine_id = combine_id;
    }

    @ManyToOne
    @JoinColumn(name = "combine_id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public OrderCombine getOrderCombine() {
        return orderCombine;
    }

    public void setOrderCombine(OrderCombine orderCombine) {
        this.orderCombine = orderCombine;
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

    public String getProduct_names() {
        return product_names;
    }

    public void setProduct_names(String product_names) {
        this.product_names = product_names;
    }

    public String getAdjust_date() {
        return adjust_date;
    }

    public void setAdjust_date(String adjust_date) {
        this.adjust_date = adjust_date;
    }

    public Double getAdjust_amt() {
        return adjust_amt;
    }

    public void setAdjust_amt(Double adjust_amt) {
        this.adjust_amt = adjust_amt;
    }

    @Transient
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
