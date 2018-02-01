package com.hundsun.boss.modules.charge.form.income;

import java.io.Serializable;

import com.hundsun.boss.base.form.BaseForm;

/**
 * 收入期间接口
 */
public class ChargeIncomePeriodInterfaceForm extends BaseForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String office_id;
    private String detailid;
    private String con_id;
    private String contract_id;
    private String ex_product_id;
    private String product_id;
    private String payment_type;
    private String income_begin_date;
    private String income_end_date;
    private String send_flag;
    private String create_date;
    private String create_by;
    private String ids;
    //财务请求的数据偏移量
    private int offset;
    //财务请求的数据分页数
    private int pagesize;
    private String backtype;
    private String showEmptyIncomeDate;
    private String hs_customername;

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public String getCon_id() {
        return con_id;
    }

    public void setCon_id(String con_id) {
        this.con_id = con_id;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getDetailid() {
        return detailid;
    }

    public void setDetailid(String detailid) {
        this.detailid = detailid;
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

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
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

    public String getSend_flag() {
        return send_flag;
    }

    public void setSend_flag(String send_flag) {
        this.send_flag = send_flag;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public String getBacktype() {
        return backtype;
    }

    public void setBacktype(String backtype) {
        this.backtype = backtype;
    }

    public String getShowEmptyIncomeDate() {
        return showEmptyIncomeDate;
    }

    public void setShowEmptyIncomeDate(String showEmptyIncomeDate) {
        this.showEmptyIncomeDate = showEmptyIncomeDate;
    }

    public String getHs_customername() {
        return hs_customername;
    }

    public void setHs_customername(String hs_customername) {
        this.hs_customername = hs_customername;
    }

}
