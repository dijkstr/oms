package com.hundsun.boss.modules.charge.service.income;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.common.ChargeConstant;
import com.hundsun.boss.modules.charge.dao.income.ChargeIncomePeriodInterfaceDao;
import com.hundsun.boss.modules.charge.entity.income.ChargeIncomePeriodInterface;
import com.hundsun.boss.modules.charge.form.income.ChargeIncomePeriodInterfaceForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 收入期间接口Service
 */
@Component
public class ChargeIncomePeriodInterfaceService extends BaseService {

    @Autowired
    private ChargeIncomePeriodInterfaceDao chargeIncomePeriodInterfaceDao;

    /**
     * 获取收入期间对象
     * 
     * @param id
     * @return
     */
    public ChargeIncomePeriodInterface get(String id) {
        return chargeIncomePeriodInterfaceDao.get(id);
    }

    /**
     * 查询收入期间
     * 
     * @param page
     * @param chargeIncomePeriodInterface
     * @return
     */
    public Page<ChargeIncomePeriodInterface> find(Page<ChargeIncomePeriodInterface> page, ChargeIncomePeriodInterfaceForm form) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = chargeIncomePeriodInterfaceDao.createDetachedCriteria();
        if (!CommonUtil.isNullorEmpty(form.getContract_id())) {
            dc.add(Restrictions.like("contract_id", "%" + form.getContract_id() + "%"));
        }
        if (!CommonUtil.isNullorEmpty(form.getOffice_id())) {
            dc.add(Restrictions.eq("office_id", form.getOffice_id()));
        }
        if (!CommonUtil.isNullorEmpty(form.getSend_flag())) {
            dc.add(Restrictions.in("send_flag", form.getSend_flag().split(",")));
        }
        if ("0".equals(form.getShowEmptyIncomeDate())) {
            dc.add(Restrictions.isNotNull("income_begin_date"));
            dc.add(Restrictions.isNotNull("income_end_date"));
        } else if ("1".equals(form.getShowEmptyIncomeDate())) {
            dc.add(Restrictions.or(Restrictions.isNull("income_begin_date"), Restrictions.isNull("income_end_date")));
        }
        if(!CommonUtil.isNullorEmpty(form.getHs_customername())) {
            dc.createAlias("syncContract", "syncContract");
            dc.createAlias("syncContract.syncCustomer", "syncCustomer");
            dc.add(Restrictions.like("syncCustomer.chinesename", "%" + form.getHs_customername() + "%"));
        }
        dc.createAlias("office", "office");
        dc.add(dataScopeFilter(currentUser, "office", ""));
        dc.addOrder(Order.desc("createDate")).addOrder(Order.asc("contract_id")).addOrder(Order.asc("income_begin_date")).addOrder(Order.asc("product_id")).addOrder(Order.asc("payment_type"));
        return chargeIncomePeriodInterfaceDao.find(page, dc);
    }

    /**
     * 将收入期间
     */
    public void setIncomePeriodSent() {
        chargeIncomePeriodInterfaceDao.setIncomePeriodSent();
    }

    /**
     * 确认选中的收入期间，设置为待发送状态
     * 
     * @param ids
     */
    public void confirm(String ids) {
        String[] idArray = ids.split(",");
        List<ChargeIncomePeriodInterface> ChargeIncomePeriodInterfaces = new ArrayList<ChargeIncomePeriodInterface>();
        for (int i = 0; i < idArray.length; i++) {
            ChargeIncomePeriodInterface chargeIncomePeriodInterface = get(idArray[i]);
            chargeIncomePeriodInterface.setSend_flag(ChargeConstant.SEND_FLAG_PENDING);
            ChargeIncomePeriodInterfaces.add(chargeIncomePeriodInterface);
        }
        chargeIncomePeriodInterfaceDao.save(ChargeIncomePeriodInterfaces);
    }

    /**
     * 删除收入期间
     * 
     * @param id
     */
    public void delete(String id) {
        chargeIncomePeriodInterfaceDao.deleteById(id);
    }

    /**
     * 获取同步用分页数据
     * 
     * @param offset
     * @param pagesize
     * @return
     */
    public List<ChargeIncomePeriodInterface> getUnsendPageData(int offset, int pagesize) {
        return chargeIncomePeriodInterfaceDao.getUnsendPageData(offset, pagesize);
    }

}
