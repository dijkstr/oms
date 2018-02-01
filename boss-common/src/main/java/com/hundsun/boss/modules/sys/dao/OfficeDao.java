package com.hundsun.boss.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.sys.entity.Office;

/**
 * 机构DAO接口
 */
@Repository
public class OfficeDao extends HibernateDao<Office> {

    public List<Office> findByParentIdsLike(String parentIds) {
        return find("from Office where parentIds like :p1", new Parameter(parentIds));
    }

    public Office findByCode(String code) {
        return getByHql("from Office where code = :p1 and del_flag = :p2", new Parameter(code, Office.DEL_FLAG_NORMAL));
    }

    //	@Query("from Office where (id=?1 or parent.id=?1 or parentIds like ?2) and delFlag='" + Office.DEL_FLAG_NORMAL + "' order by code")
    //	public List<Office> findAllChild(Long parentId, String likeParentIds);

    public Office findDeptByCode(String codes) {
        return getByHql("FROM Office o1 WHERE id IN (SELECT SUBSTRING(o2.parentIds, 38, 32) FROM Office o2 WHERE o2.code in :p1)", new Parameter(codes));
    }
}
