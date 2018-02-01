/**
 * hundsun.com
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.hundsun.boss.security.encryptsetting;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import net.iharder.Base64;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * java 使用AES算法使用256密钥加解密 <br />
 * 
 * 依赖说明： <br />
 * bcprov-jdk16-1.45.jar：PKCS7Padding <br />
 * maven依赖 <br />
 * groupId : org.bouncycastle <br />
 * artifactId : bcprov-jdk16 <br />
 * version : 1.45 <br />
 * 
 * javabase64-2.3.9.jar：base64 <br />
 * maven依赖 <br />
 * groupId : net.iharder <br />
 * artifactId : base64 <br />
 * version : 2.3.9 <br />
 * 
 * 下载JDK配套的local_policy.jar 和 US_export_policy.jar添加到%JAVE_HOME%/jre/lib/security中
 * 
 * @author shilei
 * @version $Id: AES256Utils.java, v 0.1 2016年7月26日 上午8:45:15 shilei Exp $
 */
public class AESUtils {

    private static Logger logger = LoggerFactory.getLogger(AESUtils.class);

    public static final String KEY_ALGORITHM = "AES";

    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    private AESUtils() {

    }

    /**
     * 加密
     * 
     * @param content 待加密内容
     * @param secretKey 密钥
     * @param iv 向量
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, byte[] secretKey, byte[] iv) {

        //根据给定的字节数组构造一个密钥。enCodeFormat：密钥内容；"AES"：与给定的密钥内容相关联的密钥算法的名称  
        SecretKeySpec sks = new SecretKeySpec(secretKey, KEY_ALGORITHM);
        //将提供程序添加到下一个可用位置
        Security.addProvider(new BouncyCastleProvider());

        try {
            //创建一个实现指定转换的 Cipher对象，该转换由指定的提供程序提供。  
            //"AES/ECB/PKCS7Padding"：转换的名称；"BC"：提供程序的名称  
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
            //创建向量
            IvParameterSpec ivps = new IvParameterSpec(iv);
            //初始化一个 Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, sks, ivps);
            byte[] byteContent = content.getBytes("utf-8");
            byte[] cryptograph = cipher.doFinal(byteContent);
            return Base64.encodeBytes(cryptograph);
        } catch (Exception e) {
            logger.error("Encryption failed,cause by:{}", e);
        }
        return Constants.STR_BLANK;
    }

    /**
     * 解密
     * 
     * @param content 待解密的内容
     * @param secretKey 密钥
     * @param iv 向量
     * @return
     * @throws Exception
     */
    public static String decrypt(String content, byte[] secretKey, byte[] iv) {

        SecretKeySpec sks = new SecretKeySpec(secretKey, KEY_ALGORITHM);
        Security.addProvider(new BouncyCastleProvider());
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
            IvParameterSpec ivps = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, sks, ivps);
            byte[] result = cipher.doFinal(Base64.decode(content));
            return new String(result);
        } catch (Exception e) {
            logger.error("Decryption failure,cause by:{}", e);
        }

        return Constants.STR_BLANK;
    }
}
