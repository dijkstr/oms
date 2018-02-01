package com.hundsun.boss.common;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.hundsun.boss.common.Mail;
import com.hundsun.boss.common.MailConfig;
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
            // 打印一些调试信息
            // session.setDebug(true);
        } catch (Exception e) {
            logger.error("MailUtil.initMail: error", e);
        }
    }

    /**
     * @Description: 发送邮件
     * 
     * @param recipient 收件人邮箱地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @throws AddressException
     * @throws MessagingException
     */
    public void sendMail(Mail mail, String isTest, String email) throws Exception {
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
            // 设置CC
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
                    mail.addRecipients(email);
                }
                message.addRecipients(Message.RecipientType.TO, mail.getRecipients());
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
    }
}
