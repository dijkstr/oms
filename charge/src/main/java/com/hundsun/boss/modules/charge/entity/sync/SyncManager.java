package com.hundsun.boss.modules.charge.entity.sync;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 协同客户经理Entity
 */
@Entity
@Table(name = "v_manager_jf")
public class SyncManager implements Serializable {
    private static final long serialVersionUID = 1L;
  //客户经理_员工号
    @Id
    private String customermanagerno;
    //客户经理_姓名
    private String customermanagername;
    //创建日期
    private String createdate;
    //更新日期
    private String updatedate;
    //邮箱
    private String email;
    //手机
    private String mobile_tel;
    //上级姓名
    private String manager_name;
    //上级员工号
    private String manager_no;
    public String getCustomermanagerno() {
        return customermanagerno;
    }
    public void setCustomermanagerno(String customermanagerno) {
        this.customermanagerno = customermanagerno;
    }
    public String getCustomermanagername() {
        return customermanagername;
    }
    public void setCustomermanagername(String customermanagername) {
        this.customermanagername = customermanagername;
    }
    public String getCreatedate() {
        return createdate;
    }
    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
    public String getUpdatedate() {
        return updatedate;
    }
    public void setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMobile_tel() {
        return mobile_tel;
    }
    public void setMobile_tel(String mobile_tel) {
        this.mobile_tel = mobile_tel;
    }
    public String getManager_name() {
        return manager_name;
    }
    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }
    public String getManager_no() {
        return manager_no;
    }
    public void setManager_no(String manager_no) {
        this.manager_no = manager_no;
    }
    
    

}
