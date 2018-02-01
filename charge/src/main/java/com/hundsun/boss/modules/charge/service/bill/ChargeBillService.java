package com.hundsun.boss.modules.charge.service.bill;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.exception.ServiceException;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.jres.T2EventUtil;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.CommonUtils;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.modules.charge.common.ChargeConstant;
import com.hundsun.boss.modules.charge.dao.bill.ChargeBillDao;
import com.hundsun.boss.modules.charge.dao.finance.FinSummaryDao;
import com.hundsun.boss.modules.charge.dao.order.OrderInfoMyBatisDao;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillForm;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillInfo;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillSearchForm;
import com.hundsun.boss.modules.charge.form.bill.ChargeOrderInfo;
import com.hundsun.boss.modules.charge.form.bill.ChargePeriodTemp;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.form.order.OrderInfoForm;
import com.hundsun.boss.modules.charge.service.common.CommonProgress;
import com.hundsun.boss.modules.forecast.bean.IncomeForecastDataBean;
import com.hundsun.boss.modules.forecast.dao.IncomeForecastMybatisDao;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 账单Service
 */
@Component
@Transactional(readOnly = false)
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ChargeBillService extends BaseService {

    @Autowired
    private ChargeBillDao chargeBillDao;
    @Autowired
    private FinSummaryDao finSummaryDao;
    @Autowired
    private OrderInfoMyBatisDao orderInfoMyBatisDao;
    @Autowired
    private IncomeForecastMybatisDao incomeForecastMybatisDao;

    public Page<ChargeBillForm> queryChargeBillList(Page<ChargeBillForm> page, ChargeBillSearchForm chargeBillSearchForm) {
        User currentUser = UserUtils.getUser();
        chargeBillSearchForm.setDept(getDept(currentUser, "office", ""));
        List list = chargeBillDao.queryChargeBillList(chargeBillSearchForm);
        page.setList(list);
        page.setCount(chargeBillSearchForm.getCount());
        return page;
    }

    public String doCharge(ChargeBillSearchForm chargeBillSearchForm) {
        try {
            Map mP = new HashMap();
            // 生成 批次号
            String batch_no = Formatter.formatDate(Formatter.TIME_FORMAT2, new java.util.Date());
            // 获得计费期间开始日
            Date chargeBeginDate = Formatter.parseUtilDate(Formatter.DATE_FORMAT3, ((String) chargeBillSearchForm.getCharge_begin_month()) + "01");
            // 获得计费期间结束日
            Date chargeEndDate = CommonUtils.getMaxMonthDate(Formatter.parseUtilDate(Formatter.DATE_FORMAT3, ((String) chargeBillSearchForm.getCharge_end_month()) + "01"));
            // 1、查询计费对象合同
            OrderInfoForm orderInfoForm = new OrderInfoForm();
            orderInfoForm.setContract_id(chargeBillSearchForm.getContract_id());
            orderInfoForm.setOrder_begin_date(Formatter.formatDate(Formatter.DATE_FORMAT3, chargeBeginDate));
            orderInfoForm.setOrder_end_date(Formatter.formatDate(Formatter.DATE_FORMAT3, chargeEndDate));
            orderInfoForm.setOffice_id(chargeBillSearchForm.getOffice_id());
            List<Map> orderInfos = new ArrayList<Map>();
            if (CommonUtil.isNullorEmpty(orderInfoForm.getOffice_id())) {
                User currentUser = UserUtils.getUser();
                orderInfoForm.setOffice_id(getDept(currentUser, "office", ""));
            }
            orderInfos = orderInfoMyBatisDao.chargeContractList(orderInfoForm);
            // 2、循环处于计费期间内的月份数
            for (int i = 0; i < orderInfos.size(); i++) {
                String contractId = (String) orderInfos.get(i).get("contract_id");
                String orderBeginDate = (String) orderInfos.get(i).get("order_begin_date");
                String orderEndDate = (String) orderInfos.get(i).get("order_end_date");
                Calendar orderBegin = Calendar.getInstance();
                Calendar orderEnd = Calendar.getInstance();
                orderBegin.setTime(Formatter.parseUtilDate(Formatter.DATE_FORMAT1, orderBeginDate));
                orderEnd.setTime(Formatter.parseUtilDate(Formatter.DATE_FORMAT1, orderEndDate));
                // 调用数据中心统一接口查询
                Map mParams = new HashMap();
                mParams.put("view_name", ChargeConstant.UNIFY_INTERFACE_VIEW_NAME);
                mParams.put("XieTong_CONTRACT_ID", "'" + contractId + "'");
                mParams.put("page_size", "400");

                // 根据查询条件获取统一接口数据
                List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> unifyInterfaceDatas = new ArrayList<Map<String, Object>>();
                // 开始获取统一接口数据
                CommonProgress.progressNomal("开始处理");
                //如果合同未计费，回溯到第一期计费
                if (chargeBillDao.countChargeOrder(contractId) == 0) {
                    mParams.put("BEGIN_DATE", Formatter.formatDate(Formatter.DATE_FORMAT3, orderBegin.getTime()));
                    mParams.put("END_DATE", Formatter.formatDate(Formatter.DATE_FORMAT3, orderEnd.getTime()));
                } else {
                    mParams.put("BEGIN_DATE", Formatter.formatDate(Formatter.DATE_FORMAT3, chargeBeginDate));
                    mParams.put("END_DATE", Formatter.formatDate(Formatter.DATE_FORMAT3, chargeEndDate));
                }

                int datasum = 0;
                // 循环查询统一接口数据
                for (int ii = 0;; ii++) {
                    mParams.put("page_id", String.valueOf(ii));
                    unifyInterfaceDatas = T2EventUtil.callResultListMap(ChargeConstant.UNIFY_INTERFACE_FUNCTION_ID, mParams);
                    // 如果查询到达末尾
                    if (unifyInterfaceDatas.size() == 1 && !CommonUtils.isNullorEmpty(unifyInterfaceDatas.get(0).get("error_no"))) {
                        // 如果查不到数据
                        if (((String) unifyInterfaceDatas.get(0).get("error_no")).startsWith(ChargeConstant.UNIFY_INTERFACE_QUERY_END_CODE)) {
                            break;
                        } else {
                            throw new ServiceException((String) unifyInterfaceDatas.get(0).get("error_no"), null);
                        }
                    } else {
                        // 逐条插入
                        for (int j = 0; j < unifyInterfaceDatas.size(); j++) {
                            unifyInterfaceDatas.get(j).put("batch_no", batch_no);
                            chargeBillDao.insertUnifyDatas(unifyInterfaceDatas.get(j));
                        }
                    }
                    datasum += unifyInterfaceDatas.size();
                    CommonProgress.progressNomal("循环查询统一接口数据,已处理" + datasum + "条记录");
                }
                // 插入统一接口传来的数据
                for (int ii = 0; ii < datas.size(); ii++) {
                    datas.get(ii).put("batch_no", batch_no);
                    chargeBillDao.insertUnifyDatas(datas.get(ii));
                    CommonProgress.progressNomal("插入统一接口传来的数据", ii, datas.size());
                }
                // 根据错单表内容，删除统一接口表的错误记录(没做)
                // 对合同内不同期间(月份)的数据进行拆分，插入计费子账单期间临时表
                CommonProgress.progressNomal("插入计费子账单期间临时表");

                // charge_period_temp 表批量插入数据
                List<ChargePeriodTemp> periodList = new ArrayList<ChargePeriodTemp>();
                // 查询产品
                List<Map> prodLists = new ArrayList<Map>();
                prodLists = orderInfoMyBatisDao.queryProdList(contractId);

                //如果计费使用的计费开始年月小于等于计费开始时间，删除现有的所有账单，然后再计费
                String orderBeginMonth = Formatter.formatDate(Formatter.DATE_FORMAT4, orderBegin.getTime());
                if (orderBeginMonth.compareTo((String) chargeBillSearchForm.getCharge_begin_month()) >= 0) {
                    chargeBillDao.deleteBill(orderInfos.get(i));
                } else {
                    if (chargeBillDao.countChargeOrder(contractId) > 0) {
                        orderBegin.setTime(chargeBeginDate);
                    }
                }
                //计费使用的计费结束年月与合同结束日期取小值
                String orderEndMonth = Formatter.formatDate(Formatter.DATE_FORMAT4, orderEnd.getTime());
                if (orderEndMonth.compareTo((String) chargeBillSearchForm.getCharge_end_month()) >= 0) {
                    orderEnd.setTime(chargeEndDate);
                }

                while (orderBegin.getTimeInMillis() <= orderEnd.getTimeInMillis()) {
                    int month_flag = 0;
                    for (int j = 0; j < prodLists.size(); j++) {
                        orderBegin.set(Calendar.DAY_OF_MONTH, 1);
                        String prodBeginDate = (String) prodLists.get(j).get("prod_begin_date");
                        String prodEndDate = (String) prodLists.get(j).get("prod_end_date");
                        Calendar prodBegin = Calendar.getInstance();
                        prodBegin.setTime(Formatter.parseUtilDate(Formatter.DATE_FORMAT1, prodBeginDate));
                        prodBegin.set(Calendar.DAY_OF_MONTH, 1);
                        Calendar prodEnd = Calendar.getInstance();
                        prodEnd.setTime(Formatter.parseUtilDate(Formatter.DATE_FORMAT1, prodEndDate));
                        prodEnd.set(Calendar.DAY_OF_MONTH, 1);

                        ChargePeriodTemp chargePeriodTemp = new ChargePeriodTemp();
                        chargePeriodTemp.setContract_id(contractId);
                        chargePeriodTemp.setBatch_no(batch_no);
                        chargePeriodTemp.setProd_begin_date((String) prodLists.get(j).get("prod_begin_date"));
                        chargePeriodTemp.setProd_end_date((String) prodLists.get(j).get("prod_end_date"));
                        chargePeriodTemp.setProduct_id((String) prodLists.get(j).get("product_id"));
                        chargePeriodTemp.setId((String) prodLists.get(j).get("id"));
                        chargePeriodTemp.setFee_type((String) prodLists.get(j).get("fee_type"));
                        chargePeriodTemp.setBill_id(contractId + "-" + Formatter.formatDate(Formatter.DATE_FORMAT4, orderBegin.getTime()));

                        // a.compareTo(c) : a比c早返回-1;a与c相同,返回0;a比c晚,返回1
                        if (prodBegin.compareTo(orderBegin) == 0) {
                            chargePeriodTemp.setCharge_begin_date(prodBeginDate);
                            // 设置当月最后一天
                            prodBegin.set(Calendar.DAY_OF_MONTH, prodBegin.getActualMaximum(Calendar.DAY_OF_MONTH));
                            chargePeriodTemp.setCharge_end_date(Formatter.formatDate(Formatter.DATE_FORMAT1, prodBegin.getTime()));
                            if (!prodBeginDate.substring(8).equals("01")) {
                                chargePeriodTemp.setBill_id(contractId + "-" + Formatter.formatDate(Formatter.DATE_FORMAT4, orderBegin.getTime()) + "-odd");
                            }
                            month_flag++;
                        } else if (prodBegin.compareTo(orderBegin) == -1 && prodEnd.compareTo(orderBegin) >= 0) {
                            // 设置当月第一天
                            orderBegin.set(Calendar.DAY_OF_MONTH, 1);
                            chargePeriodTemp.setCharge_begin_date(Formatter.formatDate(Formatter.DATE_FORMAT1, orderBegin.getTime()));
                            if (prodEnd.compareTo(orderBegin) == 0) {
                                // 设置当月最后一天,即产品结束时间
                                chargePeriodTemp.setCharge_end_date(prodEndDate);
                            } else {
                                orderBegin.set(Calendar.DAY_OF_MONTH, orderBegin.getActualMaximum(Calendar.DAY_OF_MONTH));
                                chargePeriodTemp.setCharge_end_date(Formatter.formatDate(Formatter.DATE_FORMAT1, orderBegin.getTime()));
                            }
                            month_flag++;
                        } else {
                            continue;
                        }
                        periodList.add(chargePeriodTemp);
                    }
                    // 插入中间间隔的月份插入空记录
                    if (month_flag == 0) {
                        ChargePeriodTemp temp = new ChargePeriodTemp();
                        temp.setContract_id(contractId);
                        temp.setBatch_no(batch_no);
                        temp.setBill_id(contractId + "-" + Formatter.formatDate(Formatter.DATE_FORMAT4, orderBegin.getTime()));
                        temp.setCharge_begin_date(Formatter.formatDate(Formatter.DATE_FORMAT1, orderBegin.getTime()));
                        orderBegin.set(Calendar.DAY_OF_MONTH, orderBegin.getActualMaximum(Calendar.DAY_OF_MONTH));
                        temp.setCharge_end_date(Formatter.formatDate(Formatter.DATE_FORMAT1, orderBegin.getTime()));
                        periodList.add(temp);
                    }
                    orderBegin.set(Calendar.DAY_OF_MONTH, 1);
                    orderBegin.add(Calendar.MONTH, 1);

                }
                CommonProgress.progressNomal("插入临时表");
                chargeBillDao.insertChargePeriodTemp(periodList);
                // 插入拆分的空记录
                Map emptyBillData = new HashMap();
                emptyBillData.put("batch_no", batch_no);
                emptyBillData.put("contract_id", contractId);
                chargeBillDao.insertSplitTemp(emptyBillData);
            }
            // 调用计费账单生成存储过程
            mP.put("batch_no", batch_no);
            // 根据统一接口临时表计费产品id，协同合同号，协同客户号，进行错单判断，并将错误信息插入错单表
            CommonProgress.progressNomal("错单判断,将错误信息插入错单表");
            chargeBillDao.genChargeWrong(mP);
            // 删除本期间内出过的账单
            CommonProgress.progressNomal("删除本期间内出过的账单");
            chargeBillDao.chargeDeleteOldRecord(mP);
            // 按批次号，合同号，期间的开始、终了日，按照现有合同内容拍快照
            CommonProgress.progressNomal("按照现有合同内容生成快照");
            chargeBillDao.genChargeSnapshot(mP);
            // 补足统一接口日保底数据
            CommonProgress.progressNomal("补足统一接口日保底数据");
            chargeBillDao.genInsertDayMinConsumeUnifyInterface(mP);
            // 进行计费，生成产品级计费信息,组合级计费信息，合同级计费信息
            CommonProgress.progressNomal("生成产品、组合、合同计费信息");
            chargeBillDao.genChargeFee(mP);
            CommonProgress.progressFinish();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            CommonProgress.progressWrong();
            return "系统异常，请重试或者联系系统管理员";
        }
        return "success";
    }

    /**
     * 出账
     * 
     * @param queryChargeBillReq
     * @throws Exception
     */
    public void accountReceitp(ChargeBillSearchForm chargeBillSearchForm) {
        try {
            List<Map> lstResult = new ArrayList<Map>();
            // 选择出账
            CommonProgress.progressNomal("得到要出账的账单ID");
            if (!CommonUtils.isNullorEmpty(chargeBillSearchForm.getBill_id())) {
                for (String bill_id : chargeBillSearchForm.getBill_id().toString().split(",")) {
                    ChargeBillSearchForm form = new ChargeBillSearchForm();
                    form.setBill_id(bill_id);
                    lstResult.addAll(chargeBillDao.getBillIdForAccountReceipt(form));
                }
            } else {
                // （批量出账）查询月份bill_id
                if (CommonUtil.isNullorEmpty(chargeBillSearchForm.getOffice_id())) {
                    User currentUser = UserUtils.getUser();
                    chargeBillSearchForm.setDept(getDept(currentUser, "office", ""));
                }
                lstResult = chargeBillDao.getBillIdForAccountReceipt(chargeBillSearchForm);

            }
            List<String> list = new ArrayList();
            for (int i = 0; i < lstResult.size(); i++) {
                // 出账(计算技术服务费以及各种年度累计用于显示与控制画面)
                chargeBillDao.accountReceitp(lstResult.get(i));
                // 出账(计算合同级累计费用)
                chargeBillDao.accountBill(lstResult.get(i));
                CommonProgress.progressNomal("出账,各种费用计算中", i, lstResult.size());

                String contractId = lstResult.get(i).get("contract_id").toString();
                if (!list.contains(contractId)) {
                    list.add(contractId);
                }
            }
            // 生成收入插入临时表
            for (String id : list) {
                Map map = new HashMap();
                map.put("until_month", ChargeConstant.UNTIL_MONTH);
                map.put("contract_id", id);
                chargeBillDao.financeDetail(map);
                chargeBillDao.countYearlyProductIncome(map);
                chargeBillDao.countYearlyContractIncome(map);
            }
            // 对出账对象的数据进行收入预测
            incomeForecastMybatisDao.deletePreviousForecastResultsByContractIds(BaseService.getMultipleCondition(list));
            CommonProgress.progressFinish();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            CommonProgress.progressWrong();
        }

    }

    // 计费进度
    public Map getChargeOrderProgress() {
        return CommonProgress.chargeOrderrogress;

    }

    // 出账进度
    public Map getGenerateBillProgress() {
        return CommonProgress.chargeOrderrogress;

    }

    // 重置变量
    public void resetChargeOrderStatus() {
        CommonProgress.chargeOrderrogress = new HashMap();

    }

    public void updateSendStatus(Map map) {
        chargeBillDao.updateSendStatus(map);

    }

    public void deleteBill(Map map) {
        chargeBillDao.deleteBill(map);
    }

    /**
     * 获取账单发送信息
     */
    public ChargeBillForm getBillAccount(ChargeBillSearchForm chargeBillSearchForm) {
        return chargeBillDao.getBillAccount(chargeBillSearchForm);
    }

    /**
     * 更新审核状态
     * 
     * @param map bill_id
     */
    public int checkBill(Map map) {
        return chargeBillDao.checkBill(map);
    }

    /**
     * 计费合同列表导出
     * 
     * @param req
     * @return
     */
    public List<Map> queryBillChargingList(ChargeBillSearchForm req) {
        if (CommonUtil.isNullorEmpty(req.getOffice_id())) {
            User currentUser = UserUtils.getUser();
            req.setOffice_id(getDept(currentUser, "office", ""));
        }
        req.setPageSize(-1);
        return chargeBillDao.queryBillChargingList(req);
    }

    /**
     * 获取账单部门
     * 
     * @param downloadForm
     * @return
     */
    public List<Map> getOrderSource(DownloadForm downloadForm) {
        if (CommonUtil.isNullorEmpty(downloadForm.getDept())) {
            User currentUser = UserUtils.getUser();
            downloadForm.setDept(getDept(currentUser, "office", ""));
        }
        return chargeBillDao.getOrderSource(downloadForm);
    }

    /**
     * 获取账单id
     * 
     * @param downloadForm
     * @return
     */
    public List<String> getBillIds(DownloadForm downloadForm) {
        return chargeBillDao.getBillIds(downloadForm);
    }

    /**
     * 获取账单id
     * 
     * @param downloadForm
     * @return
     */
    public Map getMaxBillInfo(Map params) {
        return chargeBillDao.getMaxBillInfo(params);
    }

    /**
     * 获取计费合同详情.
     * 
     * @param req 请求参数.
     * @return 返回某条账单的详情信息.
     */
    public ChargeOrderInfo getChargeOrderInfo(ChargeOrderInfo chargeOrderInfo) {
        return chargeBillDao.getChargeOrderInfo(chargeOrderInfo);
    }

    /**
     * 获取用于
     * 
     * @param contract_ids
     * @return
     */
    public List<IncomeForecastDataBean> getFinsummaryForForecast(String contract_ids) {
        return finSummaryDao.getFinsummaryForForecast(contract_ids);
    }
    
    /**
     * 获取是否超保底
     */
    public ChargeBillInfo getBillConsume(ChargeBillSearchForm chargeBillSearchForm) {
        return chargeBillDao.getBillConsume(chargeBillSearchForm);
    }
}
