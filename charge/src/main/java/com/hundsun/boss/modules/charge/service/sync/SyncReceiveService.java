package com.hundsun.boss.modules.charge.service.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.modules.charge.dao.sync.SyncReceiveDao;
import com.hundsun.boss.modules.charge.entity.sync.SyncReceive;

/**
 * 合同到款备份Service
 */
@Component
@Transactional(readOnly = true)
public class SyncReceiveService extends BaseService{
    @Autowired
    private SyncReceiveDao syncReciveDao;
/**
 * 获取到款
 * @param receiveid
 * @return
 */
    public SyncReceive get(String receiveid) {
        return syncReciveDao.get(receiveid);
    }

//    public Page<SyncReceive> find(Page<SyncReceive> page, SyncReceive syncReceive) {
//        DetachedCriteria dc = syncReciveDao.createDetachedCriteria();
//        return syncReciveDao.find(page, dc);
//    }
/**
 * 保存到款
 * @param syncReceive
 */
    @Transactional(readOnly = false)
    public void save(SyncReceive syncReceive) {
        syncReciveDao.save(syncReceive);
    }
    /**
     * 更新到款
     * @param syncReceive
     */
    @Transactional(readOnly = false)
    public void update(SyncReceive syncReceive) {
        syncReciveDao.update(syncReceive);
    }
    

//    @Transactional(readOnly = false)
//    public void delete(String detailid) {
//        syncReciveDao.deleteById(detailid);
//    }
//    @Transactional(readOnly = false)
//    public void delete(String receiveid,String column) {
//        syncReciveDao.deleteByColumn(receiveid,column);
//    }

}
