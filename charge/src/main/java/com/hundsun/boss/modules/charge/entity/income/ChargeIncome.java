package com.hundsun.boss.modules.charge.entity.income;

import java.io.Serializable;
/**
 * 计费财务收入接口Entity
 */
public class ChargeIncome implements Serializable {
    private static final long serialVersionUID = 1L;

    //产品行id
    private String detailid;
    // 服务开始日期
    private String begindate;
    // 服务结束日期
    private String enddate;
    // 收入月份
    private String incomedate;
    // 协同合同号
    private String contractid;
    //协同销售产品ID
    private String productid;
    //产品计费类型
    private String chargetype;
    //累计财务收入
    private Double allincomemny;
    public String getDetailid() {
        return detailid;
    }
    public void setDetailid(String detailid) {
        this.detailid = detailid;
    }
    public String getBegindate() {
        return begindate;
    }
    public void setBegindate(String begindate) {
        this.begindate = begindate;
    }
    public String getEnddate() {
        return enddate;
    }
    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
    public String getIncomedate() {
        return incomedate;
    }
    public void setIncomedate(String incomedate) {
        this.incomedate = incomedate;
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
    public String getChargetype() {
        return chargetype;
    }
    public void setChargetype(String chargetype) {
        this.chargetype = chargetype;
    }
    public Double getAllincomemny() {
        return allincomemny;
    }
    public void setAllincomemny(Double allincomemny) {
        this.allincomemny = allincomemny;
    }
    

}
