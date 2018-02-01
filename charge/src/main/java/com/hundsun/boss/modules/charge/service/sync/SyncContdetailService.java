package com.hundsun.boss.modules.charge.service.sync;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.modules.charge.dao.sync.SyncContdetailDao;
import com.hundsun.boss.modules.charge.entity.sync.SyncContdetail;

/**
 * 合同子表Service
 */
@Component
@Transactional(readOnly = true)
public class SyncContdetailService extends BaseService {
    @Autowired
    private SyncContdetailDao syncContdetailDao;

    /**
     * 获取合同子表
     * 
     * @param detailid
     * @return
     */
    public SyncContdetail get(String detailid) {
        return syncContdetailDao.get(detailid);
    }

    /**
     * 列表分页显示合同子表
     * 
     * @param page
     * @param syncContdetail
     * @return
     */
    public Page<SyncContdetail> find(Page<SyncContdetail> page, SyncContdetail syncContdetail) {
        DetachedCriteria dc = syncContdetailDao.createDetachedCriteria();
        return syncContdetailDao.find(page, dc);
    }

    /**
     * 保存合同子表
     * 
     * @param syncContdetail
     */
    @Transactional(readOnly = false)
    public void save(SyncContdetail syncContdetail) {
        syncContdetailDao.save(syncContdetail);
    }

    /**
     * 删除合同子表
     * 
     * @param detailid
     */
    @Transactional(readOnly = false)
    public void delete(String detailid) {
        syncContdetailDao.deleteById(detailid);
    }

    /**
     * 查询合同所有表体数据
     * 
     * @param conid
     * @return
     */
    public List<SyncContdetail> getAlldatais(String conid) {

        return syncContdetailDao.getAlldetails(conid);
    }

    /**
     * 保存合同子表全部数据
     * 
     * @param lists
     */
    @Transactional(readOnly = false)
    public void saveAll(List<SyncContdetail> lists) {
        syncContdetailDao.save(lists);
        ;
    }

}
