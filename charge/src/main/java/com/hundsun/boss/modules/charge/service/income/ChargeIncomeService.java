package com.hundsun.boss.modules.charge.service.income;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.modules.charge.dao.income.ChargeIncomeMybatisDao;
import com.hundsun.boss.modules.charge.entity.income.ChargeIncomeInterface;
import com.hundsun.boss.modules.charge.form.income.ChargeIncomeForm;

/**
 * 计费财务收入接口Service
 */
@Component
public class ChargeIncomeService extends BaseService {
    @Autowired
    private ChargeIncomeMybatisDao chargeIncomeDao;

    /**
     * 获取财务收入接口的数据
     * 
     * @param incomemonths
     * @return
     */
    public List<ChargeIncomeInterface> get(ChargeIncomeForm chargeIncomeForm) {
        // 本地备份
        return chargeIncomeDao.get(chargeIncomeForm);
    }

    /**
     * 财务系统数据落地后，通知计费系统更新推送的数据的标志
     * 
     * @param incomemonths
     */
    public void update(String incomemonths) {
        chargeIncomeDao.update(incomemonths);
    }

    /**
     * 固化财务收入
     * 
     * @param incomemonths
     */
    public void backup(String incomemonths) {
        chargeIncomeDao.backup(incomemonths);
    }

    /**
     * 根据协同合同编号删除历史收入记录
     * 
     * @param contract_id
     */
    public void deleteIncomeByContractId(String contract_id) {
        chargeIncomeDao.deleteIncomeByContractId(contract_id);
    }

}
