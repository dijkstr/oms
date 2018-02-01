package com.hundsun.boss.modules.charge.service.receipt;

import java.io.File;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.report.XlsxUtil;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.modules.charge.dao.receipt.ChargeReceiptDao;
import com.hundsun.boss.modules.charge.form.receipt.ChargeReceiptForm;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;
/**
 * 合同到款导出service
 * @author feigq
 *
 */
@Service("exportReceiveService")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ExportReceiveService extends BaseService{
    @Autowired
    private ChargeReceiptDao chargeReceiptDao;
    @Autowired
    private ChargeReceiptService chargeReceiptService;
    
    public void generateReport(String zipFileName, ReportConfig reportConfig, ChargeReceiptForm downloadForm, String filePath) throws Exception {
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
            logger.error(e.getMessage());
            throw e;
        }
    }
    
    public Map getData(ReportConfig reportConfig, ChargeReceiptForm downloadForm) throws Exception {
        Map mValues = new HashMap();
        String content = StringEscapeUtils.unescapeHtml(reportConfig.getTemplate_content());
        try {
            mValues.put("template", reportConfig.getTemplate());
            mValues.put("content", content);
            mValues.put("filename", Formatter.formatDate(Formatter.DATE_FORMAT3, new Date()) + reportConfig.getFile_name());

           //不分页 
            downloadForm.setPageSize(-1);
            //对查询条件作解码处理
            //合同编号
            if(!CommonUtil.isNullorEmpty(downloadForm.getContract_id())){
               String namestr= HtmlUtils.htmlUnescape(downloadForm.getContract_id());
              String  namestr1=URLDecoder.decode(namestr, "UTF-8");
               downloadForm.setContract_id(namestr1);
            }
            //协同客户号
            if(!CommonUtil.isNullorEmpty(downloadForm.getCustomerid())){
                String namestr= HtmlUtils.htmlUnescape(downloadForm.getCustomerid());
               String  namestr1=URLDecoder.decode(namestr, "UTF-8");
                downloadForm.setCustomerid(namestr1);
             }
            //协同客户名称
            if(!CommonUtil.isNullorEmpty(downloadForm.getCustname())){
                String namestr= HtmlUtils.htmlUnescape(downloadForm.getCustname());
               String  namestr1=URLDecoder.decode(namestr, "UTF-8");
                downloadForm.setCustname(namestr1);
             }
          //销售产品编号
            if(!CommonUtil.isNullorEmpty(downloadForm.getSaleprdid())){
                String namestr= HtmlUtils.htmlUnescape(downloadForm.getSaleprdid());
               String  namestr1=URLDecoder.decode(namestr, "UTF-8");
                downloadForm.setSaleprdid(namestr1);
             }
            //考核部门
            if(!CommonUtil.isNullorEmpty(downloadForm.getAudit_branch_name())){
                String namestr= HtmlUtils.htmlUnescape(downloadForm.getAudit_branch_name());
               String  namestr1=URLDecoder.decode(namestr, "UTF-8");
                downloadForm.setAudit_branch_name(namestr1);
             }
            //考核人
            if(!CommonUtil.isNullorEmpty(downloadForm.getAudit_employee_name())){
                String namestr= HtmlUtils.htmlUnescape(downloadForm.getAudit_employee_name());
               String  namestr1=URLDecoder.decode(namestr, "UTF-8");
                downloadForm.setAudit_employee_name(namestr1);
             }
            //部门
            if(CommonUtil.isNullorEmpty(downloadForm.getDepartment())){
                //部门查询条件为空
                User currentUser = UserUtils.getUser();
                //根据当前用户设置部门权限
                downloadForm.setDepartment(getDept(currentUser, "office", ""));
             } 
            
            //查询数据
          List<Map> listmap= chargeReceiptDao.exportChargeReceipt(downloadForm);
         
          mValues.put("griddata", listmap);
             
           
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        return mValues;
    }

}
