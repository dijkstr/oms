package com.hundsun.boss.modules.charge.service.export;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import com.hundsun.boss.common.report.XlsxUtil;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.modules.charge.entity.income.ChargeIncomeInterface;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.form.income.ChargeIncomeForm;
import com.hundsun.boss.modules.charge.service.income.ChargeIncomeService;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.utils.DictUtils;

@Service("ncInterfaceDetailReportService")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NCInterfaceDetailReportService extends ApplicationObjectSupport implements IReportService {

    @Autowired
    private ChargeIncomeService chargeIncomeService;

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

    /**
     * 导出财务收入接口
     */
    public Map getData(ReportConfig reportConfig, DownloadForm downloadForm) throws Exception {
        Map mValues = new HashMap();
        String content = StringEscapeUtils.unescapeHtml(reportConfig.getTemplate_content());
        try {
            mValues.put("template", reportConfig.getTemplate());
            mValues.put("content", content);
            mValues.put("filename", Formatter.formatDate(Formatter.DATE_FORMAT3, new Date()) + reportConfig.getFile_name());
            ChargeIncomeForm chargeIncomeForm = new ChargeIncomeForm();
            chargeIncomeForm.setIncomemonths(downloadForm.getCharge_month());
            List<ChargeIncomeInterface> syncIncomes = chargeIncomeService.get(chargeIncomeForm);
            List<Map> results = new ArrayList<Map>();
            for (int i = 0; i < syncIncomes.size(); i++) {
                Map map = CommonUtil.convertBean(syncIncomes.get(i));
                map.put("chargetype", DictUtils.getDictLabel(map.get("chargetype").toString(), "chargetype", ""));
                results.add(map);
            }
            mValues.put("griddata", results);
        } catch (Exception e) {
            throw e;
        }
        return mValues;
    }

}
