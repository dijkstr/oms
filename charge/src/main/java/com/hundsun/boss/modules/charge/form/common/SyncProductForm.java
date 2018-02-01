package com.hundsun.boss.modules.charge.form.common;

import com.hundsun.boss.base.page.Page;

/**
 * 协同产品form
 */
@SuppressWarnings("rawtypes")
public class SyncProductForm extends Page{

    //销售产品id.
    private String productid;
    //销售产品名称.
    private String productname;
    //产品英文名称.
    private String englishname;
    //英文简称.
    private String abbrenglishname;
    //技术产品id编号.
    private String productid_tec;
    //技术产品名称编号.
    private String productname_tec;
    //产品品牌.
    private String brandname;
    //产品线id.
    private String lineid;
    //产品线名称.
    private String linename;
    //是否1.0.
    private String isproduct1;
    //是否2.0.
    private String isproduct2;
    //产品线级别.
    private String productclass;
    //产品登记日期.
    private String productdate;
    //创建日期.
    private String createdate;
    //更新日期.
    private String updatedate;
    //协同id.
    private String prdid;
    //销售状态
    private String salesstatus;
    //协同合同号
    private String contractid;
    
    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getEnglishname() {
        return englishname;
    }

    public void setEnglishname(String englishname) {
        this.englishname = englishname;
    }

    public String getAbbrenglishname() {
        return abbrenglishname;
    }

    public void setAbbrenglishname(String abbrenglishname) {
        this.abbrenglishname = abbrenglishname;
    }

    public String getProductid_tec() {
        return productid_tec;
    }

    public void setProductid_tec(String productid_tec) {
        this.productid_tec = productid_tec;
    }

    public String getProductname_tec() {
        return productname_tec;
    }

    public void setProductname_tec(String productname_tec) {
        this.productname_tec = productname_tec;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getLineid() {
        return lineid;
    }

    public void setLineid(String lineid) {
        this.lineid = lineid;
    }

    public String getLinename() {
        return linename;
    }

    public void setLinename(String linename) {
        this.linename = linename;
    }

    public String getIsproduct1() {
        return isproduct1;
    }

    public void setIsproduct1(String isproduct1) {
        this.isproduct1 = isproduct1;
    }

    public String getIsproduct2() {
        return isproduct2;
    }

    public void setIsproduct2(String isproduct2) {
        this.isproduct2 = isproduct2;
    }

    public String getProductclass() {
        return productclass;
    }

    public void setProductclass(String productclass) {
        this.productclass = productclass;
    }

    public String getProductdate() {
        return productdate;
    }

    public void setProductdate(String productdate) {
        this.productdate = productdate;
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

    public String getPrdid() {
        return prdid;
    }

    public void setPrdid(String prdid) {
        this.prdid = prdid;
    }

    public String getSalesstatus() {
        return salesstatus;
    }

    public void setSalesstatus(String salesstatus) {
        this.salesstatus = salesstatus;
    }

    public String getContractid() {
        return contractid;
    }

    public void setContractid(String contractid) {
        this.contractid = contractid;
    }
    
    
    
}
