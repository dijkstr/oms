package com.hundsun.boss.modules.charge.web.mail;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
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
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.common.mail.Mail;
import com.hundsun.boss.common.mail.MailConfig;
import com.hundsun.boss.common.mail.MailSender;
import com.hundsun.boss.common.report.ReportUtils;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.FormUtils;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.common.utils.NumberToCN;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.entity.bill.OrderRelation;
import com.hundsun.boss.modules.charge.entity.mail.ContactMail;
import com.hundsun.boss.modules.charge.entity.order.OrderInfo;
import com.hundsun.boss.modules.charge.entity.sync.SyncContract;
import com.hundsun.boss.modules.charge.form.bill.ChargeOrderInfo;
import com.hundsun.boss.modules.charge.form.finance.FinSummaryForm;
import com.hundsun.boss.modules.charge.form.mail.ContactMailForm;
import com.hundsun.boss.modules.charge.service.bill.ChargeBillService;
import com.hundsun.boss.modules.charge.service.bill.RelationService;
import com.hundsun.boss.modules.charge.service.finance.PayableService;
import com.hundsun.boss.modules.charge.service.mail.ContactMailService;
import com.hundsun.boss.modules.charge.service.order.OrderInfoService;
import com.hundsun.boss.modules.charge.service.receipt.ChargeReceiptService;
import com.hundsun.boss.modules.charge.service.sync.SyncContractService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.MailConfigService;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.service.SysConfigService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 邮件履历Controller
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Controller
@RequestMapping(value = "${adminPath}/charge/mail/contactMail")
public class ContactMailController extends BaseController {
    @Autowired
    private RelationService relationService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private MailConfigService mailConfigService;

    @Autowired
    private PayableService payableService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private SyncContractService syncContractService;

    @Autowired
    private ChargeBillService chargeBillService;

    @Autowired
    private ContactMailService contactMailService;

    @Autowired
    private ChargeReceiptService chargeReceiptService;

