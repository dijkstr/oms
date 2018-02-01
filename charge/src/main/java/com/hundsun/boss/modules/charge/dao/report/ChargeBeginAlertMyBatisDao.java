package com.hundsun.boss.modules.charge.dao.report;

import java.util.List;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.entity.report.ChargeBeginAlert;
import com.hundsun.boss.modules.charge.form.common.SearchForm;

/**
 * 合同收入预警
 */
@MyBatisDao
public interface ChargeBeginAlertMyBatisDao {
    /**
     * 第一次收入
     * 
     * @param req
     * @return
     */
    public List<ChargeBeginAlert> list(SearchForm searchForm);
}
