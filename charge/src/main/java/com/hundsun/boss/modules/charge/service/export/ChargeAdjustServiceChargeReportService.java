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

import com.hundsun.boss.common.report.XlsxUtil;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.modules.charge.entity.finance.ChargeAdjustServiceCharge;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.form.finance.ChargeAdjustServiceChargeForm;
import com.hundsun.boss.modules.charge.service.finance.ChargeAdjustServiceChargeService;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.service.OfficeService;

@Service("chargeAdjustServiceChargeReportService")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChargeAdjustServiceChargeReportService extends ApplicationObjectSupport implements IReportService {

    @Autowired
    private ChargeAdjustServiceChargeService chargeAdjustServiceChargeService;

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
            ChargeAdjustServiceChargeForm chargeAdjustServiceCharge = new ChargeAdjustServiceChargeForm();
            BeanUtils.copyProperties(downloadForm, chargeAdjustServiceCharge);
            List<ChargeAdjustServiceCharge> chargeAdjustServiceCharges = chargeAdjustServiceChargeService.find(chargeAdjustServiceCharge);
            List<Map> datas = new ArrayList<Map>();

            for (ChargeAdjustServiceCharge adjustServiceCharge : chargeAdjustServiceCharges) {
                Map map = new HashMap<String, String>();
                // 调账编号 id
                map.put("id", adjustServiceCharge.getId());
                // 所属部门 office_name
                map.put("office_name", adjustServiceCharge.getOffice().getName());
                // 协同合同id contract_id
                map.put("contract_id", adjustServiceCharge.getContract_id());
                // 收入开始日期 combine_begin_date
                map.put("combine_begin_date", adjustServiceCharge.getCombine_begin_date());
                // 收入结束日期 combine_end_date
                map.put("combine_end_date", adjustServiceCharge.getCombine_end_date());
                // 产品名称 product_name
                map.put("product_names", adjustServiceCharge.getProduct_names());
                // 调账日期 adjust_date
                map.put("adjust_date", adjustServiceCharge.getAdjust_date());
                // 调账金额 adjust_amt format=&quot;#,##0.########
                map.put("adjust_amt", adjustServiceCharge.getAdjust_amt());
                // 备注 remarks
                map.put("remarks", adjustServiceCharge.getRemarks());
                // 是否可用
                if (!CommonUtil.isNullorEmpty(adjustServiceCharge.getOrderCombine()) && "0".equals(adjustServiceCharge.getOrderCombine().getDelFlag())) {
                    map.put("status", "是");
                } else {
                    map.put("status", "否");
                }
                datas.add(map);
            }
            mValues.put("griddata", datas);
        } catch (Exception e) {
            throw e;
        }
        return mValues;
    }
}
