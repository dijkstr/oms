package com.hundsun.boss.modules.charge.service.finance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtils;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.modules.charge.dao.finance.FinSummaryDao;
import com.hundsun.boss.modules.charge.form.finance.FinSummaryForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 财务汇总表Service
 */
@Component
@Transactional(readOnly = false)
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FinSummaryService extends BaseService {

    @Autowired
    private FinSummaryDao finSummaryDao;

    /**
     * 查询合同级财务汇总表
     * 
     * @param page
     * @param finSummaryForm
     * @return
     */
    public Page<FinSummaryForm> queryChargeBillList(Page<FinSummaryForm> page, FinSummaryForm finSummaryForm) {
        User currentUser = UserUtils.getUser();
        finSummaryForm.setDept(getDept(currentUser, "office", ""));
        if (CommonUtils.isNullorEmpty(finSummaryForm.getCharge_month())) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
            finSummaryForm.setCharge_month(df.format(new Date()));
        }
        // 逗号分隔查询
        String orgIncomeSource = finSummaryForm.getIncome_source();
        finSummaryForm.setIncome_source(getMultipleCondition(finSummaryForm.getIncome_source()));
        List list = finSummaryDao.queryFinSummaryList(finSummaryForm);
        finSummaryForm.setIncome_source(orgIncomeSource);
        page.setList(list);
        page.setCount(finSummaryForm.getCount());
        return page;
    }

    /**
     * 查询财务收入明细列表
     * 
     * @param req
     * @return
     */
    public List<FinSummaryForm> getFinSummaryDetailList(FinSummaryForm req) {
        List<FinSummaryForm> list = new ArrayList<FinSummaryForm>();
        list = finSummaryDao.getFinSummaryDetailList(req);
        return list;
    }

    /**
     * 导出 财务收入明细列表
     * 
     * @param req
     * @return
     */
    public List<Map> exportFinanceDetailList(FinSummaryForm req) {
        if (CommonUtils.isNullorEmpty(req.getOffice_id())) {
            User currentUser = UserUtils.getUser();
            req.setOffice_id(getDept(currentUser, "office", ""));
        }
        req.setPageSize(-1);
        List<Map> list = new ArrayList<Map>();
        if (!CommonUtils.isNullorEmpty(req.getCharge_month())) {
            list = finSummaryDao.exportLastDetail(req);
        } else {
            list = finSummaryDao.exportDetailList(req);
        }
        return list;
    }

    /**
     * 导出产品级明细
     * 
     * @param req
     * @return
     */
    public List<Map> exportProductDetailList(FinSummaryForm req) {
        if (CommonUtils.isNullorEmpty(req.getOffice_id())) {
            User currentUser = UserUtils.getUser();
            req.setOffice_id(getDept(currentUser, "office", ""));
        }
        req.setPageSize(-1);
        List<Map> list = new ArrayList<Map>();
        list = finSummaryDao.exportProductDetail(req);
        return list;
    }

    /**
     * 导出合同级明细
     * 
     * @param req
     * @return
     */
    public List<Map> exportContractDetailList(FinSummaryForm req) {
        if (CommonUtils.isNullorEmpty(req.getOffice_id())) {
            User currentUser = UserUtils.getUser();
            req.setOffice_id(getDept(currentUser, "office", ""));
        }
        req.setPageSize(-1);
        List<Map> list = new ArrayList<Map>();
        list = finSummaryDao.exportContractDetail(req);
        return list;
    }

    /**
     * 导出公司级明细
     * 
     * @param req
     * @return
     */
    public List<Map> exportBUDetailList(FinSummaryForm req) {
        if (CommonUtils.isNullorEmpty(req.getOffice_id())) {
            User currentUser = UserUtils.getUser();
            req.setOffice_id(getDept(currentUser, "office", ""));
        }
        req.setPageSize(-1);
        List<Map> list = new ArrayList<Map>();
        list = finSummaryDao.exportBUDetail(req);
        return list;
    }

    /**
     * 确认收入
     * 
     * @param req
     */
    public void confirmFinanceIncome(FinSummaryForm req) throws Exception {
        Map map = new HashMap();
        // 生成 批次号
        String batch_no = Formatter.formatDate(Formatter.TIME_FORMAT2, new java.util.Date());
        map.put("batch_no", batch_no);
        try {
            if (CommonUtils.isNullorEmpty(req.getContract_id())) {
                map.put("contract_id", "");
            } else {
                map.put("contract_id", req.getContract_id());
            }

            if (CommonUtils.isNullorEmpty(req.getDept())) {
                User currentUser = UserUtils.getUser();
                String[] depts = getDept(currentUser, "office", "").split(",");
                for (String dept : depts) {
                    dept = dept.replace("'", "");
                    map.put("dept", dept);
                    finSummaryDao.insertIncomeInterface(map);
                }
            } else {
                map.put("dept", req.getDept());
                finSummaryDao.insertIncomeInterface(map);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
