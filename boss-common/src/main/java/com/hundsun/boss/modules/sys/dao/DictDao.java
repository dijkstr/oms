package com.hundsun.boss.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.sys.entity.Dict;

/**
 * 字典DAO接口
 */
@Repository
public class DictDao extends HibernateDao<Dict> {

    public List<Dict> findAllList() {
        return find("from Dict where delFlag=:p1 order by sort", new Parameter(Dict.DEL_FLAG_NORMAL));
    }

    public List<String> findTypeList() {
        return find("select type from Dict where delFlag=:p1 group by type", new Parameter(Dict.DEL_FLAG_NORMAL));
    }
}
