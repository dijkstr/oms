package com.hundsun.boss.modules.charge.dao.audit;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.charge.entity.audit.OrderInfoAuditDetail;
import com.hundsun.boss.modules.charge.entity.order.OrderInfo;

/**
 * 计费合同审核明细DAO接口
 */
@Repository
public class OrderInfoAuditDetailDao extends HibernateDao<OrderInfoAuditDetail> {
    public List<OrderInfoAuditDetail> getByContractProcessKey(String contractProcessKey) {
        return find("from OrderInfoAuditDetail where contract_process_key = :p1 and del_flag = :p2 order by create_date asc", new Parameter(contractProcessKey, OrderInfo.DEL_FLAG_NORMAL));
    }

    public int deleteByContractProcessKey(String contractProcessKey) {
        return update("delete from OrderInfoAuditDetail where contract_process_key = :p1 ", new Parameter(contractProcessKey));
    }

    public List<OrderInfoAuditDetail> sortByUpdateDateDesc() {
        return find("from OrderInfoAuditDetail order by update_date desc");
    }
}
