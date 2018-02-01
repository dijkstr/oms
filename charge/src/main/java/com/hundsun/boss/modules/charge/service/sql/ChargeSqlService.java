package com.hundsun.boss.modules.charge.service.sql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.dao.sql.ChargeSqlDao;
import com.hundsun.boss.modules.charge.form.sql.ChargeSqlForm;

/**
 * 计费sql查询Service
 */
@Component
@Transactional(readOnly = true)
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ChargeSqlService extends BaseService {
    @Autowired
    private ChargeSqlDao chargeSqlDao;

    public Page<List> find(Page<List> page, ChargeSqlForm chargeSqlForm) {
        if (!CommonUtil.isNullorEmpty(chargeSqlForm.getSql())) {
            List list = chargeSqlDao.excutesql(chargeSqlForm);
            page.setList(list);
            page.setCount(chargeSqlForm.getCount());

        }

        return page;
    }

}
