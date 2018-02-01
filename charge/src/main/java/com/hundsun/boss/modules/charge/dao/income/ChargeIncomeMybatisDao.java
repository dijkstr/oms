package com.hundsun.boss.modules.charge.dao.income;

import java.util.List;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.entity.income.ChargeIncomeInterface;
import com.hundsun.boss.modules.charge.form.income.ChargeIncomeForm;

/**
 * 计费财务收入DAO接口
 */
@MyBatisDao
public interface ChargeIncomeMybatisDao {

    /**
     * 获取财务收入数据
     * 
     * @param receiveid
     * @return
     */
    public List<ChargeIncomeInterface> get(ChargeIncomeForm chargeIncomeForm);

    /**
     * 更新计费财务收入接口数据的更新标志
     * 
     * @param receiveid
     * @return
     */
    public void update(String incomemonths);

    /**
     * 本地留存
     */
    public void backup(String incomemonths);

    /**
     * 根据协同合同编号删除历史收入记录
     * 
     * @param contract_id
     * @return
     */
    public void deleteIncomeByContractId(String contract_id);
}
