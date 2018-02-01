package com.hundsun.boss.modules.charge.dao.sync;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.charge.entity.sync.SyncManager;

/**
 * 协同客户经理DAO接口
 */
@Repository
public class SyncManagerDao extends HibernateDao<SyncManager>{

    public SyncManager getByCustomerManagerNo(String customermanagerno) {
        return getByHql("from SyncManager where customermanagerno = :p1", new Parameter(customermanagerno));
    }
}
