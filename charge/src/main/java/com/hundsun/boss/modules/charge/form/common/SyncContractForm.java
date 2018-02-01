package com.hundsun.boss.modules.charge.form.common;

import java.util.ArrayList;
import java.util.List;

import com.hundsun.boss.base.page.Page;
/**
 * 合同form
 * @author feigq
 *
 */
@SuppressWarnings("rawtypes")
public class SyncContractForm extends Page{
    //协同合同的id
    private String con_id;
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
    //创建时间
    private String createdate;
    //修改时间
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
  //所属公司id
    private String companyid;
  //所属公司
    private String companyname;
    //合同签订日期
    private String signeddate;
    //计费起点上报类型id(新添加的字段)
    private String reporttype_id;
    
    //合同子表list
    private List<SyncContractDetailForm> details = new ArrayList<SyncContractDetailForm>();
    
    //计费系统使用
    private String usetype;
    
    public String getUsetype() {
        return usetype;
    }

    public void setUsetype(String usetype) {
        this.usetype = usetype;
    }

    public String getCon_id() {
        return con_id;
    }

    public void setCon_id(String con_id) {
        this.con_id = con_id;
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

    public List<SyncContractDetailForm> getDetails() {
        return details;
    }

    public void setDetails(List<SyncContractDetailForm> details) {
        this.details = details;
    }

    public String getReporttype_id() {
        return reporttype_id;
    }

    public void setReporttype_id(String reporttype_id) {
        this.reporttype_id = reporttype_id;
    }
    

}
