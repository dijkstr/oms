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
import com.hundsun.boss.modules.charge.entity.income.ChargeIncomePeriodInterface;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.form.income.ChargeIncomePeriodInterfaceForm;
import com.hundsun.boss.modules.charge.service.income.ChargeIncomePeriodInterfaceService;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.utils.DictUtils;

@Service("chargeIncomePeriodInterfaceReportService")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChargeIncomePeriodInterfaceReportService extends ApplicationObjectSupport implements IReportService {

    @Autowired
    private ChargeIncomePeriodInterfaceService chargeIncomePeriodInterfaceService;

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
            ChargeIncomePeriodInterfaceForm chargeIncomePeriodInterfaceForm = new ChargeIncomePeriodInterfaceForm();
            BeanUtils.copyProperties(downloadForm, chargeIncomePeriodInterfaceForm);

            chargeIncomePeriodInterfaceForm.setPagesize(-1);
            Page<ChargeIncomePeriodInterface> page = chargeIncomePeriodInterfaceService.find(new Page<ChargeIncomePeriodInterface>(), chargeIncomePeriodInterfaceForm);

            List<Map> datas = new ArrayList<Map>();

            for (ChargeIncomePeriodInterface chargeIncomePeriodInterface : page.getList()) {
                Map map = new HashMap<String, String>();
                // 所属部门 office_name
                map.put("office_name", chargeIncomePeriodInterface.getOffice().getName());
                map.put("detailid", chargeIncomePeriodInterface.getDetailid());
                map.put("accountidentity", DictUtils.getDictLabel(chargeIncomePeriodInterface.getAccountidentity(), "accountidentity", ""));                
                map.put("contract_id", chargeIncomePeriodInterface.getContract_id());
                map.put("cust_name", chargeIncomePeriodInterface.getSyncContract().getSyncCustomer().getChinesename());
                map.put("product_id", chargeIncomePeriodInterface.getProduct_id());
                map.put("product_name", chargeIncomePeriodInterface.getSyncProduct().getProductname());
                map.put("payment_type", DictUtils.getDictLabel(chargeIncomePeriodInterface.getPayment_type(), "payment_type", ""));
                map.put("servicestartdate", chargeIncomePeriodInterface.getServicestartdate());
                map.put("serviceenddate", chargeIncomePeriodInterface.getServiceenddate());
                map.put("income_begin_date", chargeIncomePeriodInterface.getIncome_begin_date());
                map.put("income_end_date", chargeIncomePeriodInterface.getIncome_end_date());
                map.put("hasreceive", chargeIncomePeriodInterface.getHasreceive());
                map.put("send_flag", DictUtils.getDictLabel(chargeIncomePeriodInterface.getSend_flag(), "send_flag", ""));
                map.put("createDate", chargeIncomePeriodInterface.getCreateDate());
                map.put("createBy", chargeIncomePeriodInterface.getCreateBy().getName());
                datas.add(map);
            }
            mValues.put("griddata", datas);
        } catch (Exception e) {
            throw e;
        }
        return mValues;
    }
}
