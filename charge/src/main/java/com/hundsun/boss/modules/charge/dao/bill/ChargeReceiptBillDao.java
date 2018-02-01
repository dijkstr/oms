package com.hundsun.boss.modules.charge.dao.bill;

import java.util.List;
import java.util.Map;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.form.bill.ChargeReceiptForm;

/**
 * 详单管理DAO接口
 */
@MyBatisDao
@SuppressWarnings("rawtypes")
public interface ChargeReceiptBillDao {
    /**
     * 详单条目列表(带分页)
     * 
     * @param req
     * @return
     * @throws Exception
     */
    public List<Map> queryChargeReceipt(ChargeReceiptForm form);

    public List<Map> getChrargeReceiptList(ChargeReceiptForm form);
}
