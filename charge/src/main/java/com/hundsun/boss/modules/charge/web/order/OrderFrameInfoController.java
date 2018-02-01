package com.hundsun.boss.modules.charge.web.order;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.common.report.PDFUtil;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.FormUtils;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.entity.order.OrderFrameInfo;
import com.hundsun.boss.modules.charge.entity.order.OrderFrameSub;
import com.hundsun.boss.modules.charge.entity.order.OrderInfo;
import com.hundsun.boss.modules.charge.entity.sync.SyncContract;
import com.hundsun.boss.modules.charge.form.order.OrderFrameInfoForm;
import com.hundsun.boss.modules.charge.form.order.OrderFramePriceForm;
import com.hundsun.boss.modules.charge.form.order.OrderFrameSubForm;
import com.hundsun.boss.modules.charge.service.order.OrderFrameInfoService;
import com.hundsun.boss.modules.charge.service.order.OrderInfoService;
import com.hundsun.boss.modules.charge.service.sync.SyncContractService;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.service.SysConfigService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 框架合同Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/order/orderFrameInfo")
@SuppressWarnings({"unchecked","rawtypes"})
public class OrderFrameInfoController extends BaseController {
    
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderFrameInfoService orderFrameInfoService;

    @Autowired
    private OfficeService officeService;
    
    @Autowired
    private SyncContractService syncContractService;
    
    @Autowired
    private SysConfigService sysConfigService;
    
