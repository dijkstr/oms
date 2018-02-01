package com.hundsun.boss.modules.charge.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.modules.charge.dao.common.MyBatisSyncChooserDao;
import com.hundsun.boss.modules.charge.form.common.SyncProductForm;


@Service
@Transactional(readOnly = true)
public class SyncChooserService extends BaseService {

    @Autowired
    private MyBatisSyncChooserDao myBatisSynergyChooserDao;

    public Page<SyncProductForm> queryXtProds(Page<SyncProductForm> page, SyncProductForm form) {
        int countNum = myBatisSynergyChooserDao.queryXtProdsCount(form);
        form.setPageSize(page.getPageSize());
        form.setPageNo(page.getPageNo());
        List<SyncProductForm> list = myBatisSynergyChooserDao.queryXtProds(form);
        page.setCount(countNum);
        page.setList(list);
        return page;
    }
      
}
