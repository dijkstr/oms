package com.hundsun.boss.modules.oa.dao;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.oa.entity.Leave;

/**
 * 请假DAO接口
 * 
 * @author liuj
 * @version 2013-8-23
 */
@Repository
public class LeaveDao extends HibernateDao<Leave> {

    public int updateProcessInstanceId(String id, String processInstanceId) {
        return update("update Leave set processInstanceId=:p1 where id = :p2", new Parameter(processInstanceId, id));
    }

}
