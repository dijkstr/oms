package com.hundsun.boss.modules.charge.dao.report;

import java.util.List;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.entity.report.ChargeOverMin;
import com.hundsun.boss.modules.charge.form.common.SearchForm;

/**
 * 第一次收入
 */
@MyBatisDao
public interface ChargeOverMinMyBatisDao {
    /**
     * 第一次收入
     * 
     * @param req
     * @return
     */
    public List<ChargeOverMin> list(SearchForm searchForm);
}
