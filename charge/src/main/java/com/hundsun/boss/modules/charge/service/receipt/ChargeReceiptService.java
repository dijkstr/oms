package com.hundsun.boss.modules.charge.service.receipt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.dao.receipt.ChargeReceiptDao;
import com.hundsun.boss.modules.charge.entity.receipt.ChargeReceipt;
import com.hundsun.boss.modules.charge.form.receipt.ChargeReceiptForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 合同到款业务Service
 *
 */
@Component
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ChargeReceiptService extends BaseService {
    @Autowired
    private ChargeReceiptDao chargeReceiptDao;

    /**
     * 获取到款信息
     * 
     * @param receiveid
     * @return
     */
    public ChargeReceipt get(String receiveid) {
        return chargeReceiptDao.get(receiveid);
    }

    /**
     * 分页查询到款
     * 
     * @param page
     * @param chargeReceiptForm
     * @return
     */
    public Page<ChargeReceiptForm> find(Page<ChargeReceiptForm> page, ChargeReceiptForm chargeReceiptForm) {
        if (CommonUtil.isNullorEmpty(chargeReceiptForm.getDepartment())) {
            //部门查询条件为空
            User currentUser = UserUtils.getUser();
            //根据当前用户设置部门权限
            chargeReceiptForm.setDepartment(getDept(currentUser, "office", ""));
        }
        //根据当前操作人的权限查询数据
        List list = chargeReceiptDao.queryChargeReceiptList(chargeReceiptForm);
        page.setList(list);
        page.setCount(chargeReceiptForm.getCount());
        return page;
    }

    /**
     * 保存到款
     * 
     * @param chargeReceipt
     */
    public void save(ChargeReceipt chargeReceipt) {
        chargeReceiptDao.save(chargeReceipt);
    }

    /**
     * 更新到款
     * 
     * @param chargeReceipt
     */
    public void update(ChargeReceipt chargeReceipt) {
        chargeReceiptDao.update(chargeReceipt);
    }

    /**
     * 根据协同合同号获取累计到款
     * 
     * @param contract_id
     * @return
     */
    public String getReceiptByContractId(String contract_id) {
        String totalReceipt = chargeReceiptDao.getReceiptByContractId(contract_id);
        return totalReceipt;
    }

}
