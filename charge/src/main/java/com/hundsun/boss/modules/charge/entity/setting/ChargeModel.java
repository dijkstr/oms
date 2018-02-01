package com.hundsun.boss.modules.charge.entity.setting;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import com.hundsun.boss.base.hibernate.IdEntity;
import com.hundsun.boss.modules.sys.entity.Office;

/**
 * 计费模式Entity
 */
@Entity
@Table(name = "charge_model")
public class ChargeModel extends IdEntity<ChargeModel> {

    private static final long serialVersionUID = 1L;

    public ChargeModel() {
        super();
    }

    public ChargeModel(String id) {
        this();
        this.id = id;
    }

    private String model_name; 	// 名称
    private String classify_id;// VARCHAR(64) DEFAULT '0',
    private String fee_formula;// VARCHAR(10),
    private String fee_type;// VARCHAR(40),
    private String min_type;// VARCHAR(10),
    private Double min_consume;// DECIMAL(25,8),
    private String max_type;// VARCHAR(10),
    private Double max_consume;// DECIMAL(25,8),
    private Double discount;// DECIMAL(25,8),
    private String feemodel_status;// VARCHAR(10) DEFAULT '1',
    private String fix_charge_type;// VARCHAR(10),
    private String is_multiplied_actualdays;// VARCHAR(10) DEFAULT '0',
    private String office_id;
    private Office office;  // 归属部门

    private Classify classify;

    @OneToOne(cascade = { CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "classify_id", nullable = false, insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public Classify getClassify() {
        return classify;
    }

    public void setClassify(Classify classify) {
        this.classify = classify;
    }

    private Set<ChargePrice> chargePrices = new HashSet<ChargePrice>();

    @OneToMany(cascade = { CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "feemodel_id", referencedColumnName = "id")
    @Where(clause = "DEL_FLAG=0")
    @OrderBy(value="step_begin")
    public Set<ChargePrice> getChargePrices() {
        return chargePrices;
    }

    public void setChargePrices(Set<ChargePrice> chargePrices) {
        this.chargePrices = chargePrices;
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

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getClassify_id() {
        return classify_id;
    }

    public void setClassify_id(String classify_id) {
        this.classify_id = classify_id;
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

    @Length(max = 10)
    public String getFeemodel_status() {
        return feemodel_status;
    }

    public void setFeemodel_status(String feemodel_status) {
        this.feemodel_status = feemodel_status;
    }

    @Length(max = 10)
    public String getFix_charge_type() {
        return fix_charge_type;
    }

    public void setFix_charge_type(String fix_charge_type) {
        this.fix_charge_type = fix_charge_type;
    }

    @Length(min = 1, max = 10)
    @Column(nullable = false)
    public String getIs_multiplied_actualdays() {
        return is_multiplied_actualdays;
    }

    public void setIs_multiplied_actualdays(String is_multiplied_actualdays) {
        this.is_multiplied_actualdays = is_multiplied_actualdays;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }
}
