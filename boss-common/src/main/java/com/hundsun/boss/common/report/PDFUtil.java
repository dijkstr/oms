package com.hundsun.boss.common.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ApplicationObjectSupport;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.SpringContextHolder;
import com.hundsun.boss.modules.sys.service.SysConfigService;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * PDF 生成
 */
@SuppressWarnings({ "rawtypes" })
public class PDFUtil extends ApplicationObjectSupport {
    private static final Logger logger = LoggerFactory.getLogger(PDFUtil.class);
    /* pdf导出共同模板文件名称 */
    public final static String CONST_COMMON_PDF_TEMPLATE_NAME = "CommonPdf.ftl";
    /*
     * PDF字体文件存放路径
     */
    public static String pdf_font_file_path;

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
    public static void generate(String htmlContent, String outputFile) throws Exception {
        OutputStream out = null;
        ITextRenderer iTextRenderer = null;
        try {
            // 绑定字体文件路径
            if (CommonUtil.isNullorEmpty(PDFUtil.pdf_font_file_path)) {
                SysConfigService sysConfigService = SpringContextHolder.getBean(SysConfigService.class);
                PDFUtil.pdf_font_file_path = sysConfigService.getValue("fontfilepath");
            }
            File f = new File(outputFile);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdir();
            }
            out = new FileOutputStream(outputFile);
            // 获取对象池中对象
            iTextRenderer = (ITextRenderer) ITextRendererObjectFactory.getObjectPool().borrowObject();
            SharedContext sharedContext = iTextRenderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            sharedContext.setReplacedElementFactory(new B64ImgReplacedElementFactory());
            sharedContext.getTextRenderer().setSmoothingThreshold(0);
            try {
                // 使用jsoup转换html中的文本中的字符，并按照xml的标准封闭img或者br这些在html中默认不关闭的标签
                Document document = Jsoup.parse(htmlContent);
                document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
                document.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
                if(!CommonUtil.isNullorEmpty(document.getElementById("remove"))){
                    document.getElementById("remove").remove();
                }                
                iTextRenderer.setDocumentFromString(document.html());
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
     * 使用模板,模板数据,生成PDF
     * 
     * @param templateName
     * @param mValues
     * @param outputFile
     * @throws Exception
     */
    public static void generate(String templateName, Map mValues, String outputFile) throws Exception {
        String htmlContent = generatePDFHtml(templateName, mValues);
        generate(htmlContent, outputFile);
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
    private static void setTemplateWriter(String templateName, Map mValues, BufferedWriter writer) throws Exception {
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
        stringTemplateLoader.putTemplate(CONST_COMMON_PDF_TEMPLATE_NAME, PropertyUtil.readTemplate(CONST_COMMON_PDF_TEMPLATE_NAME));
        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(stringTemplateLoader);
        pdfCommonTemplate = configuration.getTemplate(CONST_COMMON_PDF_TEMPLATE_NAME);
    }

    public static void main(String[] args) throws Exception {
        String htmlContent = "<img src='aa'>bb<br>";
        String outputFile = "d:/test.pdf";

        Document document = Jsoup.parse(htmlContent);

        OutputStream out = null;
        File f = new File(outputFile);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdir();
        }
        out = new FileOutputStream(outputFile);

        ITextRenderer iTextRenderer = new ITextRenderer();
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        document.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        String html = document.html();
        iTextRenderer.setDocumentFromString(html);

        iTextRenderer.layout();
        iTextRenderer.createPDF(out);
        out.close();
    }
}