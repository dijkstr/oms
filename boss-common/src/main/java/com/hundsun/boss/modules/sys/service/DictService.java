package com.hundsun.boss.modules.sys.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CacheUtils;
import com.hundsun.boss.modules.sys.dao.DictDao;
import com.hundsun.boss.modules.sys.entity.Dict;
import com.hundsun.boss.modules.sys.utils.DictUtils;

/**
 * 字典Service
 */
@Service
@Transactional(readOnly = true)
public class DictService extends BaseService {

    @Autowired
    private DictDao dictDao;

    //	@Autowired
    //	private MyBatisDictDao myBatisDictDao;

    public Dict get(String id) {
        // MyBatis 查询
        //		return myBatisDictDao.get(id);
        // Hibernate 查询
        return dictDao.get(id);
    }

    public Page<Dict> find(Page<Dict> page, Dict dict) {
        // MyBatis 查询
        //		dict.setPage(page);
        //		page.setList(myBatisDictDao.find(dict));
        //		return page;
        // Hibernate 查询
        DetachedCriteria dc = dictDao.createDetachedCriteria();
        if (StringUtils.isNotEmpty(dict.getType())) {
            dc.add(Restrictions.eq("type", dict.getType()));
        }
        if (StringUtils.isNotEmpty(dict.getDescription())) {
            dc.add(Restrictions.like("description", "%" + dict.getDescription() + "%"));
        }
        dc.add(Restrictions.eq(Dict.FIELD_DEL_FLAG, Dict.DEL_FLAG_NORMAL));
        dc.addOrder(Order.asc("type")).addOrder(Order.asc("sort")).addOrder(Order.desc("id"));
        return dictDao.find(page, dc);
    }

    public List<String> findTypeList() {
        return dictDao.findTypeList();
    }

    @Transactional(readOnly = false)
    public void save(Dict dict) {
        dictDao.save(dict);
        CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        dictDao.deleteById(id);
        CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
    }

}
