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
import com.hundsun.boss.modules.charge.dao.sync.SyncContractDao;
import com.hundsun.boss.modules.charge.entity.sync.SyncContract;
import com.hundsun.boss.modules.charge.form.common.SyncContractForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 合同主表Service
 */
@Component
@Transactional(readOnly = true)
public class SyncContractService extends BaseService {
    @Autowired
    private SyncContractDao syncContractDao;

    /**
     * 根据主键获取合同对象
     * 
     * @param con_id
     * @return
     */
    public SyncContract get(String con_id) {
        return syncContractDao.get(con_id);
    }

    /**
     * 分页查询合同信息
     * 
     * @param page
     * @param form
     * @return
     */
    public Page<SyncContract> find(Page<SyncContract> page, SyncContractForm form) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = syncContractDao.createDetachedCriteria();
        //根据合同编号查询
        if (!CommonUtil.isNullorEmpty(form.getContractid())) {
            dc.add(Restrictions.like("contractid", "%" + form.getContractid() + "%"));

        }
        //根据所属公司查询
        if (!CommonUtil.isNullorEmpty(form.getCompanyname())) {
            dc.add(Restrictions.like("companyname", "%" + form.getCompanyname() + "%"));

        }
        //根据客户编号查询
        if (!CommonUtil.isNullorEmpty(form.getCustomerid())) {
            dc.add(Restrictions.like("customerid", "%" + form.getCustomerid() + "%"));

        }
        //根据客户名称查询
        if (!CommonUtil.isNullorEmpty(form.getCustomername())) {
            dc.add(Restrictions.like("customername", "%" + form.getCustomername() + "%"));

        }

        //所属公司查询
        if (!CommonUtil.isNullorEmpty(form.getCompanyid())) {
            dc.add(Restrictions.eq("companyid", form.getCompanyid()));

        }

        //计费系统使用
        if (!CommonUtil.isNullorEmpty(form.getUsetype())) {

            if ("1".equals(form.getUsetype())) {
                dc.add(Restrictions.sqlRestriction(" contractid in(select contract_id from order_info)"));
            } else {
                dc.add(Restrictions.sqlRestriction(" contractid not in(select contract_id from order_info)"));
            }

        }

        //计费起点上报类型(新加)
        if (!CommonUtil.isNullorEmpty(form.getReporttype_id())) {

            dc.add(Restrictions.eq("reporttype_id", form.getReporttype_id()));

        }

        dc.createAlias("office", "office");

        dc.add(dataScopeFilter(currentUser, "office", ""));
        //根据合同编号排序
        dc.addOrder(Order.desc("contractid"));
        return syncContractDao.find(page, dc);
    }

    /**
     * 保存合同
     * 
     * @param syncContract
     */
    @Transactional(readOnly = false)
    public void save(SyncContract syncContract) {
        syncContractDao.save(syncContract);
    }

    /**
     * 删除合同
     * 
     * @param con_id
     */
    @Transactional(readOnly = false)
    public void delete(String con_id) {
        syncContractDao.deleteById(con_id);
    }

    /**
     * 根据合同编号查询合同
     * 
     * @param contractid
     * @return
     */
    public SyncContract getByContractid(String contractid) {
        return syncContractDao.getByContractid(contractid);
    }

    /**
     * 合同检验
     * 
     * @param contractid
     * @throws Exception
     */
    @Transactional
    public void validateContract(String contractid) throws Exception {
        syncContractDao.validateContract(contractid);
    }
}
