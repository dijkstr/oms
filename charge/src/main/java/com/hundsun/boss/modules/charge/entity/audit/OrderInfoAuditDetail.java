package com.hundsun.boss.modules.charge.entity.audit;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.hundsun.boss.base.hibernate.IdEntity;

/**
 * 合同审核明细Entity
 * 
 * @author zhengzh20786
 *
 */
@Entity
@Table(name = "wf_contract_audit_detail")
public class OrderInfoAuditDetail extends IdEntity<OrderInfoAuditDetail> {

    private static final long serialVersionUID = 1L;

    private String contract_process_key; // 合同流程编号
    private String operate_user_id;      // 操作人编号
    private Date operate_date;         // 操作时间
    private String audit_user_id;        // 审核人编号
    private Date audit_date;      // 审核时间
    private String comments;             // 备注

    public String getContract_process_key() {
        return contract_process_key;
    }

    public void setContract_process_key(String contract_process_key) {
        this.contract_process_key = contract_process_key;
    }

    public String getOperate_user_id() {
        return operate_user_id;
    }

    public void setOperate_user_id(String operate_user_id) {
        this.operate_user_id = operate_user_id;
    }

    public Date getOperate_date() {
        return operate_date;
    }

    public void setOperate_date(Date operate_date) {
        this.operate_date = operate_date;
    }

    public String getAudit_user_id() {
        return audit_user_id;
    }

    public void setAudit_user_id(String audit_user_id) {
        this.audit_user_id = audit_user_id;
    }

    public Date getAudit_date() {
        return audit_date;
    }

    public void setAudit_date(Date audit_date) {
        this.audit_date = audit_date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comment) {
        this.comments = comment;
    }

}
