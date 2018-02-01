package com.hundsun.boss.modules.charge.form.finance;

import org.hibernate.validator.constraints.NotBlank;

import com.hundsun.boss.base.form.BaseForm;
import com.hundsun.boss.common.beanvalidator.Numberic;
import com.hundsun.boss.modules.charge.entity.finance.ChargeAdjustServiceCharge;

/**
 * 收入调账form
 */
public class ChargeAdjustServiceChargeForm extends BaseForm {
    public ChargeAdjustServiceChargeForm() {
        bindClass = ChargeAdjustServiceCharge.class.getName();
    }

    private String id;
    // 所属部门
    private String office_id;
    // 协同合同id
    private String contract_id;
    // 组合id
    private String combine_id;
    // 收入开始日期
    private String combine_begin_date;
    // 收入结束日期
    private String combine_end_date;
    // 协同销售产品名称
    private String product_names;
    // 调账日期
    private String adjust_date;
    // 调账金额
    private String adjust_amt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    @NotBlank(message = "必须选择调账对象收入行")
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

    public String getCombine_begin_date() {
        return combine_begin_date;
    }

    public void setCombine_begin_date(String combine_begin_date) {
        this.combine_begin_date = combine_begin_date;
    }

    public String getCombine_end_date() {
        return combine_end_date;
    }

    public void setCombine_end_date(String combine_end_date) {
        this.combine_end_date = combine_end_date;
    }

    public String getProduct_names() {
        return product_names;
    }

    public void setProduct_names(String product_names) {
        this.product_names = product_names;
    }

    @NotBlank(message = "调账日期不允许为空")
    public String getAdjust_date() {
        return adjust_date;
    }

    public void setAdjust_date(String adjust_date) {
        this.adjust_date = adjust_date;
    }

    @Numberic(message = "调账金额必须输入数字类型")
    @NotBlank(message = "调账金额不允许为空")
    public String getAdjust_amt() {
        return adjust_amt;
    }

    public void setAdjust_amt(String adjust_amt) {
        this.adjust_amt = adjust_amt;
    }

}
