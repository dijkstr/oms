package com.hundsun.boss.modules.charge.dao.order;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.charge.entity.order.OrderInfo;

/**
 * 计费合同DAO接口
 */
@Repository
public class OrderInfoDao extends HibernateDao<OrderInfo> {
	
    /**
     * 根据协同合同编号获取计费合同对象
     * 
     * @param contractId
     * @return
     */
    public List<OrderInfo> findByContractId(String contractId) {
        return find("from OrderInfo where contract_id = :p1 and del_flag = :p2", new Parameter(contractId,OrderInfo.DEL_FLAG_NORMAL));
    }
   
}
