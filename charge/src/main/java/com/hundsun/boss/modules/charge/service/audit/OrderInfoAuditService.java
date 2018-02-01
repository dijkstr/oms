package com.hundsun.boss.modules.charge.service.audit;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.WorkflowBaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.dao.audit.OrderInfoAuditDao;
import com.hundsun.boss.modules.charge.dao.order.OrderInfoDao;
import com.hundsun.boss.modules.charge.dao.order.OrderInfoMyBatisDao;
import com.hundsun.boss.modules.charge.dao.sync.SyncContractDao;
import com.hundsun.boss.modules.charge.entity.audit.OrderInfoAudit;
import com.hundsun.boss.modules.charge.entity.order.OrderInfo;
import com.hundsun.boss.modules.charge.form.audit.OrderAuditSearchForm;
import com.hundsun.boss.modules.charge.form.order.OrderInfoForm;
import com.hundsun.boss.modules.charge.service.order.OrderInfoService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 计费合同审核Service
 */
@Component
@Transactional(readOnly = false)
public class OrderInfoAuditService extends WorkflowBaseService {

    @Autowired
    private OrderInfoAuditDao orderInfoAuditDao;

    @Autowired
    private OrderInfoMyBatisDao orderInfoMyBatisDao;

    @Autowired
    private SyncContractDao syncContractDao;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private OrderInfoDao orderInfoDao;

    /**
     * 通过 id 获得 OrderInfoAudit
     * 
     * @param id
     * @return
     */
    public OrderInfoAudit get(String id) {
        return orderInfoAuditDao.get(id);
    }

    /**
     * 查询分页列表
     * 
     * @param page
     * @param orderInfoAudit
     * @return
     */
    public Page<OrderInfoAudit> find(Page<OrderInfoAudit> page, OrderInfoAudit orderInfoAudit) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = orderInfoAuditDao.createDetachedCriteria();
        if (StringUtils.isNotEmpty(orderInfoAudit.getContract_process_key())) {
            dc.add(Restrictions.like("contract_process_key", "%" + orderInfoAudit.getContract_process_key() + "%"));
        }
        dc.createAlias("office", "office");
        dc.add(dataScopeFilter(currentUser, "office", ""));
        dc.add(Restrictions.eq(OrderInfoAudit.FIELD_DEL_FLAG, OrderInfoAudit.DEL_FLAG_NORMAL));
        dc.addOrder(Order.asc("id"));
        return orderInfoAuditDao.find(page, dc);
    }

    /**
     * 保存
     * 
     * @param orderInfoAudit
     */
    public void save(OrderInfoAudit orderInfoAudit) {
        orderInfoAuditDao.save(orderInfoAudit);
        orderInfoAuditDao.flush();
    }

    /**
     * 删除
     * 
     * @param id
     */
    public void delete(String id) {
        OrderInfoAudit orderInfoAudit = orderInfoAuditDao.get(id);
        orderInfoAuditDao.delete(orderInfoAudit);
    }

    /**
     * 合同审核通过
     * 
     * @param orderInfoAudit
     */
    public void pass(String contractId) {

        //        OrderInfoAudit orderInfoAudit = orderInfoAuditDao.getByContractId(contractId);
        //        completeTask(orderInfoAudit.getProcess_instance_id(), null);
    }

    /**
     * 合同审核失败
     * 
     * @param orderInfoAudit
     */
    public void reject(OrderInfo orderInfo, OrderInfoForm form) {
        //        orderInfoAudit.getContract_id();
        //
        //        Map params = new HashMap();
        //        params.put("orderInfoId", orderInfo.getId());
        //        params.put("orderInfoAuditId", orderInfoAudit.getId());
        //        params.put("remarks", orderInfoAudit.getRemarks());
        //
        //        completeTask(orderInfoAudit, params);
    }

    /**
     * 通过合同编号获取 OrderInfoAudit
     * 
     * @param contractId
     * @return
     */
    public OrderInfoAudit getByContractProcessKey(String contractId) {
        return orderInfoAuditDao.getByContractProcessKey(contractId);
    }

    /**
     * 查询审核合同列表
     * 
     * @param form
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Page<Map> queryOrderInfoList(Page<Map> page, OrderAuditSearchForm form) {
        if (CommonUtil.isNullorEmpty(form.getDept())) {
            User currentUser = UserUtils.getUser();
            form.setDept(getDept(currentUser, "office", ""));
        }

        List<Map> list = orderInfoMyBatisDao.queryAuditOrderInfoList(form);
        page.setList(list);
        page.setCount(form.getCount());
        return page;
    }
}
