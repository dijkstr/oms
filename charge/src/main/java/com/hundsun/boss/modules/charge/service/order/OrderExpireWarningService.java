package com.hundsun.boss.modules.charge.service.order;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.dao.order.OrderExpireWarningDao;
import com.hundsun.boss.modules.charge.dao.order.OrderInfoMyBatisDao;
import com.hundsun.boss.modules.charge.entity.order.OrderExpireWarning;
import com.hundsun.boss.modules.charge.form.order.OrderExpireWarningForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 合同到期提醒Service
 */
@Component
@Transactional(readOnly = false)
@SuppressWarnings("rawtypes")
public class OrderExpireWarningService extends BaseService {

    @Autowired
    private OrderExpireWarningDao orderExpireWarningDao;

    @Autowired
    private OrderInfoMyBatisDao orderInfoMyBatisDao;

    public OrderExpireWarning get(String id) {
        return orderExpireWarningDao.get(id);
    }

    /**
     * 合同到期提醒查询
     * 
     * @param page
     * @param orderExpireWarning
     * @return
     */
    public Page<OrderExpireWarning> find(Page<OrderExpireWarning> page, OrderExpireWarningForm orderExpireWarning) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = orderExpireWarningDao.createDetachedCriteria();
        if (!CommonUtil.isNullorEmpty(orderExpireWarning.getContract_id())) {
            dc.add(Restrictions.like("contract_id", "%" + orderExpireWarning.getContract_id() + "%"));
        }

        dc.createAlias("orderInfo", "orderInfo");
        if (!CommonUtil.isNullorEmpty(orderExpireWarning.getOffice_id())) {
            dc.add(Restrictions.eq("orderInfo.office_id", orderExpireWarning.getOffice_id()));
        }

        dc.createAlias("orderInfo.office", "office");
        dc.add(dataScopeFilter(currentUser, "office", ""));
        dc.add(Restrictions.eq(OrderExpireWarning.FIELD_DEL_FLAG, OrderExpireWarning.DEL_FLAG_NORMAL));
        dc.addOrder(Order.desc("updateDate"));
        return orderExpireWarningDao.find(page, dc);
    }

    @Transactional(readOnly = false)
    public void save(OrderExpireWarning orderExpireWarning) throws Exception {
        orderExpireWarningDao.save(orderExpireWarning);
    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        OrderExpireWarning orderExpireWarning = orderExpireWarningDao.get(id);
        orderExpireWarningDao.delete(orderExpireWarning);
    }

    public List<Map> queryOrderWarningList() {
        return orderInfoMyBatisDao.queryOrderWarningList();
    }

    public List<Map> queryOrderWarningListOnYunYi() {
        return orderInfoMyBatisDao.queryOrderWarningListOnYunYi();
    }
}
