package com.hundsun.boss.modules.charge.dao.income;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.charge.entity.income.ChargeIncomeSourceInterface;

/**
 * 计费来源系统DAO接口
 */
@SuppressWarnings("unchecked")
@Repository
public class ChargeIncomeSourceInterfaceDao extends HibernateDao<ChargeIncomeSourceInterface> {

    /**
     * 根据协同合同号获取未发送收入来源
     * 
     * @param contractId
     * @return
     */
    public List<ChargeIncomeSourceInterface> findUnsentByContractId(String contractId) {
        return find("from ChargeIncomeSourceInterface where contract_id = :p1 and send_flag in ('pending', 'create')", new Parameter(contractId));
    }

    /**
     * 根据协同合同号获取未发送收入来源
     * 
     * @param contractId
     * @return
     */
    public List<ChargeIncomeSourceInterface> findSentByContractId(String contractId) {
        return find("from ChargeIncomeSourceInterface where contract_id = :p1 and send_flag = 'sent' order by create_date desc", new Parameter(contractId));
    }

    /**
     * 将收入来源
     */
    public void setIncomeSourceSent() {
        update("update ChargeIncomeSourceInterface set send_flag = 'sent' where send_flag = 'pending'");
    }

    /**
     * 删除未同步的数据
     * 
     * @param contractId
     */
    public void deleteUnsent(String contractId) {
        update("delete ChargeIncomeSourceInterface where send_flag in ('pending', 'create') and contract_id = :p1", new Parameter(contractId));
    }

    /**
     * 获取同步用分页数据
     * 
     * @param offset
     * @param pagesize
     * @return
     */
    public List<ChargeIncomeSourceInterface> getUnsendPageData(int offset, int pagesize) {
        String hql = "from ChargeIncomeSourceInterface where send_flag = 'pending'";
        return getSession().createQuery(hql).setFirstResult(offset).setMaxResults(pagesize).list();
    }
}
