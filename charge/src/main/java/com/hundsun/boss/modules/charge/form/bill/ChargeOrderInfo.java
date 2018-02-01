package com.hundsun.boss.modules.charge.form.bill;

/***
 * 计费模式.
 * 
 * @author lzh
 * @date 2016-06-27
 */
public class ChargeOrderInfo {
    /**
     * 账单ID.
     */
    private String bill_id;

    private String contract_id;

    private String bill_title;

    private String bill_display_date;

    private String customer_name;

    private String pay_deadline;

    private String pay_deadline_ymd;

    private String collec_comp_name;

    private String bank_name;

    private String bank_account;

    private String total_service_charge;

    private String total_bankreceipt;

    private String total_adjust_amt;

    private String total_advance_service_charge;

    private String adjust_amt;

    private String receivable;

    private String payable;

    private String season_charge;

    private String receipt_name;

    private String total_advance_charge;

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getBill_title() {
        return bill_title;
    }

    public void setBill_title(String bill_title) {
        this.bill_title = bill_title;
    }

    public String getBill_display_date() {
        return bill_display_date;
    }

    public void setBill_display_date(String bill_display_date) {
        this.bill_display_date = bill_display_date;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getPay_deadline() {
        return pay_deadline;
    }

    public void setPay_deadline(String pay_deadline) {
        this.pay_deadline = pay_deadline;
    }

    public String getPay_deadline_ymd() {
        return pay_deadline_ymd;
    }

    public void setPay_deadline_ymd(String pay_deadline_ymd) {
        this.pay_deadline_ymd = pay_deadline_ymd;
    }

    public String getCollec_comp_name() {
        return collec_comp_name;
    }

    public void setCollec_comp_name(String collec_comp_name) {
        this.collec_comp_name = collec_comp_name;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    public String getTotal_service_charge() {
        return total_service_charge;
    }

    public void setTotal_service_charge(String total_service_charge) {
        this.total_service_charge = total_service_charge;
    }

    public String getTotal_bankreceipt() {
        return total_bankreceipt;
    }

    public void setTotal_bankreceipt(String total_bankreceipt) {
        this.total_bankreceipt = total_bankreceipt;
    }

    public String getTotal_adjust_amt() {
        return total_adjust_amt;
    }

    public void setTotal_adjust_amt(String total_adjust_amt) {
        this.total_adjust_amt = total_adjust_amt;
    }

    public String getTotal_advance_service_charge() {
        return total_advance_service_charge;
    }

    public void setTotal_advance_service_charge(String total_advance_service_charge) {
        this.total_advance_service_charge = total_advance_service_charge;
    }

    public String getAdjust_amt() {
        return adjust_amt;
    }

    public void setAdjust_amt(String adjust_amt) {
        this.adjust_amt = adjust_amt;
    }

    public String getReceivable() {
        return receivable;
    }

    public void setReceivable(String receivable) {
        this.receivable = receivable;
    }

    public String getPayable() {
        return payable;
    }

    public void setPayable(String payable) {
        this.payable = payable;
    }

    public String getSeason_charge() {
        return season_charge;
    }

    public void setSeason_charge(String season_charge) {
        this.season_charge = season_charge;
    }

    public String getReceipt_name() {
        return receipt_name;
    }

    public void setReceipt_name(String receipt_name) {
        this.receipt_name = receipt_name;
    }

    public String getTotal_advance_charge() {
        return total_advance_charge;
    }

    public void setTotal_advance_charge(String total_advance_charge) {
        this.total_advance_charge = total_advance_charge;
    }

}
