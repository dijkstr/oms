package com.hundsun.boss.modules.charge.dao.income;

import java.util.List;
import java.util.Map;

import com.hundsun.boss.base.mybatis.MyBatisDao;

/**
 * 收入期间同步接口
 */
@MyBatisDao
public interface ChargeIncomePeriodInterfaceMyBatisDao {
    /**
     * 根据协同合同号获取协同明细行列表
     * 
     * @param contract_id
     * @return
     */
    public List<Map<String, String>> getContdetailForIncomeperiodSync(String contract_id);

    /**
     * 获取某个明细行最新一次已推送的结果
     * 
     * @param detailid
     * @return
     */
    public Map<String, String> getLastIncomePeriodForCompare(String detailid);

}
