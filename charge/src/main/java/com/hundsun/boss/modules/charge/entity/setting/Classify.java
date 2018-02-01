package com.hundsun.boss.modules.charge.entity.setting;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.hundsun.boss.base.hibernate.IdEntity;
import com.hundsun.boss.modules.sys.entity.Office;

/**
 * 计费分类Entity
 */
@Entity
@Table(name = "Classify")
public class Classify extends IdEntity<Classify> {

    private static final long serialVersionUID = 1L;
    private String classify_name;
    private String classify_parent;
    private String parent_ids;
    private String order_in;
    private String office_id;
    private Office office;  // 归属部门

    public Classify() {
        super();
        this.order_in = "1";
    }

    public Classify(String id) {
        this();
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "office_id", referencedColumnName = "code", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public ChargeModel ChargeModel;

    @OneToOne(cascade = { CascadeType.REFRESH })
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "id")
    @Where(clause = "DEL_FLAG=0")
    @NotFound(action = NotFoundAction.IGNORE)
    public ChargeModel getChargeModel() {
        return ChargeModel;
    }

    public void setChargeModel(ChargeModel chargeModel) {
        ChargeModel = chargeModel;
    }

    public String getClassify_name() {
        return classify_name;
    }

    public void setClassify_name(String classify_name) {
        this.classify_name = classify_name;
    }

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

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    @Transient
    public static void sortList(List<Classify> list, List<Classify> sourcelist, String parentId) {
        for (int i = 0; i < sourcelist.size(); i++) {
            Classify e = sourcelist.get(i);
            if (e.getClassify_parent() != null && e.getClassify_parent().equals(parentId)) {
                list.add(e);
                // 判断是否还有子节点, 有则继续获取子节点
                for (int j = 0; j < sourcelist.size(); j++) {
                    Classify child = sourcelist.get(j);
                    if (child.getClassify_parent() != null && child.getClassify_parent().equals(e.getId())) {
                        sortList(list, sourcelist, e.getId());
                        break;
                    }
                }
            }
        }
    }

}
