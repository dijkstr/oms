package com.hundsun.boss.modules.charge.service.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.modules.charge.dao.report.ChargeOverMinMyBatisDao;
import com.hundsun.boss.modules.charge.entity.report.ChargeOverMin;
import com.hundsun.boss.modules.charge.form.common.SearchForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 第一次超保底Service
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Component
@Transactional(readOnly = true)
public class ChargeOverMinService extends BaseService {

    @Autowired
    private ChargeOverMinMyBatisDao chargeOverMinMyBatisDao;

    /**
     * 第一次收入
     * 
     * @param page
     * @param form
     * @return
     */

    public Page<ChargeOverMin> list(Page<ChargeOverMin> page, SearchForm form) {
        User currentUser = UserUtils.getUser();
        form.setDept(getDept(currentUser, "office", ""));
        List list = chargeOverMinMyBatisDao.list(form);
        page.setList(list);
        page.setCount(form.getCount());
        return page;
    }
}
