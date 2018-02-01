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

/*
 * 计费合同Entity
 */
@Entity
@Table(name = "order_info")
public class OrderInfo extends IdEntity<OrderInfo> {

    private static final long serialVersionUID = 1L;

    public OrderInfo() {
        super();
    }

    public OrderInfo(String id) {
        this();
        this.id = id;
    }

    /*
     * 协同合同编号.
     */
    private String contract_id;
    /*
     * 框架合同号
     */
    private String frame_contract_id;
    /*
     * 订单开始日期.
     */
    private String order_begin_date;
    /*
     * 订单结束日期.
     */
    private String order_end_date;
    /*
     * 协同用户id.
     */
    private String customer_id;
    /*
     * 结算周期.
     */
    private String payment_cycle;
    /*
     * 最迟付款日.
     */
    private String pay_deadline;
    /*
     * 是否发送邮件
     */
    private String is_send;
    /*
     * 合同状态
     */
    private String order_status;
    /*
     * 归属部门
     */
    private String office_id;
    private Office office;
    /*
     * 绑定工作流编号 格式为 yyyyMMddHHmmss
     */
    private String wf_process_key;
    /*
     * 是否抄送给销售
     */
    private String cc_flag;
    /*
     * 是否抄送给销售
     */
    private String income_source;

    @ManyToOne
    @JoinColumn(name = "office_id", referencedColumnName = "code", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    private Set<OrderCombine> orderCombines = new HashSet<OrderCombine>();

    @OneToMany(cascade = { CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "contract_id", referencedColumnName = "contract_id")
    @Where(clause = "DEL_FLAG=0")
    @OrderBy(value = "id")
    public Set<OrderCombine> getOrderCombines() {
        return orderCombines;
    }

    public void setOrderCombines(Set<OrderCombine> orderCombines) {
        this.orderCombines = orderCombines;
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

    public String getFrame_contract_id() {
        return frame_contract_id;
    }

    public void setFrame_contract_id(String frame_contract_id) {
        this.frame_contract_id = frame_contract_id;
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

    public String getPay_deadline() {
        return pay_deadline;
    }

    public void setPay_deadline(String pay_deadline) {
        this.pay_deadline = pay_deadline;
    }

    public String getIs_send() {
        return is_send;
    }

    public void setIs_send(String is_send) {
        this.is_send = is_send;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public String getWf_process_key() {
        return wf_process_key;
    }

    public void setWf_process_key(String wf_process_key) {
        this.wf_process_key = wf_process_key;
    }

    public String getCc_flag() {
        return cc_flag;
    }

    public void setCc_flag(String cc_flag) {
        this.cc_flag = cc_flag;
    }

    public String getIncome_source() {
        return income_source;
    }

    public void setIncome_source(String income_source) {
        this.income_source = income_source;
    }

}
