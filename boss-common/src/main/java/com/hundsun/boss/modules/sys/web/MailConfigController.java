package com.hundsun.boss.modules.sys.web;

import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.common.report.ReportUtils;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.sys.entity.MailConfig;
import com.hundsun.boss.modules.sys.entity.Office;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.MailConfigService;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.service.SysConfigService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 邮件配置Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/setting/mailConfig")
public class MailConfigController extends BaseController {

    @Autowired
    private MailConfigService mailConfigService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private SysConfigService sysConfigService;

    @ModelAttribute
    public MailConfig get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return mailConfigService.get(id);
        } else {
            return new MailConfig();
        }
    }

    @RequiresPermissions("sys:setting:mailConfig:view")
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
        return "modules/" + "sys/setting/mailConfigList";
    }

    @RequiresPermissions("sys:setting:mailConfig:view")
    @RequestMapping(value = "form")
    public String form(MailConfig mailConfig, Model model) {
        model.addAttribute("mailConfig", mailConfig);
        model.addAttribute("office", officeService.findByCode(mailConfig.getOffice_id()));
        return "modules/" + "sys/setting/mailConfigForm";
    }

    @RequiresPermissions("sys:setting:mailConfig:view")
    @RequestMapping(value = "mailContentForm")
    public String mailContentForm(MailConfig mailConfig, Model model) {
        model.addAttribute("mailConfig", mailConfig);
        return "modules/" + "sys/setting/mailContentForm";
    }

    @RequiresPermissions("sys:setting:mailConfig:edit")
    @RequestMapping(value = "save")
    public String save(MailConfig mailConfig, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (!beanValidator(model, mailConfig)) {
            return form(mailConfig, model);
        }
        mailConfigService.save(mailConfig);
        addMessage(redirectAttributes, "保存邮件配置" + mailConfig.getTemplate() + "'成功");
        return "redirect:" + Global.getAdminPath() + "/sys/setting/mailConfig/?repage";
    }

    @RequiresPermissions("sys:setting:mailConfig:edit")
    @RequestMapping(value = "delete")
    public String delete(String id, RedirectAttributes redirectAttributes) {
        mailConfigService.delete(id);
        addMessage(redirectAttributes, "删除邮件配置成功");
        return "redirect:" + Global.getAdminPath() + "/sys/setting/mailConfig/?repage";
    }

    /**
     * 显示邮件内容
     * 
     * @param mail_config_id
     * @param model
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "content")
    public @ResponseBody com.hundsun.boss.common.mail.MailConfig content(String mail_config_id) throws Exception {
        Map map = new HashMap<String, String>();
        map.put("id", mail_config_id);
        com.hundsun.boss.common.mail.MailConfig mailConfig = ReportUtils.getMailConfig(map, sysConfigService).get(0);
        return mailConfig;
    }

    /**
     * 生成pdf下载
     * 
     * @param templatekey
     * @param chargeBillSearchForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequiresPermissions("sys:setting:mailConfig:edit")
    @RequestMapping(value = "/download")
    public @ResponseBody Map<String, String> pdfDownload(String fileName, String html, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        try {
            // 生成下载文件名称
            String zipFileName = fileName + Formatter.formatDate(Formatter.TIME_FORMAT2, new Date());

            StringBuffer htmlsb = new StringBuffer();
            htmlsb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;\" /><style type=\"text/css\">");
            htmlsb.append(CommonUtil.readReportCSS("a4portrait.css"));
            htmlsb.append(CommonUtil.readReportCSS("acclayout.css"));
            htmlsb.append("</style></head><body><div class=\"order_gb\"><div class=\"content\" style=\"width:670px\">");
            htmlsb.append(HtmlUtils.htmlUnescape(html));
            htmlsb.append("</div></div></body></html>");

            generatePDFDownload(htmlsb.toString(), request, response, zipFileName);
        } catch (Exception e) {
            logger.error("error:", e);
            CommonUtil.exceptionHandler(data, e);
        } finally {
        }
        return data;
    }
}
