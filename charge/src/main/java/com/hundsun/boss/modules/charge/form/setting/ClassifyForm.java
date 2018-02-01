package com.hundsun.boss.modules.charge.form.setting;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.hundsun.boss.base.form.BaseForm;
import com.hundsun.boss.modules.charge.entity.setting.Classify;

/**
 * 计费分类Form
 */
public class ClassifyForm extends BaseForm {
    private String classify_name;
    private String classify_parent;
    private String parent_ids;
    private String order_in;
    private String office_id;

    public ClassifyForm() {
        super();
        this.order_in = "1";
        bindClass = Classify.class.getName();
    }

    @NotBlank(message = "分类名称不允许为空")
    @Length(min = 0, max = 100, message = "分类名称最大长度不能超过100位")
    public String getClassify_name() {
        return classify_name;
    }

    public void setClassify_name(String classify_name) {
        this.classify_name = classify_name;
    }

    @NotBlank(message = "上级分类不允许为空")
    public String getClassify_parent() {
        return classify_parent;
    }

    public void setClassify_parent(String classify_parent) {
        this.classify_parent = classify_parent;
    }

    public String getParent_ids() {
        return parent_ids;
    }

    public void setParent_ids(String parent_ids) {
        this.parent_ids = parent_ids;
    }

    public String getOrder_in() {
        return order_in;
    }

    public void setOrder_in(String order_in) {
        this.order_in = order_in;
    }

    @NotBlank(message = "所属部门不允许为空")
    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }
}
