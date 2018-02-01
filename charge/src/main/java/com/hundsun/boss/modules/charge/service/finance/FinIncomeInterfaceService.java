package com.hundsun.boss.modules.charge.service.finance;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.dao.finance.FinSummaryDao;
import com.hundsun.boss.modules.charge.form.finance.FinSummaryForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 财务收入接口Service
 */
@Component
@Transactional(readOnly = false)
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FinIncomeInterfaceService extends BaseService {

    @Autowired
    private FinSummaryDao finSummaryDao;
    
    public Page<FinSummaryForm> queryFinanceIncomeList(Page<FinSummaryForm> page, FinSummaryForm finSummaryForm) {
        if (CommonUtil.isNullorEmpty(finSummaryForm.getOffice_id())) {
            User currentUser = UserUtils.getUser();
            finSummaryForm.setOffice_id(getDept(currentUser, "office", ""));
        }
        List list = finSummaryDao.queryFinanceIncomeList(finSummaryForm);
        page.setList(list);
        page.setCount(finSummaryForm.getCount());
        return page;
    }

    public List<Map> exportFinanceIncomeList(FinSummaryForm req) {
        if (CommonUtil.isNullorEmpty(req.getOffice_id())) {
            User currentUser = UserUtils.getUser();
            req.setOffice_id(getDept(currentUser, "office", ""));
        }
        req.setPageSize(-1);
        return finSummaryDao.queryFinanceIncomeList(req);
    }
}
