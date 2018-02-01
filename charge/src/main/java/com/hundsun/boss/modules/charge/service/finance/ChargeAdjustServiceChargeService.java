package com.hundsun.boss.modules.charge.service.finance;

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
import com.hundsun.boss.modules.charge.dao.finance.ChargeAdjustServiceChargeDao;
import com.hundsun.boss.modules.charge.entity.finance.ChargeAdjustServiceCharge;
import com.hundsun.boss.modules.charge.entity.order.OrderInfo;
import com.hundsun.boss.modules.charge.form.finance.ChargeAdjustServiceChargeForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 技术服务费调账Service
 */
@Component
@Transactional(readOnly = true)
public class ChargeAdjustServiceChargeService extends BaseService {

    @Autowired
    private ChargeAdjustServiceChargeDao chargeAdjustServiceChargeDao;

    public ChargeAdjustServiceCharge get(String id) {
        return chargeAdjustServiceChargeDao.get(id);
    }

    public Page<ChargeAdjustServiceCharge> find(Page<ChargeAdjustServiceCharge> page, ChargeAdjustServiceChargeForm chargeAdjustServiceCharge) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = chargeAdjustServiceChargeDao.createDetachedCriteria();
        if (!CommonUtil.isNullorEmpty(chargeAdjustServiceCharge.getId())) {
            dc.add(Restrictions.eq("id", chargeAdjustServiceCharge.getId()));
        }
        if (!CommonUtil.isNullorEmpty(chargeAdjustServiceCharge.getContract_id())) {
            dc.add(Restrictions.like("contract_id", "%" + chargeAdjustServiceCharge.getContract_id() + "%"));
        }
        if (!CommonUtil.isNullorEmpty(chargeAdjustServiceCharge.getOffice_id())) {
            dc.add(Restrictions.eq("office_id", chargeAdjustServiceCharge.getOffice_id()));
        }
        dc.createAlias("office", "office");
        dc.add(dataScopeFilter(currentUser, "office", ""));
        dc.add(Restrictions.eq(ChargeAdjustServiceCharge.FIELD_DEL_FLAG, OrderInfo.DEL_FLAG_NORMAL));
        dc.addOrder(Order.desc("updateDate"));
        page = chargeAdjustServiceChargeDao.find(page, dc);
        List<ChargeAdjustServiceCharge> list = page.getList();
        setUseStatus(list);
        return page;
    }

    public List<ChargeAdjustServiceCharge> find(ChargeAdjustServiceChargeForm chargeAdjustServiceCharge) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = chargeAdjustServiceChargeDao.createDetachedCriteria();
        if (!CommonUtil.isNullorEmpty(chargeAdjustServiceCharge.getId())) {
            dc.add(Restrictions.eq("id", chargeAdjustServiceCharge.getId()));
        }
        if (!CommonUtil.isNullorEmpty(chargeAdjustServiceCharge.getContract_id())) {
            dc.add(Restrictions.like("contract_id", "%" + chargeAdjustServiceCharge.getContract_id() + "%"));
        }
        if (!CommonUtil.isNullorEmpty(chargeAdjustServiceCharge.getOffice_id())) {
            dc.add(Restrictions.eq("office_id", chargeAdjustServiceCharge.getOffice_id()));
        }
        dc.createAlias("office", "office");
        dc.add(dataScopeFilter(currentUser, "office", ""));
        dc.add(Restrictions.eq(ChargeAdjustServiceCharge.FIELD_DEL_FLAG, OrderInfo.DEL_FLAG_NORMAL));
        dc.addOrder(Order.desc("updateDate"));
        List<ChargeAdjustServiceCharge> list = chargeAdjustServiceChargeDao.find(dc);
        setUseStatus(list);
        return list;
    }

    /**
     * 判断组合是否有使用
     * 
     * @param list
     */
    private void setUseStatus(List<ChargeAdjustServiceCharge> list) {
        for (int i = 0; i < list.size(); i++) {
            if (!CommonUtil.isNullorEmpty(list.get(i).getOrderCombine()) && "0".equals(list.get(i).getOrderCombine().getDelFlag())) {
                list.get(i).setStatus("是");
            } else {
                list.get(i).setStatus("否");
            }
        }
    }

    @Transactional(readOnly = false)
    public void save(ChargeAdjustServiceCharge chargeAdjustServiceCharge) {
        chargeAdjustServiceChargeDao.save(chargeAdjustServiceCharge);
    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        chargeAdjustServiceChargeDao.deleteById(id);
    }

}
