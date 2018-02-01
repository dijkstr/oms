package com.hundsun.boss.modules.charge.service.export;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import com.hundsun.boss.common.report.PDFUtil;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.modules.charge.dao.bill.ChargeBillDao;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillForm;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillSearchForm;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.service.bill.ChargeBillDetailService;
import com.hundsun.boss.modules.charge.service.bill.ChargeBillService;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.service.SysConfigService;

@Service("chargeReceiptDetailReportService")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChargeReceiptDetailReportService extends ApplicationObjectSupport implements IReportService {

    @Autowired
    private ChargeBillDetailService chargeBillDetailService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private ChargeBillService chargeBillService;

    @Autowired
    private ChargeBillDao chargeBillDao;

    /**
     * 生成打包后的压缩文件
     * 
     * @param zipFileName
     * @param reportConfig
     * @param chargeBillSearchForm
     * @param index
     * @throws Exception
     */
    public void generateReport(String zipFileName, ReportConfig reportConfig, DownloadForm chargeBillSearchForm, String filePath) throws Exception {
        try {
            File zipPath = new File(filePath + "/" + zipFileName);
            // 判断路径是否存在
            if (!zipPath.exists()) {
                File parent = zipPath.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
            }

            // 如果是 PDF导出 
            if ("pdf".equals(reportConfig.getFile_type())) {
                Map mValues = getData(reportConfig, chargeBillSearchForm);
                PDFUtil.generate(mValues, zipPath + "/" + mValues.get("filename"));
            }

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 生成pdf的html
     * 
     * @param reportConfig
     * @param downloadForm
     * @return
     * @throws Exception
     */
    public String generateReportHTML(ReportConfig reportConfig, DownloadForm downloadForm) throws Exception {
        String pdfHtml = "";
        if (downloadForm.getBill_id().length() > 0) {
            Map mValues = getData(reportConfig, downloadForm);
            // 如果是 PDF导出 
            if ("pdf".equals(reportConfig.getFile_type())) {
                pdfHtml = PDFUtil.generatePDFHtml(mValues);
            }
        }
        return pdfHtml;
    }

    /**
     * 获取账单填充数据
     */
    public Map getData(ReportConfig reportConfig, DownloadForm downloadForm) throws Exception {
        Map params = new HashMap();
        params.put("charge_end_date", downloadForm.getCharge_end_date());
        params.put("contract_id", downloadForm.getContract_id().trim());
        Map<String, String> maxBillInfo = chargeBillDao.getMaxBillInfo(params);
        ChargeBillSearchForm form = new ChargeBillSearchForm();
        form.setBill_id(maxBillInfo.get("bill_id"));
        ChargeBillForm billAccount = chargeBillService.getBillAccount(form);
        Map varibles = new HashMap();
        varibles.put("contractid", billAccount.getContract_id());
        varibles.put("customerid", billAccount.getCustomer_id());
        Date date = (Date) Formatter.parseUtilDate(Formatter.DATE_FORMAT1, billAccount.getCharge_begin_date());
        String dateString = Formatter.formatDate(Formatter.DATE_FORMAT5, date);
        varibles.put("mailmonth", dateString);
        varibles.put("custname", billAccount.getUser_name());
        String billName = CommonUtil.injectString(billAccount.getMail_subject(), varibles);

        Map mValues = new HashMap();
        // 纵向打印PDF
        mValues.put("a4portrait", CommonUtil.readReportCSS("a4portrait.css"));
        // 通用css
        mValues.put("acclayout", CommonUtil.readReportCSS("acclayout.css"));
        String content = StringEscapeUtils.unescapeHtml(reportConfig.getTemplate_content());
        try {
            mValues.put("template", reportConfig.getTemplate());
            mValues.put("content", content);
            mValues.put("filename", billName + ".pdf");
            if (!CommonUtil.isNullorEmpty(downloadForm.getPageNo())) {
                downloadForm.setPageNo((downloadForm.getPageNo() - 1) * downloadForm.getPageSize());
            }
            ChargeBillSearchForm chargeBillSearchForm = new ChargeBillSearchForm();
            chargeBillSearchForm.setContract_id(downloadForm.getContract_id());
            chargeBillSearchForm.setCharge_begin_date(downloadForm.getCharge_begin_date());
            chargeBillSearchForm.setCharge_end_date(downloadForm.getCharge_end_date());
            chargeBillSearchForm.setBill_id(maxBillInfo.get("bill_id"));
            String title = billAccount.getUser_name();
            if (!CommonUtil.isNullorEmpty(downloadForm.getCharge_begin_date())) {
                if (downloadForm.getCharge_begin_date().length() >= 7) {
                    title += downloadForm.getCharge_begin_date().substring(0, 4) + "年" + downloadForm.getCharge_begin_date().substring(5, 7) + "月";
                }
            }
            if (!CommonUtil.isNullorEmpty(downloadForm.getCharge_end_date())) {
                if (downloadForm.getCharge_end_date().length() >= 7) {
                    if (title.endsWith("月")) {
                        title += "-";
                    }
                    title += downloadForm.getCharge_end_date().substring(0, 4) + "年" + downloadForm.getCharge_end_date().substring(5, 7) + "月";
                }
            }
            String period = "";
            if (!CommonUtil.isNullorEmpty(downloadForm.getCharge_begin_date())) {
                if (downloadForm.getCharge_begin_date().length() >= 10) {
                    period += downloadForm.getCharge_begin_date().substring(0, 4) + "年" + downloadForm.getCharge_begin_date().substring(5, 7) + "月"
                            + downloadForm.getCharge_begin_date().substring(8, 10) + "日";
                }
            }
            if (!CommonUtil.isNullorEmpty(downloadForm.getCharge_end_date())) {
                if (downloadForm.getCharge_end_date().length() >= 10) {
                    if (period.endsWith("日")) {
                        period += "-";
                    }
                    period += downloadForm.getCharge_end_date().substring(0, 4) + "年" + downloadForm.getCharge_end_date().substring(5, 7) + "月" + downloadForm.getCharge_end_date().substring(8, 10)
                            + "日";
                }
            }
            if (!CommonUtil.isNullorEmpty(period)) {
                period = "计费周期：" + period;
            }
            mValues.put("title", title);
            mValues.put("period", period);
            mValues.put("chargeDetail", chargeBillDetailService.queryChargeReceiptDetailReport(chargeBillSearchForm));
        } catch (Exception e) {
            throw e;
        }
        return mValues;
    }
}
