package com.hundsun.boss.modules.charge.dao.sync;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.charge.entity.sync.SyncContdetail;

/**
 * 合同子表DAO接口
 */
@Repository
public class SyncContdetailDao extends HibernateDao<SyncContdetail> {
    /**
     * 获取合同整个表体数据
     * 
     * @param conid
     * @return
     */
    public List<SyncContdetail> getAlldetails(String conid) {
        return find("from SyncContdetail where con_id = :p1", new Parameter(conid));
    }
    
    /**
     * 根据协同明细行id获取协同明细行
     * 
     * @param conid
     * @return
     */
    public SyncContdetail getSyncContdetailByDetailid(String detailid) {
        return getByHql("from SyncContdetail where detailid = :p1", new Parameter(detailid));
    }
}
