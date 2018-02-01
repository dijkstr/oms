/**
 * hundsun.com
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.hundsun.boss.security.encryptsetting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * xml文件解析
 * 
 * @author shilei
 * @version $Id: XMLParser.java, v 0.1 2016年8月5日 下午2:44:11 shilei Exp $
 */
public class XMLParser {

    private static Logger logger = LoggerFactory.getLogger(XMLParser.class);

    private String xmlPath = null;

    public XMLParser(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    /**
     * 根据xpath表达式读取xml节点信息
     * 
     * 约束：如果xml中存在命名空间：不支持命名空间嵌套；一次只能读取同一种命名空间的节点
     * 
     * @param namespace 读取节点所在空间
     * @param xpathExpression xpath表达式
     * @return
     * @throws DocumentException
     */
    public List<Map<String, String>> readNodes(String namespace, String xpathExpression) {

        if (StringUtils.isBlank(xpathExpression)) {
            return null;
        }

        SAXReader reader = new SAXReader();
        File xmlFile = new File(xmlPath);
        Document doc = null;
        try {
            doc = reader.read(xmlFile);
        } catch (DocumentException e) {
            logger.error("Failed to read XML file:{}", xmlPath);
        }

        if (null == doc) {
            return null;
        }

        XPath xpath = null;
        if (StringUtils.isBlank(namespace)) {
            xpath = doc.createXPath(xpathExpression);
        } else {
            String alias = "default";
            Map<String, String> namespaces = new HashMap<String, String>();
            namespaces.put(alias, namespace);
            xpath = doc.createXPath(xpathExpression);
            xpath.setNamespaceURIs(namespaces);
        }

        List<?> nodes = xpath.selectNodes(doc);
        if (CollectionUtils.isEmpty(nodes)) {
            return null;
        }

        List<Map<String, String>> results = new ArrayList<Map<String, String>>();
        Element element = null;
        for (int i = 0; i < nodes.size(); i++) {
            element = (Element) nodes.get(i);
            List<?> attributes = element.attributes();
            if (CollectionUtils.isEmpty(attributes)) {
                continue;
            }
            Map<String, String> result = new HashMap<String, String>();
            Attribute attribute = null;
            for (int j = 0; j < attributes.size(); j++) {
                attribute = (Attribute) attributes.get(j);
                result.put(attribute.getName(), attribute.getValue());
            }
            results.add(result);
        }

        return results;
    }

    /**
     * 根据xpath表达式读取xml节点信息
     * 
     * 约束：如果xml中存在命名空间：不支持命名空间嵌套；一次只能读取同一种命名空间的节点
     * 
     * @param namespace 读取节点所在空间
     * @param xpathExpression xpath表达式
     * @return
     * @throws DocumentException
     */
    public Map<String, String> readNode(String namespace, String xpathExpression) {
        if (StringUtils.isBlank(xpathExpression)) {
            return null;
        }

        SAXReader reader = new SAXReader();
        File xmlFile = new File(xmlPath);
        Document doc = null;
        try {
            doc = reader.read(xmlFile);
        } catch (DocumentException e) {
            logger.error("Failed to read XML file:{}", xmlPath);
        }

        if (null == doc) {
            return null;
        }

        XPath xpath = null;
        if (StringUtils.isBlank(namespace)) {
            xpath = doc.createXPath(xpathExpression);
        } else {
            String alias = "default";
            Map<String, String> namespaces = new HashMap<String, String>();
            namespaces.put(alias, namespace);
            xpath = doc.createXPath(xpathExpression);
            xpath.setNamespaceURIs(namespaces);
        }

        Element element = (Element) xpath.selectSingleNode(doc);
        if (null == element) {
            return null;
        }
        List<?> attributes = element.attributes();
        Map<String, String> result = new HashMap<String, String>();
        Attribute attribute = null;
        for (int j = 0; j < attributes.size(); j++) {
            attribute = (Attribute) attributes.get(j);
            result.put(attribute.getName(), attribute.getValue());
        }

        return result;
    }

    /**
     * 根据xpath定位节点，并修改指定属性(name)的值(newValue)
     * 
     * @param xpath
     * @param name
     * @param newValue
     * @throws DocumentException
     * @throws IOException
     */
    public void modifyNode(String namespace, String xpathExpression, String key, String newValue) throws DocumentException, IOException {
        if (StringUtils.isBlank(xpathExpression) || StringUtils.isBlank(key) || StringUtils.isBlank(newValue)) {
            logger.error("Modify node failed, xpathExpression、key or newValue is empty");
            return;
        }

        SAXReader reader = new SAXReader();
        File xmlFile = new File(xmlPath);
        Document doc = reader.read(xmlFile);
        XPath xpath = null;

        if (StringUtils.isBlank(namespace)) {
            xpath = doc.createXPath(xpathExpression);
        } else {
            String alias = "default";
            Map<String, String> namespaces = new HashMap<String, String>();
            namespaces.put(alias, namespace);
            xpath = doc.createXPath(xpathExpression);
            xpath.setNamespaceURIs(namespaces);
        }

        Element element = (Element) xpath.selectSingleNode(doc);
        if (null == element) {
            return;
        }
        List<?> attributes = element.attributes();
        Attribute attribute = null;
        for (int j = 0; j < attributes.size(); j++) {
            attribute = (Attribute) attributes.get(j);
            if (StringUtils.equals(key, attribute.getName())) {
                attribute.setValue(newValue);
                break;
            }
        }

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(new FileOutputStream(xmlFile), format);
        writer.write(doc);
        writer.flush();
        writer.close();
    }
}
