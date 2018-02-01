package com.hundsun.boss.modules.charge.entity.temp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.hundsun.boss.base.hibernate.IdEntity;
import com.hundsun.boss.modules.sys.entity.Office;

/**
 * 中间表Entity
 */
@Entity
@Table(name = "kanban_temp_table")
public class TempTable extends IdEntity<TempTable> {

    private static final long serialVersionUID = 1L;
    private String office_id;    // 部门编码
    private Office office;
    private String name; 	// 名称
    private String query_sql;    // 查询sql
    private String chart_type = "plain";

    public TempTable() {
        super();
    }

    public TempTable(String id) {
        this();
        this.id = id;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    @OneToOne
    @JoinColumn(name = "office_id", referencedColumnName = "code", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery_sql() {
        return query_sql;
    }

    public void setQuery_sql(String query_sql) {
        this.query_sql = query_sql;
    }

    public String getChart_type() {
        return chart_type;
    }

    public void setChart_type(String chart_type) {
        this.chart_type = chart_type;
    }

}
