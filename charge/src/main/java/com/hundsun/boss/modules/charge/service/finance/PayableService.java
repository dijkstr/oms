package com.hundsun.boss.modules.charge.service.finance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.CommonUtils;
import com.hundsun.boss.modules.charge.dao.finance.PayableDao;
import com.hundsun.boss.modules.charge.form.finance.FinSummaryForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 应收款管理Service
 */
@Component
@Transactional(readOnly = false)
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PayableService extends BaseService {

    @Autowired
    private PayableDao payableDao;

    public Page<FinSummaryForm> payableList(Page<FinSummaryForm> page, FinSummaryForm finSummaryForm) {
        User currentUser = UserUtils.getUser();
        finSummaryForm.setDept(getDept(currentUser, "office", ""));
        if(CommonUtils.isNullorEmpty(finSummaryForm.getCharge_month())) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
            finSummaryForm.setCharge_month(df.format(new Date()));
        }
        List list = payableDao.payableList(finSummaryForm);
        page.setList(list);
        page.setCount(finSummaryForm.getCount());
        return page;
    }
    
    public List<Map> exportPayableList(FinSummaryForm req) {
        if (CommonUtil.isNullorEmpty(req.getDept())) {
            User currentUser = UserUtils.getUser();
            req.setDept(getDept(currentUser, "office", ""));
        }
        req.setPageSize(-1);
        return payableDao.payableList(req);
    }

}
