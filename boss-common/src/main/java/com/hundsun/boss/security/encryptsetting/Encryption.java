/**
 * hundsun.com
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.hundsun.boss.security.encryptsetting;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * 
 * @author shilei
 * @version $Id: Encryption.java, v 0.1 2016年8月3日 下午4:29:17 shilei Exp $
 */
public class Encryption {

    private static Logger logger = LoggerFactory.getLogger(Encryption.class);

    public static void encrypt() {

        //1、获取密钥
        byte[] secretKey = SecurityUtils.getSecretKey(Constants.Mode.ENCRYPT_MODE);
        if (null == secretKey || secretKey.length < 1) {
            logger.error("Unable to obtain the key, end");
            return;
        }

        //2、获取向量
        byte[] secretIv = SecurityUtils.getSecretIv(Constants.Mode.ENCRYPT_MODE);
        if (null == secretIv || secretIv.length < 1) {
            logger.error("Unable to obtain the key, end");
            return;
        }

        //3、从security-config.xml文件中读取需要加密的字段所在的文件
        String configPath = Constants.Path.CLASS_PATH + File.separator + Constants.FileName.SECURITY_CONFIG_NAME;
        XMLParser configParser = new XMLParser(configPath);
        List<Map<String, String>> fileInfos = configParser.readNodes(null, Constants.XPath.FILE_XPATH);
        if (CollectionUtils.isEmpty(fileInfos)) {
            return;
        }

        //4、获取每个文件需要加密的字段，并加密字段
        String fieldXPath = null;
        String filePath = null;
        String suffix = null;
        List<Map<String, String>> fieldInfos = null;
        for (Map<String, String> fileInfo : fileInfos) {
            filePath = fileInfo.get("path");
            //根据filePath获取当前文件需要加密的字段信息
            fieldXPath = Constants.XPath.FILE_XPATH + "[@" + "path='" + filePath + "']" + "/field";
            fieldInfos = configParser.readNodes(null, fieldXPath);
            if (CollectionUtils.isEmpty(fieldInfos)) {
                continue;
            }
            suffix = getSuffix(filePath);
            filePath = Constants.Path.CLASS_PATH + formatFliePath(filePath);
            if (StringUtils.equals(suffix, Constants.FileSuffix.XML_SUFFIX)) {
                encryptXml(fieldInfos, filePath, secretKey, secretIv);
            } else if (StringUtils.equals(suffix, Constants.FileSuffix.PROPERTIES_SUFFIX)) {
                encryptProp(fieldInfos, filePath, secretKey, secretIv);
            }
        }
    }

    /**
     * 格式化文件路径
     * 
     * @param path
     * @return
     */
    private static String formatFliePath(String path) {
        return path.replace("/", File.separator);
    }

    /**
     * 加密XML文件中的字段
     * 
     * @param fieldInfos
     * @param filePath
     * @param secretKey
     * @param secretIv
     * @throws Exception
     */
    private static void encryptXml(List<Map<String, String>> fieldInfos, String filePath, byte[] secretKey, byte[] secretIv) {
        String name = null;
        String expression = null;
        String namespace = null;
        XMLParser parser = new XMLParser(filePath);
        String newValue = null;
        for (Map<String, String> fieldInfo : fieldInfos) {
            name = fieldInfo.get("name");
            expression = fieldInfo.get("expression");
            namespace = fieldInfo.get("namespace");
            Map<String, String> node = parser.readNode(namespace, expression);
            if (CollectionUtils.isEmpty(node) || StringUtils.isBlank(node.get(name)) || StringUtils.contains(node.get(name), Constants.SECURITY_PREFIX)) {
                continue;
            }
            newValue = Constants.SECURITY_PREFIX + AESUtils.encrypt(node.get(name), secretKey, secretIv);
            try {
                parser.modifyNode(namespace, expression, name, newValue);
            } catch (Exception e) {
                logger.warn("Encryption [{}] failed,cause by:{}", name, e);
            }
        }
    }

    /**
     * 加密properties文件中的字段
     * 
     * @param fieldInfos
     * @param filePath
     * @param secretKey
     * @param secretIv
     * @throws Exception
     */
    private static void encryptProp(List<Map<String, String>> fieldInfos, String filePath, byte[] secretKey, byte[] secretIv) {
        String name = null;
        String value = null;
        String newValue = null;
        PropertiesParser parser = new PropertiesParser(filePath);
        for (Map<String, String> fieldInfo : fieldInfos) {
            name = fieldInfo.get("name");
            value = parser.getKeyValue(name);
            if (StringUtils.isBlank(value) || StringUtils.contains(value, Constants.SECURITY_PREFIX)) {
                continue;
            }
            newValue = Constants.SECURITY_PREFIX + AESUtils.encrypt(value, secretKey, secretIv);
            try {
                parser.modifyProperties(name, newValue);
            } catch (Exception e) {
                logger.warn("Encryption [{}] failed,cause by:{}", name, e);
            }
        }
    }

    /**
     * 根据文件路径获取文件后缀
     * 
     * @param path
     * @return
     */
    private static String getSuffix(String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        int position = path.lastIndexOf(".");
        String suffix = path.substring(position + 1);
        return suffix;
    }
}
