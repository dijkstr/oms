package com.hundsun.boss.modules.charge.form.ordervalidate;

import java.util.List;

import com.hundsun.boss.base.page.Page;

/**
 * 合同校验form
 * @author feigq
 *
 */

@SuppressWarnings("rawtypes")
public class OrderValidateForm extends Page{
    //流水号
    private String serial_no;
    //订单id
    private String order_id;
    //协同合同编号
    private String hs_contract_id;
    //组合id
    private String combine_id;
    //协同产品编号
    private String hs_product_id;
    //协同产品id
    private String product_id;
    //原因
    private String reason;
    // 所属类型
    private String belong_type;
    //操作时间
    private String operator_datetime;
    //操作人
    private String operator;
    //显示状态
    private String show_status;
    //协同校验状态
    private String hs_check_status;
    //校验类型
    private String check_type;
    //校验字段
    private String check_column;
    //公司
    private String order_source;
    //备注
    private String remark;
    //校验类型集合
    private List<String> hs_check_status_list;
    

    public String getSerial_no() {
        return serial_no;
    }
    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }
    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public String getHs_contract_id() {
        return hs_contract_id;
    }
    public void setHs_contract_id(String hs_contract_id) {
        this.hs_contract_id = hs_contract_id;
    }
    public String getCombine_id() {
        return combine_id;
    }
    public void setCombine_id(String combine_id) {
        this.combine_id = combine_id;
    }
    public String getHs_product_id() {
        return hs_product_id;
    }
    public void setHs_product_id(String hs_product_id) {
        this.hs_product_id = hs_product_id;
    }
    public String getProduct_id() {
        return product_id;
    }
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getBelong_type() {
        return belong_type;
    }
    public void setBelong_type(String belong_type) {
        this.belong_type = belong_type;
    }
    public String getOperator_datetime() {
        return operator_datetime;
    }
    public void setOperator_datetime(String operator_datetime) {
        this.operator_datetime = operator_datetime;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public String getShow_status() {
        return show_status;
    }
    public void setShow_status(String show_status) {
        this.show_status = show_status;
    }
    public String getHs_check_status() {
        return hs_check_status;
    }
    public void setHs_check_status(String hs_check_status) {
        this.hs_check_status = hs_check_status;
    }
    public String getCheck_type() {
        return check_type;
    }
    public void setCheck_type(String check_type) {
        this.check_type = check_type;
    }
    public String getCheck_column() {
        return check_column;
    }
    public void setCheck_column(String check_column) {
        this.check_column = check_column;
    }
    public String getOrder_source() {
        return order_source;
    }
    public void setOrder_source(String order_source) {
        this.order_source = order_source;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public List<String> getHs_check_status_list() {
        return hs_check_status_list;
    }
    public void setHs_check_status_list(List<String> hs_check_status_list) {
        this.hs_check_status_list = hs_check_status_list;
    }
    
    

}
