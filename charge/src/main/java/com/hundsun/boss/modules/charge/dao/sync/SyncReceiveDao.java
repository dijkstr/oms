package com.hundsun.boss.modules.charge.dao.sync;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.entity.sync.SyncReceive;

/**
 * 到款备份DAO接口
 */
@MyBatisDao
public interface SyncReceiveDao {
    /**
     * 插入到款备份表
     * @param syncReceive
     */
    public void save(SyncReceive syncReceive);
    /**
     * 获取到款备份表对象
     * @param receiveid
     * @return
     */
    public SyncReceive get(String receiveid);
    /**
     * 修改到款备份表对象
     * @param receiveid
     * @return
     */
    public void  update(SyncReceive syncReceive);
  

}
