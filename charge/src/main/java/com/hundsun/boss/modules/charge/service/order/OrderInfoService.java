package com.hundsun.boss.modules.charge.service.order;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.alibaba.fastjson.JSONArray;
import com.hundsun.boss.base.hibernate.IdEntity;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.WorkflowBaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.CommonUtils;
import com.hundsun.boss.modules.charge.common.ChargeConstant;
import com.hundsun.boss.modules.charge.dao.finance.FinSummaryDao;
import com.hundsun.boss.modules.charge.dao.income.ChargeIncomePeriodInterfaceDao;
import com.hundsun.boss.modules.charge.dao.income.ChargeIncomePeriodInterfaceMyBatisDao;
import com.hundsun.boss.modules.charge.dao.income.ChargeIncomeSourceInterfaceDao;
import com.hundsun.boss.modules.charge.dao.order.OrderCombineDao;
import com.hundsun.boss.modules.charge.dao.order.OrderIncomeSettingDao;
import com.hundsun.boss.modules.charge.dao.order.OrderInfoDao;
import com.hundsun.boss.modules.charge.dao.order.OrderInfoMyBatisDao;
import com.hundsun.boss.modules.charge.dao.sync.SyncContractDao;
import com.hundsun.boss.modules.charge.entity.income.ChargeIncomePeriodInterface;
import com.hundsun.boss.modules.charge.entity.order.ImportContracts;
import com.hundsun.boss.modules.charge.entity.order.OrderCombine;
import com.hundsun.boss.modules.charge.entity.order.OrderInfo;
import com.hundsun.boss.modules.charge.entity.order.OrderProduct;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.form.order.OrderIncomeSettingForm;
import com.hundsun.boss.modules.charge.form.order.OrderIncomeSettingRes;
import com.hundsun.boss.modules.charge.form.order.OrderInfoForm;
import com.hundsun.boss.modules.forecast.bean.IncomeForecastDataBean;
import com.hundsun.boss.modules.forecast.dao.IncomeForecastMybatisDao;
import com.hundsun.boss.modules.forecast.service.income.IncomeForecastService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 计费合同Service
 */
@SuppressWarnings("rawtypes")
@Component
@Transactional(readOnly = false)
public class OrderInfoService extends WorkflowBaseService {

    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private OrderIncomeSettingDao orderIncomeSettingDao;

    @Autowired
    private OrderCombineDao orderCombineDao;

    @Autowired
    private OrderInfoMyBatisDao orderInfoMyBatisDao;

    @Autowired
    private SyncContractDao syncContractDao;

    @Autowired
    private ChargeIncomePeriodInterfaceMyBatisDao chargeIncomePeriodInterfaceMyBatisDao;

    @Autowired
    private ChargeIncomeSourceInterfaceDao chargeIncomeSourceInterfaceDao;

    @Autowired
    private ChargeIncomePeriodInterfaceDao chargeIncomePeriodInterfaceDao;

    @Autowired
    private IncomeForecastService incomeForecastService;

    @Autowired
    private IncomeForecastMybatisDao incomeForecastMybatisDao;

    @Autowired
    private FinSummaryDao finSummaryDao;

    public OrderInfo get(String id) {
        return orderInfoDao.get(id);
    }

