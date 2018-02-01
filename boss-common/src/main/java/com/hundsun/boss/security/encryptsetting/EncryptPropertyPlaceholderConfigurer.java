/**
 * hundsun.com
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.hundsun.boss.security.encryptsetting;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 
 * @author shilei
 * @version $Id: EncryptPropertyPlaceholderConfigurer.java, v 0.1 2016年8月15日 下午7:05:28 shilei Exp $
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {

        //如果在加密属性名单中发现该属性  
        if (isEncryptProp(propertyValue)) {
            String[] values = propertyValue.split("HSP:");
            String value = values[1];
            String decryptValue = Decryption.decrypt(value);
            return decryptValue;
        } else {
            return propertyValue;
        }
    }

    /**
     * 判断属性的值是否需要解密
     * 
     * @param propertyValue
     * @return
     */
    private boolean isEncryptProp(String propertyValue) {
        if (StringUtils.isBlank(propertyValue)) {
            return false;
        }
        if (propertyValue.startsWith("HSP:")) {
            return true;
        }
        return false;
    }
}
