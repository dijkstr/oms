package com.hundsun.boss.modules.charge.service.export;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.hundsun.boss.modules.charge.service.order.OrderInfoService;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.service.SysConfigService;

@Service("exportBillService")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ExportBillsService extends ApplicationObjectSupport implements IReportService {

    @Autowired
    private ChargeBillDetailService chargeBillDetailService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private ChargeBillService chargeBillService;
    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 生成打包后的压缩文件
     * 
     * @param zipFileName
     * @param reportConfig
     * @param chargeBillSearchForm
     * @param index
     * @throws Exception
     */
    public void generateReport(String zipFileName, ReportConfig reportConfig, DownloadForm downloadForm, String filePath) throws Exception {
        try {
            File zipPath = new File(filePath + "/" + zipFileName);
            // 判断路径是否存在
            if (!zipPath.exists()) {
                File parent = zipPath.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
            }
            downloadForm.setZipPath(zipPath.getAbsolutePath());
            this.getData(reportConfig, downloadForm);
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
        return "";
    }

    /**
     * 按部门、销售批量导出账单
     * 
     * @param reportConfig
     * @param downloadForm
     * @param index
     * @return
     * @throws Exception
     */
    public Map getData(ReportConfig reportConfig, DownloadForm downloadForm) throws Exception {
        //按部门
        if (downloadForm.getFlag().equals("department")) {
            List<Map> map = chargeBillService.getOrderSource(downloadForm);
            for (int i = 0; i < map.size(); i++) {
                String orderSource = map.get(i).get("office_id").toString();
                String orderSourceName = map.get(i).get("dept_name").toString();
                downloadForm.setOffice_id(orderSource);
                String pathString = downloadForm.getZipPath() + "/" + orderSourceName;
                File f = new File(pathString);
                if (f != null && !f.exists()) {
                    f.mkdirs();
                }
                List<String> billIds = chargeBillService.getBillIds(downloadForm);
                for (int j = 0; j < billIds.size(); j++) {
                    downloadForm.setBill_id(billIds.get(j));
                    reportConfig = sysConfigService.getReportCongfig(orderSource);
                    Map mValues = this.getBillData(reportConfig, downloadForm, orderSource);
                    if (!CommonUtil.isNullorEmpty(mValues) && "pdf".equals(reportConfig.getFile_type())) {
                        try {
                            PDFUtil.generate(mValues, pathString + "/" + mValues.get("filename"));

                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            throw new Exception(mValues.get("bill_id") + "账单生成出错！");
                        }
                    }
                }
            }
        }
        //按销售
        if (downloadForm.getFlag().equals("saler")) {
            List<Map> map = chargeBillService.getOrderSource(downloadForm);
            for (int i = 0; i < map.size(); i++) {
                String orderSource = map.get(i).get("office_id").toString();
                String orderSourceName = map.get(i).get("dept_name").toString();
                downloadForm.setOffice_id(orderSource);
                List<Map> salerMap = orderInfoService.getSalers(downloadForm);
                for (int j = 0; j < salerMap.size(); j++) {
                    String salerName = salerMap.get(j).get("customermanagername").toString();
                    String pathString = downloadForm.getZipPath() + "/" + orderSourceName + "/" + salerName;
                    File f = new File(pathString);
                    if (f != null && !f.exists()) {
                        f.mkdirs();
                    }
                    downloadForm.setBill_id(salerMap.get(j).get("bill_id").toString());
                    reportConfig = sysConfigService.getReportCongfig(orderSource);
                    Map mValues = this.getBillData(reportConfig, downloadForm, orderSource);
                    if (!CommonUtil.isNullorEmpty(mValues) && "pdf".equals(reportConfig.getFile_type())) {
                        try {
                            PDFUtil.generate(mValues, pathString + "/" + mValues.get("filename"));

                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            throw new Exception(mValues.get("bill_id") + "账单生成出错！");
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取填充账单数据
     * 
     * @param reportConfig
     * @param downloadForm
     * @param orderSource
     * @return
     * @throws Exception
     */
    public Map getBillData(ReportConfig reportConfig, DownloadForm downloadForm, String orderSource) throws Exception {
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
            logger.error(e.getMessage());
            throw e;
        }
        return mValues;
    }

}
