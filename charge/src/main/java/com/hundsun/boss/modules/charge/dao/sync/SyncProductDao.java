package com.hundsun.boss.modules.charge.dao.sync;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.charge.entity.sync.SyncProduct;
/**
 * 协同产品Dao接口
 * @author feigq
 *
 */
@Repository
public class SyncProductDao extends HibernateDao<SyncProduct>{
    /**
     * 根据产品名称查询对象
     * @param prodname
     * @return
     */
    public List<SyncProduct> getByprodname(String prodname) {
        return find("from SyncProduct where productname = :p1", new Parameter(prodname));
    }
}
