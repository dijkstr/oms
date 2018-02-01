package com.hundsun.boss.modules.charge.service.export;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import com.hundsun.boss.common.report.XlsxUtil;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.modules.charge.entity.temp.TempTable;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.service.order.OrderInfoService;
import com.hundsun.boss.modules.charge.service.temp.TempTableService;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.service.OfficeService;

@Service("tempTableListService")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TempTableReportService extends ApplicationObjectSupport implements IReportService {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private TempTableService tempTableService;

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
        TempTable tempTable = tempTableService.get(downloadForm.getId());
        Map params = new HashMap();
        params.put("query_sql", StringEscapeUtils.unescapeHtml(tempTable.getQuery_sql()));
        List<LinkedHashMap<String, Object>> plainTableDatas = tempTableService.getPlainTableData(params);

        StringBuffer content = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.getProperty("line.separator"));
        content.append("<columns>" + System.getProperty("line.separator"));
        for(String head : plainTableDatas.get(0).keySet()) {
            content.append("<column header=\"" + head + "\" type=\"string\" field=\"" + head + "\" />" + System.getProperty("line.separator"));
        }
        content.append("</columns>" + System.getProperty("line.separator"));
        
        Map mValues = new HashMap();
        try {
            mValues.put("template", reportConfig.getTemplate());
            mValues.put("content", StringEscapeUtils.unescapeHtml(content.toString()));
            mValues.put("filename", Formatter.formatDate(Formatter.DATE_FORMAT3, new Date()) + tempTable.getName() + ".xlsx");
            if (!CommonUtil.isNullorEmpty(downloadForm.getPageNo())) {
                downloadForm.setPageNo((downloadForm.getPageNo() - 1) * downloadForm.getPageSize());
            }
            mValues.put("griddata", plainTableDatas);
        } catch (Exception e) {
            throw e;
        }
        return mValues;
    }

}
