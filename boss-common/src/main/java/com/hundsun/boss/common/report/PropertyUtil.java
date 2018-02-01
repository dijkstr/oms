package com.hundsun.boss.common.report;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hundsun.boss.common.utils.CommonUtil;

public class PropertyUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);


    /**
     * 读取配置文件
     * 
     * @param configName
     * @param key
     * @return
     */
    public static String readProperty(String configPath, String configName, String key) {
        String retValue = "";
        try {
            URL url = CommonUtil.class.getResource(configPath + configName);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = null;
            try {
                inputStream = urlConnection.getInputStream();
                Properties p = new Properties();
                p.load(inputStream);
                retValue = p.getProperty(key);
            } finally {
                if(null != inputStream)
                inputStream.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return retValue;
    }

    /**
     * 根据配置读取XML对象
     * 
     * @param configName
     * @return
     * @throws Exception
     */
    public static Document getDocumentByPath(String configPath, String externalPath) throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        URL url = CommonUtil.class.getResource(configPath + externalPath);
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        Document doc = db.parse(inputStream);
        return doc;
    }

    public static String toString(Node node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        Transformer transformer = newTransformer();
        if (transformer != null) {
            try {
                StringWriter sw = new StringWriter();
                transformer.transform(new DOMSource(node), new StreamResult(sw));
                return sw.toString();
            } catch (TransformerException te) {
                throw new RuntimeException(te.getMessage());
            }
        }
        return "";
    }

    public static Transformer newTransformer() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Properties properties = transformer.getOutputProperties();
            properties.setProperty(OutputKeys.ENCODING, "utf-8");
            properties.setProperty(OutputKeys.VERSION, "1.0");
            properties.setProperty(OutputKeys.INDENT, "no");
            transformer.setOutputProperties(properties);
            return transformer;
        } catch (TransformerConfigurationException tce) {
            throw new RuntimeException(tce.getMessage());
        }
    }


    /**
     * 获取报表的配置对象
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static List<ReportColumn> getReportColumns(String content) throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
        Document doc = db.parse(inputStream);
        List<ReportColumn> reportColumns = new ArrayList<ReportColumn>();

        NodeList columns = doc.getElementsByTagName("column");
        ReportColumn reportColumn = null;
        for (int i = 0; i < columns.getLength(); i++) {
            Element ereportColumn = (Element) columns.item(i);
            reportColumn = new ReportColumn();
            reportColumn.setFormat(ereportColumn.getAttribute("format"));
            reportColumn.setType(ereportColumn.getAttribute("type"));
            reportColumn.setHeader(ereportColumn.getAttribute("header"));
            reportColumn.setField(ereportColumn.getAttribute("field"));
            reportColumn.setIskey(ereportColumn.getAttribute("iskey"));
            reportColumns.add(reportColumn);
        }
        return reportColumns;
    }

    /**
     * 读取指定模板中的文字内容
     * 
     * @param template
     * @return
     * @throws Exception
     */
    public static String readTemplate(String templateName) throws Exception {

        try {
            URL url = CommonUtil.class.getResource("/report/templates/" + templateName);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = null;
            BufferedReader in = null;
            StringBuffer buffer = new StringBuffer();
            try {
                inputStream = urlConnection.getInputStream();
                in = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                String line = " ";
                while ((line = in.readLine()) != null) {
                    buffer.append(line);
                }
            } finally {
                if(null != inputStream)
                inputStream.close();
                if(null != in)
                in.close();
            }
            return buffer.toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

}