    /**
     * 合同查询
     * 
     * @param page
     * @param orderInfo
     * @return
     */
    public Page<OrderInfo> find(Page<OrderInfo> page, OrderInfoForm orderInfo) {
        User currentUser = UserUtils.getUser();
        DetachedCriteria dc = orderInfoDao.createDetachedCriteria();
        if (!CommonUtil.isNullorEmpty(orderInfo.getContract_id())) {
            dc.add(Restrictions.like("contract_id", "%" + orderInfo.getContract_id() + "%"));
        }

        if (!CommonUtil.isNullorEmpty(orderInfo.getCustomer_id())) {
            dc.add(Restrictions.like("customer_id", "%" + orderInfo.getCustomer_id() + "%"));
        }
        if (!CommonUtil.isNullorEmpty(orderInfo.getOffice_id())) {
            dc.add(Restrictions.eq("office_id", orderInfo.getOffice_id()));
        }
        if (!CommonUtil.isNullorEmpty(orderInfo.getOrder_status())) {
            dc.add(Restrictions.in("order_status", orderInfo.getOrder_status().split(",")));
        }
        if (!CommonUtil.isNullorEmpty(orderInfo.getIncome_source())) {
            dc.add(Restrictions.in("income_source", orderInfo.getIncome_source().split(",")));
        }
        if (!CommonUtil.isNullorEmpty(orderInfo.getHs_customername()) || !CommonUtil.isNullorEmpty(orderInfo.getCustomermanagername())) {
            dc.createAlias("syncCustomer", "syncCustomer");
            if (!CommonUtil.isNullorEmpty(orderInfo.getHs_customername())) {
                dc.add(Restrictions.like("syncCustomer.chinesename", "%" + orderInfo.getHs_customername() + "%"));
            }
            if (!CommonUtil.isNullorEmpty(orderInfo.getCustomermanagername())) {
                dc.add(Restrictions.like("syncCustomer.customermanagername", "%" + orderInfo.getCustomermanagername() + "%"));
            }
        }

        if (!CommonUtil.isNullorEmpty(orderInfo.getExpire_month())) {
            String[] expireMonths = orderInfo.getExpire_month().split(",");
            String maxExpireMonth = "0";
            for (int i = 0; i < expireMonths.length; i++) {
                if (maxExpireMonth.compareTo(expireMonths[i]) < 0) {
                    maxExpireMonth = expireMonths[i];
                }
            }
            String expireMonthQuery = " ( fun_get_month_diff(order_end_date,NOW())<=CONVERT(" + maxExpireMonth + ", SIGNED) ";
            expireMonthQuery += " ) ";
            dc.add(Restrictions.sqlRestriction(" DATE_FORMAT(order_end_date, '%Y%m%d') >= DATE_FORMAT(NOW(), '%Y%m%d') and " + expireMonthQuery));
        }
        if (!CommonUtil.isNullorEmpty(orderInfo.getTermination_status())) {
            if (orderInfo.getTermination_status().equals("0")) {
                dc.add(Restrictions.sqlRestriction(" DATE_FORMAT(order_end_date, '%Y%m%d') >= DATE_FORMAT(NOW(), '%Y%m%d')"));
            }
            if (orderInfo.getTermination_status().equals("1")) {
                dc.add(Restrictions.sqlRestriction(" DATE_FORMAT(order_end_date, '%Y%m%d') < DATE_FORMAT(NOW(), '%Y%m%d')"));
            }
        }
        if (!CommonUtil.isNullorEmpty(orderInfo.getPayment_type())) {
            String[] paymentTypes = orderInfo.getPayment_type().split(",");
            String paymentTypeQuery = " ( payment_type = " + paymentTypes[0] + " ";
            for (int i = 1; i < paymentTypes.length; i++) {
                paymentTypeQuery += " OR payment_type = " + paymentTypes[i] + " ";
            }
            paymentTypeQuery += " ) ";
            dc.add(Restrictions.sqlRestriction(" EXISTS (SELECT contract_id FROM order_income_setting WHERE contract_id = {alias}.contract_id AND " + paymentTypeQuery + " )"));
        }

        dc.createAlias("office", "office");
        dc.add(dataScopeFilter(currentUser, "office", ""));
        dc.add(Restrictions.eq(OrderInfo.FIELD_DEL_FLAG, OrderInfo.DEL_FLAG_NORMAL));
        dc.addOrder(Order.desc("updateDate"));
        return orderInfoDao.find(page, dc);
    }

    /**
     * 保存合同
     * 
     * @param orderInfo
     * @throws Exception
     */
    @Transactional(readOnly = false)
    public void save(OrderInfo orderInfo) throws Exception {
        orderInfoDao.save(orderInfo);
        orderInfoDao.flush();
        OrderIncomeSettingForm req = new OrderIncomeSettingForm();
        req.setContract_id(orderInfo.getContract_id());
        orderInfoMyBatisDao.setOrderIncome(req);
        syncContractDao.validateContract(orderInfo.getContract_id());
        incomeForecastMybatisDao.deletePreviousForecastResultsByContractId(orderInfo.getContract_id());
    }

