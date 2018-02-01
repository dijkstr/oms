package com.hundsun.boss.modules.charge.dao.order;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.charge.entity.order.OrderIncomeSetting;


/**
 * 合同收入设置DAO接口
 */
@Repository
public class OrderIncomeSettingDao extends HibernateDao<OrderIncomeSetting> {
    
    /**
     * 根据协同合同编号删除收入设置
     * 
     * @param contractId
     */
    public void deleteByContractId(String contractId) {
        update("delete OrderIncomeSetting where contract_id = :p1", new Parameter(contractId));
    }
}
