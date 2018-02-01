package com.hundsun.boss.modules.charge.service.export;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.report.XlsxUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.modules.charge.entity.report.ChargeBeginAlert;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.form.common.SearchForm;
import com.hundsun.boss.modules.charge.service.report.ChargeBeginAlertService;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.utils.OfficeUtils;

@Service("chargeBeginAlertReportService")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChargeBeginAlertReportService extends ApplicationObjectSupport implements IReportService {

    @Autowired
    private ChargeBeginAlertService chargeBeginAlertService;

    @Autowired
    private OfficeService officeService;

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
            SearchForm form = new SearchForm();
            BeanUtils.copyProperties(downloadForm, form);
            Page<ChargeBeginAlert> defaultpage = new Page<ChargeBeginAlert>();
            form.setPageSize(-1);
            Page<ChargeBeginAlert> page = chargeBeginAlertService.list(defaultpage, form);
            List<Map> datas = new ArrayList<Map>();

            for (ChargeBeginAlert chargeBeginAlert : page.getList()) {
                Map map = new HashMap<String, String>();
                // 调账编号 id
                map.put("office_name", OfficeUtils.getOfficeNameByCode(chargeBeginAlert.getOffice_id()));
                map.put("contract_id", chargeBeginAlert.getContract_id());
                map.put("order_begin_date", chargeBeginAlert.getOrder_begin_date());
                map.put("create_date", chargeBeginAlert.getCreate_date());
                map.put("charge_begin_date", chargeBeginAlert.getCharge_begin_date());
                map.put("payable", chargeBeginAlert.getPayable());
                map.put("income", chargeBeginAlert.getIncome());
                datas.add(map);
            }
            mValues.put("griddata", datas);
        } catch (Exception e) {
            throw e;
        }
        return mValues;
    }
}
