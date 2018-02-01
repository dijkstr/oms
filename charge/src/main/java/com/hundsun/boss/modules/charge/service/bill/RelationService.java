package com.hundsun.boss.modules.charge.service.bill;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.dao.bill.RelationDao;
import com.hundsun.boss.modules.charge.entity.bill.OrderRelation;
import com.hundsun.boss.modules.charge.form.bill.RelationForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 账单联系人Service
 */
@Component
@Transactional(readOnly = true)
public class RelationService extends BaseService {

    @Autowired
    private RelationDao relationDao;

    public OrderRelation get(String id) {
        return relationDao.get(id);
    }

    /**
     * 根据协同合同编号获取联系人信息
     * 
     * @param contract_id
     * @return
     */
    public OrderRelation getByContract_id(String contract_id) {
        List<OrderRelation> orderRelations = relationDao.getByContractId(contract_id);
        if (orderRelations.size() > 0) {
            return orderRelations.get(0);
        }
        return null;
    }

    public Page<OrderRelation> find(Page<OrderRelation> page, RelationForm relation) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = relationDao.createDetachedCriteria();
        if (StringUtils.isNotEmpty(relation.getContract_id())) {
            dc.add(Restrictions.like("contract_id", "%" + relation.getContract_id() + "%"));
        }
        if (StringUtils.isNotEmpty(relation.getRelation_name())) {
            dc.add(Restrictions.like("relation_name", "%" + relation.getRelation_name() + "%"));
        }
        if (StringUtils.isNotEmpty(relation.getMobile_tel())) {
            dc.add(Restrictions.like("mobile_tel", "%" + relation.getMobile_tel() + "%"));
        }
        if (StringUtils.isNotEmpty(relation.getEmail())) {
            dc.add(Restrictions.like("email", "%" + relation.getEmail() + "%"));
        }
        if (StringUtils.isNotEmpty(relation.getUser_name())) {
            dc.add(Restrictions.like("user_name", "%" + relation.getUser_name() + "%"));
        }
        if (StringUtils.isNotEmpty(relation.getOffice_id())) {
            dc.add(Restrictions.like("office_id", "%" + relation.getOffice_id() + "%"));
        }
        dc.createAlias("office", "office");
        dc.add(dataScopeFilter(currentUser, "office", ""));
        dc.add(Restrictions.eq(OrderRelation.FIELD_DEL_FLAG, OrderRelation.DEL_FLAG_NORMAL));
        dc.addOrder(Order.desc("id"));
        return relationDao.find(page, dc);
    }

    @Transactional(readOnly = false)
    public void save(OrderRelation relation) {
        relationDao.save(relation);
    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        relationDao.deleteById(id);
    }

}
