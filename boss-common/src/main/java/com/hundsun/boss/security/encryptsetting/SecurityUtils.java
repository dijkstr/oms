/**
 * hundsun.com
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.hundsun.boss.security.encryptsetting;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加密解密工具类
 * 
 * @author shilei
 * @version $Id: EncryptionUtils.java, v 0.1 2016年8月15日 下午10:49:57 shilei Exp $
 */
public class SecurityUtils {

    private static Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

    private SecurityUtils() {

    }

    /**
     * 获取密钥(32byte或者16byte)
     * 
     * @param mode
     * @return
     */
    public static byte[] getSecretKey(String mode) {

        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Get secretKey failed,cause by:{}", e);
        }
        if (null == digester) {
            return null;
        }
        digester.reset();
        digester.update(Constants.SALT);

        File seedFile = new File(Constants.Path.HOME_PATH + File.separator + Constants.FileName.SECRET_NAME);
        String seed = FileUtil.getString(seedFile);
        if (StringUtils.equals(Constants.Mode.ENCRYPT_MODE, mode) && StringUtils.isBlank(seed)) {
            seed = RandomStringUtils.randomAlphanumeric(108);
            try {
                FileUtil.createFile(Constants.Path.HOME_PATH, Constants.FileName.SECRET_NAME, seed);
            } catch (Exception e) {
                logger.error("Encryption failed,cause by:{}", e);
                return null;
            }
        }
        if (StringUtils.equals(Constants.Mode.DECRYPT_MODE, mode) && StringUtils.isBlank(seed)) {
            FileUtil.deleteFile(seedFile);
        }
        if (StringUtils.isBlank(seed)) {
            return null;
        }

        byte[] bytes = seed.getBytes();
        byte[] key = digester.digest(bytes);
        for (int i = 0; i < Constants.ITERATIONS; i++) {
            digester.reset();
            key = digester.digest(bytes);
        }
        if (StringUtils.equals(Constants.BITS_128, ContextUtil.get(Constants.Key.BITS_KEY))) {
            byte[] newkey = new byte[16];
            System.arraycopy(key, 0, newkey, 0, 16);
            return newkey;
        }
        return key;
    }

    /**
     * 获取向量(16byte)
     * 
     * @param mode
     * @return
     */
    public static byte[] getSecretIv(String mode) {
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Get secretIv failed,cause by:{}", e);
        }
        if (null == digester) {
            return null;
        }
        digester.reset();
        digester.update(Constants.SALT);

        File seedFile = new File(Constants.Path.CLASS_PATH + File.separator + Constants.FileName.IV_NAME);
        String seed = FileUtil.getString(seedFile);
        if (StringUtils.equals(Constants.Mode.ENCRYPT_MODE, mode) && StringUtils.isBlank(seed)) {
            seed = RandomStringUtils.randomAlphanumeric(108);
            try {
                FileUtil.createFile(Constants.Path.CLASS_PATH, Constants.FileName.IV_NAME, seed);
            } catch (Exception e) {
                logger.error("Encryption failed,cause by:{}", e);
                return null;
            }
        }
        if (StringUtils.equals(Constants.Mode.DECRYPT_MODE, mode) && StringUtils.isBlank(seed)) {
            FileUtil.deleteFile(seedFile);
        }
        if (StringUtils.isBlank(seed)) {
            return null;
        }

        byte[] bytes = seed.getBytes();
        byte[] key = digester.digest(bytes);
        for (int i = 0; i < Constants.ITERATIONS; i++) {
            digester.reset();
            key = digester.digest(bytes);
        }
        byte[] iv = new byte[16];
        System.arraycopy(key, 0, iv, 0, 16);
        return iv;
    }
}
