package com.hundsun.boss.modules.charge.dao.temp;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hundsun.boss.base.mybatis.MyBatisDao;

/**
 * 临时表mybatis查询类
 * 
 */
@SuppressWarnings("rawtypes")
@MyBatisDao
public interface TempTableMyBatisDao {
    /**
     * 获取二维表格式的中间表数据
     * 
     * @param params
     * @return
     */
    public List<LinkedHashMap<String, Object>> getPlainTableData(Map params);
}
