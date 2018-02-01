package com.hundsun.boss.modules.charge.dao.bill;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.charge.entity.bill.OrderRelation;

/**
 * 账单联系人DAO接口
 */
@Repository
public class RelationDao extends HibernateDao<OrderRelation> {

    public List<OrderRelation> getByContractId(String contract_id) {
        return find("from OrderRelation where contract_id = :p1", new Parameter(contract_id));
    }
}
