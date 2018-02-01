package com.hundsun.boss.modules.charge.web.audit;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.common.ChargeConstant;
import com.hundsun.boss.modules.charge.dao.audit.OrderInfoAuditDetailDao;
import com.hundsun.boss.modules.charge.entity.audit.OrderInfoAudit;
import com.hundsun.boss.modules.charge.entity.audit.OrderInfoAuditDetail;
import com.hundsun.boss.modules.charge.entity.order.OrderInfo;
import com.hundsun.boss.modules.charge.form.audit.OrderAuditSearchForm;
import com.hundsun.boss.modules.charge.service.audit.OrderInfoAuditService;
import com.hundsun.boss.modules.charge.service.order.OrderInfoService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.utils.UserUtils;



/**
 * 计费合同审核Controller
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Controller
@RequestMapping(value = "${adminPath}/charge/audit/orderInfoAudit")
public class OrderInfoAuditController extends BaseController {

    @Autowired
    private OrderInfoAuditService orderInfoAuditService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private OrderInfoAuditDetailDao orderinfoAuditDetailDao;

    @Autowired
    private OfficeService officeService;

    @ModelAttribute
    public OrderInfoAudit get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return orderInfoAuditService.get(id);
        } else {
            return new OrderInfoAudit();
        }
    }

    /**
     * 显示审核工作流列表
     * 
     * @param form
     * @param orderInfoAudit
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("charge:audit:orderInfoAudit:view")
    @RequestMapping(value = { "list", "" })
    public String list(OrderAuditSearchForm form, OrderInfoAudit orderInfoAudit, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            orderInfoAudit.setCreateBy(user);
        }
        Page defaultpage = new Page<OrderAuditSearchForm>(request, response);
        form.setPageNo(defaultpage.getPageNo());
        form.setPageSize(defaultpage.getPageSize());
        Page<Map> page = orderInfoAuditService.queryOrderInfoList(defaultpage, form);
        model.addAttribute("page", page);
        model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
        return "modules/" + "charge/audit/orderInfoAuditList";
    }

    /**
     * 查看工作流
     * 
     * @param orderInfoAudit
     * @param model
     * @return
     */
    @RequiresPermissions("charge:audit:orderInfoAudit:view")
    @RequestMapping(value = "form")
    public String form(OrderInfoAudit orderInfoAudit, Model model) {
        model.addAttribute("orderInfoAudit", orderInfoAudit);
        return "modules/" + "charge/audit/orderInfoAuditForm";
    }

    /**
     * 保存审核工作流
     * 
     * @param orderInfoAudit
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("charge:audit:orderInfoAudit:edit")
    @RequestMapping(value = "save")
    public String save(OrderInfoAudit orderInfoAudit, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, orderInfoAudit)) {
            return form(orderInfoAudit, model);
        }
        orderInfoAuditService.save(orderInfoAudit);
        addMessage(redirectAttributes, "保存计费合同审核'" + orderInfoAudit.getContract_process_key() + "'成功");
        return "redirect:" + Global.getAdminPath() + "/charge/audit/orderInfoAudit/?repage";
    }

    /**
     * 1. 删除合同审核表数据 2. 删除合同审核明细表数据 3. 删除工作流表关联的数据
     * 
     * @param id
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("charge:audit:orderInfoAudit:edit")
    @RequestMapping(value = "delete")
    public String delete(String id, String processInstanceId, RedirectAttributes redirectAttributes) {
        OrderInfoAudit orderInfoAudit = orderInfoAuditService.get(id);
        orderInfoAuditService.deleteProcessInstance(processInstanceId);
        orderInfoAuditService.delete(id);
        orderinfoAuditDetailDao.deleteByContractProcessKey(orderInfoAudit.getContract_process_key());
        addMessage(redirectAttributes, "删除计费合同审核成功");
        return "redirect:" + Global.getAdminPath() + "/charge/audit/orderInfoAudit/?repage";
    }

    /**
     * 显示某个合同审核的详情
     * 
     * @param orderInfoAudit
     * @param model
     * @return
     */
    @RequiresPermissions("charge:audit:orderInfoAudit:view")
    @RequestMapping(value = "detail")
    public String detail(String contractProcessKey, Model model, RedirectAttributes redirectAttributes) {
        List<OrderInfoAuditDetail> orderInfoAuditDetails = orderinfoAuditDetailDao.getByContractProcessKey(contractProcessKey);
        OrderInfoAudit orderInfoAudit = orderInfoAuditService.getByContractProcessKey(contractProcessKey);
        model.addAttribute("orderInfoAudit", orderInfoAudit);
        model.addAttribute("orderInfoAuditDetails", orderInfoAuditDetails);
        return "modules/charge/audit/orderInfoAuditDetail";
    }

    /**
     * 合同审核页面
     * 
     * @param contractProcessKey
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("charge:audit:orderInfoAudit:verify")
    @RequestMapping(value = "verifyView")
    public String verifyView(@RequestParam String contractProcessKey, Model model, RedirectAttributes redirectAttributes) {
        OrderInfoAudit orderInfoAudit = orderInfoAuditService.getByContractProcessKey(contractProcessKey);
        if (ChargeConstant.ORDER_STATUS_VERIFIED.equals(orderInfoAudit.getProcessStatus())) {
            addMessage(redirectAttributes, "合同流程号[" + contractProcessKey + "]已审核");
            return "redirect:" + Global.getAdminPath() + "/charge/audit/orderInfoAudit/?repage";
        }
        List<OrderInfo> orderInfos = orderInfoService.findByContractId(contractProcessKey.split("-")[0]);
        OrderInfo orderInfo = null;
        String orderInfoId = null;
        if (!CommonUtil.isNullorEmpty(orderInfos)) {
            orderInfo = orderInfos.get(0);
        } else {
            addMessage(redirectAttributes, "查看合同审核失败");
            return "modules/" + "charge/audit/orderInfoAuditList";
        }
        orderInfoId = orderInfo.getId();
        model.addAttribute("orderInfo", orderInfo);
        return "redirect:" + Global.getAdminPath() + "/charge/order/orderInfo/verifyView?id=" + orderInfoId;
    }
}
