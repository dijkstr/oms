package com.hundsun.boss.modules.charge.service.bill;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.exception.ServiceException;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.modules.charge.dao.bill.WrongBillDao;
import com.hundsun.boss.modules.charge.form.bill.WrongBillForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 错单管理Service
 */
@Component
@Transactional(readOnly = false)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WrongBillService extends BaseService {

    @Autowired
    private WrongBillDao wrongBillDao;

    public Page<WrongBillForm> queryWrongBillList(Page<WrongBillForm> page, WrongBillForm wrongBillForm) {
        User currentUser = UserUtils.getUser();
        wrongBillForm.setDept(getDept(currentUser, "office", ""));
        List list = wrongBillDao.queryWrongBill(wrongBillForm);
        page.setList(list);
        page.setCount(wrongBillForm.getCount());
        return page;
    }

    public Integer updateStatus(WrongBillForm req) {
        Integer countInteger = 0;
        try {
            countInteger = wrongBillDao.updateStatus(req);
        } catch (Exception e) {
            logger.error("数据库操作异常", e);
            throw new ServiceException("数据库操作异常");
        }
        return countInteger;
    }

}
