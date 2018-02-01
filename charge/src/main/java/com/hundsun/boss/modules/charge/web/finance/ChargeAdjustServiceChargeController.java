package com.hundsun.boss.modules.charge.web.finance;

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
import com.hundsun.boss.common.utils.FormUtils;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.entity.finance.ChargeAdjustServiceCharge;
import com.hundsun.boss.modules.charge.form.finance.ChargeAdjustServiceChargeForm;
import com.hundsun.boss.modules.charge.service.finance.ChargeAdjustServiceChargeService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 技术服务费调账Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/finance/chargeAdjustServiceCharge")
public class ChargeAdjustServiceChargeController extends BaseController {

    @Autowired
    private ChargeAdjustServiceChargeService chargeAdjustServiceChargeService;
    @Autowired
    private OfficeService officeService;

    @ModelAttribute
    public ChargeAdjustServiceCharge get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return chargeAdjustServiceChargeService.get(id);
        } else {
            return new ChargeAdjustServiceCharge();
        }
    }

    @RequiresPermissions("charge:finance:chargeAdjustServiceCharge:view")
    @RequestMapping(value = { "list", "" })
    public String list(ChargeAdjustServiceChargeForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            form.setCreateBy(user.getId());
        }
        Page<ChargeAdjustServiceCharge> page = chargeAdjustServiceChargeService.find(new Page<ChargeAdjustServiceCharge>(request, response), form);
        model.addAttribute("page", page);
        model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
        return "modules/" + "charge/finance/chargeAdjustServiceChargeList";
    }

    @RequiresPermissions("charge:finance:chargeAdjustServiceCharge:view")
    @RequestMapping(value = "form")
    public String form(ChargeAdjustServiceChargeForm form, Model model, RedirectAttributes redirectAttributes) {
        ChargeAdjustServiceCharge chargeAdjustServiceCharge = null;
        try {
            // 新增页面初始化
            if (form.getId() == null) {
                model.addAttribute("chargeAdjustServiceChargeForm", form);
            }
            // 更新页面初始化
            else if (!CommonUtil.isNullorEmpty(form.getId()) && CommonUtil.isNullorEmpty(form.getUpdateDate())) {
                chargeAdjustServiceCharge = chargeAdjustServiceChargeService.get(form.getId());
                FormUtils.setPropertyValue(form, chargeAdjustServiceCharge);
                form.setUpdateDate(chargeAdjustServiceCharge.getUpdateDate());
                form.setRemarks(chargeAdjustServiceCharge.getRemarks());
                model.addAttribute("chargeAdjustServiceChargeForm", form);
            }
            // 如果校验出错
            else {
                model.addAttribute("chargeAdjustServiceChargeForm", form);
            }
            model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
            return "modules/" + "charge/finance/chargeAdjustServiceChargeForm";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "技术服务费调账显示失败");
            model.addAttribute("chargeAdjustServiceChargeForm", form);
            model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
            return "modules/" + "charge/finance/chargeAdjustServiceChargeForm";
        }

    }

    @RequiresPermissions("charge:finance:chargeAdjustServiceCharge:edit")
    @RequestMapping(value = "save")
    public String save(ChargeAdjustServiceChargeForm form, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (!beanValidator(model, form)) {
                return form(form, model, redirectAttributes);
            }
            ChargeAdjustServiceCharge chargeAdjustServiceCharge = new ChargeAdjustServiceCharge();
            // 如果是更新，获取最新的信息
            if (!CommonUtil.isNullorEmpty(form.getId())) {
                chargeAdjustServiceCharge = chargeAdjustServiceChargeService.get(form.getId());

                // 如果有人更新内容，不允许修改
                if (!chargeAdjustServiceCharge.getUpdateDate().equals(form.getUpdateDate())) {
                    addMessage(model, "验证失败：<br/>技术服务费调账内容已经被人修改，请确认");
                    return form(form, model, redirectAttributes);
                }
            }
            // 将表单对象更新入持久曾
            FormUtils.margeFormToEntity(form, chargeAdjustServiceCharge);
            chargeAdjustServiceCharge.setRemarks(form.getRemarks());
            chargeAdjustServiceChargeService.save(chargeAdjustServiceCharge);
            addMessage(redirectAttributes, "保存技术服务费调账成功");
            return "redirect:" + Global.getAdminPath() + "/charge/finance/chargeAdjustServiceCharge/?repage";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "保存技术服务费调账失败");
            model.addAttribute("chargeAdjustServiceChargeForm", form);
            model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
            return "modules/" + "charge/finance/chargeAdjustServiceChargeForm";
        }

    }

    @RequiresPermissions("charge:finance:chargeAdjustServiceCharge:edit")
    @RequestMapping(value = "delete")
    public String delete(String id, RedirectAttributes redirectAttributes) {
        chargeAdjustServiceChargeService.delete(id);
        addMessage(redirectAttributes, "删除技术服务费调账成功");
        return "redirect:" + Global.getAdminPath() + "/charge/finance/chargeAdjustServiceCharge/?repage";
    }
}
