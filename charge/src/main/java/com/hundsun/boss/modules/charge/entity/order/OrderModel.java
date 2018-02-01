package com.hundsun.boss.modules.charge.entity.order;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import com.hundsun.boss.base.hibernate.IdEntity;

/***
 * 计费模式.
 */
@Entity
@Table(name = "order_model")
public class OrderModel extends IdEntity<OrderModel> {

    private static final long serialVersionUID = 1L;

    /**
     * 计费模式名称.
     */
    private String feemodel_name;
    /**
     * 关联外部id
     */
    private String ref_id;
    /**
     * 归属类型.
     */
    private String belong_type;
    /**
     * 计费公式.
     */
    private String fee_formula;
    /**
     * 收费类型.
     */
    private String fee_type;
    /**
     * 保底类型.
     */
    private String min_type;
    /**
     * 保底消费.
     */
    private Double min_consume;
    /**
     * 封顶类型.
     */
    private String max_type;
    /**
     * 最高消费.
     */
    private Double max_consume;
    /**
     * 折扣比例.
     */
    private Double discount;
    /**
     * 固定收费周期.
     */
    private String fix_charge_type;
    /**
     * 是否乘以实际发生天数.
     */
    private String is_multiplied_actualdays;

    private Set<OrderPrice> orderPrices = new HashSet<OrderPrice>();

    @OneToMany(cascade = { CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "feemodel_id", referencedColumnName = "id")
    @Where(clause = "DEL_FLAG=0")
    @OrderBy(value="step_begin")
    public Set<OrderPrice> getOrderPrices() {
        return orderPrices;
    }

    public void setOrderPrices(Set<OrderPrice> orderPrices) {
        this.orderPrices = orderPrices;
    }

    public String getFeemodel_name() {
        return feemodel_name;
    }

    public void setFeemodel_name(String feemodel_name) {
        this.feemodel_name = feemodel_name;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getBelong_type() {
        return belong_type;
    }

    public void setBelong_type(String belong_type) {
        this.belong_type = belong_type;
    }

    public String getFee_formula() {
        return fee_formula;
    }

    public void setFee_formula(String fee_formula) {
        this.fee_formula = fee_formula;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getMin_type() {
        return min_type;
    }

    public void setMin_type(String min_type) {
        this.min_type = min_type;
    }

    public Double getMin_consume() {
        return min_consume;
    }

    public void setMin_consume(Double min_consume) {
        this.min_consume = min_consume;
    }

    public String getMax_type() {
        return max_type;
    }

    public void setMax_type(String max_type) {
        this.max_type = max_type;
    }

    public Double getMax_consume() {
        return max_consume;
    }

    public void setMax_consume(Double max_consume) {
        this.max_consume = max_consume;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getFix_charge_type() {
        return fix_charge_type;
    }

    public void setFix_charge_type(String fix_charge_type) {
        this.fix_charge_type = fix_charge_type;
    }

    public String getIs_multiplied_actualdays() {
        return is_multiplied_actualdays;
    }

    public void setIs_multiplied_actualdays(String is_multiplied_actualdays) {
        this.is_multiplied_actualdays = is_multiplied_actualdays;
    }

}
