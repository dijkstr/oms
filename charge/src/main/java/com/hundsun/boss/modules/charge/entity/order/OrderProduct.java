package com.hundsun.boss.modules.charge.entity.order;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import com.hundsun.boss.base.hibernate.IdEntity;

/**
 * 订单产品.
 */
@Entity
@Table(name = "order_product")
public class OrderProduct extends IdEntity<OrderProduct> {

    private static final long serialVersionUID = 1L;

    private Set<OrderModel> orderModels = new HashSet<OrderModel>();

    @OneToMany(cascade = { CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "ref_id", referencedColumnName = "id")
    @Where(clause = "DEL_FLAG = 0 and belong_type ='3'")
    public Set<OrderModel> getOrderModels() {
        return orderModels;
    }

    public void setOrderModels(Set<OrderModel> orderModels) {
        this.orderModels = orderModels;
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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

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

}
