package com.hundsun.boss.modules.charge.entity.ordervalidate;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.hundsun.boss.modules.charge.entity.sync.SyncContract;
import com.hundsun.boss.modules.charge.entity.sync.SyncProduct;
import com.hundsun.boss.modules.sys.entity.Office;
/**
 * 合同校验信息Entity
 */
@Entity
@Table(name = "order_validation_info")
public class OrderValidate implements Serializable{
    private static final long serialVersionUID = 1L;
       @Id
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
       
       @ManyToOne
       @JoinColumn(name = "order_source", referencedColumnName = "code", insertable = false, updatable = false)
       @NotFound(action = NotFoundAction.IGNORE) 
       private Office office;  // 归属部门
       
      
    public Office getOffice() {
        return office;
    }
    public void setOffice(Office office) {
        this.office = office;
    }
    
    @ManyToOne
    @JoinColumn(name = "hs_product_id", referencedColumnName = "productid", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private SyncProduct syncProduct; //产品名称
    
    public SyncProduct getSyncProduct() {
        return syncProduct;
    }
    public void setSyncProduct(SyncProduct syncProduct) {
        this.syncProduct = syncProduct;
    }
    
    //合同
    @ManyToOne
    @JoinColumn(name = "hs_contract_id", referencedColumnName = "contractid", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private SyncContract syncContract;
   
    public SyncContract getSyncContract() {
        return syncContract;
    }
    public void setSyncContract(SyncContract syncContract) {
        this.syncContract = syncContract;
    }
    
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
       
       
    

}
