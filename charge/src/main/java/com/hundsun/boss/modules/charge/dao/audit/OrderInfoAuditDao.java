package com.hundsun.boss.modules.charge.dao.audit;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.charge.entity.audit.OrderInfoAudit;
import com.hundsun.boss.modules.charge.entity.order.OrderInfo;

/**
 * 计费合同审核DAO接口
 */
@Repository
public class OrderInfoAuditDao extends HibernateDao<OrderInfoAudit> {
    public OrderInfoAudit getByContractProcessKey(String contractProcessKey) {
        return getByHql("from OrderInfoAudit where contract_process_key = :p1 and del_flag = :p2", new Parameter(contractProcessKey, OrderInfo.DEL_FLAG_NORMAL));
    }

}
