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
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.modules.charge.entity.income.ChargeIncomeSourceInterface;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.form.income.ChargeIncomeSourceInterfaceForm;
import com.hundsun.boss.modules.charge.service.income.ChargeIncomeSourceInterfaceService;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.utils.DictUtils;

@Service("chargeIncomeSourceInterfaceReportService")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChargeIncomeSourceInterfaceReportService extends ApplicationObjectSupport implements IReportService {

    @Autowired
    private ChargeIncomeSourceInterfaceService chargeIncomeSourceInterfaceService;

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
            if (!CommonUtil.isNullorEmpty(downloadForm.getPageNo())) {
                downloadForm.setPageNo((downloadForm.getPageNo() - 1) * downloadForm.getPageSize());
            }
            ChargeIncomeSourceInterfaceForm chargeIncomeSourceInterfaceForm = new ChargeIncomeSourceInterfaceForm();
            BeanUtils.copyProperties(downloadForm, chargeIncomeSourceInterfaceForm);

            chargeIncomeSourceInterfaceForm.setPagesize(-1);
            Page<ChargeIncomeSourceInterface> page = chargeIncomeSourceInterfaceService.find(new Page<ChargeIncomeSourceInterface>(), chargeIncomeSourceInterfaceForm);

            List<Map> datas = new ArrayList<Map>();

            for (ChargeIncomeSourceInterface chargeIncomeSourceInterface : page.getList()) {
                Map map = new HashMap<String, String>();
                // 所属部门 office_name
                map.put("office_name", chargeIncomeSourceInterface.getOffice().getName());
                // 协同合同id contract_id
                map.put("contract_id", chargeIncomeSourceInterface.getContract_id());
                map.put("cust_name", chargeIncomeSourceInterface.getSyncContract().getSyncCustomer().getChinesename());
                map.put("income_source", DictUtils.getDictLabel(chargeIncomeSourceInterface.getIncome_source(), "income_source", ""));
                map.put("send_flag", DictUtils.getDictLabel(chargeIncomeSourceInterface.getSend_flag(), "send_flag", ""));
                map.put("createDate", chargeIncomeSourceInterface.getCreateDate());
                map.put("createBy", chargeIncomeSourceInterface.getCreateBy().getName());
                datas.add(map);
            }
            mValues.put("griddata", datas);
        } catch (Exception e) {
            throw e;
        }
        return mValues;
    }
}