package com.hundsun.boss.modules.sys.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 系统参数配置Entity
 */
@Entity
@Table(name = "tsys_config")
public class Sysconfig implements Serializable{
 private static final long serialVersionUID = 1L;
    
    //系统参数名称
    @Id
    private String prop_name;
    //系统参数值
    private String prop_code;
    //备注说明
    private String remarks;
   
    public String getProp_name() {
        return prop_name;
    }
    public void setProp_name(String prop_name) {
        this.prop_name = prop_name;
    }
    public String getProp_code() {
        return prop_code;
    }
    public void setProp_code(String prop_code) {
        this.prop_code = prop_code;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    
}
