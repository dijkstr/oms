package com.hundsun.boss.modules.charge.form.setting;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.util.HtmlUtils;

import com.hundsun.boss.base.form.BaseForm;
import com.hundsun.boss.common.beanvalidator.Numberic;
import com.hundsun.boss.modules.charge.entity.setting.ChargeModel;

public class ChargeModelForm extends BaseForm {
    public ChargeModelForm() {
        bindClass = ChargeModel.class.getName();
    }

    private String model_name;
    private String classify_id;
    private String fee_formula;
    private String fee_type;
    private String min_type;
    private String min_consume;
    private String max_type;
    private String max_consume;
    private String discount;
    private String feemodel_status;
    private String fix_charge_type;
    private String is_multiplied_actualdays;
    private String office_id;
    private String classify_name;

    private String fee_formula_name;
    private String fee_type_name;

    protected String listJson;

    private List<ChargePriceForm> chargePrices = new ArrayList<ChargePriceForm>();

    @NotBlank(message = "计费模式名称不允许为空")
    @Length(min = 0, max = 200, message = "计费模式名称最大长度不能超过200位")
    @Column(unique = true)
    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    @NotBlank(message = "计费模式分类不允许为空")
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

    @Numberic(message = "保底金额必须输入数字类型")
    @Column(nullable = true)
    public String getMin_consume() {
        return min_consume;
    }

    public void setMin_consume(String min_consume) {
        this.min_consume = min_consume;
    }

    public String getMax_type() {
        return max_type;
    }

    public void setMax_type(String max_type) {
        this.max_type = max_type;
    }

    @Numberic(message = "封顶金额的金额形式为数字类型")
    @Column(nullable = true)
    public String getMax_consume() {
        return max_consume;
    }

    public void setMax_consume(String max_consume) {
        this.max_consume = max_consume;
    }

    @Numberic(message = "折扣必须输入数字类型")
    @Column(nullable = true)
    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFeemodel_status() {
        return feemodel_status;
    }

    public void setFeemodel_status(String feemodel_status) {
        this.feemodel_status = feemodel_status;
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

    public String getListJson() {
        return HtmlUtils.htmlUnescape(listJson);
    }

    public void setListJson(String listJson) {
        this.listJson = listJson;
    }

    public List<ChargePriceForm> getChargePrices() {
        return chargePrices;
    }

    public void setChargePrices(List<ChargePriceForm> chargePrices) {
        this.chargePrices = chargePrices;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public String getFee_formula_name() {
        return fee_formula_name;
    }

    public void setFee_formula_name(String fee_formula_name) {
        this.fee_formula_name = fee_formula_name;
    }

    public String getFee_type_name() {
        return fee_type_name;
    }

    public void setFee_type_name(String fee_type_name) {
        this.fee_type_name = fee_type_name;
    }

    public String getClassify_name() {
        return classify_name;
    }

    public void setClassify_name(String classify_name) {
        this.classify_name = classify_name;
    }

}
