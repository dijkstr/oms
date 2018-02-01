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

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import com.hundsun.boss.base.hibernate.IdEntity;

/**
 * 框架子合同Entity
 */
@Entity
@Table(name = "order_frame_sub")
public class OrderFrameSub extends IdEntity<OrderFrameSub> {

    private static final long serialVersionUID = 1L;

    public OrderFrameSub() {
        super();
    }

    public OrderFrameSub(String id) {
        this();
        this.id = id;
    }

    /**
     * 框架协同合同编号.
     */
    private String frame_contract_id;

    /**
     * 子合同协同合同编号.
     */
    private String contract_id;

    private Set<OrderFramePrice> orderFramePrices = new HashSet<OrderFramePrice>();

    @OneToMany(cascade = { CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumns({ @JoinColumn(name = "contract_id", referencedColumnName = "frame_contract_id"), @JoinColumn(name = "sub_contract_id", referencedColumnName = "contract_id") })
    @Where(clause = "DEL_FLAG=0")
    @OrderBy(value="charge_source,step_begin")
    public Set<OrderFramePrice> getOrderFramePrices() {
        return orderFramePrices;
    }

    public void setOrderFramePrices(Set<OrderFramePrice> orderFramePrices) {
        this.orderFramePrices = orderFramePrices;
    }

    public String getFrame_contract_id() {
        return frame_contract_id;
    }

    public void setFrame_contract_id(String frame_contract_id) {
        this.frame_contract_id = frame_contract_id;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

}