    /**
     * 删除合同
     * 
     * @param id
     */
    @Transactional(readOnly = false)
    public void delete(String id) {
        OrderInfo orderInfo = orderInfoDao.get(id);
        orderInfoDao.delete(orderInfo);
        // 删除收入设置
        orderIncomeSettingDao.deleteByContractId(orderInfo.getContract_id());
        // 删除未同步的收入来源数据
        chargeIncomeSourceInterfaceDao.deleteUnsent(orderInfo.getContract_id());
        // 删除为同步的收入期间数据
        chargeIncomePeriodInterfaceDao.deleteUnsent(orderInfo.getContract_id());
        // 删除收入预测结果
        incomeForecastMybatisDao.deletePreviousForecastResultsByContractId(orderInfo.getContract_id());
    }

    /**
     * 根据合同号获取合同列表
     * 
     * @param contractId
     * @return
     */
    public List<OrderInfo> findByContractId(String contractId) {
        return orderInfoDao.findByContractId(contractId);
    }

    /**
     * 查询收入设置
     * 
     * @param req
     * @return
     */
    public OrderIncomeSettingRes getOrderIncomeSetting(OrderInfoForm req) {
        OrderIncomeSettingRes res = new OrderIncomeSettingRes();
        res.setContract_id(req.getContract_id());
        res.setCustomer_id(req.getCustomer_id());
        res.setHs_customername(req.getHs_customername());
        // 获取产品收入设置
        OrderIncomeSettingForm orderIncomeSetting = new OrderIncomeSettingForm();
        orderIncomeSetting.setContract_id(req.getContract_id());
        res.setListOrderIncomeSetting(orderInfoMyBatisDao.queryOrderIncomeSetting(orderIncomeSetting));
        return res;
    }

    /**
     * 查询计费组合设置
     * 
     * @param req
     * @return
     */
    public Page<OrderCombine> getOrderCombine(Page<OrderCombine> page, OrderInfoForm form) {
        DetachedCriteria dc = orderCombineDao.createDetachedCriteria();
        if (!CommonUtil.isNullorEmpty(form.getContract_id())) {
            dc.add(Restrictions.like("contract_id", "%" + form.getContract_id() + "%"));
        }
        dc.add(Restrictions.isNotNull("contract_id"));
        dc.addOrder(Order.asc("contract_id")).addOrder(Order.asc("combine_begin_date"));
        page = orderCombineDao.find(page, dc);
        for (OrderCombine orderCombine : page.getList()) {
            StringBuffer productNames = new StringBuffer();
            Set<OrderProduct> orderProducts = orderCombine.getOrderProducts();
            Iterator it = orderProducts.iterator();
            while (it.hasNext()) {
                IdEntity inEntity = (IdEntity) it.next();
                productNames.append(((OrderProduct) inEntity).getProduct_name()).append(";");
            }
            orderCombine.setProduct_names(productNames.toString());
            List<OrderInfo> orderInfos = orderInfoDao.findByContractId(orderCombine.getContract_id());
            if (orderInfos.size() > 0) {
                orderCombine.setOrderInfo(orderInfos.get(0));
            }
        }
        return page;
    }

    /**
     * 修改收入设置
     * 
     * @param req
     * @return
     * @throws Exception
     */
    public Integer insertOrderIncomeSetting(OrderIncomeSettingForm req) throws Exception {
        int count = 0;
        List<OrderIncomeSettingForm> list = JSONArray.parseArray(HtmlUtils.htmlUnescape(req.getJsonOrderIncomeSetting()), OrderIncomeSettingForm.class);
        if (!CommonUtils.isNullorEmpty(list)) {
            count = list.size();
            for (OrderIncomeSettingForm item : list) {
                orderInfoMyBatisDao.updateOrderIncomeSetting(item);
            }
        }
        OrderInfoForm submitOrderReq = new OrderInfoForm();
        submitOrderReq.setContract_id(req.getContract_id());
        submitOrderReq.setOrder_status(ChargeConstant.ORDER_STATUS_UNVERIFY);
        orderInfoMyBatisDao.updateOrderStatus(submitOrderReq);
        // 生成收入期间同步接口数据
        generateChargeIncomePeriodInterface(req);
        incomeForecastMybatisDao.deletePreviousForecastResultsByContractId(req.getContract_id());
        return count;
    }

