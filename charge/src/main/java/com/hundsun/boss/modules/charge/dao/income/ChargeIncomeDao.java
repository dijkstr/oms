package com.hundsun.boss.modules.charge.dao.income;

import java.util.List;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.form.income.ChargeIncome;
import com.hundsun.boss.modules.charge.form.income.ChargeIncomeForm;

/**
 * 计费财务收入DAO接口
 */
@MyBatisDao
public interface ChargeIncomeDao {
    
    /**
     * 获取财务收入数据
     * @param receiveid
     * @return
     */
    public List<ChargeIncome> get(ChargeIncomeForm chargeIncomeForm);
    /**
     * 更新计费财务收入接口数据的更新标志
     * @param receiveid
     * @return
     */
    public void update(String incomemonths);

}
