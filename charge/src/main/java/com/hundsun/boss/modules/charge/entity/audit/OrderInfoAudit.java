package com.hundsun.boss.modules.charge.entity.audit;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.hundsun.boss.base.hibernate.WorkflowBaseEntity;
import com.hundsun.boss.modules.sys.entity.Office;

/**
 * 计费合同审核
 */
@Entity
@Table(name = "wf_contract_audit")
public class OrderInfoAudit extends WorkflowBaseEntity<OrderInfoAudit> {

    private static final long serialVersionUID = 1L;

    private String contract_process_key; // 合同流程编号

    private String office_id;
    private Office office;  // 归属部门

    public OrderInfoAudit() {
        super();
    }

    public OrderInfoAudit(String id) {
        this();
        this.id = id;
    }

    public String getContract_process_key() {
        return contract_process_key;
    }

    public void setContract_process_key(String contract_process_key) {
        this.contract_process_key = contract_process_key;
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

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

}
