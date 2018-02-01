package com.hundsun.boss.modules.charge.entity.income;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.hundsun.boss.base.hibernate.IdEntity;
import com.hundsun.boss.modules.charge.entity.sync.SyncContdetail;
import com.hundsun.boss.modules.charge.entity.sync.SyncContract;
import com.hundsun.boss.modules.charge.entity.sync.SyncProduct;
import com.hundsun.boss.modules.sys.entity.Office;

/**
 * 收入期间接口Entity
 */
@Entity
@Table(name = "charge_income_period_interface")
public class ChargeIncomePeriodInterface extends IdEntity<ChargeIncomePeriodInterface> {

    private static final long serialVersionUID = 1L;

    public ChargeIncomePeriodInterface() {
        super();
    }

    public ChargeIncomePeriodInterface(String id) {
        this();
        this.id = id;
    }

    private String office_id;
    private Office office;
    private String detailid;
    private SyncContdetail syncContdetail;
    private String con_id;
    private SyncContract syncContract;
    private String contract_id;
    private String ex_product_id;
    private String product_id;
    private SyncProduct syncProduct;
    private String payment_type;
    private String servicestartdate;
    private String serviceenddate;
    private String income_begin_date;
    private String income_end_date;
    private String accountidentity;
    private String hasreceive;
    private String send_flag;

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    @OneToOne
    @JoinColumn(name = "office_id", referencedColumnName = "code", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getDetailid() {
        return detailid;
    }

    public void setDetailid(String detailid) {
        this.detailid = detailid;
    }

    @OneToOne
    @JoinColumn(name = "detailid", referencedColumnName = "detailid", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public SyncContdetail getSyncContdetail() {
        return syncContdetail;
    }

    public void setSyncContdetail(SyncContdetail syncContdetail) {
        this.syncContdetail = syncContdetail;
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

    public String getEx_product_id() {
        return ex_product_id;
    }

    public void setEx_product_id(String ex_product_id) {
        this.ex_product_id = ex_product_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "productid", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public SyncProduct getSyncProduct() {
        return syncProduct;
    }

    public void setSyncProduct(SyncProduct syncProduct) {
        this.syncProduct = syncProduct;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getServicestartdate() {
        return servicestartdate;
    }

    public void setServicestartdate(String servicestartdate) {
        this.servicestartdate = servicestartdate;
    }

    public String getServiceenddate() {
        return serviceenddate;
    }

    public void setServiceenddate(String serviceenddate) {
        this.serviceenddate = serviceenddate;
    }

    public String getIncome_begin_date() {
        return income_begin_date;
    }

    public void setIncome_begin_date(String income_begin_date) {
        this.income_begin_date = income_begin_date;
    }

    public String getIncome_end_date() {
        return income_end_date;
    }

    public void setIncome_end_date(String income_end_date) {
        this.income_end_date = income_end_date;
    }

    public String getAccountidentity() {
        return accountidentity;
    }

    public void setAccountidentity(String accountidentity) {
        this.accountidentity = accountidentity;
    }

    public String getHasreceive() {
        return hasreceive;
    }

    public void setHasreceive(String hasreceive) {
        this.hasreceive = hasreceive;
    }

    public String getSend_flag() {
        return send_flag;
    }

    public void setSend_flag(String send_flag) {
        this.send_flag = send_flag;
    }

}
