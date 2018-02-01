package com.hundsun.boss.modules.charge.dao.bill;

import java.util.List;
import java.util.Map;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.charge.form.bill.ChargeUnifyInterface;

/**
 * 统一接口数据DAO接口
 */
@MyBatisDao
public interface ChargeUnifyInterfaceDao {

    /**
     * 统一接口业务数据查询list
     * 
     * @param req
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<Map> BusiDataList(ChargeUnifyInterface req);

}
