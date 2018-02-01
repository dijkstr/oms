package com.hundsun.boss.modules.charge.service.ordervalidate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.dao.ordervalidate.OrderValidateDao;
import com.hundsun.boss.modules.charge.entity.ordervalidate.OrderValidate;
import com.hundsun.boss.modules.charge.form.ordervalidate.OrderValidateForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 合同校验Service
 */
@Component
@Transactional(readOnly = true)
public class OrderValidateService extends BaseService{
    @Autowired
    private  OrderValidateDao orderValidateDao;
 
    /**
     * 获取合同校验信息
     * @param serial_no
     * @return
     */
    public OrderValidate get(String serial_no) {
        return orderValidateDao.get(serial_no);
    }
/**
 * 分页查询合同校验信息
 * @param page
 * @param orderValidate
 * @return
 */
    public Page<OrderValidate> find(Page<OrderValidate> page, OrderValidateForm orderValidate) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = orderValidateDao.createDetachedCriteria();
        //协同合同编号
        if(!CommonUtil.isNullorEmpty(orderValidate.getHs_contract_id())){
            dc.add(Restrictions.like("hs_contract_id", "%"+orderValidate.getHs_contract_id()+"%"));
        }
        //所属公司
        if(!CommonUtil.isNullorEmpty(orderValidate.getOrder_source())){
            dc.add(Restrictions.eq("order_source", orderValidate.getOrder_source()));
        }
        //校验类型
        if(!CommonUtil.isNullorEmpty(orderValidate.getHs_check_status())){
            dc.add(Restrictions.eq("hs_check_status", orderValidate.getHs_check_status()));
        }
        //校验级别
        if(!CommonUtil.isNullorEmpty(orderValidate.getShow_status())){
            dc.add(Restrictions.eq("show_status", orderValidate.getShow_status()));
        }
        //产品编号
        if(!CommonUtil.isNullorEmpty(orderValidate.getHs_product_id())){
            dc.add(Restrictions.like("hs_product_id", "%"+orderValidate.getHs_product_id()+"%"));
        }
        
        //如果查询对象为空，默认显示
        if(CommonUtil.isNullorEmpty(orderValidate.getShow_status())){
            dc.add(Restrictions.eq("show_status", "0"));
        }
        dc.createAlias("office", "office");

        dc.add(dataScopeFilter(currentUser, "office", ""));
        //按部门，合同排序
        dc.addOrder(Order.desc("order_source"));
        dc.addOrder(Order.desc("hs_contract_id"));
        return orderValidateDao.find(page, dc);
    }
/**
 * 保存合同校验信息
 * @param orderValidate
 */
    @Transactional(readOnly = false)
    public void save(OrderValidate orderValidate) {
        orderValidateDao.save(orderValidate);
    }
/**
 * 删除合同校验信息
 * @param serial_no
 */
    @Transactional(readOnly = false)
    public void delete(String serial_no) {
        orderValidateDao.deleteById(serial_no);
    }

    
    /**
     * 查询合同校验list
     * @param orderValidate
     * @return
     */
    public List<OrderValidate> queryOrderValidations (OrderValidateForm orderValidate) {
      DetachedCriteria dc = orderValidateDao.createDetachedCriteria();
      //订单id
      if(!CommonUtil.isNullorEmpty(orderValidate.getOrder_id())){
          dc.add(Restrictions.eq("order_id", orderValidate.getOrder_id()));
      }
      //组合id
      if(!CommonUtil.isNullorEmpty(orderValidate.getCombine_id())){
          dc.add(Restrictions.eq("combine_id", orderValidate.getCombine_id()));
      }
      //产品id
      if(!CommonUtil.isNullorEmpty(orderValidate.getHs_product_id())){
          dc.add(Restrictions.eq("hs_product_id", orderValidate.getHs_product_id()));
      }
      //校验级别（合同，组合，产品）
      if(!CommonUtil.isNullorEmpty(orderValidate.getBelong_type())){
          dc.add(Restrictions.eq("belong_type", orderValidate.getBelong_type()));
      }
      //是否显示
      if(!CommonUtil.isNullorEmpty(orderValidate.getShow_status())){
          dc.add(Restrictions.eq("show_status", orderValidate.getShow_status()));
      }
      //校验类型(合同内校验，合同协同校验，收入协同校验)
      if(!CommonUtil.isNullorEmpty(orderValidate.getHs_check_status_list())){
          dc.add(Restrictions.in("hs_check_status", orderValidate.getHs_check_status_list()));
      }
      return orderValidateDao.find(dc);
  }
}
