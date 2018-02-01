package com.hundsun.boss.modules.sys.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.sys.dao.ReportConfigDao;
import com.hundsun.boss.modules.sys.entity.ReportConfig;

/**
 * 导出配置Service
 */
@Component
@Transactional(readOnly = true)
public class ReportConfigService extends BaseService {

    @Autowired
    private ReportConfigDao reportConfigDao;

    public ReportConfig get(String id) {
        return reportConfigDao.get(id);
    }

    public Page<ReportConfig> find(Page<ReportConfig> page, ReportConfig reportConfig) {
        DetachedCriteria dc = reportConfigDao.createDetachedCriteria();
        if (StringUtils.isNotEmpty(reportConfig.getConfig_key())) {
            dc.add(Restrictions.like("config_key", "%" + reportConfig.getConfig_key() + "%"));
        }
        dc.add(Restrictions.eq(ReportConfig.FIELD_DEL_FLAG, ReportConfig.DEL_FLAG_NORMAL));
        dc.addOrder(Order.desc("id"));
        return reportConfigDao.find(page, dc);
    }

    @Transactional(readOnly = false)
    public void save(ReportConfig reportConfig) {
        reportConfigDao.save(reportConfig);
    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        reportConfigDao.deleteById(id);
    }

}
