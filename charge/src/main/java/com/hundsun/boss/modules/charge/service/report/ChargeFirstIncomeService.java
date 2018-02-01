package com.hundsun.boss.modules.charge.service.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.modules.charge.dao.report.ChargeFirstIncomeMyBatisDao;
import com.hundsun.boss.modules.charge.entity.report.ChargeFirstIncome;
import com.hundsun.boss.modules.charge.form.common.SearchForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 第一次收入Service
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Component
@Transactional(readOnly = true)
public class ChargeFirstIncomeService extends BaseService {

    @Autowired
    private ChargeFirstIncomeMyBatisDao chargeFirstIncomeMyBatisDao;

    /**
     * 第一次收入
     * 
     * @param page
     * @param form
     * @return
     */
    public Page<ChargeFirstIncome> list(Page<ChargeFirstIncome> page, SearchForm form) {
        User currentUser = UserUtils.getUser();
        form.setDept(getDept(currentUser, "office", ""));
        List list = chargeFirstIncomeMyBatisDao.list(form);
        page.setList(list);
        page.setCount(form.getCount());
        return page;
    }
}
