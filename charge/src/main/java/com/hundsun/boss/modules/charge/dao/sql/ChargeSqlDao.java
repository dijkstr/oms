package com.hundsun.boss.modules.charge.dao.sql;

import java.util.List;
import java.util.Map;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.form.sql.ChargeSqlForm;

/**
 * 计费脚本查询DAO接口
 */
@MyBatisDao
@SuppressWarnings("rawtypes")
public interface ChargeSqlDao {
    /**
     * 查询脚本
     */  
   
    public List<Map> excutesql(ChargeSqlForm chargeSqlForm);
   

}