    @ModelAttribute
    public ContactMail get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return contactMailService.get(id);
        } else {
            return new ContactMail();
        }
    }

    @RequiresPermissions("charge:mail:contactMail:view")
    @RequestMapping(value = { "list", "" })
    public String list(ContactMailForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            form.setCreateBy(user.getCreateBy().getId());
        }
        Page<ContactMail> page = contactMailService.find(new Page<ContactMail>(request, response), form);
        model.addAttribute("page", page);
        model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
        return "modules/" + "charge/mail/contactMailList";
    }

    @RequiresPermissions("charge:mail:contactMail:view")
    @RequestMapping(value = "form")
    public String form(ContactMailForm form, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (!CommonUtil.isNullorEmpty(form.getId())) {
                ContactMail contactMail = get(form.getId());
                FormUtils.setPropertyValue(form, contactMail);
            }
            model.addAttribute("contactMailForm", form);
            model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "显示通信履历失败");
            model.addAttribute("contactSettingForm", form);
        }
        return "modules/" + "charge/mail/contactMailForm";
    }

    @RequiresPermissions("charge:mail:contactMail:view")
    @RequestMapping(value = "delete")
    public String delete(String id, RedirectAttributes redirectAttributes) {
        contactMailService.delete(id);
        addMessage(redirectAttributes, "删除邮件履历成功");
        return "redirect:" + Global.getAdminPath() + "/charge/mail/contactMail/?repage";
    }

    /**
     * 邮件发送器
     * 
     * @param form
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequiresPermissions("charge:mail:contactMail:view")
    @RequestMapping(value = { "mail" })
    public String contactMailChooser(String contract_id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        Map params = new HashMap();
        ContactMailForm form = new ContactMailForm();
        User currentUser = UserUtils.getUser();
        params.put("dept", BaseService.getDept(currentUser, "office", ""));
        params.put("mail_type", "notify");
        model.addAttribute("mailConfigs", ReportUtils.getMailConfig(params, sysConfigService));
        OrderRelation orderRelation = relationService.getByContract_id(contract_id);
        model.addAttribute("orderRelation", orderRelation);
        BeanUtils.copyProperties(form, orderRelation);
        // 默认重要度:普通
        form.setImportance("3");
        // 默认不需回执
        form.setIs_read("0");
        model.addAttribute("contactMailForm", form);
        model.addAttribute("office", officeService.findByCode(orderRelation.getOffice_id()));
        return "modules/" + "charge/mail/contactMailSend";
    }

    /**
     * 显示邮件内容
     * 
     * @param mail_config_id
     * @param contract_setting_id
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "content")
    public @ResponseBody MailConfig content(String mail_config_id, String contract_id, Model model) throws Exception {

        // 邮件通知管理对象
        OrderRelation orderRelation = relationService.getByContract_id(contract_id);
        Map map = new HashMap<String, String>();
        map.put("id", mail_config_id);
        MailConfig mailConfig = ReportUtils.getMailConfig(map, sysConfigService).get(0);
        // 补足邮件内容
        Map varibles = getMailVaribles(orderRelation);
        mailConfig.setMail_subject(CommonUtil.injectString(mailConfig.getMail_subject(), varibles));
        mailConfig.setMail_content(CommonUtil.injectString(mailConfig.getMail_content(), varibles));
        return mailConfig;
    }

    /**
     * 生成邮件填充内容
     * 
     * @param contactSetting
     * @return
     * @throws Exception
     */
    private Map getMailVaribles(OrderRelation orderRelation) throws Exception {
        // 注入用变量集合
        Map varibles = new HashMap();
        // 通用参数
        Map params = new HashMap();
        // 当前月
        params.put("now_month", Formatter.formatDate(Formatter.DATE_FORMAT4, new Date()));
        // 协同合同号
        String contractId = orderRelation.getContract_id();
        params.put("contract_id", contractId);

        // 计费合同
        OrderInfo orderInfo = orderInfoService.findByContractId(contractId).get(0);
        // 协同合同
        SyncContract syncContract = syncContractService.getByContractid(contractId);

        // 与计费结果无关的信息
        // 客户名称
        varibles.put("custname", orderRelation.getUser_name());
        // 签约日期
        varibles.put("signeddate", formatDate(syncContract.getSigneddate()));
        // 协同合同编号
        varibles.put("contractid", orderRelation.getContract_id());

        // 保底金额
        varibles.put("min_charge", Formatter.formatDecimal(Formatter.DECIMAL_FORMAT6, orderInfoService.queryOrderMinCharge(params)));
        varibles.put("min_charge_upper", NumberToCN.number2CNMontrayUnit(orderInfoService.queryOrderMinCharge(params), 2));
        // 计费开始日期
        varibles.put("order_begin_date", formatDate(orderInfo.getOrder_begin_date()));
        // 今天日期
        varibles.put("today", Formatter.formatDate(Formatter.DATE_FORMAT2, new Date()));

        String totalBankReceipt = chargeReceiptService.getReceiptByContractId(contractId);
        // 到款金额
        varibles.put("current_bankreceipt", Formatter.formatDecimal(Formatter.DECIMAL_FORMAT6, Double.valueOf(totalBankReceipt)));
        varibles.put("current_bankreceipt_upper", NumberToCN.number2CNMontrayUnit(Double.valueOf(totalBankReceipt), 2));
        // 原合同将于20**年**月**日到期
        varibles.put("order_end_date", formatDate(orderInfo.getOrder_end_date()));

        // 与账单相关的计费信息
        Map<String, String> maxBillInfo = chargeBillService.getMaxBillInfo(params);
        // 如果已经计费出账过，生成与计费结果有关的注入值
        if (!CommonUtil.isNullorEmpty(maxBillInfo.get("bill_id"))) {
            ChargeOrderInfo chargeOrderInfoParam = new ChargeOrderInfo();
            chargeOrderInfoParam.setBill_id((String) maxBillInfo.get("bill_id"));

            // 与计费结果有关的信息
            ChargeOrderInfo chargeOrderInfo = chargeBillService.getChargeOrderInfo(chargeOrderInfoParam);
            // 技术服务费
            Double total_service_charge = Double.valueOf(chargeOrderInfo.getTotal_service_charge()) + Double.valueOf(chargeOrderInfo.getTotal_advance_charge());
            varibles.put("total_service_charge", Formatter.formatDecimal(Formatter.DECIMAL_FORMAT6, total_service_charge));
            varibles.put("total_service_charge_upper", NumberToCN.number2CNMontrayUnit(total_service_charge, 2));
            // 累计计费天数
            varibles.put("charge_date_count", maxBillInfo.get("charge_date_count"));

            // 应付信息
            FinSummaryForm finSummaryForm = new FinSummaryForm();
            finSummaryForm.setContract_id(contractId);
            finSummaryForm.setCharge_month(Formatter.formatDate(Formatter.DATE_FORMAT4, new Date()));
            Map payable = payableService.exportPayableList(finSummaryForm).get(0);
            // 应付
            varibles.put("payable", Formatter.formatDecimal(Formatter.DECIMAL_FORMAT6, payable.get("current_pay")));
            varibles.put("payable_upper", NumberToCN.number2CNMontrayUnit((BigDecimal) (payable.get("current_pay")), 2));
        } else {
            varibles.put("total_service_charge", "0.00");
            varibles.put("total_service_charge_upper", NumberToCN.CN_ZEOR_FULL);
            varibles.put("charge_date_count", "");
            varibles.put("payable", "");
            varibles.put("payable_upper", NumberToCN.CN_ZEOR_FULL);
        }

        return varibles;
    }

    /**
     * 将日期转换为年月日
     * 
     * @param orgDate
     * @return
     * @throws Exception
     */
    private static String formatDate(String orgDate) throws Exception {
        Date date = (Date) Formatter.parseUtilDate(Formatter.DATE_FORMAT1, orgDate);
        String dateString = Formatter.formatDate(Formatter.DATE_FORMAT2, date);
        return dateString;
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
    @RequiresPermissions("charge:mail:contactMail:view")
    @RequestMapping(value = "/download")
    public @ResponseBody Map<String, String> pdfDownload(String subject, String html, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        try {
            // 生成下载文件名称
            String zipFileName = subject + Formatter.formatDate(Formatter.TIME_FORMAT2, new Date());

            StringBuffer sbPDF = new StringBuffer();
            sbPDF.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;\" /><style type=\"text/css\">");
            sbPDF.append(CommonUtil.readReportCSS("a4portrait.css"));
            sbPDF.append(CommonUtil.readReportCSS("acclayout.css"));
            sbPDF.append("</style></head><body><div class=\"order_gb\"><div class=\"content\" style=\"width:670px;text-align:center;line-height:100px\">");
            sbPDF.append(subject);
            sbPDF.append("</div><div class=\"content\" style=\"width:670px\">");
            sbPDF.append(HtmlUtils.htmlUnescape(html));
            sbPDF.append("</div></div></body></html>");

            generatePDFDownload(sbPDF.toString(), request, response, zipFileName);
        } catch (Exception e) {
            logger.error("error:", e);
            CommonUtil.exceptionHandler(data, e);
        } finally {
        }
        return data;
    }

    /**
     * 邮件发送通知.
     * 
     * @param downloadForm
     * @param request
     * @return
     */
    @RequiresPermissions("charge:mail:contactMail:view")
    @RequestMapping(value = "/sendMail")
    public String sendBill(ContactMailForm form, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
        String pdfFilePath = "";
        try {
            // 发送邮件
            String testEmail = "";
            String email = form.getEmail();
            String cc = form.getCc();
            String bcc = form.getBcc();

            String isTest = sysConfigService.getValue("bos.mail.istest");
            if (CommonUtil.isNullorEmpty(isTest) || isTest.equals("0")) {
                testEmail = sysConfigService.getValue("bos.mail.testmailto");
                cc = "";
                bcc = "";
            }

            // 发送邮件属性
            Map map = new HashMap<String, String>();
            map.put("id", form.getMail_config_id());
            MailConfig mailConfig = ReportUtils.getMailConfig(map, sysConfigService).get(0);
            MailSender mailSender = new MailSender(mailConfig);
            Mail mail = new Mail();
            mail.setSubject(form.getMail_subject());
            mail.setImportance(form.getImportance());
            mail.setIs_read(form.getIs_read());

            Document document = Jsoup.parse(HtmlUtils.htmlUnescape(form.getMail_content()));
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            document.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
            mail.setContent(document.html());
            mail.addSenders(mailConfig.getSmtpsender());
            mail.addCCs(cc);
            mail.addBCCs(bcc);
            mail.addRecipients(email);

            // 邮件通知管理对象
            OrderRelation orderRelation = relationService.get(form.getId());
            // 补足邮件内容
            Map varibles = getMailVaribles(orderRelation);
            mail.injectVarible(varibles);
            // 生成邮件附件
            StringBuffer sbPDF = new StringBuffer();
            sbPDF.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;\" /><style type=\"text/css\">");
            sbPDF.append(CommonUtil.readReportCSS("a4portrait.css"));
            sbPDF.append(CommonUtil.readReportCSS("acclayout.css"));
            sbPDF.append("</style></head><body><div class=\"order_gb\"><div class=\"content\" style=\"text-align:center;\">");
            sbPDF.append(form.getMail_subject());
            sbPDF.append("</div><br><br><div class=\"content\" style=\"width:670px\">");
            sbPDF.append(HtmlUtils.htmlUnescape(form.getMail_content()));
            sbPDF.append("</div></div></body></html>");
            pdfFilePath = generatePDF(sbPDF.toString(), request.getSession().getServletContext().getRealPath("/"), form.getMail_subject());

            mail.addAttachment(pdfFilePath);
            mailSender.sendMail(mail, isTest, testEmail);

            // 纪录发信履历
            ContactMail contactMail = new ContactMail();
            contactMail.setCc(form.getCc());
            contactMail.setBcc(form.getBcc());
            contactMail.setEmail(form.getEmail());
            contactMail.setOffice_id(orderRelation.getOffice_id());
            contactMail.setContract_id(orderRelation.getContract_id());
            contactMail.setCustomer_id(orderRelation.getCustomer_id());
            contactMail.setUser_name(orderRelation.getUser_name());
            contactMail.setMail_subject(form.getMail_subject());
            contactMail.setMail_content(form.getMail_content());
            contactMailService.save(contactMail);
            addMessage(redirectAttributes, "邮件发送成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "邮件发送失败");
        }
        // 删除下载附件
        File pdfFile = new File(pdfFilePath);
        if (pdfFile.exists()) {
            CommonUtil.deleteAllFilesOfDir(pdfFile);
        }
        return "redirect:" + Global.getAdminPath() + "/charge/order/orderInfo/?repage";
    }
}
