package com.hundsun.boss.modules.charge.service.setting;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.dao.setting.ClassifyDao;
import com.hundsun.boss.modules.charge.entity.setting.Classify;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 计费分类Service
 */
@Component
@Transactional(readOnly = true)
public class ClassifyService extends BaseService {

    @Autowired
    private ClassifyDao classifyDao;

    public Classify get(String id) {
        return classifyDao.get(id);
    }

    public List<Classify> findAllMenu() {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = classifyDao.createDetachedCriteria();
        
        dc.createAlias("office", "office");
        dc.add(dataScopeFilter(currentUser, "office", ""));

        dc.add(Restrictions.eq(Classify.FIELD_DEL_FLAG, Classify.DEL_FLAG_NORMAL));
        dc.addOrder(Order.asc("classify_parent")).addOrder(Order.asc("order_in"));
        return classifyDao.find(dc);
    }

    public Page<Classify> find(Page<Classify> page, Classify classify) {
        DetachedCriteria dc = classifyDao.createDetachedCriteria();
        if (StringUtils.isNotEmpty(classify.getClassify_name())) {
            dc.add(Restrictions.like("classify_name", "%" + classify.getClassify_name() + "%"));
        }
        if (StringUtils.isNotEmpty(classify.getOffice_id())) {
            dc.add(Restrictions.like("office_id", "%" + classify.getOffice_id() + "%"));
        }
        dc.add(Restrictions.eq(Classify.FIELD_DEL_FLAG, Classify.DEL_FLAG_NORMAL));
        dc.addOrder(Order.desc("id"));
        return classifyDao.find(page, dc);
    }

    @Transactional(readOnly = false)
    public void save(Classify classify) {
        classifyDao.save(classify);
    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        classifyDao.delete(id,"%," + id + ",%");
    }

    
    public List<Classify> findCommonMenu() {
        return classifyDao.findCommonMenu();
    }
    
    /**
     * 查询某个分类及下级分类的id集合
     * @param id
     * @return
     */
    public List<String> findClassifysById(String id) {
        return classifyDao.findClassifysById(id, "%," + id + ",%");
    }
    
    /**
     * 查询某个分类及下级分类的部门code集合
     * @param id
     * @return
     */
    public List<String> findOfficeCodesById(String id) {
        return classifyDao.findOfficeCodesById(id, "%," + id + ",%");
    }
    
    public List<Classify> findListByOfficeCode(String officeCode) {
        DetachedCriteria dc = classifyDao.createDetachedCriteria();
        dc.add(Restrictions.eq("office_id", officeCode));
        dc.add(Restrictions.eq(Classify.FIELD_DEL_FLAG, Classify.DEL_FLAG_NORMAL));
        return classifyDao.find(dc);
    }
}
