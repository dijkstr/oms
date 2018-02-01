package com.hundsun.boss.util;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.hundsun.boss.common.Constant;
import com.hundsun.boss.common.utils.CommonUtil;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * PDF 生成
 */
@SuppressWarnings({ "rawtypes" })
public class PDFUtil {
    private static final Logger logger = LoggerFactory.getLogger(PDFUtil.class);

    /*
     * pdf模板内容
     */
    private static Template pdfCommonTemplate = null;

    /**
     * 使用共通模板，生成pdf
     * 
     * @param mValues
     * @param outputFile
     * @throws Exception
     */
    public static void generate(Map mValues, String outputFile) throws Exception {
        generate((String) mValues.get("template"), mValues, outputFile);
    }

    /**
     * 使用模板,模板数据,生成PDF
     * 
     * @param templateName
     * @param mValues
     * @param outputFile
     * @throws Exception
     */
    public static void generate(String templateName, Map mValues, String outputFile) throws Exception {

        String htmlContent = generatePDFHtml(templateName, mValues);
        OutputStream out = null;
        ITextRenderer iTextRenderer = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(htmlContent.getBytes("UTF-8")));
            File f = new File(outputFile);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdir();
            }
            out = new FileOutputStream(outputFile);
            // 获取对象池中对象
            iTextRenderer = (ITextRenderer) ITextRendererObjectFactory.getObjectPool().borrowObject();
            try {
                iTextRenderer.setDocument(doc, null);
                iTextRenderer.layout();
                iTextRenderer.createPDF(out);
            } catch (Exception e) {
                ITextRendererObjectFactory.getObjectPool().invalidateObject(iTextRenderer);
                iTextRenderer = null;
                throw e;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null)
                out.close();

            if (iTextRenderer != null) {
                try {
                    ITextRendererObjectFactory.getObjectPool().returnObject(iTextRenderer);
                } catch (Exception ex) {
                    logger.error("Cannot return object from pool.", ex);
                }
            }
        }
    }

    /**
     * 使用共通模板，生成pdf的预览html
     * 
     * @param mValues
     * @return
     * @throws Exception
     */
    public static String generatePDFHtml(Map mValues) throws Exception {
        return generatePDFHtml((String) mValues.get("template"), mValues);
    }

    /**
     * 使用共通模板，生成pdf的预览html
     * 
     * @param templateName
     * @param mValues
     * @return
     * @throws Exception
     */
    public static String generatePDFHtml(String templateName, Map mValues) throws Exception {
        BufferedWriter writer = null;
        String htmlContent = "";
        try {
            StringWriter stringWriter = new StringWriter();
            writer = new BufferedWriter(stringWriter);
            // 如果是使用共通模板的pdf,实例化共同模板
            if (CommonUtil.isNullorEmpty(templateName)) {
                initCommonTemplate();
                writer = new BufferedWriter(stringWriter);
                pdfCommonTemplate.setEncoding("UTF-8");
                pdfCommonTemplate.process(mValues, writer);
            } else {
                setTemplateWriter(templateName, mValues, writer);
            }

            htmlContent = stringWriter.toString();
            logger.debug(htmlContent);
            writer.flush();
        } finally {
            if (writer != null)
                writer.close();
        }
        return htmlContent;
    }

    /**
     * 根据模板文件名称读取模板内容，并将内容填充到模板中
     * 
     * @param templateName
     * @param mValues
     * @param writer
     * @throws Exception 
     */
    private static void setTemplateWriter(String templateName, Map mValues, BufferedWriter writer) throws Exception{
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate(templateName, mValues.get("content").toString());
        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(stringTemplateLoader);
        Template template = configuration.getTemplate(templateName);
        template.setEncoding("UTF-8");
        template.process(mValues, writer);
    }

    /**
     * 初始化共通模板
     * 
     * @param template
     * @throws Exception
     * @throws IOException
     */
    private static void initCommonTemplate() throws Exception, IOException {
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate(Constant.CONST_COMMON_PDF_TEMPLATE_NAME, PropertyUtil.readTemplate(Constant.CONST_COMMON_PDF_TEMPLATE_NAME));
        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(stringTemplateLoader);
        pdfCommonTemplate = configuration.getTemplate(Constant.CONST_COMMON_PDF_TEMPLATE_NAME);
    }
}