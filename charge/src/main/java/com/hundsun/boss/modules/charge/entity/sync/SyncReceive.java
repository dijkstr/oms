package com.hundsun.boss.modules.charge.entity.sync;

import java.io.Serializable;
/**
 * 协同合同到款备份Entity
 */
//@Entity
//@Table(name="charge_bankreceipt_bk")
public class SyncReceive implements Serializable{
    private static final long serialVersionUID = 1L;
    //到款日期
    private String dbilldate;
    // 到款单号
    private String vbillcode;
    // 业务单元
    private String company;
    // 合同编号
    private String contractid;
    // 协同销售产品id
    private String productid;
    //销售产品名称
    private String productname;
    //到款金额
    private Double receivemny;
    //到款时间戳
    private String ts;
    //到款行id
//    @Id
    private String receiveid;
  //计费创建时间
    private String create_datetime;
    
    public String getCreate_datetime() {
        return create_datetime;
    }
    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }
    public String getDbilldate() {
        return dbilldate;
    }
    public void setDbilldate(String dbilldate) {
        this.dbilldate = dbilldate;
    }
    public String getVbillcode() {
        return vbillcode;
    }
    public void setVbillcode(String vbillcode) {
        this.vbillcode = vbillcode;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getContractid() {
        return contractid;
    }
    public void setContractid(String contractid) {
        this.contractid = contractid;
    }
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
    public Double getReceivemny() {
        return receivemny;
    }
    public void setReceivemny(Double receivemny) {
        this.receivemny = receivemny;
    }
    public String getTs() {
        return ts;
    }
    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getReceiveid() {
        return receiveid;
    }
    public void setReceiveid(String receiveid) {
        this.receiveid = receiveid;
    }
    

}
