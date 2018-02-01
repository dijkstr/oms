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

/**
 * 合同到期提醒Entity
 */
@Entity
@Table(name = "order_expire_warning")
public class OrderExpireWarning extends IdEntity<OrderExpireWarning> {

    private static final long serialVersionUID = 1L;

    public OrderExpireWarning() {
        super();
    }

    public OrderExpireWarning(String id) {
        this();
        this.id = id;
    }

    /**
     * 协同合同编号.
     */
    private String contract_id;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 需提醒次数.
     */
    private String warn_times;
    /**
     * 已提醒次数
     */
    private String warn_counter;
    /**
     * 提醒类型
     */
    private String warn_type;

    private OrderInfo orderInfo;
    
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWarn_times() {
        return warn_times;
    }

    public void setWarn_times(String warn_times) {
        this.warn_times = warn_times;
    }

    public String getWarn_counter() {
        return warn_counter;
    }

    public void setWarn_counter(String warn_counter) {
        this.warn_counter = warn_counter;
    }

    public String getWarn_type() {
        return warn_type;
    }

    public void setWarn_type(String warn_type) {
        this.warn_type = warn_type;
    }

}
