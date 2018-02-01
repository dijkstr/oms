package com.hundsun.boss.modules.cms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.cms.entity.Article;

/**
 * 文章DAO接口
 */
@Repository
public class ArticleDao extends HibernateDao<Article> {

    public List<Article> findByIdIn(String[] ids) {
        return find("from Article where id in (:p1)", new Parameter(new Object[] { ids }));
    }

    public int updateHitsAddOne(String id) {
        return update("update Article set hits=hits+1 where id = :p1", new Parameter(id));
    }

    public int updateExpiredWeight() {
        return update("update Article set weight=0 where weight > 0 and weightDate < current_timestamp()");
    }

}
