package com.hundsun.boss.modules.charge.service.order;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.dao.order.OrderFrameInfoDao;
import com.hundsun.boss.modules.charge.entity.order.OrderFrameInfo;
import com.hundsun.boss.modules.charge.form.order.OrderFrameInfoForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 框架合同Service
 */
@Component
@Transactional(readOnly = false)
public class OrderFrameInfoService extends BaseService {

    @Autowired
    private OrderFrameInfoDao orderFrameInfoDao;

    public OrderFrameInfo get(String id) {
        return orderFrameInfoDao.get(id);
    }

    public Page<OrderFrameInfo> find(Page<OrderFrameInfo> page, OrderFrameInfoForm orderFrameInfoForm) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = orderFrameInfoDao.createDetachedCriteria();
        if (!CommonUtil.isNullorEmpty(orderFrameInfoForm.getContract_id())) {
            dc.add(Restrictions.like("contract_id", "%" + orderFrameInfoForm.getContract_id() + "%"));
        }
        if (!CommonUtil.isNullorEmpty(orderFrameInfoForm.getCustomer_id())) {
            dc.add(Restrictions.like("customer_id", "%" + orderFrameInfoForm.getCustomer_id() + "%"));
        }
        if (!CommonUtil.isNullorEmpty(orderFrameInfoForm.getOffice_id())) {
            dc.add(Restrictions.eq("office_id", orderFrameInfoForm.getOffice_id()));
        }
        
        if (!CommonUtil.isNullorEmpty(orderFrameInfoForm.getHs_customername()) || !CommonUtil.isNullorEmpty(orderFrameInfoForm.getCustomermanagername())) {
            dc.createAlias("syncCustomer", "syncCustomer");
            if (!CommonUtil.isNullorEmpty(orderFrameInfoForm.getHs_customername())) {
                dc.add(Restrictions.like("syncCustomer.chinesename", "%" + orderFrameInfoForm.getHs_customername() + "%"));
            }
            if (!CommonUtil.isNullorEmpty(orderFrameInfoForm.getCustomermanagername())) {
                dc.add(Restrictions.like("syncCustomer.customermanagername", "%" + orderFrameInfoForm.getCustomermanagername() + "%"));
            }
        }

        dc.createAlias("office", "office");
        dc.add(dataScopeFilter(currentUser, "office", ""));
        dc.add(Restrictions.eq(OrderFrameInfo.FIELD_DEL_FLAG, OrderFrameInfo.DEL_FLAG_NORMAL));
        dc.addOrder(Order.desc("updateDate"));
        return orderFrameInfoDao.find(page, dc);
    }

    @Transactional(readOnly = false)
    public void save(OrderFrameInfo orderFrameInfo){
        orderFrameInfoDao.save(orderFrameInfo);

    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        OrderFrameInfo orderFrameInfo = orderFrameInfoDao.get(id);
        orderFrameInfoDao.delete(orderFrameInfo);
    }

}
