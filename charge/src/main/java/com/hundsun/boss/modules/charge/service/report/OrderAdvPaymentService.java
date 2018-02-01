package com.hundsun.boss.modules.charge.service.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.modules.charge.dao.report.OrderAdvPaymentMyBatisDao;
import com.hundsun.boss.modules.charge.entity.report.OrderAdvPayment;
import com.hundsun.boss.modules.charge.form.common.SearchForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 预付查询Service
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Component
@Transactional(readOnly = true)
public class OrderAdvPaymentService extends BaseService {

    @Autowired
    private OrderAdvPaymentMyBatisDao orderAdvPaymentMyBatisDao;

    /**
     * 预付查询
     * 
     * @param page
     * @param form
     * @return
     */
    public Page<OrderAdvPayment> list(Page<OrderAdvPayment> page, SearchForm form) {
        User currentUser = UserUtils.getUser();
        form.setDept(getDept(currentUser, "office", ""));
        List list = orderAdvPaymentMyBatisDao.list(form);
        page.setList(list);
        page.setCount(form.getCount());
        return page;
    }
}
