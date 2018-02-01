package com.hundsun.boss.modules.charge.service.sync;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.dao.sync.SyncCustomerDao;
import com.hundsun.boss.modules.charge.entity.sync.SyncCustomer;
import com.hundsun.boss.modules.charge.form.common.SyncCustomerForm;

/**
 * 客户Service
 */
@Component
@Transactional(readOnly = true)
public class SyncCustomerService extends BaseService {

    @Autowired
    private SyncCustomerDao syncCustomerDao;
/**
 * 获取客户对象
 * @param customerId
 * @return
 */
    public SyncCustomer get(String customerId) {
        return syncCustomerDao.get(customerId);
    }
/**
 * 分页查询客户
 * @param page
 * @param syncCustomer
 * @return
 */
    public Page<SyncCustomer> find(Page<SyncCustomer> page, SyncCustomerForm syncCustomer) {
        DetachedCriteria dc = syncCustomerDao.createDetachedCriteria();
        //根据客户编号模糊匹配
        if(!CommonUtil.isNullorEmpty(syncCustomer.getCustomerid())){
            dc.add(Restrictions.like("customerid", "%"+syncCustomer.getCustomerid()+"%"));           
        }
        //根据客户名称模糊匹配
        if(!CommonUtil.isNullorEmpty(syncCustomer.getChinesename())){
            dc.add(Restrictions.like("chinesename", "%"+syncCustomer.getChinesename()+"%"));           
        }
        //根据客户编号降序排列
        dc.addOrder(Order.desc("customerid"));
        return syncCustomerDao.find(page, dc);
    }
/**
 * 保存客户
 * @param syncCustomer
 */
    @Transactional(readOnly = false)
    public void save(SyncCustomer syncCustomer) {
        syncCustomerDao.save(syncCustomer);
    }
/**
 * 删除客户
 * @param customerId
 */
    @Transactional(readOnly = false)
    public void delete(String customerId) {
        syncCustomerDao.deleteById(customerId);
    }
}
