package com.hundsun.boss.modules.charge.form.order;

import com.hundsun.boss.base.form.BaseForm;

public class OrderExpireWarningForm extends BaseForm {

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

    /**
     * 所属部门.
     */
    private String office_id;
    
    
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

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

}
