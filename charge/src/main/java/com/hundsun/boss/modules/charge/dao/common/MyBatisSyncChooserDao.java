package com.hundsun.boss.modules.charge.dao.common;

import java.util.List;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.form.common.SyncProductForm;


@MyBatisDao
public interface MyBatisSyncChooserDao {
	
    /**
     * 获取符合条件的协同产品信息.
     * @param 参数.
     * @return 返回值.
     */
    public List<SyncProductForm> queryXtProds(SyncProductForm req);
    /**
     * 获取符合条件的协同产品信息的条目数.
     * @param 参数.
     * @return 返回值.
     */
    public int queryXtProdsCount(SyncProductForm req);
}
