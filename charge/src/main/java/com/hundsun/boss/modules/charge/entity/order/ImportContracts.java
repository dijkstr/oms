package com.hundsun.boss.modules.charge.entity.order;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.utils.excel.annotation.ExcelField;

public class ImportContracts extends Page<ImportContracts> {
    private String contract_id;
    private String office_id;
    private Date order_begin_date;
    private Date order_end_date;
    private String min_cousume;
    private String product_id;
    private String payment_amount;
    private Date advance_date;

    @NotNull(message = "contract_id不能为空")
    @ExcelField(title = "合同编号", align = 2, sort = 10)
    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    @ExcelField(title = "客户名称", align = 2, sort = 20)
    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    @ExcelField(title = "合同起始时间", align = 2, sort = 30)
    public Date getOrder_begin_date() {
        return order_begin_date;
    }

    public void setOrder_begin_date(Date order_begin_date) {
        this.order_begin_date = order_begin_date;
    }

    @ExcelField(title = "合同结束时间", align = 2, sort = 40)
    public Date getOrder_end_date() {
        return order_end_date;
    }

    public void setOrder_end_date(Date order_end_date) {
        this.order_end_date = order_end_date;
    }

    @ExcelField(title = "月保底金额", align = 2, sort = 50)
    public String getMin_cousume() {
        return min_cousume;
    }

    public void setMin_cousume(String min_cousume) {
        this.min_cousume = min_cousume;
    }

    @ExcelField(title = "产品编号", align = 2, sort = 60)
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    @ExcelField(title = "预付金额", align = 2, sort = 70)
    public String getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(String payment_amount) {
        this.payment_amount = payment_amount;
    }

    @ExcelField(title = "预付时间", align = 2, sort = 80)
    public Date getAdvance_date() {
        return advance_date;
    }

    public void setAdvance_date(Date advance_date) {
        this.advance_date = advance_date;
    }

}
