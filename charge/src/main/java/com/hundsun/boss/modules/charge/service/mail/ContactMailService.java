package com.hundsun.boss.modules.charge.service.mail;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.entity.mail.ContactMail;
import com.hundsun.boss.modules.charge.form.mail.ContactMailForm;
import com.hundsun.boss.modules.charge.dao.mail.ContactMailDao;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 邮件履历Service
 */
@Component
@Transactional(readOnly = true)
public class ContactMailService extends BaseService {

    @Autowired
    private ContactMailDao contactMailDao;

    public ContactMail get(String id) {
        return contactMailDao.get(id);
    }

    public Page<ContactMail> find(Page<ContactMail> page, ContactMailForm form) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = contactMailDao.createDetachedCriteria();
        if (StringUtils.isNotEmpty(form.getContract_id())) {
            dc.add(Restrictions.like("contract_id", "%" + form.getContract_id() + "%"));
        }
        if (StringUtils.isNotEmpty(form.getUser_name())) {
            dc.add(Restrictions.like("user_name", "%" + form.getUser_name() + "%"));
        }
        if (StringUtils.isNotEmpty(form.getOffice_id())) {
            dc.add(Restrictions.like("office_id", "%" + form.getOffice_id() + "%"));
        }
        dc.createAlias("office", "office");
        dc.add(dataScopeFilter(currentUser, "office", ""));
        dc.add(Restrictions.eq(ContactMail.FIELD_DEL_FLAG, ContactMail.DEL_FLAG_NORMAL));
        dc.addOrder(Order.desc("id"));
        return contactMailDao.find(page, dc);
    }

    @Transactional(readOnly = false)
    public void save(ContactMail contactMail) {
        contactMailDao.save(contactMail);
    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        contactMailDao.deleteById(id);
    }

}
