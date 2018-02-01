package com.hundsun.boss.modules.charge.dao.finance;

import java.util.List;
import java.util.Map;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.form.finance.AdjustBillForm;

/**
 * 调账管理DAO接口
 */
@MyBatisDao
@SuppressWarnings("rawtypes")
public interface AdjustBillDao {
    /**
     * 查询账单调整记录条数.
     * 
     * @param req 请求.
     * @return
     */
    public Integer getAdjustBillCount(AdjustBillForm req);

    /**
     * 查询账单调整记录.
     * 
     * @param req 请求.
     * @return 返回.
     */
    public List<Map> queryAdjustBill(AdjustBillForm req);

    /**
     * 账单调整新增
     * 
     * @param req
     * @return
     */
    public Integer insertAdjustBill(AdjustBillForm req);

    /**
     * 账单调整删除
     * 
     * @param req
     * @return
     */
    public Integer deleteAdjustBill(AdjustBillForm req);

    /**
     * 账单调整修改
     * 
     * @param req
     * @return
     */
    public Integer updateAdjustBill(AdjustBillForm req);

    /**
     * 获取账单调整信息
     * 
     * @param req
     * @return
     */
    public AdjustBillForm getAdjustBill(AdjustBillForm req);

    /**
     * 批量更新账单调整的状态
     * 
     * @param datas
     */
    public void updateAdjustBillStatus(Map data);

}
