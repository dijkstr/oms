/**
 * hundsun.com
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.hundsun.boss.security.encryptsetting;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author shilei
 * @version $Id: Decryption.java, v 0.1 2016年8月15日 下午7:06:40 shilei Exp $
 */
public class Decryption {

    private static Logger logger = LoggerFactory.getLogger(Decryption.class);

    /**
     * 解密加密的字符串
     * 
     * @param value
     * @return
     * @throws Exception
     */
    public static String decrypt(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        //1、获取密钥
        byte[] secretKey = SecurityUtils.getSecretKey(Constants.Mode.DECRYPT_MODE);
        if (null == secretKey || secretKey.length < 1) {
            logger.error("Unable to obtain the key, decryption failure");
            return null;
        }

        //2、获取向量
        byte[] secretIv = SecurityUtils.getSecretIv(Constants.Mode.DECRYPT_MODE);
        if (null == secretIv || secretIv.length < 1) {
            logger.error("Unable to obtain the key, decryption failure");
            return null;
        }

        return AESUtils.decrypt(value, secretKey, secretIv);
    }
}
