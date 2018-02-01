package com.hundsun.boss.modules.charge.dao.sync;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.charge.entity.sync.SyncContract;
/**
 *合同主表DAO接口
 */
@Repository
public class SyncContractDao extends HibernateDao<SyncContract>{

    public SyncContract getByContractid(String contractid) {
        return getByHql("from SyncContract where contractid = :p1", new Parameter(contractid));
    }
    /**
     * 调用存储过程校验合同信息
     * @param contractid
     */
    public void validateContract(String contractid)throws Exception{
      String sqlString="{CALL sp_charge_validate_order(?)}";
      SQLQuery sqlQuery=getSession().createSQLQuery(sqlString);
      sqlQuery.setParameter(0, contractid);
      sqlQuery.executeUpdate();
      
    }
    
}
