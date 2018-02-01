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
import com.hundsun.boss.modules.charge.form.bill.ChargeBillForm;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillSearchForm;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.service.bill.ChargeBillDetailService;
import com.hundsun.boss.modules.charge.service.bill.ChargeBillService;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.service.SysConfigService;

@Service("receivableReportService")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BillReportService extends ApplicationObjectSupport implements IReportService {

    @Autowired
    private ChargeBillDetailService chargeBillDetailService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private ChargeBillService chargeBillService;

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
        ChargeBillSearchForm form = new ChargeBillSearchForm();
        form.setBill_id(downloadForm.getBill_id());
        ChargeBillForm billAccount = chargeBillService.getBillAccount(form);
        if(!CommonUtil.isNullorEmpty(sysConfigService.getCustomizeBillTemplateConfig(billAccount.getContract_id()))) {
            reportConfig = sysConfigService.getCustomizeBillTemplateConfig(billAccount.getContract_id());
        }
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
            chargeBillSearchForm.setBill_id(downloadForm.getBill_id());
            mValues.put("chargeDetail", chargeBillDetailService.queryChargeDetail(chargeBillSearchForm));
        } catch (Exception e) {
            throw e;
        }
        return mValues;
    }

}
