package com.hundsun.boss.modules.charge.service.setting;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.formula.functions.T;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.hibernate.BaseEntity;
import com.hundsun.boss.base.hibernate.IdEntity;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.dao.setting.ChargeModelDao;
import com.hundsun.boss.modules.charge.entity.setting.ChargeModel;
import com.hundsun.boss.modules.charge.form.setting.ChargeModelForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 计费模式Service
 */
@Component
@Transactional(readOnly = true)
public class ChargeModelService extends BaseService {

    @Autowired
    private ChargeModelDao chargeModelDao;

    public ChargeModel get(String id) {
        return chargeModelDao.get(id);
    }

    public Page<ChargeModel> find(Page<ChargeModel> page, ChargeModelForm form) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = chargeModelDao.createDetachedCriteria();
        if (StringUtils.isNotEmpty(form.getModel_name())) {
            dc.add(Restrictions.like("model_name", "%" + form.getModel_name() + "%"));
        }
        if (StringUtils.isNotEmpty(form.getFee_type())) {
            dc.add(Restrictions.eq("fee_type", form.getFee_type()));
        }
        if (StringUtils.isNotEmpty(form.getFee_formula())) {
            dc.add(Restrictions.eq("fee_formula", form.getFee_formula()));
        }
        if (StringUtils.isNotEmpty(form.getClassify_id())) {
            dc.add(Restrictions.eq("classify_id", form.getClassify_id()));
        }
        if (!CommonUtil.isNullorEmpty(form.getOffice_id())) {
            dc.add(Restrictions.eq("office_id", form.getOffice_id()));
        }
        dc.createAlias("office", "office");
        dc.add(dataScopeFilter(currentUser, "office", ""));
        
        dc.add(Restrictions.eq(ChargeModel.FIELD_DEL_FLAG, ChargeModel.DEL_FLAG_NORMAL));
        dc.addOrder(Order.desc("updateDate"));
        return chargeModelDao.find(page, dc);
    }

    @Transactional(readOnly = false)
    public void save(ChargeModel chargeModel) {        
        chargeModelDao.save(chargeModel);
    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        ChargeModel chargeModel = chargeModelDao.get(id);
        chargeModelDao.delete(chargeModel);
    }

    public Set<IdEntity<T>> getSetByList(Set<IdEntity<T>> chargePriceSet, List<IdEntity<T>> chargePriceList) {
        Set<IdEntity<T>> set = new HashSet<IdEntity<T>>();
        set.addAll(chargePriceList);
        Iterator<IdEntity<T>> it = chargePriceSet.iterator();
        while (it.hasNext()) {
            boolean flag = true;
            IdEntity<T> setT = it.next();
            for (IdEntity<T> listT : chargePriceList) {
                if ((setT.getId()).equals(listT.getId())) {
                    flag = false;
                    break;
                }
            }
            if (!flag) {
                break;
            } else {
                setT.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
                set.add(setT);
            }
        }
        return set;
    }

    
    public List<ChargeModel> findListByClassify(List<String> ids) {
        DetachedCriteria dc = chargeModelDao.createDetachedCriteria();
        dc.add(Restrictions.in("classify_id", ids));
        dc.add(Restrictions.eq(ChargeModel.FIELD_DEL_FLAG, ChargeModel.DEL_FLAG_NORMAL));
        return chargeModelDao.find(dc);
    }
}
