package com.hundsun.boss.modules.charge.entity.sync;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.hundsun.boss.modules.charge.entity.order.OrderInfo;
import com.hundsun.boss.modules.sys.entity.Office;

/**
 * 协同合同主表Entity
 */
@Entity
@Table(name = "v_contract_jf")
public class SyncContract implements Serializable {
    private static final long serialVersionUID = 1L;
    // 卡片编号
    private String cardid;
    // 合同编号
    private String contractid;
    // 合同类型
    private String typeid;
    // 客户id
    private String customerid;
    // 客户姓名
    private String customername;
    // 考核分公司
    private String evaluatbranch;
    // 合同税率
    private String htsl;
    // 来源
    private String source;
    // 创建日期
    private String createdate;
    // 更新日期
    private String updatedate;
    // 合同状态
    private String contractstate;
    //考核人员工号
    private String evaluatobjno;
    //考核人姓名
    private String evaluatname;
    //合同小类
    private String subcategory;
    //合同类别
    private String contracttype;
    //合同核算月
    private String entrydate;
    //协同合同的id
    @Id
    private String con_id;
    //所属公司id
    private String companyid;

    //所属公司
    private String companyname;
    //    //合同类型ID
    //    private String typeid_id;
    //    //来源ID
    //    private String source_id;
    //    //合同状态ID
    //    private String contractstate_id;
    //    //合同小类ID
    //    private String subcategory_id;
    //    //合同类别ID
    //    private String contracttype_id;
    //合同签订日期
    private String signeddate;

    //计费起点上报类型id(新添加的字段)
    private String reporttype_id;

    private String categoryid;
    private String categoryname;

    @ManyToOne
    @JoinColumn(name = "companyid", referencedColumnName = "code", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Office office;  // 归属部门

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    @ManyToOne
    @JoinColumn(name = "customerid", referencedColumnName = "customerid", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private SyncCustomer syncCustomer;

    public SyncCustomer getSyncCustomer() {
        return syncCustomer;
    }

    public void setSyncCustomer(SyncCustomer syncCustomer) {
        this.syncCustomer = syncCustomer;
    }

    //合同子表集合
    @OneToMany(cascade = { CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "con_id", referencedColumnName = "con_id")
    private Set<SyncContdetail> details = new HashSet<SyncContdetail>();

    public Set<SyncContdetail> getDetails() {
        return details;
    }

    public void setDetails(Set<SyncContdetail> details) {
        this.details = details;
    }

    //计费订单集合
    @ManyToOne
    @JoinColumn(name = "contractid", referencedColumnName = "contract_id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private OrderInfo orders;

    public OrderInfo getOrders() {
        return orders;
    }

    public void setOrders(OrderInfo orders) {
        this.orders = orders;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getContractid() {
        return contractid;
    }

    public void setContractid(String contractid) {
        this.contractid = contractid;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getEvaluatbranch() {
        return evaluatbranch;
    }

    public void setEvaluatbranch(String evaluatbranch) {
        this.evaluatbranch = evaluatbranch;
    }

    public String getHtsl() {
        return htsl;
    }

    public void setHtsl(String htsl) {
        this.htsl = htsl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
    }

    public String getContractstate() {
        return contractstate;
    }

    public void setContractstate(String contractstate) {
        this.contractstate = contractstate;
    }

    public String getEvaluatobjno() {
        return evaluatobjno;
    }

    public void setEvaluatobjno(String evaluatobjno) {
        this.evaluatobjno = evaluatobjno;
    }

    public String getEvaluatname() {
        return evaluatname;
    }

    public void setEvaluatname(String evaluatname) {
        this.evaluatname = evaluatname;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getContracttype() {
        return contracttype;
    }

    public void setContracttype(String contracttype) {
        this.contracttype = contracttype;
    }

    public String getEntrydate() {
        return entrydate;
    }

    public void setEntrydate(String entrydate) {
        this.entrydate = entrydate;
    }

    public String getCon_id() {
        return con_id;
    }

    public void setCon_id(String con_id) {
        this.con_id = con_id;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getSigneddate() {
        return signeddate;
    }

    public void setSigneddate(String signeddate) {
        this.signeddate = signeddate;
    }

    public String getReporttype_id() {
        return reporttype_id;
    }

    public void setReporttype_id(String reporttype_id) {
        this.reporttype_id = reporttype_id;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

}
