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
import com.hundsun.boss.modules.charge.dao.income.ChargeIncomeSourceInterfaceDao;
import com.hundsun.boss.modules.charge.entity.income.ChargeIncomeSourceInterface;
import com.hundsun.boss.modules.charge.form.income.ChargeIncomeSourceInterfaceForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 收入来源接口Service
 */
@Component
public class ChargeIncomeSourceInterfaceService extends BaseService {

    @Autowired
    private ChargeIncomeSourceInterfaceDao chargeIncomeSourceInterfaceDao;

    /**
     * 获取收入来源对象
     * 
     * @param id
     * @return
     */
    public ChargeIncomeSourceInterface get(String id) {
        return chargeIncomeSourceInterfaceDao.get(id);
    }

    /**
     * 查询收入来源
     * 
     * @param page
     * @param chargeIncomeSourceInterface
     * @return
     */
    public Page<ChargeIncomeSourceInterface> find(Page<ChargeIncomeSourceInterface> page, ChargeIncomeSourceInterfaceForm form) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = chargeIncomeSourceInterfaceDao.createDetachedCriteria();
        if (!CommonUtil.isNullorEmpty(form.getContract_id())) {
            dc.add(Restrictions.like("contract_id", "%" + form.getContract_id() + "%"));
        }
        if (!CommonUtil.isNullorEmpty(form.getOffice_id())) {
            dc.add(Restrictions.eq("office_id", form.getOffice_id()));
        }
        if (!CommonUtil.isNullorEmpty(form.getIncome_source())) {
            dc.add(Restrictions.in("income_source", form.getIncome_source().split(",")));
        }
        if (!CommonUtil.isNullorEmpty(form.getSend_flag())) {
            dc.add(Restrictions.in("send_flag", form.getSend_flag().split(",")));
        }
        if (!CommonUtil.isNullorEmpty(form.getHs_customername())) {
            dc.createAlias("syncContract", "syncContract");
            dc.createAlias("syncContract.syncCustomer", "syncCustomer");
            dc.add(Restrictions.like("syncCustomer.chinesename", "%" + form.getHs_customername() + "%"));
        }
        dc.createAlias("office", "office");
        dc.add(dataScopeFilter(currentUser, "office", ""));
        dc.addOrder(Order.desc("createDate"));
        return chargeIncomeSourceInterfaceDao.find(page, dc);
    }

    /**
     * 保存收入来源
     * 
     * @param chargeIncomeSourceInterface
     */
    public void save(ChargeIncomeSourceInterface chargeIncomeSourceInterface) {
        // 找出未发送的记录
        List<ChargeIncomeSourceInterface> unsentRecords = chargeIncomeSourceInterfaceDao.findUnsentByContractId(chargeIncomeSourceInterface.getContract_id());
        // 找出已发送的记录
        List<ChargeIncomeSourceInterface> sentRecords = chargeIncomeSourceInterfaceDao.findSentByContractId(chargeIncomeSourceInterface.getContract_id());
        // 如果存在未发送的记录用当前的记录替代未发送的记录
        if (unsentRecords.size() > 0) {
            chargeIncomeSourceInterfaceDao.delete(unsentRecords.get(0));
        }
        // 如果收入来源未发送过，或者最后一次已发送的记录状态与当前的记录状态一致的话，保存
        if (sentRecords.size() == 0 || (sentRecords.size() > 0 && !sentRecords.get(0).getIncome_source().equals(chargeIncomeSourceInterface.getIncome_source()))) {
            chargeIncomeSourceInterfaceDao.save(chargeIncomeSourceInterface);
        }
    }

    /**
     * 将收入来源
     */
    public void setIncomeSourceSent() {
        chargeIncomeSourceInterfaceDao.setIncomeSourceSent();
    }

    /**
     * 确认选中的收入来源，设置为待发送状态
     * 
     * @param ids
     */
    public void confirm(String ids) {
        String[] idArray = ids.split(",");
        List<ChargeIncomeSourceInterface> ChargeIncomeSourceInterfaces = new ArrayList<ChargeIncomeSourceInterface>();
        for (int i = 0; i < idArray.length; i++) {
            ChargeIncomeSourceInterface chargeIncomeSourceInterface = get(idArray[i]);
            chargeIncomeSourceInterface.setSend_flag(ChargeConstant.SEND_FLAG_PENDING);
            ChargeIncomeSourceInterfaces.add(chargeIncomeSourceInterface);
        }
        chargeIncomeSourceInterfaceDao.save(ChargeIncomeSourceInterfaces);
    }

    /**
     * 删除收入来源
     * 
     * @param id
     */
    public void delete(String id) {
        chargeIncomeSourceInterfaceDao.deleteById(id);
    }

    /**
     * 获取同步用分页数据
     * 
     * @param offset
     * @param pagesize
     * @return
     */
    public List<ChargeIncomeSourceInterface> getUnsendPageData(int offset, int pagesize) {
        return chargeIncomeSourceInterfaceDao.getUnsendPageData(offset, pagesize);
    }
}
