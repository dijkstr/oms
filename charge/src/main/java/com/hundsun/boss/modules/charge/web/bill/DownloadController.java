package com.hundsun.boss.modules.charge.web.bill;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hundsun.boss.base.exception.ServiceException;
import com.hundsun.boss.common.report.PDFUtil;
import com.hundsun.boss.common.report.ZipCompressUtil;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillForm;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillSearchForm;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.service.bill.ChargeBillService;
import com.hundsun.boss.modules.charge.service.export.IReportService;
import com.hundsun.boss.modules.charge.service.order.OrderInfoService;
import com.hundsun.boss.modules.sys.entity.Menu;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.service.SysConfigService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/charge/bill/export")
public class DownloadController extends ApplicationObjectSupport {
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private ChargeBillService chargeBillService;

    /**
     * 生成pdf下载
     * 
     * @param templatekey
     * @param chargeBillSearchForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/download")
    public @ResponseBody Map<String, String> pdfDownload(String templatekey, DownloadForm downloadForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        try {
            //当前用户的所有操作权限
            String permissionInfoString = ",";
            List<Menu> list = UserUtils.getMenuList();
            for (Menu menu : list) {
                if (StringUtils.isNotBlank(menu.getPermission())) {
                    permissionInfoString = permissionInfoString + menu.getPermission().trim() + ",";
                }
            }

            if (CommonUtil.isNullorEmpty(templatekey)) {
                String[] billIds = downloadForm.getBill_id().split(",");
                for (String billId : billIds) {
                    ChargeBillSearchForm form = new ChargeBillSearchForm();
                    form.setBill_id(billId);
                    ChargeBillForm chargeBillForm = chargeBillService.getBillAccount(form);
                    if (CommonUtil.isNullorEmpty(chargeBillForm)) {
                        data.put("message2", "当前用户没有此操作权限。billId：" + billId);
                        throw new ServiceException("当前用户没有此操作权限。billId：" + billId);
                    }
                    String tempkey = chargeBillForm.getOffice_id();
                    ReportConfig reportConfig = sysConfigService.getReportCongfig(tempkey);
                    if (CommonUtil.isNullorEmpty(reportConfig) || CommonUtil.isNullorEmpty(reportConfig.getPermission())
                            || permissionInfoString.indexOf("," + reportConfig.getPermission().trim() + ",") == -1) {
                        data.put("message2", "当前用户没有此操作权限。billId：" + billId);
                        throw new ServiceException("当前用户没有此操作权限。billId：" + billId);
                    }
                }
            } else {
                ReportConfig reportConfig = sysConfigService.getReportCongfig(templatekey);
                if (CommonUtil.isNullorEmpty(reportConfig) || CommonUtil.isNullorEmpty(reportConfig.getPermission())
                        || permissionInfoString.indexOf("," + reportConfig.getPermission().trim() + ",") == -1) {
                    data.put("message2", "当前用户没有此操作权限。templatekey：" + templatekey);
                    throw new ServiceException("当前用户没有此操作权限。templatekey：" + templatekey);
                }
            }

            // 生成下载文件名称
            String zipFileName = "download" + Formatter.formatDate(Formatter.TIME_FORMAT2, new Date());
            String zipFilePath = request.getRealPath("/") + "/" + zipFileName;

            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/pdf;charset=UTF-8");
            // 注释下文直接在浏览器中打开文件，否则在浏览器中提示下载
            response.setHeader("Content-disposition", "attachment;filename=" + zipFileName + ".zip");
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;

            // pdf打印内容
            String filePath = request.getRealPath("/");

            try {
                File zipPath = new File(filePath + "/" + zipFileName);
                // 判断路径是否存在
                if (!zipPath.exists()) {
                    File parent = zipPath.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }
                }
                // 绑定字体文件路径
                if (CommonUtil.isNullorEmpty(PDFUtil.pdf_font_file_path)) {
                    PDFUtil.pdf_font_file_path = sysConfigService.getValue("fontfilepath");
                }
                if (CommonUtil.isNullorEmpty(templatekey)) {
                    String[] billIds = downloadForm.getBill_id().split(",");
                    for (String billId : billIds) {
                        ChargeBillSearchForm form = new ChargeBillSearchForm();
                        form.setBill_id(billId);
                        templatekey = chargeBillService.getBillAccount(form).getOffice_id();

                        ReportConfig reportConfig = sysConfigService.getReportCongfig(templatekey);
                        downloadForm.setBill_id(billId);
                        ((IReportService) getApplicationContext().getBean(reportConfig.getService())).generateReport(zipFileName, reportConfig, downloadForm, filePath);
                    }
                } else {
                    ReportConfig reportConfig = sysConfigService.getReportCongfig(templatekey);
                    ((IReportService) getApplicationContext().getBean(reportConfig.getService())).generateReport(zipFileName, reportConfig, downloadForm, filePath);
                }

                // pdf打印内容
                ZipCompressUtil.fileToZip(zipFilePath, zipFilePath + ".zip");

                bis = new BufferedInputStream(new FileInputStream(zipFilePath + ".zip"));
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                try {
                    while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                        bos.write(buff, 0, bytesRead);
                    }
                } catch (IOException e) {
                    logger.info(e.getMessage(), e);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw e;
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
            out.print("<script>window.close()</script>");
            // 生成完毕后，去掉临时文件夹以及文件
            // 因为同一个用户只有一个生成文件夹，而且生成文件后会将这个文件夹整个删除
            // 因此，即使由于错误或其他原因导致文件夹中出现临时文件，也会伴随下次成功
            // 导出被删除
            File zipfile = new File(zipFilePath);
            if (zipfile.exists()) {
                CommonUtil.deleteAllFilesOfDir(zipfile);
                CommonUtil.deleteAllFilesOfDir(new File(zipfile + ".zip"));
            }
        } catch (Exception e) {
            logger.error("error:", e);
            CommonUtil.exceptionHandler(data, e);
        } finally {
        }
        return data;
    }
}
