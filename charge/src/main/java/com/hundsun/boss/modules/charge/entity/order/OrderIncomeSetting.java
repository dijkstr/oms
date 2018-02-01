package com.hundsun.boss.modules.charge.entity.order;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.hundsun.boss.base.hibernate.IdEntity;
import com.hundsun.boss.modules.charge.entity.sync.SyncProduct;

/**
 * 订单收入设置对象
 *
 */
@Entity
@Table(name = "order_income_setting")
public class OrderIncomeSetting extends IdEntity<OrderIncomeSetting> {
    private static final long serialVersionUID = 1L;

    public OrderIncomeSetting() {
        super();
    }

    public OrderIncomeSetting(String id) {
        this();
        this.id = id;
    }

    /**
     * 产品id
     */
    private String product_id;
    private SyncProduct syncProduct;
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
     * 协同合同号
     */
    private String contract_id;
    private OrderInfo orderInfo;
    /**
     * 拆分金额
     */
    private String split_amount;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "productid", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public SyncProduct getSyncProduct() {
        return syncProduct;
    }

    public void setSyncProduct(SyncProduct syncProduct) {
        this.syncProduct = syncProduct;
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

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    @ManyToOne
    @JoinColumn(name = "contract_id", referencedColumnName = "contract_id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getSplit_amount() {
        return split_amount;
    }

    public void setSplit_amount(String split_amount) {
        this.split_amount = split_amount;
    }

}
