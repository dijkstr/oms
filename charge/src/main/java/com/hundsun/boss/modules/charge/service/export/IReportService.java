package com.hundsun.boss.modules.charge.service.export;

import java.util.Map;

import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.sys.entity.ReportConfig;

@SuppressWarnings("rawtypes")
public interface IReportService {

    /**
     * 生成打包后的压缩文件
     * 
     * @param zipFileName
     * @param reportConfig
     * @param downloadForm
     * @throws Exception
     */
    public void generateReport(String zipFileName, ReportConfig reportConfig, DownloadForm downloadForm, String filePath) throws Exception;

    /**
     * 生成pdf的html
     * 
     * @param reportConfig
     * @param downloadForm
     * @return
     * @throws Exception
     */
    public String generateReportHTML(ReportConfig reportConfig, DownloadForm downloadForm) throws Exception;

    public Map getData(ReportConfig reportConfig, DownloadForm downloadForm) throws Exception;

}
