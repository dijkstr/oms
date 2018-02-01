package com.hundsun.boss.modules.charge.entity.order;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.hundsun.boss.base.hibernate.IdEntity;
import com.hundsun.boss.modules.charge.entity.sync.SyncCustomer;
import com.hundsun.boss.modules.sys.entity.Office;

/**
 * 框架合同Entity
 */
@Entity
@Table(name = "order_frame_info")
public class OrderFrameInfo extends IdEntity<OrderFrameInfo> {

    private static final long serialVersionUID = 1L;

    public OrderFrameInfo() {
        super();
    }

    public OrderFrameInfo(String id) {
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
     * 结算周期.
     */
    private String payment_cycle;

    private String office_id;
    private Office office;  // 归属部门

    @ManyToOne
    @JoinColumn(name = "office_id", referencedColumnName = "code", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    private Set<OrderFrameSub> orderFrameSubs = new HashSet<OrderFrameSub>();

    @OneToMany(cascade = { CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "frame_contract_id", referencedColumnName = "contract_id")
    @Where(clause = "DEL_FLAG=0")
    @OrderBy(value = "id")
    public Set<OrderFrameSub> getOrderFrameSubs() {
        return orderFrameSubs;
    }

    public void setOrderFrameSubs(Set<OrderFrameSub> orderFrameSubs) {
        this.orderFrameSubs = orderFrameSubs;
    }

    private SyncCustomer syncCustomer;

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

    public String getPayment_cycle() {
        return payment_cycle;
    }

    public void setPayment_cycle(String payment_cycle) {
        this.payment_cycle = payment_cycle;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

}
