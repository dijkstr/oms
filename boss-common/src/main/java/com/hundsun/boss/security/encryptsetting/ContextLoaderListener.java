/**
 * hundsun.com
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.hundsun.boss.security.encryptsetting;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * web项目启动监听器
 * 
 * @author shilei
 * @version $Id: ContextLoaderListener.java, v 0.1 2016年8月8日 上午8:53:38 shilei Exp $
 */
public class ContextLoaderListener implements ServletContextListener {

    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    /**
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        EncryptStarter.start();
    }

}
