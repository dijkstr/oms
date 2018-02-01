package com.hundsun.boss.modules.charge.service.setting;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.entity.setting.MailConfig;
import com.hundsun.boss.modules.charge.dao.setting.MailConfigDao;

/**
 * 邮件配置Service
 */
@Component
@Transactional(readOnly = true)
public class MailConfigService extends BaseService {

    @Autowired
    private MailConfigDao mailConfigDao;

    public MailConfig get(String id) {
        return mailConfigDao.get(id);
    }

    public Page<MailConfig> find(Page<MailConfig> page, MailConfig mailConfig) {
        DetachedCriteria dc = mailConfigDao.createDetachedCriteria();
        if (StringUtils.isNotEmpty(mailConfig.getDepartment_name())) {
            dc.add(Restrictions.like("department_name", "%" + mailConfig.getDepartment_name() + "%"));
        }
        dc.add(Restrictions.eq(MailConfig.FIELD_DEL_FLAG, MailConfig.DEL_FLAG_NORMAL));
        dc.addOrder(Order.desc("id"));
        return mailConfigDao.find(page, dc);
    }

    @Transactional(readOnly = false)
    public void save(MailConfig mailConfig) {
        mailConfigDao.save(mailConfig);
    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        mailConfigDao.deleteById(id);
    }

}
