package com.hundsun.boss.modules.charge.form.bill;

/**
 * 账务系统对接基础类
 * 
 * @author zhouqs07071
 *
 */
public class ChargeTransDetail {

    /**
     * 账户系统错误码
     */
    private String bill_id;

    private String charge_begin_date;

    private String charge_end_date;

    private String contract_id;

    private String combine_id;

    private String product_id;

    private String feemodel_id;

    private String service_charge;

    private String occur_date;

    private String org_amt;

    private String fee_ratio;

    private String prod_begin_date;

    private int view_flag;

    private String begin_date;

    private String end_date;

    public String getProd_begin_date() {
        return prod_begin_date;
    }

    public void setProd_begin_date(String prod_begin_date) {
        this.prod_begin_date = prod_begin_date;
    }

    public int getView_flag() {
        return view_flag;
    }

    public void setView_flag(int view_flag) {
        this.view_flag = view_flag;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getCharge_begin_date() {
        return charge_begin_date;
    }

    public void setCharge_begin_date(String charge_begin_date) {
        this.charge_begin_date = charge_begin_date;
    }

    public String getCharge_end_date() {
        return charge_end_date;
    }

    public void setCharge_end_date(String charge_end_date) {
        this.charge_end_date = charge_end_date;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getCombine_id() {
        return combine_id;
    }

    public void setCombine_id(String combine_id) {
        this.combine_id = combine_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getFeemodel_id() {
        return feemodel_id;
    }

    public void setFeemodel_id(String feemodel_id) {
        this.feemodel_id = feemodel_id;
    }

    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    public String getOccur_date() {
        return occur_date;
    }

    public void setOccur_date(String occur_date) {
        this.occur_date = occur_date;
    }

    public String getOrg_amt() {
        return org_amt;
    }

    public void setOrg_amt(String org_amt) {
        this.org_amt = org_amt;
    }

    public String getFee_ratio() {
        return fee_ratio;
    }

    public void setFee_ratio(String fee_ratio) {
        this.fee_ratio = fee_ratio;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

}
