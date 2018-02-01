package com.hundsun.boss.modules.charge.entity.order;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import com.hundsun.boss.base.hibernate.IdEntity;

/**
 * 订单组合
 */
@Entity
@Table(name = "order_combine")
public class OrderCombine extends IdEntity<OrderCombine> {

    private static final long serialVersionUID = 1L;

    /*
     * 协同合同号.
     */
    private String contract_id;
    private OrderInfo orderInfo;
    /*
     * 组合开始时间。
     */
    private String combine_begin_date;
    /*
     * 组合结束时间.
     */
    private String combine_end_date;
    private String tech_charge_income_flag;

    private Set<OrderProduct> orderProducts = new HashSet<OrderProduct>();

    private String product_names;

    @OneToMany(cascade = { CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumns({ @JoinColumn(name = "combine_id", referencedColumnName = "id"), @JoinColumn(name = "contract_id", referencedColumnName = "contract_id") })
    @Where(clause = "DEL_FLAG=0")
    @OrderBy(value = "id")
    public Set<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(Set<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    private Set<OrderAdvPayment> OrderAdvPayments = new HashSet<OrderAdvPayment>();

    @OneToMany(cascade = { CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumns({ @JoinColumn(name = "combine_id", referencedColumnName = "id"), @JoinColumn(name = "contract_id", referencedColumnName = "contract_id") })
    @Where(clause = "DEL_FLAG=0")
    @OrderBy(value = "id")
    public Set<OrderAdvPayment> getOrderAdvPayments() {
        return OrderAdvPayments;
    }

    public void setOrderAdvPayments(Set<OrderAdvPayment> orderAdvPayments) {
        OrderAdvPayments = orderAdvPayments;
    }

    private Set<OrderModel> orderModels = new HashSet<OrderModel>();

    @OneToMany(cascade = { CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "ref_id", referencedColumnName = "id")
    @Where(clause = "DEL_FLAG= 0 and belong_type ='2'")
    public Set<OrderModel> getOrderModels() {
        return orderModels;
    }

    public void setOrderModels(Set<OrderModel> orderModels) {
        this.orderModels = orderModels;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    @Transient
    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
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

    @Transient
    public String getProduct_names() {
        return product_names;
    }

    public void setProduct_names(String product_names) {
        this.product_names = product_names;
    }

    public String getTech_charge_income_flag() {
        return tech_charge_income_flag;
    }

    public void setTech_charge_income_flag(String tech_charge_income_flag) {
        this.tech_charge_income_flag = tech_charge_income_flag;
    }

}
