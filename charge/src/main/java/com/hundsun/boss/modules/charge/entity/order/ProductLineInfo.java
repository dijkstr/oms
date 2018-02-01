package com.hundsun.boss.modules.charge.entity.order;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.hundsun.boss.base.hibernate.IdEntity;
import com.hundsun.boss.modules.charge.entity.sync.SyncCustomer;

/**
 * 计费合同Entity
 */
@Entity
@Table(name = "product_line_info")
public class ProductLineInfo extends IdEntity<ProductLineInfo> {

    private static final long serialVersionUID = 1L;

    public ProductLineInfo() {
        super();
    }

    public ProductLineInfo(String id) {
        this();
        this.id = id;
    }

    /**
     * 协同合同编号.
     */
    private String contract_id;
    /**
     * 协同用户id.
     */
    private String customer_id;
    /**
     * 协调产品id
     */
    private String product_id;
    /**
     * 订单开始日期.
     */
    private String product_line_date;

    /**
     * 状态
     */
    private String online_status;
    
    private SyncCustomer syncCustomer;
    
    private OrderInfo orderInfo;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "customer_id", referencedColumnName = "customerid", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)

    public SyncCustomer getSyncCustomer() {
        return syncCustomer;
    }

    public void setSyncCustomer(SyncCustomer syncCustomer) {
        this.syncCustomer = syncCustomer;
    }

    @ManyToOne
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "contract_id", referencedColumnName = "contract_id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_line_date() {
        return product_line_date;
    }

    public void setProduct_line_date(String product_line_date) {
        this.product_line_date = product_line_date;
    }

    public String getOnline_status() {
        return online_status;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }

}
