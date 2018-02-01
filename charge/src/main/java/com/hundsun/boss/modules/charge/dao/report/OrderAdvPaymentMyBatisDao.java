package com.hundsun.boss.modules.charge.dao.report;

import java.util.List;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.entity.report.OrderAdvPayment;
import com.hundsun.boss.modules.charge.form.common.SearchForm;

/**
 * 预付查询
 */
@MyBatisDao
public interface OrderAdvPaymentMyBatisDao {
    /**
     * 预付查询
     * 
     * @param req
     * @return
     */
    public List<OrderAdvPayment> list(SearchForm searchForm);
}
