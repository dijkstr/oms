package com.hundsun.boss.modules.charge.service.bill;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.dao.bill.ChargeUnifyInterfaceDao;
import com.hundsun.boss.modules.charge.form.bill.ChargeUnifyInterface;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 统一接口数据Service
 */
@Component
@Transactional(readOnly = false)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChargeUnifyInterfaceService extends BaseService {

    @Autowired
    private ChargeUnifyInterfaceDao chargeUnifyInterfaceDao;

    public Page<ChargeUnifyInterface> queryBusiDataPage(Page<ChargeUnifyInterface> page, ChargeUnifyInterface form) {
        if (CommonUtil.isNullorEmpty(form.getOffice_id())) {
            User currentUser = UserUtils.getUser();
            form.setOffice_id(getDept(currentUser, "office", ""));
        }
        List list = chargeUnifyInterfaceDao.BusiDataList(form);
        page.setList(list);
        page.setCount(form.getCount());
        return page;
    }

    public List<Map> exportUnifyDataList(ChargeUnifyInterface req) {
        if (CommonUtil.isNullorEmpty(req.getOffice_id())) {
            User currentUser = UserUtils.getUser();
            req.setOffice_id(getDept(currentUser, "office", ""));
        }
        req.setPageSize(-1);
        return chargeUnifyInterfaceDao.BusiDataList(req);
    }

}
