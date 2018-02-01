package com.hundsun.boss.modules.charge.dao.setting;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.charge.entity.setting.ChargeType;

/**
 * 业务字典DAO接口
 */
@Repository
public class ChargeTypeDao extends HibernateDao<ChargeType> {
    
    public List<String> findTypeList() {
        return find("select type from ChargeType where delFlag=:p1 group by type", new Parameter(ChargeType.DEL_FLAG_NORMAL));
    }
    
    
    public ChargeType getChargeType(String type,String value) {
        return getByHql("from ChargeType where delFlag=:p1 and type =:p2 and value=:p3", new Parameter(ChargeType.DEL_FLAG_NORMAL,type,value));
    }
}
