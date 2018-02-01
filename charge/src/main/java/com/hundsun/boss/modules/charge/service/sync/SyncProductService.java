package com.hundsun.boss.modules.charge.service.sync;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.dao.sync.SyncProductDao;
import com.hundsun.boss.modules.charge.entity.sync.SyncProduct;
import com.hundsun.boss.modules.charge.form.common.SyncProductForm;
/**
 * 协同产品Service
 */
@Component
@Transactional(readOnly = true)
public class SyncProductService extends BaseService{
    @Autowired
    private SyncProductDao syncProductDao;
/**
 * 获取产品
 * @param prdid
 * @return
 */
    public SyncProduct get(String prdid) {
        return syncProductDao.get(prdid);
    }
/**
 * 分页查询显示产品
 * @param page
 * @param syncProduct
 * @return
 */
    public Page<SyncProduct> find(Page<SyncProduct> page, SyncProductForm syncProduct) {
        DetachedCriteria dc = syncProductDao.createDetachedCriteria();
        //产品编号模糊匹配
        if(!CommonUtil.isNullorEmpty(syncProduct.getProductid())){
            dc.add(Restrictions.like("productid", "%"+syncProduct.getProductid()+"%"));
        }
        if(!CommonUtil.isNullorEmpty(syncProduct.getProductname())){
            dc.add(Restrictions.like("productname", "%"+syncProduct.getProductname()+"%"));
        }
        //根据产品编号降序排列
        dc.addOrder(Order.desc("productid"));
        return syncProductDao.find(page, dc);
    }
/**
 * 保存产品
 * @param syncProduct
 */
    @Transactional(readOnly = false)
    public void save(SyncProduct syncProduct) {
        syncProductDao.save(syncProduct);
    }
/**
 * 删除产品
 * @param prdid
 */
    @Transactional(readOnly = false)
    public void delete(String prdid) {
        syncProductDao.deleteById(prdid);
    }
    /**
     * 根据产品名称获取协同产品
     * @param prodname
     * @return
     */
    public List<SyncProduct> getByprodname(String prodname) {
        return syncProductDao.getByprodname(prodname);
    }

}
