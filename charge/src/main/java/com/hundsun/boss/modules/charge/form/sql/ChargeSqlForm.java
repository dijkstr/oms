package com.hundsun.boss.modules.charge.form.sql;

import com.hundsun.boss.base.page.Page;

/**
 * sql脚本form
 * 
 * @author feigq
 *
 */
@SuppressWarnings("rawtypes")
public class ChargeSqlForm extends Page {

    // 脚本
    private String sql;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
