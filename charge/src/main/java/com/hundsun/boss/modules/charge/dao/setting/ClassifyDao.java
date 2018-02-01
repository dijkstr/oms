package com.hundsun.boss.modules.charge.dao.setting;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.charge.entity.setting.Classify;

/**
 * 计费分类DAO接口
 */
@Repository
public class ClassifyDao extends HibernateDao<Classify> {
    
    public List<Classify> findCommonMenu(){
        return find("from Classify where office_id = '' and del_flag = :p1 order by classify_parent,order_in",new Parameter(Classify.DEL_FLAG_NORMAL));
    }

    /**
     * 逻辑删除
     * 
     * @param id
     * @param likeParentIds
     * @return
     */
    public int delete(Serializable id, String likeParentIds) {
        return update("delete Classify where id = :p1 or parent_ids like :p2", new Parameter(id, likeParentIds));
    }
    
    /**
     * 查询某个分类及下级分类的id集合
     * @param id
     * @param likeParentIds
     * @return
     */
    public List<String> findClassifysById(Serializable id, String likeParentIds) {
        return find("select id from Classify where id = :p1 or parent_ids like :p2", new Parameter(id, likeParentIds));
    }
    
    /**
     * 查询某个分类及下级分类的部门code集合
     * @param id
     * @param likeParentIds
     * @return
     */
    public List<String> findOfficeCodesById(Serializable id, String likeParentIds) {
        return find("select DISTINCT office_id from Classify where id = :p1 or parent_ids like :p2", new Parameter(id, likeParentIds));
    }
    
}