    /**
     * 生成收入期间同步接口数据
     * 
     * @param req
     */
    public void generateChargeIncomePeriodInterface(OrderIncomeSettingForm req) {
        // 获取财物收入设置(画面传入的收入设置时间排列可能不正确，重新从数据库中读取)
        List<OrderIncomeSettingForm> orderIncomeSettingForms = orderInfoMyBatisDao.queryOrderIncomeSetting(req);
        // 获取协同合同中填报方式为上线日reporttype_id:'10572',明细行未上报accountidentity:'10931'
        List<Map<String, String>> syncContDetails = chargeIncomePeriodInterfaceMyBatisDao.getContdetailForIncomeperiodSync(req.getContract_id());
        OrderInfo orderInfo = orderInfoDao.findByContractId(req.getContract_id()).get(0);
        // 删除未发送的同合同下的收入期间记录
        chargeIncomePeriodInterfaceDao.deleteUnsentInterfaces(req.getContract_id());
        // 将收入设置与协同明细行合并
        for (Map<String, String> syncContDetail : syncContDetails) {
            ChargeIncomePeriodInterface chargeIncomePeriodInterface = new ChargeIncomePeriodInterface();
            chargeIncomePeriodInterface.setOffice_id(orderInfo.getOffice_id());
            chargeIncomePeriodInterface.setContract_id(req.getContract_id());
            chargeIncomePeriodInterface.setCon_id(syncContDetail.get("con_id"));
            chargeIncomePeriodInterface.setDetailid(syncContDetail.get("detailid"));
            chargeIncomePeriodInterface.setEx_product_id(syncContDetail.get("salprd_id"));
            chargeIncomePeriodInterface.setProduct_id(syncContDetail.get("productid"));
            chargeIncomePeriodInterface.setPayment_type(syncContDetail.get("chargetype"));
            chargeIncomePeriodInterface.setHasreceive(syncContDetail.get("hasreceive"));
            chargeIncomePeriodInterface.setServicestartdate(syncContDetail.get("servicestartdate"));
            chargeIncomePeriodInterface.setServiceenddate(syncContDetail.get("serviceenddate"));
            chargeIncomePeriodInterface.setHasreceive(syncContDetail.get("hasreceive"));
            chargeIncomePeriodInterface.setAccountidentity(syncContDetail.get("accountidentity"));
            // 根据协同产品、计费类型、既存 明细行到款匹配日期
            for (OrderIncomeSettingForm orderIncomeSettingForm : orderIncomeSettingForms) {
                if (syncContDetail.get("productid").equals(orderIncomeSettingForm.getProduct_id()) && syncContDetail.get("chargetype").equals(orderIncomeSettingForm.getPayment_type())) {
                    chargeIncomePeriodInterface.setIncome_begin_date(orderIncomeSettingForm.getIncome_begin_date());
                    chargeIncomePeriodInterface.setIncome_end_date(orderIncomeSettingForm.getIncome_end_date());
                    orderIncomeSettingForms.remove(orderIncomeSettingForm);
                    break;
                }
            }
            // 判断新生成的记录是否已推送,如果已推送，不新增
            Map<String, String> lastSendsyncContDetail = chargeIncomePeriodInterfaceMyBatisDao.getLastIncomePeriodForCompare(syncContDetail.get("detailid"));
            if (!CommonUtil.isNullorEmpty(lastSendsyncContDetail)
            // 新合同与已推送的合同相同
                    && lastSendsyncContDetail.get("contract_id").equals(chargeIncomePeriodInterface.getContract_id())
                    // 新产品与已推送的产品相同
                    && lastSendsyncContDetail.get("product_id").equals(chargeIncomePeriodInterface.getProduct_id())
                    // 新计费类型与已推送的计费类型相同
                    && lastSendsyncContDetail.get("payment_type").equals(chargeIncomePeriodInterface.getPayment_type())
                    // 新收入开始时间与已推送的时间相同
                    && null != lastSendsyncContDetail.get("income_begin_date") && lastSendsyncContDetail.get("income_begin_date").equals(chargeIncomePeriodInterface.getIncome_begin_date())
                    // 新收入结束时间与已推送的时间相同
                    && null != lastSendsyncContDetail.get("income_end_date") && lastSendsyncContDetail.get("income_end_date").equals(chargeIncomePeriodInterface.getIncome_end_date())) {
                continue;
            } else {
                chargeIncomePeriodInterface.setSend_flag(ChargeConstant.SEND_FLAG_CREATE);
            }
            chargeIncomePeriodInterfaceDao.save(chargeIncomePeriodInterface);
        }
    }

