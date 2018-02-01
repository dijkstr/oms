package com.hundsun.boss.modules.charge.dao.report;

import java.util.List;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.entity.report.ChargeFirstIncome;
import com.hundsun.boss.modules.charge.form.common.SearchForm;

/**
 * 第一次收入
 */
@MyBatisDao
public interface ChargeFirstIncomeMyBatisDao {
    /**
     * 第一次收入
     * 
     * @param req
     * @return
     */
    public List<ChargeFirstIncome> list(SearchForm searchForm);
}
