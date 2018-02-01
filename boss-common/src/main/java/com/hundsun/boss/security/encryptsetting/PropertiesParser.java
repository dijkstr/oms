/**
 * hundsun.com
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.hundsun.boss.security.encryptsetting;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * properties文件解析器
 * 
 * @author shilei
 * @version $Id: PropertiesParser.java, v 0.1 2016年8月11日 下午1:36:52 shilei Exp $
 */
public class PropertiesParser {

    private static Logger logger = LoggerFactory.getLogger(PropertiesParser.class);

    //properties文件地址
    private String profilepath = null;

    public PropertiesParser(String profilepath) {
        this.profilepath = profilepath;
    }

    /**
     * 根据key获得value
     * 
     * @param key
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
    public String getKeyValue(String key) {

        if (StringUtils.isBlank(key)) {
            return Constants.STR_BLANK;
        }

        Properties pros = new Properties();
        try {
            pros.load(new FileInputStream(profilepath));
        } catch (Exception e) {
            logger.error("Failed to get value({}) from file:{}", key, profilepath);
            return Constants.STR_BLANK;
        }

        return pros.getProperty(key);
    }

    /**
     * 修改
     * 
     * @param key
     * @param newValue
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void modifyProperties(String key, String newValue) throws FileNotFoundException, IOException {

        Properties pros = new Properties();
        pros.load(new FileInputStream(profilepath));
        OutputStream fos = new FileOutputStream(profilepath);
        pros.setProperty(key, newValue);
        pros.store(fos, "");
        fos.close();
    }
}
