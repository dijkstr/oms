package com.hundsun.boss.modules.charge.service.bill;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.dao.bill.ChargeReceiptBillDao;
import com.hundsun.boss.modules.charge.form.bill.ChargeReceiptForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 详单管理Service
 */
@Component
@Transactional(readOnly = false)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChargeReceiptBillService extends BaseService {

    @Autowired
    private ChargeReceiptBillDao chargeReceiptBillDao;

    /**
     * 月级别查看
     * 
     * @param page
     * @param form
     * @return
     */
    public Page<ChargeReceiptForm> queryChargeReceiptList(Page<ChargeReceiptForm> page, ChargeReceiptForm form) {
        User currentUser = UserUtils.getUser();
        form.setDept(getDept(currentUser, "office", ""));
        List list = chargeReceiptBillDao.queryChargeReceipt(form);
        page.setList(list);
        page.setCount(form.getCount());
        return page;
    }

    /**
     * 按日/月级别导出
     * 
     * @param req
     * @return
     */
    public List<Map> exportChargeReceiptBill(ChargeReceiptForm req) {
        if (CommonUtil.isNullorEmpty(req.getOffice_id())) {
            User currentUser = UserUtils.getUser();
            req.setDept(getDept(currentUser, "office", ""));
        }
        req.setPageSize(-1);
        // 1:日      2:月
        if (req.getFlag().equals("1")) {
            return chargeReceiptBillDao.getChrargeReceiptList(req);
        } else {
            return chargeReceiptBillDao.queryChargeReceipt(req);
        }
    }

    /**
     * 日结级别查看
     * 
     * @param req
     * @return
     */
    public List<Map> getFinSummaryDetailList(ChargeReceiptForm req) {
        List<Map> list = new ArrayList<Map>();
        list = chargeReceiptBillDao.getChrargeReceiptList(req);
        return list;
    }

}
