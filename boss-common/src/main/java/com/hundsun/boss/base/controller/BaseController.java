package com.hundsun.boss.base.controller;

import java.beans.PropertyEditorSupport;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hundsun.boss.common.beanvalidator.BeanValidators;
import com.hundsun.boss.common.report.PDFUtil;
import com.hundsun.boss.common.report.ZipCompressUtil;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.DateUtils;

/**
 * 控制器支持类
 */
public abstract class BaseController extends ApplicationObjectSupport {

    public static String ERROR_MESSAGE_PERFIX = "数据验证失败：";

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 验证Bean实例对象
     */
    @Autowired
    protected Validator validator;

    /**
     * 服务端参数有效性验证
     * 
     * @param object 验证的实体对象
     * @param groups 验证组
     * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
     */
    protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
        try {
            BeanValidators.validateWithException(validator, object, groups);
        } catch (ConstraintViolationException ex) {
            List<String> list = BeanValidators.extractMessage(ex);
            list.add(0, ERROR_MESSAGE_PERFIX);
            addMessage(model, list.toArray(new String[] {}));
            return false;
        }
        return true;
    }

    /**
     * 服务端参数有效性验证
     * 
     * @param object 验证的实体对象
     * @param groups 验证组
     * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
     */
    protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
        try {
            BeanValidators.validateWithException(validator, object, groups);
        } catch (ConstraintViolationException ex) {
            List<String> list = BeanValidators.extractMessage(ex);
            list.add(0, ERROR_MESSAGE_PERFIX);
            addMessage(redirectAttributes, list.toArray(new String[] {}));
            return false;
        }
        return true;
    }

    /**
     * 添加Model消息
     * 
     * @param messages 消息
     */
    protected void addMessage(Model model, String... messages) {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message).append(messages.length > 1 ? "<br/>" : "");
        }
        model.addAttribute("message", sb.toString());
    }

    /**
     * 添加Flash消息
     * 
     * @param messages 消息
     */
    protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message).append(messages.length > 1 ? "<br/>" : "");
        }
        redirectAttributes.addFlashAttribute("message", sb.toString());
    }

    /**
     * 初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 根据html生成pdf
     * 
     * @param html
     * @param request
     * @param response
     * @param zipFileName
     * @param sysConfigService
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    protected void generatePDFDownload(String html, HttpServletRequest request, HttpServletResponse response, String zipFileName) throws IOException, UnsupportedEncodingException, Exception {
        String zipFilePath = request.getSession().getServletContext().getRealPath("/") + "/" + zipFileName + ".zip";
        String pdfFilePath = request.getSession().getServletContext().getRealPath("/") + "/" + zipFileName + ".pdf";

        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/pdf;charset=UTF-8");
        // 注释下文直接在浏览器中打开文件，否则在浏览器中提示下载
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(zipFileName, "UTF-8") + ".zip");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        // pdf打印内容
        String filePath = request.getSession().getServletContext().getRealPath("/");

        try {
            File zipPath = new File(filePath + "/" + zipFileName);
            // 判断路径是否存在
            if (!zipPath.exists()) {
                File parent = zipPath.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
            }

            PDFUtil.generate(html, pdfFilePath);
            // pdf打印内容
            ZipCompressUtil.fileToZip(pdfFilePath, zipFilePath);

            bis = new BufferedInputStream(new FileInputStream(zipFilePath));
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
        }
        File pdffile = new File(pdfFilePath);
        if (pdffile.exists()) {
            CommonUtil.deleteAllFilesOfDir(pdffile);
        }
    }

    /**
     * 根据html生成pdf
     * 
     * @param html
     * @param filePath
     * @param fileName
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    protected String generatePDF(String html, String filePath, String fileName) throws Exception {
        String pdfFilePath = filePath + "/" + fileName + ".pdf";
        File pdfFile = new File(pdfFilePath);
        // 判断路径是否存在
        if (!pdfFile.exists()) {
            File parent = pdfFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
        }
        PDFUtil.generate(html, pdfFilePath);
        return pdfFilePath;
    }
}
