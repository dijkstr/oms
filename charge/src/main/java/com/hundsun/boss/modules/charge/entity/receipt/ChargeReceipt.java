package com.hundsun.boss.modules.charge.entity.receipt;

import java.io.Serializable;
/**
 * 协同合同到款业务Entity
 */
//@Entity
//@Table(name="charge_bankreceipt")
public class ChargeReceipt  implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // 到款单号
    private String bankreceipt_serialno;
    // 业务单元
    private String department;
    // 合同编号
    private String contract_id;
    // 协同销售产品id
    private String ex_product_id;
    //销售产品名称
    private String product_name;
    //到款金额
    private Double bankreceipt_amount;
    //到款日期
    private String bankreceipt_month;
    //到款行id
    private String receiveid;
    //创建时间
    private String bankreceipt_date;
   
  
    public String getBankreceipt_serialno() {
        return bankreceipt_serialno;
    }
    public void setBankreceipt_serialno(String bankreceipt_serialno) {
        this.bankreceipt_serialno = bankreceipt_serialno;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
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
    public String getProduct_name() {
        return product_name;
    }
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
    public Double getBankreceipt_amount() {
        return bankreceipt_amount;
    }
    public void setBankreceipt_amount(Double bankreceipt_amount) {
        this.bankreceipt_amount = bankreceipt_amount;
    }
    public String getBankreceipt_month() {
        return bankreceipt_month;
    }
    public void setBankreceipt_month(String bankreceipt_month) {
        this.bankreceipt_month = bankreceipt_month;
    }
    public String getReceiveid() {
        return receiveid;
    }
    public void setReceiveid(String receiveid) {
        this.receiveid = receiveid;
    }
    public String getBankreceipt_date() {
        return bankreceipt_date;
    }
    public void setBankreceipt_date(String bankreceipt_date) {
        this.bankreceipt_date = bankreceipt_date;
    }
       
}