    @Autowired
    private OrderInfoService orderInfoService;
    /**
     * 由id得合同信息
     * @param id
     * @return
     */
    @ModelAttribute
    public OrderFrameInfo get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return orderFrameInfoService.get(id);
        } else {
            return new OrderFrameInfo();
        }
    }

    /**
     * 查询合同列表list
     * @param form
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("charge:order:orderFrameInfo:view")
    @RequestMapping(value = { "list", "" })
    public String list(OrderFrameInfoForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            form.setCreateBy(user.getId());
        }
        Page<OrderFrameInfo> page = orderFrameInfoService.find(new Page<OrderFrameInfo>(request, response), form);
        model.addAttribute("page", page);
        model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
        return "modules/" + "charge/order/orderFrameInfoList";
    }

    /**
     * 合同新增/修改/保存的回显
     * @param form
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("charge:order:orderFrameInfo:view")
    @RequestMapping(value = "form")
    public String form(OrderFrameInfoForm form, Model model, RedirectAttributes redirectAttributes) {
        OrderFrameInfo orderInfo = null;
        try {
            // 新增页面初始化
            if (CommonUtil.isNullorEmpty(form.getId()) && form.getContract_id() == null) {
                model.addAttribute("orderInfoForm", form);
            }
            // 更新页面初始化
            else if (!CommonUtil.isNullorEmpty(form.getId()) && CommonUtil.isNullorEmpty(form.getUpdateDate())) {
                orderInfo = orderFrameInfoService.get(form.getId());
                FormUtils.setPropertyValue(form, orderInfo);
                form.setUpdateDate(orderInfo.getUpdateDate());
                form.setRemarks(orderInfo.getRemarks());
                
                //组合由set(hibernate对象)转list(form对象)
                form.getOrderFrameSubs().addAll(convertSetToList(orderInfo.getOrderFrameSubs()));
                model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
                SyncContract syncContract = syncContractService.getByContractid(form.getContract_id());
                if(!CommonUtil.isNullorEmpty(syncContract)){
                    form.setHs_customername(syncContract.getCustomername());
                    if(!CommonUtil.isNullorEmpty(syncContract.getSyncCustomer())){
                        form.setCustomermanagername(syncContract.getSyncCustomer().getCustomermanagername());
                        form.setCustomermanager2name(syncContract.getSyncCustomer().getCustomermanager2name());
                    } 
                }
                model.addAttribute("orderInfoForm", form); 
            }
            // 如果校验出错
            else {
                model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
                //form.setOrderCombines(getCombineListByJson(form));
                model.addAttribute("orderInfoForm", form);  
            }
            
            //利用模板得合同html
            Map mValues = new HashMap();
            ReportConfig reportConfig = sysConfigService.getReportCongfig("orderFrameSubShow");
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
            
            return "modules/" + "charge/order/orderFrameInfoForm";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "显示合同'" + form.getContract_id() + "'失败");
            model.addAttribute("orderInfoForm", form);
            return "modules/" + "charge/order/orderFrameInfoForm";
        }

    }

    /**
     * 框架合同保存
     * @param form
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("charge:order:orderFrameInfo:edit")
    @RequestMapping(value = "save")
    public String save(OrderFrameInfoForm form, Model model, RedirectAttributes redirectAttributes) {
        try{
            form.setOrderFrameSubs(getOrderSubListByJson(form));
            OrderFrameInfo orderFrameInfo = new OrderFrameInfo();
            //验证
            if (!orderValidator(model, form)) {
                return form(form, model, redirectAttributes);
            }
            if(!CommonUtil.isNullorEmpty(form.getId())){
                orderFrameInfo = orderFrameInfoService.get(form.getId());
                //不能修改协同合同编号
                if (!orderFrameInfo.getContract_id().equals(form.getContract_id())) {
                    addMessage(model, "验证失败：<br/>框架合同修改：不能修改协同合同编号。(原协同合同编号：" + orderFrameInfo.getContract_id()+")");
                    return form(form, model, redirectAttributes);
                }
                // 如果有人更新内容，不允许修改
                if (!orderFrameInfo.getUpdateDate().equals(form.getUpdateDate())) {
                    addMessage(model, "验证失败：<br/>框架合同：" + form.getContract_id() + "已经被人修改，请确认<br/>");
                    return form(form, model, redirectAttributes);
                }
            }
            // 将表单对象更新入持久曾
            FormUtils.margeFormToEntity(form, orderFrameInfo);
            orderFrameInfo.setRemarks(form.getRemarks());
            orderFrameInfoService.save(orderFrameInfo);
            addMessage(redirectAttributes, "保存框架合同'" + form.getContract_id() + "'成功");
            return "redirect:" + Global.getAdminPath() + "/charge/order/orderFrameInfo/form/?id="+orderFrameInfo.getId();
        }catch(Exception e) {
            logger.error(e.getMessage(), e);
            model.addAttribute("orderFrameInfoForm", form);
            addMessage(redirectAttributes, "保存框架合同'" + form.getContract_id() + "失败");
            return "redirect:" + Global.getAdminPath() + "/charge/order/orderFrameInfo/?repage";
        }
    }

    /**
     * 合同删除（物理删除） 
     * @param id
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("charge:order:orderFrameInfo:edit")
    @RequestMapping(value = "delete")
    public String delete(String id, RedirectAttributes redirectAttributes) {
        orderFrameInfoService.delete(id);
        addMessage(redirectAttributes, "删除框架合同成功");
        return "redirect:" + Global.getAdminPath() + "/charge/order/orderFrameInfo/?repage";
    }

    private static Type orderSubFormType = new TypeToken<List<OrderFrameSubForm>>() {
    }.getType();
    private static Type orderFramePriceFormType = new TypeToken<List<OrderFramePriceForm>>() {
    }.getType();
    

    /**
     * 根据画面表单传来的数据获取实体类
     * 
     * @param json
     * @return
     */
    private List<OrderFrameSubForm> getOrderSubListByJson(OrderFrameInfoForm form) {
        String json = form.getJsonListOrderFrameSub();
        Gson gson = new Gson();
        // 获取合同组合列表
        List<OrderFrameSubForm> orderSubForms = (List<OrderFrameSubForm>) gson.fromJson(json, orderSubFormType);
        OrderFrameSubForm orderSubForm = null;
        for (int i = 0; i < orderSubForms.size(); i++) {
            orderSubForm = orderSubForms.get(i);
            // 获取合同组合预付列表
            orderSubForm.setOrderFramePrices((List<OrderFramePriceForm>) gson.fromJson(orderSubForm.getJsonListOrderFramePrice(), orderFramePriceFormType));
        }
        return orderSubForms;
    }
  
    //合同保存信息的验证
    private boolean orderValidator(Model model,OrderFrameInfoForm form) {
        if(!beanValidator(model, form)){
            return false;
        }
        List<OrderFrameSubForm> orderFrameSubs = form.getOrderFrameSubs();
        for (int i = 0;!CommonUtil.isNullorEmpty(orderFrameSubs) && i < orderFrameSubs.size(); i++) {
            OrderFrameSubForm orderFrameSub = orderFrameSubs.get(i);
            if(!beanValidator(model, orderFrameSub)){
                return false;
            }
            List<OrderFramePriceForm> orderFramePrices= orderFrameSub.getOrderFramePrices();
            for (int j = 0;!CommonUtil.isNullorEmpty(orderFramePrices) && j < orderFramePrices.size(); j++) {
                if(!beanValidator(model, orderFramePrices.get(j))){
                    return false;
                }
            }
        }
        return true;
    }
    
  //合同组合由set(hibernate对象)转list(form对象)
    public List<OrderFrameSubForm> convertSetToList(Set<OrderFrameSub> set) throws Exception {
        List<OrderFrameSubForm> list = new ArrayList<OrderFrameSubForm>();
        if (!CommonUtil.isNullorEmpty(set)) {
            Iterator<OrderFrameSub> it = set.iterator();
            while (it.hasNext()) {
                OrderFrameSub orderFrameSub = it.next();
                //组合本身
                OrderFrameSubForm orderFrameSubForm = OrderFrameSubForm.class.newInstance();
                FormUtils.setPropertyValue(orderFrameSubForm, orderFrameSub);
                orderFrameSubForm.setId(orderFrameSub.getId());
                
                List<OrderInfo> orderList =orderInfoService.findByContractId(orderFrameSub.getContract_id());
                if(!CommonUtil.isNullorEmpty(orderList) && orderList.size()==1){
                    orderFrameSubForm.setOrder_begin_date(orderList.get(0).getOrder_begin_date());
                    orderFrameSubForm.setOrder_end_date(orderList.get(0).getOrder_end_date());
                    orderFrameSubForm.setRemarks(orderList.get(0).getRemarks());
                }
                
                //组合预付
                List<OrderFramePriceForm> orderFramePriceList = FormUtils.convertSetToList(OrderFramePriceForm.class, orderFrameSub.getOrderFramePrices());
                orderFrameSubForm.setOrderFramePrices(orderFramePriceList);
                
                list.add(orderFrameSubForm);
            }
        }
        return list;
    }
       
}
