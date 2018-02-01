package com.hundsun.boss.modules.charge.form.income;

import java.io.Serializable;

import com.hundsun.boss.base.form.BaseForm;

/**
 * 收入来源接口
 */
public class ChargeIncomeSourceInterfaceForm extends BaseForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String office_id;
    private String con_id;
    private String contract_id;
    private String customer_id;
    private String income_source;
    private String send_flag;
    private String create_date;
    private String create_by;
    private String ids;
    //财务请求的数据偏移量
    private int offset;
    //财务请求的数据分页数
    private int pagesize;
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

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getIncome_source() {
        return income_source;
    }

    public void setIncome_source(String income_source) {
        this.income_source = income_source;
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

    public String getHs_customername() {
        return hs_customername;
    }

    public void setHs_customername(String hs_customername) {
        this.hs_customername = hs_customername;
    }

}
