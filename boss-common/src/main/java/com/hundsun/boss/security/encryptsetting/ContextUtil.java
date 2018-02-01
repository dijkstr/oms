/**
 * hundsun.com
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.hundsun.boss.security.encryptsetting;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author shilei
 * @version $Id: ContextUtil.java, v 0.1 2016年8月19日 上午10:33:56 shilei Exp $
 */
public class ContextUtil {

    //环境变量存放
    private static ConcurrentHashMap<String, String> context = new ConcurrentHashMap<String, String>();

    public static String get(String key) {
        if (context.containsKey(key)) {
            return context.get(key);
        }
        return null;
    }

    public static void set(String key, String value) {
        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
            context.put(key, value);
        }
    }

}
