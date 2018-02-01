package com.hundsun.boss.modules.charge.form.audit;

import com.hundsun.boss.modules.charge.form.order.OrderInfoForm;

public class OrderInfoAuditForm extends OrderInfoForm {
    public OrderInfoAuditForm() {
        bindClass = OrderInfoAuditForm.class.getName();
    }

    /**
     * 审核不通过原因
     */
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
