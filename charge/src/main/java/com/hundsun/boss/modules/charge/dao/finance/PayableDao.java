package com.hundsun.boss.modules.charge.dao.finance;

import java.util.List;
import java.util.Map;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.form.finance.FinSummaryForm;

/**
 * 应收款管理DAO接口
 */
@MyBatisDao
@SuppressWarnings("rawtypes")
public interface PayableDao {
    /**
     * 应收款列表
     * 
     * @param req
     * @return
     */
    public List<Map> payableList(FinSummaryForm req);
}