    /**
     * 导出收入设置
     * 
     * @param req
     * @return
     */
    public List<Map> queryIncomeSettingList(OrderInfoForm req) {
        if (!CommonUtil.isNullorEmpty(req.getExpire_month())) {
            String[] expireMonths = req.getExpire_month().split(",");
            String maxExpireMonth = "0";
            for (int i = 0; i < expireMonths.length; i++) {
                if (maxExpireMonth.compareTo(expireMonths[i]) < 0) {
                    maxExpireMonth = expireMonths[i];
                }
            }
            req.setExpire_month(maxExpireMonth);
        }
        if (CommonUtil.isNullorEmpty(req.getOffice_id())) {
            User currentUser = UserUtils.getUser();
            req.setOffice_id(getDept(currentUser, "office", ""));
        }
        return orderInfoMyBatisDao.queryIncomeSettingList(req);
    }

    /**
     * 导出合同列表查询
     * 
     * @param req
     * @return
     */
    public List<Map> queryContractList(OrderInfoForm req) {
        if (!CommonUtil.isNullorEmpty(req.getExpire_month())) {
            String[] expireMonths = req.getExpire_month().split(",");
            String maxExpireMonth = "0";
            for (int i = 0; i < expireMonths.length; i++) {
                if (maxExpireMonth.compareTo(expireMonths[i]) < 0) {
                    maxExpireMonth = expireMonths[i];
                }
            }
            req.setExpire_month(maxExpireMonth);
        }
        if (CommonUtil.isNullorEmpty(req.getOffice_id())) {
            User currentUser = UserUtils.getUser();
            req.setOffice_id(getDept(currentUser, "office", ""));
        }
        return orderInfoMyBatisDao.queryContractList(req);
    }

    /**
     * 根据部门获取合同列表
     * 
     * @param officeCode
     * @return
     */
    public List<OrderInfo> findListByOfficeCode(String officeCode) {
        DetachedCriteria dc = orderInfoDao.createDetachedCriteria();
        dc.add(Restrictions.eq("office_id", officeCode));
        dc.add(Restrictions.eq(OrderInfo.FIELD_DEL_FLAG, OrderInfo.DEL_FLAG_NORMAL));
        return orderInfoDao.find(dc);
    }

    /**
     * 更新合同状态
     * 
     * @param orderInfo
     * @throws Exception
     */
    @Transactional(readOnly = false)
    public void saveStatus(OrderInfo orderInfo) throws Exception {
        orderInfoDao.save(orderInfo);
        orderInfoDao.flush();
    }

    /**
     * 获取销售经理列表
     * 
     * @param downloadForm
     * @return
     */

    public List<Map> getSalers(DownloadForm downloadForm) {
        return orderInfoMyBatisDao.getSalers(downloadForm);
    }

    /**
     * 查询当年度的保底累计
     * 
     * @param params
     * @return
     */
    public Double queryOrderMinCharge(Map params) {
        return orderInfoMyBatisDao.queryOrderMinCharge(params);
    }

    /**
     * 获取合同联系人列表
     * 
     * @param map
     * @return
     */
    public List<Map> queryOrderRelationList(Map map) {
        return orderInfoMyBatisDao.queryOrderRelationList(map);
    }

    /**
     * 获取固定费用收入类型的内容
     * 
     * @param contract_id
     * @return
     */
    public List<IncomeForecastDataBean> queryFixedIncomeSettingList(String contract_id) {
        return orderInfoMyBatisDao.queryFixedIncomeSettingList(contract_id);
    }

    /**
     * 查询一条表中的数据(下载模板使用)
     * 
     * @return 单条结果数据
     */
    public ImportContracts queryImportContractData() {
        ImportContracts importContracts = orderInfoMyBatisDao.queryImportContractData();
        return importContracts;
    }

    /**
     * 将excel中的数据插到数据库中(先根据批次号删除)
     * 
     * @param continueContracts
     */
    public void interImportContractsData(List<ImportContracts> importContractLists) {
        if (!CommonUtil.isNullorEmpty(importContractLists)) {
            for (ImportContracts importContracts : importContractLists) {
                orderInfoMyBatisDao.deleteImportContractsData(importContracts);
            }
            orderInfoMyBatisDao.insertImportContractsData(importContractLists);
        } else {
            throw new RuntimeException("导入的数据有误,有可能表中无数据.");
        }
    }
}
