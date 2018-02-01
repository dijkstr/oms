package com.hundsun.boss.modules.charge.service.temp;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.dao.temp.TempTableDao;
import com.hundsun.boss.modules.charge.dao.temp.TempTableMyBatisDao;
import com.hundsun.boss.modules.charge.entity.temp.TempTable;

/**
 * 中间表Service
 */
@SuppressWarnings("rawtypes")
@Component
@Transactional(readOnly = true)
public class TempTableService extends BaseService {
    @Autowired
    private TempTableDao tempTableDao;

    @Autowired
    private TempTableMyBatisDao tempTableMyBatisDao;

    public TempTable get(String id) {
        return tempTableDao.get(id);
    }

    public Page<TempTable> find(Page<TempTable> page, TempTable tempTable) {
        DetachedCriteria dc = tempTableDao.createDetachedCriteria();
        if (StringUtils.isNotEmpty(tempTable.getName())) {
            dc.add(Restrictions.like("name", "%" + tempTable.getName() + "%"));
        }
        dc.add(Restrictions.eq(TempTable.FIELD_DEL_FLAG, TempTable.DEL_FLAG_NORMAL));
        dc.addOrder(Order.desc("id"));
        return tempTableDao.find(page, dc);
    }

    @Transactional(readOnly = false)
    public void save(TempTable tempTable) {
        tempTableDao.save(tempTable);
    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        tempTableDao.deleteById(id);
    }

    /**
     * 获取二维表格式的中间表数据
     * 
     * @param params
     * @return
     */
    public List<LinkedHashMap<String, Object>> getPlainTableData(Map params) {
        return tempTableMyBatisDao.getPlainTableData(params);
    }
}
