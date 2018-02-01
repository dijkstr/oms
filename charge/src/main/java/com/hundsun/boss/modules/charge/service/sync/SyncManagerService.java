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
import com.hundsun.boss.modules.charge.dao.sync.SyncManagerDao;
import com.hundsun.boss.modules.charge.entity.sync.SyncManager;
import com.hundsun.boss.modules.charge.form.common.SyncManagerForm;
/**
 * 客户经理Service
 */
@Component
@Transactional(readOnly = true)
public class SyncManagerService extends BaseService{
    @Autowired
    private SyncManagerDao syncManagerDao;
/**
 * 获取客户经理
 * @param customermanagerno
 * @return
 */
    public SyncManager get(String customermanagerno) {
        return syncManagerDao.get(customermanagerno);
    }
/**
 * 分页查询客户经理
 * @param page
 * @param syncManager
 * @return
 */
    public Page<SyncManager> find(Page<SyncManager> page, SyncManagerForm syncManager) {
        DetachedCriteria dc = syncManagerDao.createDetachedCriteria();
        //客户经理员工号查询
        if(!CommonUtil.isNullorEmpty(syncManager.getCustomermanagerno())){
            dc.add(Restrictions.like("customermanagerno", "%"+syncManager.getCustomermanagerno()+"%"));
        }
        //客户经理名称查询
        if(!CommonUtil.isNullorEmpty(syncManager.getCustomermanagername())){
            dc.add(Restrictions.like("customermanagername", "%"+syncManager.getCustomermanagername()+"%"));
        }
        //根据员工号降序排列
        dc.addOrder(Order.desc("customermanagerno"));
        return syncManagerDao.find(page, dc);
    }
/**
 * 保存客户经理
 * @param syncManager
 */
    @Transactional(readOnly = false)
    public void save(SyncManager syncManager) {
        syncManagerDao.save(syncManager);
    }
/**
 * 删除客户经理
 * @param customermanagerno
 */
    @Transactional(readOnly = false)
    public void delete(String customermanagerno) {
        syncManagerDao.deleteById(customermanagerno);
    }
    
    /**
     * 查询协同客户经理
     * @param customermanagerno
     * @return
     */
    public SyncManager getByCustomerManagerNo(String customermanagerno) {
        return syncManagerDao.getByCustomerManagerNo(customermanagerno);
    }
}
