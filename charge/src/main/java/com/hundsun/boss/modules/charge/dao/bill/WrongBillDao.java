package com.hundsun.boss.modules.charge.dao.bill;

import java.util.List;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.form.bill.WrongBillForm;

/**
 * 错单管理DAO接口
 */
@MyBatisDao
public interface WrongBillDao {
    /**
     * 错单条目列表(带分页)
     * 
     * @param req
     * @return
     * @throws Exception
     */
    public List<WrongBillForm> queryWrongBill(WrongBillForm req);

    /**
     * 关闭错单
     * 
     * @param req
     * @return
     * @throws Exception
     */
    public Integer updateStatus(WrongBillForm req);

}
