package com.hundsun.boss.modules.charge.entity.setting;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.hundsun.boss.base.hibernate.IdEntity;
import com.hundsun.boss.common.beanvalidator.Numberic;
import com.hundsun.boss.modules.sys.entity.Office;

/**
 * 字典Entity
 */
@Entity
@Table(name = "charge_type")
public class ChargeType extends IdEntity<ChargeType> {

    private static final long serialVersionUID = 1L;
    private String label;	// 标签名
    private String value;	// 数据值
    private String type;	// 类型
    private String description;// 描述
    private String sort;	// 排序
    private String office_id;   //所属部门
    private Office office;  // 归属部门
    
    @ManyToOne
    @JoinColumn(name = "office_id", referencedColumnName = "code", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public ChargeType() {
        super();
    }

    public ChargeType(String id) {
        this();
        this.id = id;
    }

    @Length(min = 1, max = 100)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Length(min = 1, max = 100)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Length(min = 1, max = 100)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Length(min = 0, max = 100)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotBlank
    @Numberic
    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @NotBlank(message = "所属部门不允许为空")
    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    

}