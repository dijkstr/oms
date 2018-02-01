package com.hundsun.boss.modules.charge.web.bill;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeBodyPart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.exception.ServiceException;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.mail.Mail;
import com.hundsun.boss.common.mail.MailConfig;
import com.hundsun.boss.common.mail.MailSender;
import com.hundsun.boss.common.report.PDFUtil;
import com.hundsun.boss.common.report.ReportUtils;
import com.hundsun.boss.common.report.XlsxUtil;
import com.hundsun.boss.common.report.ZipCompressUtil;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.modules.charge.common.ChargeConstant;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillForm;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillInfo;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillSearchForm;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.service.bill.ChargeBillDetailService;
import com.hundsun.boss.modules.charge.service.bill.ChargeBillService;
import com.hundsun.boss.modules.charge.service.export.IReportService;
import com.hundsun.boss.modules.charge.service.order.OrderInfoService;
import com.hundsun.boss.modules.charge.thread.ChargeOrderThread;
import com.hundsun.boss.modules.charge.thread.GenerateBillThread;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.service.SysConfigService;

/**
 * 账单Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/bill/chargeBill")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChargeBillController extends BaseController {

    @Autowired
    private ChargeBillService chargeBillService;

    @Autowired
    private ChargeBillDetailService chargeBillDetailService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private OrderInfoService orderInfoService;

    @RequiresPermissions("charge:bill:chargeBill:view")
    @RequestMapping(value = { "list", "" })
    public String list(ChargeBillSearchForm chargeBillSearchForm, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page defaultpage = new Page<ChargeBillForm>(request, response);
        chargeBillSearchForm.setPageNo(defaultpage.getPageNo());
        chargeBillSearchForm.setPageSize(defaultpage.getPageSize());
        Page<ChargeBillForm> page = chargeBillService.queryChargeBillList(defaultpage, chargeBillSearchForm);
        model.addAttribute("page", page);
        model.addAttribute("chargeBillSearchForm", chargeBillSearchForm);
        model.addAttribute("office", officeService.findByCode(chargeBillSearchForm.getOffice_id()));
        return "modules/" + "charge/bill/chargeBillList";
    }

    //计费
    @RequestMapping(value = "/chargeOrder")
    public @ResponseBody Map<String, String> chargeOrder(ChargeBillSearchForm chargeBillSearchForm, HttpServletRequest request) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        Map progress = chargeBillService.getChargeOrderProgress();
        if (!CommonUtil.isNullorEmpty(progress) && "1".equals(progress.get(ChargeConstant.CHARGE_ORDER_PROGRAM_STATUS))) {
            data.put("result", "success");
            data.put("wait", "wait");
            return data;
        }
        chargeBillService.resetChargeOrderStatus();

        ChargeOrderThread thread = new ChargeOrderThread(chargeBillSearchForm, chargeBillService);
        thread.start();
        data.put("result", "success");
        return data;

    }

    /**
     * 出账
     * 
     * @param chargeBillSearchForm
     * @return
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalAccessException @
     */
    @RequestMapping(value = "/generateBill")
    public @ResponseBody Map<String, String> generateBill(ChargeBillSearchForm chargeBillSearchForm, HttpServletRequest request) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        Map progress = chargeBillService.getGenerateBillProgress();
        if (!CommonUtil.isNullorEmpty(progress) && "1".equals(progress.get(ChargeConstant.CHARGE_ORDER_PROGRAM_STATUS))) {
            data.put("result", "success");
            data.put("wait", "wait");
            return data;
        }
        chargeBillService.resetChargeOrderStatus();
        chargeBillSearchForm.setPageSize(-1);
        GenerateBillThread thread = new GenerateBillThread(chargeBillSearchForm, chargeBillService);
        thread.start();
        data.put("result", "success");
        return data;
    }

    /**
     * 获取计费进度
     * 
     * @return
     */
    @RequestMapping(value = "/getChargeOrderProgress")
    public @ResponseBody Map<String, Object> getChargeOrderProgress() {

        Map<String, Object> data = new HashMap<String, Object>();
        // status:1--正在处理，9--处理终了，-1--异常
        // msg:"开始处理"，"处理终了"，"系统异常，请联系管理员"
        try {
            Map map = chargeBillService.getChargeOrderProgress();
            if ("1".equals(map.get(ChargeConstant.CHARGE_ORDER_PROGRAM_STATUS))) {
                data.put("contant", map.get(ChargeConstant.CHARGE_ORDER_PROGRESS_CONTENT));
                data.put("percent", map.get(ChargeConstant.CHARGE_ORDER_PROGRESS_PERCENT));
                data.put("status", "1");
            } else if ("9".equals(map.get(ChargeConstant.CHARGE_ORDER_PROGRAM_STATUS))) {
                data.put("status", "9");
                data.put("msg", "计费成功");
            } else if ("-1".equals(map.get(ChargeConstant.CHARGE_ORDER_PROGRAM_STATUS))) {
                data.put("status", "-1");
                data.put("msg", "系统异常，请联系管理员");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            data.put("status", "-1");
            data.put("msg", "系统异常，请联系管理员");
        }
        return data;
    }

    /**
     * 获取出账进度
     * 
     * @return
     */
    @RequestMapping(value = "/getGenerateBillProgress")
    public @ResponseBody Map<String, Object> getGenerateBillProgress() {

        Map<String, Object> data = new HashMap<String, Object>();
        // status:1--正在处理，9--处理终了，-1--异常
        // msg:"开始处理"，"处理终了"，"系统异常，请联系管理员"
        try {
            Map map = chargeBillService.getGenerateBillProgress();
            if ("1".equals(map.get(ChargeConstant.CHARGE_ORDER_PROGRAM_STATUS))) {
                data.put("contant", map.get(ChargeConstant.CHARGE_ORDER_PROGRESS_CONTENT));
                data.put("percent", map.get(ChargeConstant.CHARGE_ORDER_PROGRESS_PERCENT));
                data.put("status", "1");
            } else if ("9".equals(map.get(ChargeConstant.CHARGE_ORDER_PROGRAM_STATUS))) {
                data.put("status", "9");
                data.put("msg", "出账成功");
            } else if ("-1".equals(map.get(ChargeConstant.CHARGE_ORDER_PROGRAM_STATUS))) {
                data.put("status", "-1");
                data.put("msg", "系统异常，请联系管理员");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            data.put("status", "-1");
            data.put("msg", "系统异常，请联系管理员");
        }
        return data;
    }

    /**
     * 账单详情
     * 
     * @param templatekey
     * @param downloadForm
     * @param model
     * @param viewType
     * @param request
     * @return
     */
    @RequiresPermissions("charge:bill:chargeBill:view")
    @RequestMapping(value = "/viewPDFDetail")
    public String pdfDownload(String templatekey, DownloadForm downloadForm, Model model, String viewType, HttpServletRequest request) {
        try {
            ChargeBillSearchForm chargeBillSearchForm = new ChargeBillSearchForm();
            chargeBillSearchForm.setBill_id(downloadForm.getBill_id());
            templatekey = chargeBillService.getBillAccount(chargeBillSearchForm).getOffice_id();
            // 生成下载用文件预览
            ReportConfig reportConfig = sysConfigService.getReportCongfig(templatekey);
            String html = ((IReportService) getApplicationContext().getBean(reportConfig.getService())).generateReportHTML(reportConfig, downloadForm);
            model.addAttribute("html", html);
        } catch (ServiceException e) {
            if ("403".equals(e.getMessage())) {
                return "error/403";
            } else {
                return "error/500";
            }
        } catch (Exception e) {
            logger.error("error:", e);
            return "error/500";
        } finally {
        }
        return "modules/" + "charge/bill/viewPDFDetail";
    }

    /**
     * 账单审核
     * 
     * @param templatekey
     * @param downloadForm
     * @param model
     * @param viewType
     * @param request
     * @return
     */
    @RequiresPermissions("charge:bill:chargeBill:edit")
    @RequestMapping(value = "/checkView")
    public String checkView(String templatekey, DownloadForm downloadForm, Model model, String viewType, HttpServletRequest request) {
        try {
            ChargeBillSearchForm chargeBillSearchForm = new ChargeBillSearchForm();
            chargeBillSearchForm.setBill_id(downloadForm.getBill_id());
            ChargeBillForm form = new ChargeBillForm();
            form = chargeBillService.getBillAccount(chargeBillSearchForm);
            templatekey = form.getOffice_id();
            // 生成下载用文件预览
            ReportConfig reportConfig = sysConfigService.getReportCongfig(templatekey);
            String html = ((IReportService) getApplicationContext().getBean(reportConfig.getService())).generateReportHTML(reportConfig, downloadForm);
            model.addAttribute("html", html);
            model.addAttribute("bill_id", downloadForm.getBill_id());
            model.addAttribute("cc_flag", form.getCc_flag());
        } catch (ServiceException e) {
            if ("403".equals(e.getMessage())) {
                return "error/403";
            } else {
                return "error/500";
            }
        } catch (Exception e) {
            logger.error("error:", e);
            return "error/500";
        } finally {
        }
        return "modules/" + "charge/bill/checkView";
    }

    /**
     * 邮件发送账单.
     * 
     * @param downloadForm
     * @param request
     * @return
     */
    @RequestMapping(value = "/sendBill")
    public @ResponseBody Map<String, String> sendBill(DownloadForm downloadForm, HttpServletRequest request) {
        Map<String, String> data = new HashMap<String, String>();
        String billIdStr = downloadForm.getBill_id();

        String[] billIdArray = billIdStr.split(",");
        try {
            // 发送邮件
            for (String billId : billIdArray) {
                String templatekey = "";
                ChargeBillSearchForm form = new ChargeBillSearchForm();
                form.setBill_id(billId);
                ChargeBillForm billAccount = chargeBillService.getBillAccount(form);
                if (CommonUtil.isNullorEmpty(billAccount)) {
                    data.put("result", "fail");
                    data.put("message", "账单：" + billId + "不存在，请确认。");
                    return data;
                }
                if (billAccount.getIs_send().equals("0")) {
                    data.put("result", "fail");
                    data.put("message", "合同：" + billAccount.getContract_id() + "不允许发送邮件，请确认。");
                    return data;
                }
                if (billAccount.getCheck_status().equals("0")) {
                    data.put("result", "fail");
                    data.put("message", "账单：" + billId + "未审核通过，请确认。");
                    return data;
                }

                String email = "";
                String cc = "";
                String bcc = "";
                // 获取订单关联信息中的客户邮件
                Map mParam = new HashMap();
                mParam.put("contract_id", billAccount.getContract_id());
                List<Map> emailList = orderInfoService.queryOrderRelationList(mParam);
                if (!CommonUtil.isNullorEmpty(emailList)) {
                    for (Map orderRelation : emailList) {
                        email += orderRelation.get("email").toString().trim() + ",";
                        if (!CommonUtil.isNullorEmpty(orderRelation.get("cc"))) {
                            cc += orderRelation.get("cc").toString().trim() + ",";
                        }
                        if (!CommonUtil.isNullorEmpty(orderRelation.get("bcc"))) {
                            bcc += orderRelation.get("bcc").toString().trim() + ",";
                        }
                    }
                }
                String isTest = sysConfigService.getValue("bos.mail.istest");
                if (CommonUtil.isNullorEmpty(isTest) || isTest.equals("0")) {
                    email = sysConfigService.getValue("bos.mail.testmailto");
                    cc = "";
                    bcc = "";
                }
                if (CommonUtil.isNullorEmpty(email)) {
                    data.put("result", "fail");
                    data.put("message", "用户编号:" + billAccount.getCustomer_id() + "该客户未录入邮箱，请先补录邮箱后再发送");
                    return data;
                }

                templatekey = billAccount.getOffice_id();
                ReportConfig reportConfig = sysConfigService.getReportCongfig(templatekey);
                downloadForm.setBill_id(billId);
                Map mValues = ((IReportService) getApplicationContext().getBean(reportConfig.getService())).getData(reportConfig, downloadForm);

                String filePath = request.getSession().getServletContext().getRealPath("/");

                // 发送邮件属性
                Map map = new HashMap<String, String>();
                map.put("office_id", templatekey);
                // 云毅合同模板选取
                boolean consumeFlag = false;
                if ("9774".equals(templatekey)) {
                    consumeFlag = isOverConsume(form);
                }
                if (consumeFlag) {
                    map.put("mail_type", "1");
                } else {
                    map.put("mail_type", "0");
                }
                MailConfig mailConfig = ReportUtils.getMailConfig(map, sysConfigService).get(0);
                MailSender mailSender = new MailSender(mailConfig);
                Mail mail = mailConfig.getMail();
                //邮件图片
                if (!CommonUtil.isNullorEmpty(mailConfig.getFile_path())) {
                    String file_path = request.getSession().getServletContext().getRealPath(mailConfig.getFile_path().substring(7));
                    File file = new File(file_path);
                    if (file.exists()) {
                        MimeBodyPart image = new MimeBodyPart();
                        image.setDataHandler(new DataHandler(new FileDataSource(file_path)));
                        image.setContentID("logo_jpg.jpg");
                        mail.addMultipart(image);
                    } else {
                        data.put("result", "fail");
                        data.put("message", "账单：" + billId + "所在部门邮件模板内容图片不存在");
                        return data;
                    }
                }
                // 补足邮件内容
                Map varibles = new HashMap();
                varibles.put("contractid", billAccount.getContract_id());
                varibles.put("customerid", billAccount.getCustomer_id());
                Date date = (Date) Formatter.parseUtilDate(Formatter.DATE_FORMAT1, billAccount.getCharge_begin_date());
                String dateString = Formatter.formatDate(Formatter.DATE_FORMAT5, date);
                varibles.put("mailmonth", dateString);
                varibles.put("custname", billAccount.getUser_name());
                varibles.put("payable", billAccount.getPayable());

                mail.injectVarible(varibles);

                String zipFileName = mail.getSubject();
                String zipFilePath = filePath + zipFileName;
                File zipPath = new File(filePath + "/" + zipFileName);
                // 判断路径是否存在
                if (!zipPath.exists()) {
                    File parent = zipPath.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }
                }
                // 绑定字体文件路径
                if (CommonUtil.isNullorEmpty(PDFUtil.pdf_font_file_path)) {
                    PDFUtil.pdf_font_file_path = sysConfigService.getValue("fontfilepath");
                }
                // 如果是 PDF导出
                if ("pdf".equals(reportConfig.getFile_type())) {
                    PDFUtil.generate(mValues, zipPath + "/" + mValues.get("filename"));
                } else if ("xlsx".equals(reportConfig.getFile_type())) {
                    // 如果是xlsx导出
                    XlsxUtil.generate(mValues, zipPath + "/" + zipFileName + ".xlsx");
                }
                // pdf压缩
                ZipCompressUtil.fileToZip(zipFilePath + "/" + mValues.get("filename"), zipFilePath + ".zip");
                // 发件人、昵称
                mail.addSenders(mailConfig.getSmtpsender(), sysConfigService.getValue("sendNickName"));
                // 设置收信人
                mail.addRecipients(email);
                if (!CommonUtil.isNullorEmpty(cc)) {
                    mail.addCCs(cc);
                }
                // 设置BCC
                if (!CommonUtil.isNullorEmpty(bcc)) {
                    mail.addBCCs(bcc);
                }
                // 抄送客户经理
                if (billAccount.getCc_flag().equals("1") && !CommonUtil.isNullorEmpty(billAccount.getCm_email())) {
                    mail.addCCs(billAccount.getCm_email());
                }
                // 密抄
                if (!CommonUtil.isNullorEmpty(mailConfig.getBcc())) {
                    mail.addBCCs(mailConfig.getBcc());
                }
                mail.addAttachment(filePath + zipFileName + ".zip");
                mailSender.sendMail(mail, isTest, email);

                File zipfile = new File(zipFilePath);
                if (zipfile.exists()) {
                    CommonUtil.deleteAllFilesOfDir(zipfile);
                    CommonUtil.deleteAllFilesOfDir(new File(zipfile + ".zip"));
                }

                // 变更发信对象账单状态
                Map mParams = new HashMap();
                mParams.put("bill_id", billId);
                chargeBillService.updateSendStatus(mParams);

            }
            data.put("result", "success");
            data.put("message", "账单发送成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            data.put("result", "fail");
            data.put("message", "部分邮件发送失败,原因：" + e.getMessage());
        }
        return data;
    }

    /**
     * 邮件改变审核状态.
     * 
     * @param downloadForm
     * @param request
     * @return
     */
    @RequestMapping(value = "/checkBill")
    public @ResponseBody Map<String, Object> checkBill(DownloadForm downloadForm, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            // 变更账单审核状态
            Map mParams = new HashMap();
            mParams.put("bill_id", downloadForm.getBill_id());
            mParams.put("cc_flag", downloadForm.getCcFlag());
            int count = chargeBillService.checkBill(mParams);
            if (count == 1) {
                data.put("result", "success");
                data.put("message", "账单审核通过");
            } else {
                data.put("result", "fail");
                data.put("message", "账单审核失败");
            }
        } catch (Exception e) {
            data.put("result", "fail");
            data.put("message", "账单审核失败");
        }
        return data;
    }

    /**
     * 显示删除条件
     * 
     * @return
     */
    @RequiresPermissions("charge:bill:chargeBill:edit")
    @RequestMapping(value = "deleteBillShow")
    public String deleteBillShow() {
        return "modules/" + "charge/bill/chargeDeleteBill";
    }

    /**
     * 删除帐单
     * 
     * @param downloadForm
     * @param request
     * @return
     */
    @RequiresPermissions("charge:bill:chargeBill:edit")
    @RequestMapping(value = "/deleteBill")
    public @ResponseBody Map<String, Object> deleteBill(DownloadForm downloadForm, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            // 删除账单
            Map mParams = new HashMap();
            mParams.put("contract_id", downloadForm.getContract_id());
            chargeBillService.deleteBill(mParams);
            data.put("result", "success");
            data.put("message", "账单删除成功");
        } catch (Exception e) {
            data.put("result", "fail");
            data.put("message", "账单删除失败");
        }
        return data;
    }

    /**
     * 验证账单是否存在
     * 
     * @param downloadForm
     * @param request
     * @return
     */
    @RequestMapping(value = "/billExist")
    public @ResponseBody Map<String, Object> billExist(DownloadForm downloadForm) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            Map params = new HashMap();
            params.put("charge_end_date", downloadForm.getCharge_end_date());
            params.put("contract_id", downloadForm.getContract_id().trim());
            Map<String, String> maxBillInfo = chargeBillService.getMaxBillInfo(params);
            if (!CommonUtil.isNullorEmpty(maxBillInfo.get("bill_id"))) {
                data.put("result", "success");
            } else {
                data.put("result", "fail");
                data.put("message", "没有账单明细可供导出");
            }

        } catch (Exception e) {
            data.put("result", "fail");
            data.put("message", e.getMessage());
        }
        return data;
    }

    /**
     * 判断是否超保底
     * 
     * @param form
     * @return
     */
    public boolean isOverConsume(ChargeBillSearchForm form) {
        ChargeBillInfo billConsume = chargeBillService.getBillConsume(form);
        if (CommonUtil.isNullorEmpty(billConsume.getMin_type())) {
            return true;
        } else {
            Double chargeAmt = Double.valueOf(billConsume.getCharge_amt());
            Double minConsume = Double.valueOf(billConsume.getMin_consume());
            if (chargeAmt >= minConsume) {
                return true;
            }
            return false;
        }
    }

}
