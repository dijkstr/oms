package com.hundsun.boss.modules.charge.web.setting;

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

import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.sys.entity.Office;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.utils.UserUtils;
import com.hundsun.boss.modules.charge.entity.setting.MailConfig;
import com.hundsun.boss.modules.charge.service.setting.MailConfigService;

/**
 * 邮件配置Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/setting/mailConfig")
public class MailConfigController extends BaseController {

    @Autowired
    private MailConfigService mailConfigService;
    @Autowired
    private OfficeService officeService;

    @ModelAttribute
    public MailConfig get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return mailConfigService.get(id);
        } else {
            return new MailConfig();
        }
    }

    @RequiresPermissions("charge:setting:mailConfig:view")
    @RequestMapping(value = { "list", "" })
    public String list(MailConfig mailConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            mailConfig.setCreateBy(user);
        }
        Page<MailConfig> page = mailConfigService.find(new Page<MailConfig>(request, response), mailConfig);
        model.addAttribute("page", page);
        Office office = new Office();
        office.setName(mailConfig.getDepartment_name());
        office.setId(mailConfig.getOffice_id());
        model.addAttribute("office", office);
        return "modules/" + "charge/setting/mailConfigList";
    }

    @RequiresPermissions("charge:setting:mailConfig:view")
    @RequestMapping(value = "form")
    public String form(MailConfig mailConfig, Model model) {
        model.addAttribute("mailConfig", mailConfig);
        model.addAttribute("office", officeService.findByCode(mailConfig.getOffice_id()));
        return "modules/" + "charge/setting/mailConfigForm";
    }

    @RequiresPermissions("charge:setting:mailConfig:view")
    @RequestMapping(value = "mailContentForm")
    public String mailContentForm(MailConfig mailConfig, Model model) {
        model.addAttribute("mailConfig", mailConfig);
        return "modules/" + "charge/setting/mailContentForm";
    }

    @RequiresPermissions("charge:setting:mailConfig:edit")
    @RequestMapping(value = "save")
    public String save(MailConfig mailConfig, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, mailConfig)) {
            return form(mailConfig, model);
        }
        mailConfigService.save(mailConfig);
        addMessage(redirectAttributes, "保存邮件配置'" + mailConfig.getDepartment_name() + "'成功");
        return "redirect:" + Global.getAdminPath() + "/charge/setting/mailConfig/?repage";
    }

    @RequiresPermissions("charge:setting:mailConfig:edit")
    @RequestMapping(value = "delete")
    public String delete(String id, RedirectAttributes redirectAttributes) {
        mailConfigService.delete(id);
        addMessage(redirectAttributes, "删除邮件配置成功");
        return "redirect:" + Global.getAdminPath() + "/charge/setting/mailConfig/?repage";
    }

}
