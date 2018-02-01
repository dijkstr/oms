package com.hundsun.boss.modules.charge.entity.income;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.hundsun.boss.base.hibernate.IdEntity;
import com.hundsun.boss.modules.charge.entity.sync.SyncContract;
import com.hundsun.boss.modules.sys.entity.Office;

/**
 * 收入来源接口
 */
@Entity
@Table(name = "charge_income_source_interface")
public class ChargeIncomeSourceInterface extends IdEntity<ChargeIncomeSourceInterface> {

    private static final long serialVersionUID = 1L;

    public ChargeIncomeSourceInterface() {
        super();
    }

    public ChargeIncomeSourceInterface(String id) {
        this();
        this.id = id;
    }

    private String office_id;
    private Office office;
    private String con_id;
    private SyncContract syncContract;
    private String contract_id;
    private String income_source;
    private String send_flag;

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
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

    public String getCon_id() {
        return con_id;
    }

    public void setCon_id(String con_id) {
        this.con_id = con_id;
    }

    @OneToOne
    @JoinColumn(name = "con_id", referencedColumnName = "con_id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public SyncContract getSyncContract() {
        return syncContract;
    }

    public void setSyncContract(SyncContract syncContract) {
        this.syncContract = syncContract;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getIncome_source() {
        return income_source;
    }

    public void setIncome_source(String income_source) {
        this.income_source = income_source;
    }

    public String getSend_flag() {
        return send_flag;
    }

    public void setSend_flag(String send_flag) {
        this.send_flag = send_flag;
    }
}
