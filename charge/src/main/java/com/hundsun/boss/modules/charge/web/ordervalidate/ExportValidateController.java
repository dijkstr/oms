package com.hundsun.boss.modules.charge.web.ordervalidate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hundsun.boss.common.report.ZipCompressUtil;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.modules.charge.form.ordervalidate.OrderValidateForm;
import com.hundsun.boss.modules.charge.service.ordervalidate.ExportValidateService;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.service.SysConfigService;
/**
 * 合同校验信息导出controller
 * @author feigq
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/order/validate/export")
//@SuppressWarnings({ "rawtypes", "unchecked" }
public class ExportValidateController extends ApplicationObjectSupport{
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private ExportValidateService exportValidateService;
    
    
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/download")
    public @ResponseBody Map<String, String> download(String templatekey, OrderValidateForm downloadForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        try {
            // 生成下载文件名称
            String zipFileName = "download" + Formatter.formatDate(Formatter.TIME_FORMAT2, new Date());
            String zipFilePath = request.getRealPath("/") + zipFileName;

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
                if (CommonUtil.isNullorEmpty(templatekey)) {
                    String[] billIds = downloadForm.getSerial_no().split(",");
                    for (String billId : billIds) {
                        
                        ReportConfig reportConfig = sysConfigService.getReportCongfig(templatekey);
                        downloadForm.setSerial_no(billId);
                        exportValidateService.generateReport(zipFileName, reportConfig, downloadForm, filePath);
                    }
                } else {
                    ReportConfig reportConfig = sysConfigService.getReportCongfig(templatekey);
                    exportValidateService.generateReport(zipFileName, reportConfig, downloadForm, filePath);
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
                    data.put("message", "文件导出失败！");
                    logger.info(e.getMessage(), e);
                }
            } catch (Exception e) {
                data.put("message", "文件导出失败！");
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
            data.put("message", "文件导出成功！");
        } catch (Exception e) {
            data.put("message", "文件导出失败！");
            logger.error("error:", e);
            CommonUtil.exceptionHandler(data, e);
           
        } finally {
        }
        return data;
    }


}
