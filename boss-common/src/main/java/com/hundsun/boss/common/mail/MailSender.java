package com.hundsun.boss.common.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.hundsun.boss.common.utils.CommonUtil;

/**
 * 发送邮件共通类
 * 
 */
public class MailSender {
    private static Logger logger = Logger.getLogger(MailSender.class);

    /**
     * 邮件服务器登录验证
     */
    private MailAuthenticator authenticator;

    /**
     * 邮箱session
     */
    private Session session;

    /**
     * 发件人地址
     */
    private String sender;

    /**
     * 初始化邮件发送器
     * 
     * @param mailConfig
     */
    public MailSender(MailConfig mailConfig) {
        initMail(mailConfig);
    }

    /**
     * 初始化邮件发送器
     * 
     * @param mailConfig
     */
    private void initMail(MailConfig mailConfig) {
        logger.debug("MailUtil.initMail: start");
        try {
            Properties props = new Properties();
            // 初始化props
            props.put("mail.smtp.host", mailConfig.getSmtphost());
            props.put("mail.smtp.auth", mailConfig.getSmtpauth());
            props.put("mail.smtp.port", mailConfig.getSmtpport());
            // 验证
            authenticator = new MailAuthenticator(mailConfig.getSmtpuser(), mailConfig.getSmtppassword());
            // 创建session
            session = Session.getInstance(props, authenticator);
            // 发件人地址
            sender = mailConfig.getSmtpsender();
            // 打印一些调试信息
            // session.setDebug(true);
        } catch (Exception e) {
            logger.error("MailUtil.initMail: error", e);
        }
    }

    /**
     * 发送邮件
     * 
     * @param mail
     * @param isTest
     * @param testEmail
     * @throws Exception
     */
    public void sendMail(Mail mail, String isTest, String testEmail) throws Exception {
        logger.debug("MailUtil.send: start");
        try {
            // 创建mime类型邮件
            MimeMessage message = new MimeMessage(session);
            // 设置发信人
            if (CommonUtil.isNullorEmpty(mail.getSenders())) {
                throw new Exception("邮件发送人地址不能为空");
            } else {
                message.addFrom(mail.getSenders());
            }
            // 设置BCC
            if (!CommonUtil.isNullorEmpty(mail.getBCCs())) {
                message.addRecipients(Message.RecipientType.BCC, mail.getBCCs());
            }
            // 设置CC
            if (!CommonUtil.isNullorEmpty(mail.getCCs())) {
                message.addRecipients(Message.RecipientType.CC, mail.getCCs());
            }
            // 设置收件人
            if (CommonUtil.isNullorEmpty(mail)) {
                throw new Exception("收信人地址不能为空");
            } else {
                if (CommonUtil.isNullorEmpty(isTest) || isTest.equals("0")) {
                    mail.clearRecipients();
                    mail.addRecipients(testEmail);
                }
                message.addRecipients(Message.RecipientType.TO, mail.getRecipients());
            }
            // 设置重要度(1:高   3:普通   5:低)  
            if (!CommonUtil.isNullorEmpty(mail.getImportance())) {
                message.setHeader("X-Priority", mail.getImportance());
            }
            // 要求阅读回执(收件人阅读邮件时会提示回复发件人,表明邮件已收到,并已阅读)  
            if (!CommonUtil.isNullorEmpty(mail.getIs_read()) && "1".equals(mail.getIs_read())) {
                message.setHeader("Disposition-Notification-To", sender);
            }
            // 设置主题
            message.setSubject(mail.getSubject());
            // 将特殊格式内容（例如附件）加入到信件
            message.setContent(mail.getMultipart(), "text/html;charset=utf-8");
            // 设置发信时间
            message.setSentDate(new Date());
            // 存储邮件信息
            message.saveChanges();
            // 发送邮件
            Transport.send(message);
            mail = null;
            message = null;
        } catch (Exception e) {
            logger.error("MailUtil.send: error", e);
            throw new Exception("邮件发送异常");
        }
        logger.debug("MailUtil.send: end");
    }
}
