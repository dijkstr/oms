package com.hundsun.boss.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;

import com.hundsun.boss.common.utils.CommonUtil;


/**
 * 邮件信息类
 * 
 */
public class Mail {

    // 收信人地址
    private List<InternetAddress> senders = new ArrayList<InternetAddress>();

    // 收信人地址
    private List<InternetAddress> CCs = new ArrayList<InternetAddress>();

    // 系统管理员地址
    private List<InternetAddress> BCCs = new ArrayList<InternetAddress>();

    // 收信人地址
    private List<InternetAddress> recipients = new ArrayList<InternetAddress>();

    // 主题
    private String subject;

    // 内容
    private String content;

    // 添加附件等
    private Multipart multipart = new MimeMultipart("mixed");

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }
    
    public void setContent(String content, String type) throws MessagingException{
        this.content = content;
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(content, type);
        this.multipart.addBodyPart(bodyPart);
    }

    public InternetAddress[] getSenders() {
        return (InternetAddress[]) CommonUtil.listToArray(senders, InternetAddress.class);
    }

    public InternetAddress[] getCCs() {
        return (InternetAddress[]) CommonUtil.listToArray(CCs, InternetAddress.class);
    }

    public InternetAddress[] getBCCs() {
        return (InternetAddress[]) CommonUtil.listToArray(BCCs, InternetAddress.class);
    }

    public InternetAddress[] getRecipients() {
        return (InternetAddress[]) CommonUtil.listToArray(recipients, InternetAddress.class);
    }

    public Multipart getMultipart() {
        return multipart;
    }

    // 添加发信人
    public void addSenders(String strSender) throws ParseException {
        this.senders.add(new InternetAddress(strSender));
    }
    
    // 添加发信人
    public void addSenders(String strSender, String nickName) throws Exception {
        this.senders.add(new InternetAddress(strSender, MimeUtility.encodeText(nickName)));
    }

    // 添加CC
    public void addBCCs(String bcc) throws ParseException {
        String[] aBCC = bcc.split(",");
        for (int i = 0; i < aBCC.length; i++) {
            this.BCCs.add(new InternetAddress(aBCC[i]));
        }
    }

    // 添加CC
    public void addCCs(String cc) throws ParseException {
        String[] aCC = cc.split(",");
        for (int i = 0; i < aCC.length; i++) {
            this.CCs.add(new InternetAddress(aCC[i]));
        }
    }
    
    public void clearRecipients(){
        recipients.clear();
    }

    // 添加收信人
    public void addRecipients(String strRecipient) throws ParseException {
        if (!CommonUtil.isNullorEmpty(strRecipient)) {
            String[] aRecipient = strRecipient.split(",");
            for (int i = 0; i < aRecipient.length; i++) {
                recipients.add(new InternetAddress(aRecipient[i]));
            }
        }
    }

    // 添加复杂结构内容
    public void addMultipart(BodyPart bodyPart) throws MessagingException {
        this.multipart.addBodyPart(bodyPart);
    }

    // 添加附件
    public void addAttachment(String strFileName) throws MessagingException, UnsupportedEncodingException {
        FileDataSource fileDataSource = new FileDataSource(strFileName);
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setDataHandler(new DataHandler(fileDataSource));
        bodyPart.setFileName(MimeUtility.encodeText(fileDataSource.getName()));
        this.multipart.addBodyPart(bodyPart);
    }

    @SuppressWarnings("rawtypes")
    public void injectVarible(Map varibles) throws MessagingException {
        this.setSubject(CommonUtil.injectString(this.getSubject(), varibles));
        this.setContent(CommonUtil.injectString(this.getContent(), varibles), "text/html;charset=utf-8");
    }

}
