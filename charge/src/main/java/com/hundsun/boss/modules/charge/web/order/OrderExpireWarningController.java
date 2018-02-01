package com.hundsun.boss.modules.charge.web.order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
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

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.FormUtils;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.entity.order.OrderExpireWarning;
import com.hundsun.boss.modules.charge.form.order.OrderExpireWarningForm;
import com.hundsun.boss.modules.charge.service.order.OrderExpireWarningService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 合同到期提醒Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/order/orderExpireWarning")
public class OrderExpireWarningController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderExpireWarningService orderExpireWarningService;

    @Autowired
    private OfficeService officeService;
    
   
    
    @ModelAttribute
    public OrderExpireWarning get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return orderExpireWarningService.get(id);
        } else {
            return new OrderExpireWarning();
        }
    }

    @RequiresPermissions("charge:order:orderExpireWarning:view")
    @RequestMapping(value = { "list", "" })
    public String list(OrderExpireWarningForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            form.setCreateBy(user.getId());
        }
        model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
        Page<OrderExpireWarning> page = orderExpireWarningService.find(new Page<OrderExpireWarning>(request, response), form);
        model.addAttribute("page", page);
        return "modules/" + "charge/order/orderExpireWarningList";
    }
    
    @RequiresPermissions("charge:order:orderExpireWarning:view")
    @RequestMapping(value = "form")
    public String form(OrderExpireWarningForm form, Model model, RedirectAttributes redirectAttributes) {
        try {
            if(form.getId() == null){
                form.setWarn_counter("0");
                model.addAttribute("orderExpireWarningForm", form );
            }else if(!CommonUtil.isNullorEmpty(form.getId()) && CommonUtil.isNullorEmpty(form.getUpdateDate())){
                OrderExpireWarning orderExpireWarning = get(form.getId());
                FormUtils.setPropertyValue(form, orderExpireWarning);
                form.setUpdateDate(orderExpireWarning.getUpdateDate());
                model.addAttribute("orderExpireWarningForm", form);
            }else{
                model.addAttribute("orderExpireWarningForm", form );
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "显示合同到期提醒设置失败");
            model.addAttribute("orderExpireWarningForm", form);
        }
        return "modules/" + "charge/order/orderExpireWarningForm";
    }

    @RequiresPermissions("charge:order:orderExpireWarning:edit")
    @RequestMapping(value = "save")
    public String save(OrderExpireWarningForm form, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (!beanValidator(model, form)) {
                return form(form, model, redirectAttributes);
            }
            OrderExpireWarning orderExpireWarning = new OrderExpireWarning();
            if(!CommonUtil.isNullorEmpty(form)){
                orderExpireWarning = get(form.getId());
            }
            BeanUtils.copyProperties(orderExpireWarning, form);
            orderExpireWarningService.save(orderExpireWarning);
            addMessage(redirectAttributes, "保存合同到期提醒设置成功");
            return "redirect:" + Global.getAdminPath() + "/charge/order/orderExpireWarning";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "保存合同到期提醒设置失败");
            model.addAttribute("orderExpireWarningForm", form);
            return "modules/" + "charge/order/orderExpireWarningForm";
        }
    }
    
    @RequiresPermissions("charge:order:orderExpireWarning:edit")
    @RequestMapping(value = "delete")
    public String delete(String id, RedirectAttributes redirectAttributes) {
        orderExpireWarningService.delete(id);
        addMessage(redirectAttributes, "删除合同到期提醒设置成功");
        return "redirect:" + Global.getAdminPath() + "/charge/order/orderExpireWarning";
    }

}
