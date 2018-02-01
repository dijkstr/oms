package com.hundsun.boss.modules.charge.service.tsysconfig;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.dao.tsysconfig.TsysconfigDao;
import com.hundsun.boss.modules.charge.entity.tsysconfig.Tsysconfig;
/**
 * 系统参数配置Service
 * @author feigq
 *
 */
@Component
@Transactional(readOnly = true)
public class TsysconfigService extends  BaseService{
    @Autowired
    private TsysconfigDao tsysconfigDao;
/**
 * 获取系统参数配置
 * @param receiveid
 * @return
 */
    public Tsysconfig get(String prop_name) {
        return tsysconfigDao.get(prop_name);
    }
    
    /**
     * 查询系统配置
     * @param page
     * @param tsysconfigForm
     * @return
     */
    
    public Page<Tsysconfig> find(Page<Tsysconfig> page, Tsysconfig tsysconfig) {
       
        DetachedCriteria dc = tsysconfigDao.createDetachedCriteria();
        //系统参数名称
        if (StringUtils.isNotEmpty(tsysconfig.getProp_name())) {
            dc.add(Restrictions.like("prop_name", "%" + tsysconfig.getProp_name() + "%"));
        }
       
        dc.addOrder(Order.desc("prop_name"));
        return tsysconfigDao.find(page, dc);
    }
    
    /**
     * 保存系统参数配置
     * @param tsysconfig
     */
    @Transactional(readOnly = false)
    public void save(Tsysconfig tsysconfig) {
        tsysconfigDao.save(tsysconfig);
    }
    /**
     * 删除系统参数配置
     * @param prop_name
     */
    @Transactional(readOnly = false)
    public void delete(String prop_name) {
        tsysconfigDao.deleteById(prop_name);
    }
}
