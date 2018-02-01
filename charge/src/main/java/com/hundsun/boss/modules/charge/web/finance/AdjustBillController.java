package com.hundsun.boss.modules.charge.web.finance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.modules.charge.form.finance.AdjustBillForm;
import com.hundsun.boss.modules.charge.service.finance.AdjustBillService;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.util.CommonUtils;

/**
 * 调账管理Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/finance/adjustBill")
public class AdjustBillController extends BaseController {

    @Autowired
    private AdjustBillService adjustBillService;

    @Autowired
    private OfficeService officeService;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequiresPermissions("charge:finance:adjustBill:view")
    @RequestMapping(value = { "list", "" })
    public String list(AdjustBillForm adjustBillForm, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page defaultpage = new Page<AdjustBillForm>(request, response);
        adjustBillForm.setPageNo(defaultpage.getPageNo());
        adjustBillForm.setPageSize(defaultpage.getPageSize());
        Page<AdjustBillForm> page = adjustBillService.adjustList(defaultpage, adjustBillForm);
        model.addAttribute("page", page);
        model.addAttribute("adjustBillForm", adjustBillForm);
        model.addAttribute("office", officeService.findByCode(adjustBillForm.getOffice_id()));
        return "modules/" + "charge/finance/adjustBillList";
    }
    
    @RequiresPermissions("charge:finance:adjustBill:view")
    @RequestMapping(value = "form")
    public String form(AdjustBillForm adjustBillForm, Model model) {
        if(!CommonUtils.isNullorEmpty(adjustBillForm.getId())) {
            model.addAttribute("adjustBillForm", adjustBillService.getAdjustBill(adjustBillForm));
        } else {
            model.addAttribute("adjustBillForm", adjustBillForm);
        }
        return "modules/" + "charge/finance/adjustBillForm";
    }
    
    @RequiresPermissions("charge:finance:adjustBill:edit")
    @RequestMapping(value = "save")
    public String save(AdjustBillForm adjustBillForm, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, adjustBillForm)) {
            return form(adjustBillForm, model);
        }
        adjustBillService.insertOrUpdateAdjustBill(adjustBillForm);
        addMessage(redirectAttributes, "保存调账信息成功");
        return "redirect:" + Global.getAdminPath() + "/charge/finance/adjustBill/?repage";
    }

    @RequiresPermissions("charge:finance:adjustBill:edit")
    @RequestMapping(value = "delete")
    public String delete(AdjustBillForm adjustBillForm, RedirectAttributes redirectAttributes) {
        adjustBillService.deleteAdjustBill(adjustBillForm);
        addMessage(redirectAttributes, "删除调账信息成功");
        return "redirect:" + Global.getAdminPath() + "/charge/finance/adjustBill/?repage";
    }

}
