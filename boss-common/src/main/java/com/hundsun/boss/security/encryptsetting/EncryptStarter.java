/**
 * hundsun.com
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.hundsun.boss.security.encryptsetting;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加密启动器
 * 
 * @author shilei
 * @version $Id: Starter.java, v 0.1 2016年8月26日 上午9:57:42 shilei Exp $
 */
public class EncryptStarter {

    private static Logger logger = LoggerFactory.getLogger(EncryptStarter.class);

    public static void start() {

        String configPath = Constants.Path.CLASS_PATH + File.separator + Constants.FileName.SECURITY_CONFIG_NAME;
        XMLParser parser = new XMLParser(configPath);

        String onoff = "off";
        String bits = "128";

        try {
            Map<String, String> args = parser.readNode("", Constants.XPath.ARGS_XPATH);
            bits = args.get(Constants.Key.BITS_KEY);
            onoff = args.get(Constants.Key.ONOFF_KEY);
            if (!StringUtils.equals(onoff, "on")) {
                logger.info("Development model, the end of encryption");
                return;
            }
        } catch (Exception ex) {
            logger.error("Failed to read properties from the file:{}", configPath);
            return;
        }

        ContextUtil.set(Constants.Key.BITS_KEY, bits);

        Encryption.encrypt();
    }
}
