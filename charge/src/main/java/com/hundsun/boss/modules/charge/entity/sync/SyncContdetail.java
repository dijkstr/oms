package com.hundsun.boss.modules.charge.entity.sync;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 协同合同子表Entity
 */
@Entity
@Table(name = "v_contract_detail_jf")
public class SyncContdetail implements Serializable {
    private static final long serialVersionUID = 1L;
    // 合同主表id
    private String con_id;
    //产品线id
    private String line_id;
    // 销售产品id
    private String salprd_id;
    // 技术产品id
    private String tecprd_id;
    //单价
    private String prdprice;
    //数量
    private String quantity;
    //小计
    private String subtotal;
    //总价
    private String amount;
    //说明1
    private String remarks;
    //成本
    private String productcost;
    //毛利
    private String profit;
    //说明2
    private String note;
    //序号
    private String num;
    //冻结
    private String islock;
    //合同性质
    private String contractnature;
    //服务起始日期
    private String servicestartdate;
    //服务终止日期
    private String serviceenddate;
    //服务总月数
    private String servicemonth;
    //受益期应收款
    private String benefitshould;
    //子表id
    private String dt_id;
    //签约产品id
    private String htwbid;
    //产品所处阶段
    private String productstage;
    //关联软件
    private String relatedsoft_id;
    //累计已开票
    private String addinvoiced;
    //NC产品子表ID
    private String pkprodinfo;
    //计费填报标识
    private String accountidentity;
    //子表32位id
    @Id
    private String detailid;
    //产品计费类型
    private String chargetype;
    //创建日期
    private String createdate;
    //更新日期
    private String updatedate;
    // 到款
    private String hasreceive;

    public String getCon_id() {
        return con_id;
    }

    public void setCon_id(String con_id) {
        this.con_id = con_id;
    }

    public String getLine_id() {
        return line_id;
    }

    public void setLine_id(String line_id) {
        this.line_id = line_id;
    }

    public String getSalprd_id() {
        return salprd_id;
    }

    public void setSalprd_id(String salprd_id) {
        this.salprd_id = salprd_id;
    }

    public String getTecprd_id() {
        return tecprd_id;
    }

    public void setTecprd_id(String tecprd_id) {
        this.tecprd_id = tecprd_id;
    }

    public String getPrdprice() {
        return prdprice;
    }

    public void setPrdprice(String prdprice) {
        this.prdprice = prdprice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getProductcost() {
        return productcost;
    }

    public void setProductcost(String productcost) {
        this.productcost = productcost;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getIslock() {
        return islock;
    }

    public void setIslock(String islock) {
        this.islock = islock;
    }

    public String getContractnature() {
        return contractnature;
    }

    public void setContractnature(String contractnature) {
        this.contractnature = contractnature;
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

    public String getServicemonth() {
        return servicemonth;
    }

    public void setServicemonth(String servicemonth) {
        this.servicemonth = servicemonth;
    }

    public String getBenefitshould() {
        return benefitshould;
    }

    public void setBenefitshould(String benefitshould) {
        this.benefitshould = benefitshould;
    }

    public String getDt_id() {
        return dt_id;
    }

    public void setDt_id(String dt_id) {
        this.dt_id = dt_id;
    }

    public String getHtwbid() {
        return htwbid;
    }

    public void setHtwbid(String htwbid) {
        this.htwbid = htwbid;
    }

    public String getProductstage() {
        return productstage;
    }

    public void setProductstage(String productstage) {
        this.productstage = productstage;
    }

    public String getRelatedsoft_id() {
        return relatedsoft_id;
    }

    public void setRelatedsoft_id(String relatedsoft_id) {
        this.relatedsoft_id = relatedsoft_id;
    }

    public String getAddinvoiced() {
        return addinvoiced;
    }

    public void setAddinvoiced(String addinvoiced) {
        this.addinvoiced = addinvoiced;
    }

    public String getPkprodinfo() {
        return pkprodinfo;
    }

    public void setPkprodinfo(String pkprodinfo) {
        this.pkprodinfo = pkprodinfo;
    }

    public String getAccountidentity() {
        return accountidentity;
    }

    public void setAccountidentity(String accountidentity) {
        this.accountidentity = accountidentity;
    }

    public String getDetailid() {
        return detailid;
    }

    public void setDetailid(String detailid) {
        this.detailid = detailid;
    }

    public String getChargetype() {
        return chargetype;
    }

    public void setChargetype(String chargetype) {
        this.chargetype = chargetype;
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

    public String getHasreceive() {
        return hasreceive;
    }

    public void setHasreceive(String hasreceive) {
        this.hasreceive = hasreceive;
    }

}
