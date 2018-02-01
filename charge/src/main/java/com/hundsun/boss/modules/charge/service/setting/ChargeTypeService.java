package com.hundsun.boss.modules.charge.service.setting;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.dao.setting.ChargeTypeDao;
import com.hundsun.boss.modules.charge.entity.setting.ChargeType;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 业务字典Service
 */
@Component
@Transactional(readOnly = true)
public class ChargeTypeService extends BaseService {

    @Autowired
    private ChargeTypeDao chargeTypeDao;

    public ChargeType get(String id) {
        return chargeTypeDao.get(id);
    }

    public Page<ChargeType> find(Page<ChargeType> page, ChargeType chargeType) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = chargeTypeDao.createDetachedCriteria();
        if (!CommonUtil.isNullorEmpty(chargeType.getType())) {
            dc.add(Restrictions.like("type", "%" + chargeType.getType() + "%"));
        }
        if (!CommonUtil.isNullorEmpty(chargeType.getDescription())) {
            dc.add(Restrictions.like("description", "%" + chargeType.getDescription() + "%"));
        }
        if (!CommonUtil.isNullorEmpty(chargeType.getValue())) {
            dc.add(Restrictions.like("value", "%" + chargeType.getValue() + "%"));
        }
        if (!CommonUtil.isNullorEmpty(chargeType.getLabel())) {
            dc.add(Restrictions.like("label", "%" + chargeType.getLabel() + "%"));
        }
        
        if (!CommonUtil.isNullorEmpty(chargeType.getOffice_id())) {
            dc.add(Restrictions.eq("office_id", chargeType.getOffice_id()));
        }
        dc.createAlias("office", "office");
        dc.add(dataScopeFilter(currentUser, "office", ""));
        
        dc.add(Restrictions.eq(ChargeType.FIELD_DEL_FLAG, ChargeType.DEL_FLAG_NORMAL));
        dc.addOrder(Order.asc("type")).addOrder(Order.asc("sort")).addOrder(Order.desc("id"));
        return chargeTypeDao.find(page, dc);
    }

    @Transactional(readOnly = false)
    public void save(ChargeType chargeType) {
        chargeTypeDao.save(chargeType);
    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        chargeTypeDao.deleteById(id);
    }

    public List<String> findTypeList() {
        return chargeTypeDao.findTypeList();
    }
    
    
    public List<ChargeType> getDictList(String type,List<String> officeIds) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = chargeTypeDao.createDetachedCriteria();
        if (!CommonUtil.isNullorEmpty(type)) {
            dc.add(Restrictions.eq("type", type));
        }
        
        dc.createAlias("office", "office");
        dc.add(dataScopeFilter(currentUser, "office", ""));
        
        if(!CommonUtil.isNullorEmpty(officeIds)){
            dc.add(Restrictions.in("office_id", officeIds));
        }
        
        dc.add(Restrictions.eq(ChargeType.FIELD_DEL_FLAG, ChargeType.DEL_FLAG_NORMAL));
        dc.addOrder(Order.asc("type")).addOrder(Order.asc("sort")).addOrder(Order.desc("id"));
        List<ChargeType> list =  chargeTypeDao.find(dc);
        return list;
    }
    
    public List<ChargeType> findListByOfficeCode(String officeCode) {
        DetachedCriteria dc = chargeTypeDao.createDetachedCriteria();
        dc.add(Restrictions.eq("office_id", officeCode));
        dc.add(Restrictions.eq(ChargeType.FIELD_DEL_FLAG, ChargeType.DEL_FLAG_NORMAL));
        return chargeTypeDao.find(dc);
    }
    
    public ChargeType getChargeType(String type,String value){
        return chargeTypeDao.getChargeType(type, value);
    }
    
}
