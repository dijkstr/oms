package com.hundsun.boss.modules.cms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.cms.entity.Link;

/**
 * 链接DAO接口
 */
@Repository
public class LinkDao extends HibernateDao<Link> {

    public List<Link> findByIdIn(Long[] ids) {
        return find("front Like where id in (:p1)", new Parameter(new Object[] { ids }));
    }

    public int updateExpiredWeight() {
        return update("update Link set weight=0 where weight > 0 and weightDate < current_timestamp()");
    }
}
