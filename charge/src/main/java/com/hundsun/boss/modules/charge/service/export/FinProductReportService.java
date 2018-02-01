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

import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.report.XlsxUtil;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.form.finance.FinSummaryForm;
import com.hundsun.boss.modules.charge.service.finance.FinSummaryService;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.utils.DictUtils;

@Service("finProductReportService")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FinProductReportService extends ApplicationObjectSupport implements IReportService {

    @Autowired
    private FinSummaryService finSummaryService;

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
            // 如果是excel导出 
            if ("xlsx".equals(reportConfig.getFile_type())) {
                Map mValues = getData(reportConfig, downloadForm);
                XlsxUtil.generate(mValues, zipPath + "/" + mValues.get("filename"));
            }

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 生成xls的html
     * 
     * @param reportConfig
     * @param downloadForm
     * @return
     * @throws Exception
     */
    public String generateReportHTML(ReportConfig reportConfig, DownloadForm downloadForm) throws Exception {
        return "";
    }

    public Map getData(ReportConfig reportConfig, DownloadForm downloadForm) throws Exception {
        Map mValues = new HashMap();
        String content = StringEscapeUtils.unescapeHtml(reportConfig.getTemplate_content());
        try {
            mValues.put("template", reportConfig.getTemplate());
            mValues.put("content", content);
            mValues.put("filename", Formatter.formatDate(Formatter.DATE_FORMAT3, new Date()) + reportConfig.getFile_name());
            if (!CommonUtil.isNullorEmpty(downloadForm.getPageNo())) {
                downloadForm.setPageNo((downloadForm.getPageNo() - 1) * downloadForm.getPageSize());
            }
            FinSummaryForm req = new FinSummaryForm();
            req.setContract_id(downloadForm.getContract_id());
            req.setCharge_month(downloadForm.getCharge_end_month());
            req.setOffice_id(downloadForm.getOffice_id());
            req.setIncome_source(BaseService.getMultipleCondition(downloadForm.getIncome_source()));
            List<Map> finSummaryList = finSummaryService.exportProductDetailList(req);
            for (Map finSummary : finSummaryList) {
                finSummary.put("income_source", DictUtils.getDictLabel((String) finSummary.get("income_source"), "income_source", ""));
            }
            mValues.put("griddata", finSummaryList);
        } catch (Exception e) {
            throw e;
        }
        return mValues;
    }

}
