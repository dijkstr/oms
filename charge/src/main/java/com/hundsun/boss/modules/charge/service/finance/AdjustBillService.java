package com.hundsun.boss.modules.charge.service.finance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.exception.ServiceException;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.modules.charge.dao.finance.AdjustBillDao;
import com.hundsun.boss.modules.charge.form.finance.AdjustBillForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;
import com.hundsun.boss.util.CommonUtils;

/**
 * 调账管理Service
 */
@Component
@Transactional(readOnly = false)
public class AdjustBillService extends BaseService {

    @Autowired
    private AdjustBillDao adjustBillDao;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Page<AdjustBillForm> adjustList(Page<AdjustBillForm> page, AdjustBillForm adjustBillForm) {
        User currentUser = UserUtils.getUser();
        adjustBillForm.setDept(getDept(currentUser, "office", ""));
        List list = adjustBillDao.queryAdjustBill(adjustBillForm);
        page.setList(list);
        page.setCount(adjustBillForm.getCount());
        return page;
    }

    public Integer insertOrUpdateAdjustBill(AdjustBillForm req) {
        int count = 0;
        if (CommonUtils.isNullorEmpty(req.getId())) {
            count = adjustBillDao.insertAdjustBill(req);
        } else {
            adjustBillDao.getAdjustBill(req);
            //1:账单调整状态是已生效
            if ("1".equals(adjustBillDao.getAdjustBill(req).getBill_adjust_status())) {
                throw new ServiceException("账单调整完毕的记录不能修改.");
            } else {
                count = adjustBillDao.updateAdjustBill(req);
            }
        }
        return count;
    }

    public Integer deleteAdjustBill(AdjustBillForm req) {
        //1:账单调整状态是已生效
        if ("1".equals(adjustBillDao.getAdjustBill(req).getBill_adjust_status())) {
            throw new ServiceException("账单调整完毕的记录不能删除.");
        }
        int count = adjustBillDao.deleteAdjustBill(req);
        return count;
    }
    
    public AdjustBillForm getAdjustBill(AdjustBillForm req) {
        return adjustBillDao.getAdjustBill(req);
    }

}
