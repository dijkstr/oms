/**
 * hundsun.com
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.hundsun.boss.security.encryptsetting;

/**
 * 常量类
 * 
 * @author shilei
 * @version $Id: Constants.java, v 0.1 2016年8月15日 下午10:29:05 shilei Exp $
 */
public class Constants {

    //系统名称
    public static final String OS_NAME = System.getProperty("os.name");

    //加密之后的密文前缀
    public static final String SECURITY_PREFIX = "HSP:";

    public static final int ITERATIONS = 10240;

    public static final String BITS_128 = "128";

    public static final String BITS_256 = "256";

    public static final String STR_BLANK = "";

    public static final byte[] SALT = "vgPLm3YRLi0iXHBUF72HlVOIGJTEKCa7sNTEYTqtU7ZAHhJZw".getBytes();

    /**
     * 加解密相关文件名称
     * 
     * @author shilei
     * @version $Id: Constants.java, v 0.1 2016年8月15日 下午10:34:33 shilei Exp $
     */
    public interface FileName {

        //向量文件名称
        public static final String IV_NAME = ".minor.hssecret";

        //密钥文件名称
        public static final String SECRET_NAME = ".adult.hssecret";

        //加密字段文件名称
        public static final String SECURITY_CONFIG_NAME = "security-config.xml";
    }

    /**
     * 文件类型后缀
     * 
     * @author shilei
     * @version $Id: Constants.java, v 0.1 2016年8月15日 下午10:35:02 shilei Exp $
     */
    public interface FileSuffix {
        //xml文件后缀
        public static final String XML_SUFFIX = "xml";

        //properties文件后缀
        public static final String PROPERTIES_SUFFIX = "properties";
    }

    /**
     * 路径常量
     * 
     * @author shilei
     * @version $Id: Constants.java, v 0.1 2016年8月15日 下午10:47:42 shilei Exp $
     */
    public interface Path {
        //用户目录路径
        public static final String HOME_PATH = System.getProperty("user.home");

        //项目跟路径
        public static final String CLASS_PATH = Constants.class.getClassLoader().getResource("").getPath();

    }

    /**
     * XPath表达式常量
     * 
     * @author shilei
     * @version $Id: Constants.java, v 0.1 2016年8月15日 下午10:47:55 shilei Exp $
     */
    public interface XPath {
        //文件地址XPath表达式
        public static final String FILE_XPATH = "/files/file";

        public static final String ARGS_XPATH = "/files/args";
    }

    /**
     * 
     * 
     * @author shilei
     * @version $Id: Constants.java, v 0.1 2016年8月19日 上午9:57:28 shilei Exp $
     */
    public interface Key {

        public static final String BITS_KEY = "bits";

        public static final String ONOFF_KEY = "onoff";
    }

    public interface Mode {

        //解密模式
        public static final String DECRYPT_MODE = "2";

        //加密模式
        public static final String ENCRYPT_MODE = "1";

    }
}
