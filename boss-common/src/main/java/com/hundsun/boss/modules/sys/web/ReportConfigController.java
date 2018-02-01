package com.hundsun.boss.modules.sys.web;

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
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.ReportConfigService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 导出配置Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/setting/reportConfig")
public class ReportConfigController extends BaseController {

    @Autowired
    private ReportConfigService reportConfigService;

    @ModelAttribute
    public ReportConfig get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return reportConfigService.get(id);
        } else {
            return new ReportConfig();
        }
    }

    @RequiresPermissions("sys:setting:reportConfig:view")
    @RequestMapping(value = { "list", "" })
    public String list(ReportConfig reportConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            reportConfig.setCreateBy(user);
        }
        Page<ReportConfig> page = reportConfigService.find(new Page<ReportConfig>(request, response), reportConfig);
        model.addAttribute("page", page);
        return "modules/" + "sys/setting/reportConfigList";
    }

    @RequiresPermissions("sys:setting:reportConfig:view")
    @RequestMapping(value = "form")
    public String form(ReportConfig reportConfig, Model model) {
        model.addAttribute("reportConfig", reportConfig);
        return "modules/" + "sys/setting/reportConfigForm";
    }

    @RequiresPermissions("sys:setting:reportConfig:view")
    @RequestMapping(value = "reportContentForm")
    public String reportContentForm(ReportConfig reportConfig, Model model) {
        model.addAttribute("reportConfig", reportConfig);
        return "modules/" + "sys/setting/reportContentForm";
    }

    @RequiresPermissions("sys:setting:reportConfig:edit")
    @RequestMapping(value = "save")
    public String save(ReportConfig reportConfig, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, reportConfig)) {
            return form(reportConfig, model);
        }
        reportConfigService.save(reportConfig);
        addMessage(redirectAttributes, "保存导出配置成功");
        return "redirect:" + Global.getAdminPath() + "/sys/setting/reportConfig/?repage";
    }

    @RequiresPermissions("sys:setting:reportConfig:edit")
    @RequestMapping(value = "delete")
    public String delete(String id, RedirectAttributes redirectAttributes) {
        reportConfigService.delete(id);
        addMessage(redirectAttributes, "删除导出配置成功");
        return "redirect:" + Global.getAdminPath() + "/sys/setting/reportConfig/?repage";
    }

}
