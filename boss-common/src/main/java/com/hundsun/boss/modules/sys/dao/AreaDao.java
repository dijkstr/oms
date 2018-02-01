package com.hundsun.boss.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.sys.entity.Area;

/**
 * 区域DAO接口
 */
@Repository
public class AreaDao extends HibernateDao<Area> {

    public List<Area> findByParentIdsLike(String parentIds) {
        return find("from Area where parentIds like :p1", new Parameter(parentIds));
    }

    public List<Area> findAllList() {
        return find("from Area where delFlag=:p1 order by code", new Parameter(Area.DEL_FLAG_NORMAL));
    }

    public List<Area> findAllChild(Long parentId, String likeParentIds) {
        return find("from Area where delFlag=:p1 and (id=:p2 or parent.id=:p2 or parentIds like :p3) order by code", new Parameter(Area.DEL_FLAG_NORMAL, parentId, likeParentIds));
    }
    
    public List<String> findAreasById(String id, String likeParentIds) {
        return find("select id from Area where delFlag=:p1 and (id=:p2 or parent.id=:p2 or parentIds like :p3) order by code", new Parameter(Area.DEL_FLAG_NORMAL, id, likeParentIds));
    }
}
