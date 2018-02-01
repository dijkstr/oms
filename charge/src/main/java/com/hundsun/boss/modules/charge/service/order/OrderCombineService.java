package com.hundsun.boss.modules.charge.service.order;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.dao.order.OrderCombineDao;
import com.hundsun.boss.modules.charge.entity.order.OrderCombine;
import com.hundsun.boss.modules.charge.entity.order.OrderInfo;
import com.hundsun.boss.modules.charge.entity.order.OrderProduct;
import com.hundsun.boss.modules.charge.form.order.OrderInfoForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 计费合同Service
 */
@Component
@Transactional(readOnly = false)
public class OrderCombineService extends BaseService {

    @Autowired
    private OrderCombineDao orderCombineDao;

    public OrderCombine get(String id) {
        return orderCombineDao.get(id);
    }

    /**
     * 合同查询
     * 
     * @param page
     * @param orderInfo
     * @return
     */
    public List<OrderCombine> find(OrderInfoForm orderInfo) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = orderCombineDao.createDetachedCriteria();
        if (!CommonUtil.isNullorEmpty(orderInfo.getContract_id())) {
            dc.add(Restrictions.like("contract_id", "%" + orderInfo.getContract_id() + "%"));
        }
        if (!CommonUtil.isNullorEmpty(orderInfo.getCustomer_id())) {
            dc.add(Restrictions.like("customer_id", "%" + orderInfo.getCustomer_id() + "%"));
        }
        if (!CommonUtil.isNullorEmpty(orderInfo.getOffice_id())) {
            dc.add(Restrictions.eq("office_id", orderInfo.getOffice_id()));
        }
        if (!CommonUtil.isNullorEmpty(orderInfo.getOrder_status())) {
            dc.add(Restrictions.eq("order_status", orderInfo.getOrder_status()));
        }

        dc.createAlias("office", "office");
        dc.add(dataScopeFilter(currentUser, "office", ""));
        dc.add(Restrictions.eq(OrderInfo.FIELD_DEL_FLAG, OrderInfo.DEL_FLAG_NORMAL));
        dc.addOrder(Order.desc("updateDate"));
        List<OrderCombine> orderCombines = orderCombineDao.find(dc);
        for (OrderCombine orderCombine : orderCombines) {
            StringBuffer productNames = new StringBuffer();
            List<OrderProduct> orderProducts = new ArrayList<OrderProduct>();
            for (OrderProduct orderProduct : orderProducts) {
                productNames.append(orderProduct.getProduct_name()).append(";");
            }
            orderCombine.setProduct_names(productNames.toString());
        }
        return orderCombines;
    }

    @Transactional(readOnly = false)
    public void save(OrderCombine orderCombine) throws Exception {
        orderCombineDao.save(orderCombine);
        orderCombineDao.flush();

    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        OrderCombine orderCombine = orderCombineDao.get(id);
        orderCombineDao.delete(orderCombine);
    }

}
