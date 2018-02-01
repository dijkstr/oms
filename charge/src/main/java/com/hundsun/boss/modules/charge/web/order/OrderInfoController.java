package com.hundsun.boss.modules.charge.web.order;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.common.report.PDFUtil;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.FormUtils;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.common.utils.excel.ExportExcel;
import com.hundsun.boss.common.utils.excel.ImportExcel;
import com.hundsun.boss.modules.charge.common.ChargeConstant;
import com.hundsun.boss.modules.charge.common.chargeTypeUtils;
import com.hundsun.boss.modules.charge.dao.audit.OrderInfoAuditDetailDao;
import com.hundsun.boss.modules.charge.entity.audit.OrderInfoAudit;
import com.hundsun.boss.modules.charge.entity.audit.OrderInfoAuditDetail;
import com.hundsun.boss.modules.charge.entity.income.ChargeIncomeSourceInterface;
import com.hundsun.boss.modules.charge.entity.order.ImportContracts;
import com.hundsun.boss.modules.charge.entity.order.OrderCombine;
import com.hundsun.boss.modules.charge.entity.order.OrderInfo;
import com.hundsun.boss.modules.charge.entity.order.OrderModel;
import com.hundsun.boss.modules.charge.entity.order.OrderPrice;
import com.hundsun.boss.modules.charge.entity.order.OrderProduct;
import com.hundsun.boss.modules.charge.entity.order.ProductLineInfo;
import com.hundsun.boss.modules.charge.entity.ordervalidate.OrderValidate;
import com.hundsun.boss.modules.charge.entity.sync.SyncContract;
import com.hundsun.boss.modules.charge.form.order.OrderAdvPaymentForm;
import com.hundsun.boss.modules.charge.form.order.OrderCombineForm;
import com.hundsun.boss.modules.charge.form.order.OrderIncomeSettingForm;
import com.hundsun.boss.modules.charge.form.order.OrderIncomeSettingRes;
import com.hundsun.boss.modules.charge.form.order.OrderInfoForm;
import com.hundsun.boss.modules.charge.form.order.OrderModelForm;
import com.hundsun.boss.modules.charge.form.order.OrderPriceForm;
import com.hundsun.boss.modules.charge.form.order.OrderProductForm;
import com.hundsun.boss.modules.charge.form.order.ProductLineInfoForm;
import com.hundsun.boss.modules.charge.form.ordervalidate.OrderValidateForm;
import com.hundsun.boss.modules.charge.service.audit.OrderInfoAuditService;
import com.hundsun.boss.modules.charge.service.income.ChargeIncomeSourceInterfaceService;
import com.hundsun.boss.modules.charge.service.order.OrderInfoService;
import com.hundsun.boss.modules.charge.service.order.ProductLineInfoService;
import com.hundsun.boss.modules.charge.service.ordervalidate.OrderValidateService;
import com.hundsun.boss.modules.charge.service.sync.SyncContractService;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.service.SysConfigService;
import com.hundsun.boss.modules.sys.utils.DictUtils;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 计费合同Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/order/orderInfo")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OrderInfoController extends BaseController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private OrderInfoAuditService orderInfoAuditService;
    @Autowired
    private OfficeService officeService;

    @Autowired
    private SyncContractService syncContractService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private OrderValidateService orderValidateService;

    @Autowired
    private ProductLineInfoService productLineInfoService;

    @Autowired
    private OrderInfoAuditDetailDao orderInfoAuditDetailDao;

    @Autowired
    private ChargeIncomeSourceInterfaceService chargeIncomeSourceInterfaceService;

    private static final String PROCESS_DEFINITION_KEY = "contractAudit";

    /**
     * 由id得合同信息
     * 
     * @param id
     * @return
     */
    @ModelAttribute
    public OrderInfo get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return orderInfoService.get(id);
        } else {
            return new OrderInfo();
        }
    }

    /**
     * 查询合同列表list
     * 
     * @param form
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("charge:order:orderInfo:view")
    @RequestMapping(value = { "list", "" })
    public String list(OrderInfoForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            User user = UserUtils.getUser();
            if (!user.isAdmin()) {
                form.setCreateBy(user.getId());
            }
            Page<OrderInfo> orderPage = orderInfoService.find(new Page<OrderInfo>(request, response), form);
            Page<OrderInfoForm> page = new Page<OrderInfoForm>();
            page.setCount(orderPage.getCount());
            page.setPageNo(orderPage.getPageNo());
            page.setPageSize(orderPage.getPageSize());
            List<OrderInfo> pageList = orderPage.getList();
            List<OrderInfoForm> formList = new ArrayList<OrderInfoForm>();
            for (int i = 0; !CommonUtil.isNullorEmpty(pageList) && i < pageList.size(); i++) {
                OrderInfo orderInfo = pageList.get(i);
                OrderInfoForm orderInfoForm = new OrderInfoForm();
                FormUtils.setPropertyValue(orderInfoForm, orderInfo);
                orderInfoForm.setId(orderInfo.getId());
                SyncContract syncContract = syncContractService.getByContractid(orderInfo.getContract_id());
                if (!CommonUtil.isNullorEmpty(syncContract)) {
                    orderInfoForm.setCustomername(syncContract.getCustomername());
                    orderInfoForm.setSigneddate(syncContract.getSigneddate());
                    orderInfoForm.setReporttype_id(syncContract.getReporttype_id());
                }
                if (!CommonUtil.isNullorEmpty(orderInfo.getSyncCustomer())) {
                    orderInfoForm.setCustomermanagername(orderInfo.getSyncCustomer().getCustomermanagername());
                    orderInfoForm.setHs_customername(orderInfo.getSyncCustomer().getChinesename());
                }
                formList.add(orderInfoForm);
            }
            page.setList(formList);

            model.addAttribute("page", page);
            model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "modules/" + "charge/order/orderInfoList";
    }

    /**
     * 合同新增/修改/保存的回显
     * 
     * @param form
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("charge:order:orderInfo:view")
    @RequestMapping(value = "form")
    public String form(OrderInfoForm form, Model model, RedirectAttributes redirectAttributes) {
        OrderInfo orderInfo = null;
        try {
            // 新增页面初始化
            if (CommonUtil.isNullorEmpty(form.getId()) && form.getContract_id() == null) {
                model.addAttribute("orderInfoForm", form);
            }
            // 更新页面初始化
            else if (!CommonUtil.isNullorEmpty(form.getId()) && CommonUtil.isNullorEmpty(form.getUpdateDate())) {
                orderInfo = orderInfoService.get(form.getId());
                FormUtils.setPropertyValue(form, orderInfo);
                form.setUpdateDate(orderInfo.getUpdateDate());
                form.setRemarks(orderInfo.getRemarks());
                //合同级验证信息
                OrderValidateForm orderValidate = new OrderValidateForm();
                orderValidate.setOrder_id(form.getId());
                orderValidate.setBelong_type("1");
                orderValidate.setShow_status("0");
                orderValidate.setHs_check_status_list(Arrays.asList("0", "1"));
                List orderValidateList = orderValidateService.queryOrderValidations(orderValidate);
                Set<OrderValidate> orderValidateSet = new HashSet<OrderValidate>();
                orderValidateSet.addAll(orderValidateList);
                form.setOrderValidates(orderValidateSet);

                //组合由set(hibernate对象)转list(form对象)
                form.getOrderCombines().addAll(convertSetToList(orderInfo.getOrderCombines()));
                model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
                SyncContract syncContract = syncContractService.getByContractid(form.getContract_id());
                if (!CommonUtil.isNullorEmpty(syncContract)) {
                    form.setCustomername(syncContract.getCustomername());
                    form.setSigneddate(syncContract.getSigneddate());
                    form.setReporttype_id(syncContract.getReporttype_id());

                }
                if (!CommonUtil.isNullorEmpty(orderInfo.getSyncCustomer())) {
                    form.setCustomermanagername(orderInfo.getSyncCustomer().getCustomermanagername());
                    form.setHs_customername(orderInfo.getSyncCustomer().getChinesename());
                }
                model.addAttribute("orderInfoForm", form);
            }
            // 如果校验出错
            else {
                model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
                form.setOrderCombines(getCombineListByJson(form));
                model.addAttribute("orderInfoForm", form);
            }

            //利用模板得合同html
            Map mValues = new HashMap();
            ReportConfig reportConfig = sysConfigService.getReportCongfig("orderCombineShow");
            mValues.put("template", reportConfig.getTemplate());
            String content = StringEscapeUtils.unescapeHtml(reportConfig.getTemplate_content());
            content = content.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "");
            mValues.put("content", content);
            mValues.put("form", form);
            String pdfHtml = PDFUtil.generatePDFHtml(mValues);
            pdfHtml = pdfHtml.replaceAll("\'", "\\\\\'");
            model.addAttribute("html", pdfHtml);

            //区分新增与修改的权限
            model.addAttribute("shiroPermission", form.getShiroPermission());

            return "modules/" + "charge/order/orderInfoForm";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "显示合同'" + form.getContract_id() + "'失败");
            model.addAttribute("orderInfoForm", form);
            return "modules/" + "charge/order/orderInfoForm";
        }

    }

    /**
     * 合同保存
     * 
     * @param form
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("charge:order:orderInfo:edit")
    @RequestMapping(value = "save")
    public String save(OrderInfoForm form, Model model, RedirectAttributes redirectAttributes) {
        try {
            OrderInfo orderInfo;
            form.setOrderCombines(getCombineListByJson(form));
            if (!orderValidator(model, form)) {
                return form(form, model, redirectAttributes);
            }
            List<OrderInfo> orderList = orderInfoService.findByContractId(form.getContract_id());
            orderInfo = new OrderInfo();
            //协同合同号唯一性验证
            // 如果是更新，获取最新的计费模式信息
            if (!CommonUtil.isNullorEmpty(form.getId())) {
                orderInfo = orderInfoService.get(form.getId());
                //不能修改协同合同编号
                if (!orderInfo.getContract_id().equals(form.getContract_id())) {
                    addMessage(model, "验证失败：<br/>合同修改：不能修改协同合同编号。(原协同合同编号：" + orderInfo.getContract_id() + ")");
                    return form(form, model, redirectAttributes);
                }
                // 如果有人更新内容，不允许修改
                if (!orderInfo.getUpdateDate().equals(form.getUpdateDate())) {
                    addMessage(model, "验证失败：<br/>合同：" + form.getContract_id() + "已经被人修改，请确认<br/>");
                    return form(form, model, redirectAttributes);
                }
                if (!CommonUtil.isNullorEmpty(orderList)) {
                    if (orderInfo.getContract_id().equals(form.getContract_id())) {
                        if (orderList.size() > 1) {
                            addMessage(model, "数据验证失败：<br/>协同合同编号：" + form.getContract_id() + "已经存在<br/>");
                            return form(form, model, redirectAttributes);
                        }
                    } else {
                        if (orderList.size() > 0) {
                            addMessage(model, "数据验证失败：<br/>协同合同编号：" + form.getContract_id() + "已经存在<br/>");
                            return form(form, model, redirectAttributes);
                        }
                    }
                }
            } else {
                if (!CommonUtil.isNullorEmpty(orderList)) {
                    if (orderList.size() > 0) {
                        addMessage(model, "数据验证失败：<br/>协同合同编号：" + form.getContract_id() + "已经存在<br/>");
                        return form(form, model, redirectAttributes);
                    }
                }
            }
            String orderStatus = orderInfo.getOrder_status();
            // 将表单对象更新入持久层
            FormUtils.margeFormToEntity(form, orderInfo);
            //修改阶梯的金额（加阶梯单位），费率（加千分位后的）
            ModifyPriceStep(orderInfo.getOrderCombines());
            orderInfo.setIncome_source(form.getIncome_source());
            orderInfo.setRemarks(form.getRemarks());
            orderInfo.setCc_flag(form.getCc_flag());
            getContractDate(orderInfo);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String now = dateFormat.format(new Date());
            // 新建合同审核明细
            OrderInfoAuditDetail orderInfoAuditDetail = new OrderInfoAuditDetail();
            // 如果没有计费结束日期，未开始
            if (CommonUtil.isNullorEmpty(orderInfo.getOrder_end_date())) {
                orderInfo.setOrder_status(ChargeConstant.ORDER_STATUS_NOT_BEGIN);
                // 如果计费合同的计费结束时间晚于当前，合同状态为计费结束
            } else if (orderInfo.getOrder_end_date().substring(0, 10).compareTo(now) <= 0) {
                orderInfo.setOrder_status(ChargeConstant.ORDER_STATUS_CHARGE_OUT_OF_TIME);
            } else {
                OrderInfoAudit orderInfoAudit = orderInfoAuditService.getByContractProcessKey(orderInfo.getWf_process_key());

                // 新建合同审核流程
                if (CommonUtil.isNullorEmpty(orderInfoAudit) || ChargeConstant.ORDER_STATUS_VERIFIED.equals(orderStatus)) {
                    // 合同审核表和合同表关联的主键
                    String wfProcessKey = null;
                    orderInfoAudit = new OrderInfoAudit();
                    orderInfoAudit.setOffice_id(form.getOffice_id());
                    orderInfoAudit.setRemarks("合同新建成功");
                    orderInfoAuditDetail.setComments("合同新建成功");
                    wfProcessKey = form.getContract_id() + "-" + Formatter.formatDate(Formatter.TIME_FORMAT2, new Date());
                    String processInstanceId = orderInfoService.startProcessInstance(wfProcessKey, PROCESS_DEFINITION_KEY).getProcessInstanceId();
                    orderInfoAudit.setProcess_instance_id(processInstanceId);
                    orderInfoAudit.setContract_process_key(wfProcessKey);
                    // 设置合同表和工作流表的关联主键
                    orderInfo.setWf_process_key(wfProcessKey);
                    // 编辑合同状态
                } else {
                    orderInfoAudit.setRemarks("合同编辑成功");
                    orderInfoAuditDetail.setComments("合同编辑成功");
                }

                orderInfoAudit.setProcessStatus(ChargeConstant.ORDER_STATUS_UNVERIFY);
                orderInfoAuditService.save(orderInfoAudit);

                orderInfoAuditDetail.setContract_process_key(orderInfoAudit.getContract_process_key());
                orderInfoAuditDetail.setOperate_user_id(UserUtils.getUser().getId());
                orderInfoAuditDetail.setOperate_date(new Date());
                // 设置合同的状态为待审核
                orderInfo.setOrder_status(ChargeConstant.ORDER_STATUS_UNVERIFY);
            }

            orderInfoService.save(orderInfo);
            orderInfoAuditDetailDao.save(orderInfoAuditDetail);
            // 更新收入来源接口
            saveIncomeSource(orderInfo);
            addMessage(redirectAttributes, "保存合同'" + form.getContract_id() + "'成功");
            String nextAction = "orderIncomeSetting";
            return "redirect:" + Global.getAdminPath() + "/charge/order/orderInfo/form/?id=" + orderInfo.getId() + "&shiroPermission=" + nextAction;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            model.addAttribute("orderInfoForm", form);
            addMessage(redirectAttributes, "保存计费合同'" + form.getContract_id() + "失败");
            return "redirect:" + Global.getAdminPath() + "/charge/order/orderInfo/?repage";
        }

    }

    /**
     * 添加收入来源接口
     */
    private void saveIncomeSource(OrderInfo orderInfo) {
        ChargeIncomeSourceInterface bean = new ChargeIncomeSourceInterface();
        bean.setOffice_id(orderInfo.getOffice_id());
        bean.setContract_id(orderInfo.getContract_id());
        SyncContract syncContract = syncContractService.getByContractid(orderInfo.getContract_id());
        bean.setCon_id(syncContract.getCon_id());
        bean.setIncome_source(orderInfo.getIncome_source());
        bean.setSend_flag(ChargeConstant.SEND_FLAG_CREATE);
        chargeIncomeSourceInterfaceService.save(bean);
    }

    /**
     * 合同保存信息的验证
     * 
     * @param model
     * @param form
     * @return
     */
    private boolean orderValidator(Model model, OrderInfoForm form) {
        if (!beanValidator(model, form)) {
            return false;
        }
        List<OrderCombineForm> orderCombines = form.getOrderCombines();
        for (int i = 0; !CommonUtil.isNullorEmpty(orderCombines) && i < orderCombines.size(); i++) {
            OrderCombineForm OrderCombine = orderCombines.get(i);
            if (!beanValidator(model, OrderCombine)) {
                return false;
            }
            List<OrderModelForm> orderModels = OrderCombine.getOrderModels();
            if (!CommonUtil.isNullorEmpty(orderModels)) {
                if (!beanValidator(model, orderModels.get(0))) {
                    return false;
                }
            }
            List<OrderAdvPaymentForm> orderAdvPayments = OrderCombine.getOrderAdvPayments();
            for (int j = 0; !CommonUtil.isNullorEmpty(orderAdvPayments) && j < orderAdvPayments.size(); j++) {
                if (!beanValidator(model, orderAdvPayments.get(j))) {
                    return false;
                }
            }
            List<OrderProductForm> orderProducts = OrderCombine.getOrderProducts();
            for (int j = 0; !CommonUtil.isNullorEmpty(orderProducts) && j < orderProducts.size(); j++) {
                OrderProductForm OrderProduct = orderProducts.get(j);
                if (!beanValidator(model, OrderProduct)) {
                    return false;
                }
                List<OrderModelForm> productOrderModels = OrderProduct.getOrderModels();
                if (!CommonUtil.isNullorEmpty(productOrderModels)) {
                    OrderModelForm productOrderModel = productOrderModels.get(0);
                    if (!beanValidator(model, productOrderModel)) {
                        return false;
                    }
                    List<OrderPriceForm> orderPrices = productOrderModel.getOrderPrices();
                    for (int k = 0; !CommonUtil.isNullorEmpty(orderPrices) && k < orderPrices.size(); k++) {
                        if (!beanValidator(model, orderPrices.get(k))) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 合同删除（物理删除）
     * 
     * @param id
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("charge:order:orderInfo:edit")
    @RequestMapping(value = "delete")
    public String delete(String id, RedirectAttributes redirectAttributes) {
        orderInfoService.delete(id);
        addMessage(redirectAttributes, "删除计费合同成功");
        return "redirect:" + Global.getAdminPath() + "/charge/order/orderInfo/?repage";
    }

    private static Type orderCombineFormType = new TypeToken<List<OrderCombineForm>>() {
    }.getType();
    private static Type orderAdvPaymentFormType = new TypeToken<List<OrderAdvPaymentForm>>() {
    }.getType();
    private static Type orderProductFormType = new TypeToken<List<OrderProductForm>>() {
    }.getType();
    private static Type orderModelFormType = new TypeToken<List<OrderModelForm>>() {
    }.getType();
    private static Type orderPriceFormType = new TypeToken<List<OrderPriceForm>>() {
    }.getType();

    /**
     * 根据画面表单传来的数据获取实体类
     * 
     * @param json
     * @return
     */
    private List<OrderCombineForm> getCombineListByJson(OrderInfoForm form) {
        String json = form.getJsonListOrderCombie();
        Gson gson = new Gson();
        // 获取合同组合列表
        List<OrderCombineForm> orderCombineForms = (List<OrderCombineForm>) gson.fromJson(json, orderCombineFormType);
        OrderCombineForm orderCombineForm = null;
        for (int i = 0; i < orderCombineForms.size(); i++) {
            orderCombineForm = orderCombineForms.get(i);
            orderCombineForm.setContract_id(form.getContract_id());
            // 获取合同组合计费模式
            orderCombineForm.setOrderModels((List<OrderModelForm>) gson.fromJson(orderCombineForm.getJsonListOrderModel(), orderModelFormType));
            // 获取合同组合预付列表
            orderCombineForm.setOrderAdvPayments((List<OrderAdvPaymentForm>) gson.fromJson(orderCombineForm.getJsonListPayment(), orderAdvPaymentFormType));
            // 获取合同产品列表
            orderCombineForm.setOrderProducts((List<OrderProductForm>) gson.fromJson(orderCombineForm.getJsonListOrderProduct(), orderProductFormType));
            OrderProductForm orderProductForm = null;
            for (int j = 0; j < orderCombineForm.getOrderProducts().size(); j++) {
                orderProductForm = orderCombineForm.getOrderProducts().get(j);
                // 获取合同产品计费模式
                orderProductForm.setOrderModels((List<OrderModelForm>) gson.fromJson(orderProductForm.getJsonListOrderModel(), orderModelFormType));
                // 获取合同产品计费价格
                if (!CommonUtil.isNullorEmpty(orderProductForm.getOrderModels())) {
                    orderProductForm.getOrderModels().get(0).setOrderPrices((List<OrderPriceForm>) gson.fromJson(orderProductForm.getOrderModels().get(0).getJsonListOrderPrice(), orderPriceFormType));
                }
            }
        }
        return orderCombineForms;
    }

    //合同组合由set(hibernate对象)转list(form对象)
    public List<OrderCombineForm> convertSetToList(Set<OrderCombine> set) throws Exception {
        List<OrderCombineForm> list = new ArrayList<OrderCombineForm>();
        if (!CommonUtil.isNullorEmpty(set)) {
            Iterator<OrderCombine> it = set.iterator();
            while (it.hasNext()) {
                OrderCombine orderCombine = it.next();
                //组合本身
                OrderCombineForm orderCombineForm = OrderCombineForm.class.newInstance();
                FormUtils.setPropertyValue(orderCombineForm, orderCombine);
                orderCombineForm.setId(orderCombine.getId());
                //组合的校验信息
                OrderValidateForm combineValidate = new OrderValidateForm();
                //combineValidate.setOrder_id(orderCombineForm.get);
                combineValidate.setCombine_id(orderCombine.getId());
                combineValidate.setBelong_type("2");
                combineValidate.setShow_status("0");
                combineValidate.setHs_check_status_list(Arrays.asList("0", "1"));
                List combineValidateList = orderValidateService.queryOrderValidations(combineValidate);
                Set<OrderValidate> combineValidateSet = new HashSet<OrderValidate>();
                combineValidateSet.addAll(combineValidateList);
                orderCombineForm.setOrderValidates(combineValidateSet);

                //组合计费模式
                List<OrderModelForm> orderModelFormList = FormUtils.convertSetToList(OrderModelForm.class, orderCombine.getOrderModels());
                orderCombineForm.setOrderModels(orderModelFormList);
                //组合预付
                List<OrderAdvPaymentForm> orderAdvPaymentList = FormUtils.convertSetToList(OrderAdvPaymentForm.class, orderCombine.getOrderAdvPayments());
                orderCombineForm.setOrderAdvPayments(orderAdvPaymentList);

                // 获取合同产品列表
                List<OrderProductForm> prodList = new ArrayList<OrderProductForm>();
                Set<OrderProduct> orderProducts = orderCombine.getOrderProducts();
                if (!CommonUtil.isNullorEmpty(orderProducts)) {
                    Iterator<OrderProduct> prodit = orderProducts.iterator();
                    while (prodit.hasNext()) {
                        OrderProduct orderProduct = prodit.next();
                        //产品本身
                        OrderProductForm orderProductForm = OrderProductForm.class.newInstance();
                        FormUtils.setPropertyValue(orderProductForm, orderProduct);
                        orderProductForm.setId(orderProduct.getId());
                        //产品的校验信息
                        OrderValidateForm prodValidate = new OrderValidateForm();
                        //prodValidate.setOrder_id();
                        prodValidate.setCombine_id(orderProduct.getCombine_id());
                        prodValidate.setHs_product_id(orderProduct.getProduct_id());
                        prodValidate.setBelong_type("3");
                        prodValidate.setShow_status("0");
                        prodValidate.setHs_check_status_list(Arrays.asList("0", "1"));
                        List prodValidateList = orderValidateService.queryOrderValidations(prodValidate);
                        Set<OrderValidate> prodValidateSet = new HashSet<OrderValidate>();
                        prodValidateSet.addAll(prodValidateList);
                        orderProductForm.setOrderValidates(prodValidateSet);

                        //产品计费模式
                        List<OrderModelForm> prodModelList = new ArrayList<OrderModelForm>();
                        if (!CommonUtil.isNullorEmpty(orderProduct.getOrderModels()) && orderProduct.getOrderModels().iterator().hasNext()) {
                            OrderModel ordermodel = orderProduct.getOrderModels().iterator().next();
                            OrderModelForm ordermodelForm = OrderModelForm.class.newInstance();
                            FormUtils.setPropertyValue(ordermodelForm, ordermodel);
                            ordermodelForm.setId(ordermodel.getId());
                            ordermodelForm.setFee_formula_name(DictUtils.getDictLabel(ordermodelForm.getFee_formula(), "fee_formula", ""));
                            ordermodelForm.setFee_type_name(chargeTypeUtils.getDictLabel(ordermodelForm.getFee_type(), "fee_type", ""));

                            //产品计费价格
                            List<OrderPriceForm> orderPriceList = FormUtils.convertSetToList(OrderPriceForm.class, ordermodel.getOrderPrices());
                            for (int i = 0; !CommonUtil.isNullorEmpty(orderPriceList) && i < orderPriceList.size(); i++) {
                                OrderPriceForm orderPriceForm = orderPriceList.get(i);
                                if (!CommonUtil.isNullorEmpty(orderPriceForm) && !CommonUtil.isNullorEmpty(orderPriceForm.getStep_unit())) {
                                    double step_unit = 1;
                                    if ("1".equals(orderPriceForm.getStep_unit())) {
                                        step_unit = 10000;
                                    } else if ("2".equals(orderPriceForm.getStep_unit())) {
                                        step_unit = 100000000;
                                    }
                                    if (!CommonUtil.isNullorEmpty(orderPriceForm.getStep_begin())) {
                                        orderPriceForm.setStep_begin(Formatter.formatDecimal(Formatter.DECIMAL_FORMAT15, Double.parseDouble(orderPriceForm.getStep_begin()) / step_unit));
                                    }
                                    if (!CommonUtil.isNullorEmpty(orderPriceForm.getStep_end())) {
                                        orderPriceForm.setStep_end(Formatter.formatDecimal(Formatter.DECIMAL_FORMAT15, Double.parseDouble(orderPriceForm.getStep_end()) / step_unit));
                                    }
                                }
                                if (!CommonUtil.isNullorEmpty(orderPriceForm) && !CommonUtil.isNullorEmpty(orderPriceForm.getFee_ratio())) {
                                    orderPriceForm.setFee_ratio(Formatter.formatDecimal(Formatter.DECIMAL_FORMAT15, Double.parseDouble(orderPriceForm.getFee_ratio()) * 1000));
                                }
                            }
                            ordermodelForm.setOrderPrices(orderPriceList);

                            prodModelList.add(ordermodelForm);
                        }
                        orderProductForm.setOrderModels(prodModelList);
                        prodList.add(orderProductForm);
                    }
                }

                orderCombineForm.setOrderProducts(prodList);

                list.add(orderCombineForm);
            }
        }
        return list;
    }

    //修改阶梯的金额（加阶梯单位），费率（加千分位后的）
    public void ModifyPriceStep(Set<OrderCombine> set) throws Exception {
        if (!CommonUtil.isNullorEmpty(set)) {
            Iterator<OrderCombine> it = set.iterator();
            while (it.hasNext()) {
                OrderCombine orderCombine = it.next();
                // 获取合同产品列表
                Set<OrderProduct> orderProducts = orderCombine.getOrderProducts();
                if (!CommonUtil.isNullorEmpty(orderProducts)) {
                    Iterator<OrderProduct> prodit = orderProducts.iterator();
                    while (prodit.hasNext()) {
                        OrderProduct orderProduct = prodit.next();
                        //产品的开始结束时间取组合的
                        orderProduct.setProd_begin_date(orderCombine.getCombine_begin_date());
                        orderProduct.setProd_end_date(orderCombine.getCombine_end_date());
                        //产品计费模式
                        if (!CommonUtil.isNullorEmpty(orderProduct.getOrderModels()) && orderProduct.getOrderModels().iterator().hasNext()) {
                            OrderModel ordermodel = orderProduct.getOrderModels().iterator().next();
                            //产品计费价格
                            Set<OrderPrice> orderPrices = ordermodel.getOrderPrices();
                            if (!CommonUtil.isNullorEmpty(orderPrices)) {
                                Iterator<OrderPrice> prodPriceit = orderPrices.iterator();
                                while (prodPriceit.hasNext()) {
                                    OrderPrice orderPrice = prodPriceit.next();
                                    if (!CommonUtil.isNullorEmpty(orderPrice) && !"1".equals(orderPrice.getDelFlag()) && !CommonUtil.isNullorEmpty(orderPrice.getStep_unit())) {
                                        double step_unit = 1;
                                        if ("1".equals(orderPrice.getStep_unit())) {
                                            step_unit = 10000;
                                        } else if ("2".equals(orderPrice.getStep_unit())) {
                                            step_unit = 100000000;
                                        }
                                        if (!CommonUtil.isNullorEmpty(orderPrice.getStep_begin())) {
                                            orderPrice.setStep_begin(orderPrice.getStep_begin() * step_unit);
                                        }
                                        if (!CommonUtil.isNullorEmpty(orderPrice.getStep_end())) {
                                            orderPrice.setStep_end(orderPrice.getStep_end() * step_unit);
                                        }
                                    }
                                    if (!CommonUtil.isNullorEmpty(orderPrice) && !CommonUtil.isNullorEmpty(orderPrice.getFee_ratio())) {
                                        orderPrice.setFee_ratio(orderPrice.getFee_ratio() / 1000);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //由组合计费时间得合同的开始结束时间
    public void getContractDate(OrderInfo orderInfo) throws Exception {
        Set<OrderCombine> set = orderInfo.getOrderCombines();
        String order_begin_date = "";
        String order_end_date = "";
        if (!CommonUtil.isNullorEmpty(set)) {
            Iterator<OrderCombine> it = set.iterator();
            while (it.hasNext()) {
                OrderCombine orderCombine = it.next();
                if (CommonUtil.isNullorEmpty(orderCombine) || (OrderInfo.DEL_FLAG_DELETE).equals(orderCombine.getDelFlag())) {
                    continue;
                }
                String combine_begin_date = orderCombine.getCombine_begin_date();
                String combine_end_date = orderCombine.getCombine_end_date();
                if (!CommonUtil.isNullorEmpty(combine_begin_date)) {
                    if (CommonUtil.isNullorEmpty(order_begin_date) || combine_begin_date.substring(0, 10).compareTo(order_begin_date) < 0) {
                        order_begin_date = combine_begin_date;
                    }
                }
                if (!CommonUtil.isNullorEmpty(combine_end_date)) {
                    if (CommonUtil.isNullorEmpty(order_end_date) || combine_end_date.substring(0, 10).compareTo(order_end_date) > 0) {
                        order_end_date = combine_end_date;
                    }
                }
            }
        }
        orderInfo.setOrder_begin_date(order_begin_date);
        orderInfo.setOrder_end_date(order_end_date);
    }

    //收入设置画面
    @RequiresPermissions("charge:order:orderInfo:view")
    @RequestMapping(value = "incomeSettingForm")
    public String incomeSettingForm(OrderInfoForm form, Model model) {
        OrderIncomeSettingRes orderIncomeRes = orderInfoService.getOrderIncomeSetting(form);
        SyncContract syncContract = syncContractService.getByContractid(form.getContract_id());
        model.addAttribute("syncContract", syncContract);
        model.addAttribute("incomeSettingForm", form);
        model.addAttribute("orderIncomeRes", orderIncomeRes.getListOrderIncomeSetting());
        return "modules/" + "charge/order/incomeSettingForm";
    }

    //提交收入设置
    @RequestMapping(value = "submitOrderIncome")
    public @ResponseBody Map<String, String> submitOrderIncome(OrderIncomeSettingForm form) {
        Map<String, String> data = new HashMap<String, String>();
        try {
            orderInfoService.insertOrderIncomeSetting(form);
            data.put("message", "确认收入成功");
            data.put("result", "success");
        } catch (Exception e) {
            data.put("result", "fail");
            data.put("message", "确认收入失败，系统异常:" + e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return data;
    }

    // 刷新收入期间接口数据
    @RequestMapping(value = "refreshIncomePeriod")
    public String refreshIncomePeriod(OrderIncomeSettingForm form) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        try {
            orderInfoService.generateChargeIncomePeriodInterface(form);
            data.put("message", "刷新收入期间接口数据成功");
            data.put("result", "success");
        } catch (Exception e) {
            data.put("result", "fail");
            data.put("message", "刷新收入期间接口数据失败，系统异常:" + e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return "redirect:" + Global.getAdminPath() + "/charge/income/chargeIncomePeriodInterface/form?contract_id=" + URLEncoder.encode(form.getContract_id(), "UTF-8");
    }

    /**
     * 合同审核查看
     * 
     * @param form
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("charge:audit:orderInfoAudit:verify")
    @RequestMapping(value = "verifyView")
    public String verifyView(OrderInfoForm form, Model model, RedirectAttributes redirectAttributes) {
        OrderInfo orderInfo = null;
        try {
            orderInfo = orderInfoService.get(form.getId());
            FormUtils.setPropertyValue(form, orderInfo);
            form.setUpdateDate(orderInfo.getUpdateDate());
            form.setRemarks(orderInfo.getRemarks());
            //合同级验证信息
            OrderValidateForm orderValidate = new OrderValidateForm();
            orderValidate.setOrder_id(form.getId());
            orderValidate.setBelong_type("1");
            orderValidate.setShow_status("0");
            orderValidate.setHs_check_status_list(Arrays.asList("0", "1"));
            List orderValidateList = orderValidateService.queryOrderValidations(orderValidate);
            Set<OrderValidate> orderValidateSet = new HashSet<OrderValidate>();
            orderValidateSet.addAll(orderValidateList);
            form.setOrderValidates(orderValidateSet);

            //组合由set(hibernate对象)转list(form对象)
            form.getOrderCombines().addAll(convertSetToList(orderInfo.getOrderCombines()));
            model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
            SyncContract syncContract = syncContractService.getByContractid(form.getContract_id());
            if (!CommonUtil.isNullorEmpty(syncContract)) {
                form.setCustomername(syncContract.getCustomername());
                form.setSigneddate(syncContract.getSigneddate());
            }
            if (!CommonUtil.isNullorEmpty(orderInfo.getSyncCustomer())) {
                form.setCustomermanagername(orderInfo.getSyncCustomer().getCustomermanagername());
                form.setHs_customername(orderInfo.getSyncCustomer().getChinesename());
            }
            model.addAttribute("orderInfoForm", form);

            //利用模板得合同html
            Map mValues = new HashMap();
            ReportConfig reportConfig = sysConfigService.getReportCongfig("orderCombineShow");
            mValues.put("template", reportConfig.getTemplate());
            String content = StringEscapeUtils.unescapeHtml(reportConfig.getTemplate_content());
            content = content.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "");
            mValues.put("content", content);
            mValues.put("form", form);
            String pdfHtml = PDFUtil.generatePDFHtml(mValues);
            model.addAttribute("html", pdfHtml);

            OrderIncomeSettingRes orderIncomeRes = orderInfoService.getOrderIncomeSetting(form);
            model.addAttribute("incomeSettingForm", form);
            model.addAttribute("orderIncomeRes", orderIncomeRes.getListOrderIncomeSetting());

            return "modules/" + "charge/order/orderInfoVerifyView";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "显示合同'" + form.getContract_id() + "'失败");
            model.addAttribute("orderInfoForm", form);
            return "modules/" + "charge/order/orderInfoVerifyView";
        }

    }

    /**
     * 合同审核操作
     * 
     * @param form
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("charge:audit:orderInfoAudit:verify")
    @RequestMapping(value = "verify")
    public String verify(OrderInfoForm form, Model model, RedirectAttributes redirectAttributes) {

        try {
            OrderInfo orderInfo = orderInfoService.get(form.getId());
            // 给工作流中需要的属性传值，使用 completeTask()
            Map params = new HashMap();
            params.put("isAuditPass", true);
            params.put("contractId", orderInfo.getContract_id());
            params.put("wfProcessKey", orderInfo.getWf_process_key());
            orderInfoService.completeTask(orderInfoService.getProcessInstanceId(orderInfo.getWf_process_key(), PROCESS_DEFINITION_KEY), "审核通过", params);
            // 新建合同审核明细
            OrderInfoAuditDetail orderInfoAuditDetail = orderInfoAuditDetailDao.sortByUpdateDateDesc().get(0);
            orderInfoAuditDetail.setContract_process_key(orderInfo.getWf_process_key());
            orderInfoAuditDetail.setAudit_user_id(UserUtils.getUser().getId());
            orderInfoAuditDetail.setAudit_date(new Date());
            orderInfoAuditDetail.setComments("审核通过");
            orderInfoAuditDetailDao.save(orderInfoAuditDetail);
            addMessage(redirectAttributes, "计费合同审核'" + orderInfo.getContract_id() + "'成功");
            return "redirect:" + Global.getAdminPath() + "/charge/audit/orderInfoAudit/?repage";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            model.addAttribute("orderInfoForm", form);
            addMessage(redirectAttributes, "计费合同审核失败");
            return "redirect:" + Global.getAdminPath() + "/charge/audit/orderInfoAudit/?repage";
        }

    }

    /**
     * 合同审核不通过 action
     * 
     * @param id
     * @param rejectReason
     * @param form
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("charge:audit:orderInfoAudit:verify")
    @RequestMapping(value = "reject")
    public @ResponseBody Map reject(@RequestParam String id, @RequestParam String rejectReason, OrderInfoForm form, Model model, RedirectAttributes redirectAttributes) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            OrderInfo orderInfo = orderInfoService.get(id);
            // 给工作流中需要的属性传值，使用 completeTask()
            Map params = new HashMap();
            params.put("isAuditPass", false);
            params.put("contractId", orderInfo.getContract_id());
            params.put("wfProcessKey", orderInfo.getWf_process_key());
            params.put("rejectReason", rejectReason);
            orderInfoService.completeTask(orderInfoService.getProcessInstanceId(orderInfo.getWf_process_key(), PROCESS_DEFINITION_KEY), rejectReason, params);
            // 新建合同审核明细
            OrderInfoAuditDetail orderInfoAuditDetail = orderInfoAuditDetailDao.sortByUpdateDateDesc().get(0);
            orderInfoAuditDetail.setContract_process_key(orderInfo.getWf_process_key());
            orderInfoAuditDetail.setAudit_user_id(UserUtils.getUser().getId());
            orderInfoAuditDetail.setAudit_date(new Date());
            orderInfoAuditDetail.setComments(rejectReason);
            orderInfoAuditDetailDao.save(orderInfoAuditDetail);
            result.put("result", "success");
            result.put("message", "计费合同审核不通过！");
            addMessage(redirectAttributes, "计费合同审核'" + orderInfo.getContract_id() + "'审核不通过");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            model.addAttribute("orderInfoForm", form);
            result.put("result", "fail");
            result.put("message", "保存失败!");
            addMessage(redirectAttributes, "计费合同审核失败！");
        }
        return result;

    }

    /**
     * 查询产品上线时间列表list
     * 
     * @param form
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("charge:order:productLine:view")
    @RequestMapping(value = "productLineList")
    public String productLineList(ProductLineInfoForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            form.setCreateBy(user.getId());
        }
        if (form.getOnline_status() == null) {
            form.setOnline_status("0");//默认显示
        }
        Page<ProductLineInfo> page = productLineInfoService.find(new Page<ProductLineInfo>(request, response), form);
        model.addAttribute("page", page);
        return "modules/" + "charge/order/productLineInfoList";
    }

    @RequiresPermissions("charge:order:productLine:edit")
    @RequestMapping(value = "updateProductLineStatus")
    public @ResponseBody Map<String, Object> updateProductLineStatus(String id, String status, Model model, RedirectAttributes redirectAttributes) {
        Map<String, Object> data = new HashMap<String, Object>();
        //原来的对象
        try {
            ProductLineInfo productLineInfo = productLineInfoService.get(id);
            if (!CommonUtil.isNullorEmpty(productLineInfo)) {
                //设置显示状态
                productLineInfo.setOnline_status(status);
                //更新对象
                productLineInfoService.save(productLineInfo);
                data.put("result", "success");
                if (status.equals("0")) {
                    data.put("message", "信息显示成功！");
                } else {
                    data.put("message", "信息过滤成功！");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            data.put("result", "fail");
            if (status.equals("0")) {
                data.put("message", "信息显示失败！");
            } else {
                data.put("message", "信息过滤失败！");
            }
        }
        return data;
    }

    /**
     * 根据协同合同编号判断是否存在
     * 
     * @param contract_id
     * @return
     */
    @RequiresPermissions("charge:order:orderInfo:view")
    @RequestMapping(value = "exists")
    public @ResponseBody Map exists(String contract_id) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (orderInfoService.findByContractId(contract_id).size() > 0) {
                result.put("result", "success");
            } else {
                result.put("result", "fail");
                result.put("result", "协同合同编号不存在");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("result", "fail");
            result.put("message", e.getMessage());
        }
        return result;

    }

    /**
     * 合同导入
     * 
     * @param file
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importContractFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int lineNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<ImportContracts> list = ei.getDataList(ImportContracts.class);
            List<ImportContracts> importContractsList = new ArrayList<ImportContracts>();
            for (int i = 0; i < list.size(); i++) {
                try {
                    lineNum = i + 3;
                    if (!CommonUtil.isNullorEmpty(list.get(i).getContract_id())) {
                        importContractsList.add(list.get(i));
                    } else {
                        failureMsg.append("<br/>导入数据失败,失败数据所在第 " + lineNum + "行;" + "<br/>失败原因:该行的contract_id为空");
                        addMessage(redirectAttributes, failureMsg + ",请核查并修改该条数据，然后重新导入。");
                        return "redirect:" + Global.getAdminPath() + "/charge/order/orderInfo/?repage";
                    }
                } catch (ConstraintViolationException e) {
                    failureMsg.append("<br/>导入数据失败,失败数据所在第 " + lineNum + "行;");
                    logger.error(e.getMessage(), e);
                    addMessage(redirectAttributes, failureMsg + "<br/>请核查并修改该条数据，然后重新导入,如仍未解决,请尝试联系系统管理员。");
                    return "redirect:" + Global.getAdminPath() + "/charge/order/orderInfo/?repage";
                }
            }
            orderInfoService.interImportContractsData(importContractsList);
            addMessage(redirectAttributes, "已成功导入数据");
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入用户失败！失败信息：" + e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return "redirect:" + Global.getAdminPath() + "/charge/order/orderInfo/?repage";
    }

    /**
     * 下载标注模板
     * 
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequestMapping("import/template")
    public String importContractsFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "导入合同模板.xlsx";
            List<ImportContracts> list = Lists.newArrayList();
            list.add(orderInfoService.queryImportContractData());
            new ExportExcel("导入合同列表", ImportContracts.class, 2).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/charge/order/orderInfo/?repage";
    }
}
