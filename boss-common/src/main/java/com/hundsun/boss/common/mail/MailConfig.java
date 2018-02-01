package com.hundsun.boss.common.mail;

public class MailConfig {
    private String id;
    private String key;
    private String smtphost;
    private String smtpauth;
    private String smtpport;
    private String smtpsender;
    private String smtpuser;
    private String smtppassword;
    private String template;
    private Mail mail;
    private String mail_subject;
    private String mail_content;
    private String cc;
    private String bcc;
    private String file_path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSmtphost() {
        return smtphost;
    }

    public String getSmtpuser() {
        return smtpuser;
    }

    public void setSmtpuser(String smtpuser) {
        this.smtpuser = smtpuser;
    }

    public void setSmtphost(String smtphost) {
        this.smtphost = smtphost;
    }

    public String getSmtpauth() {
        return smtpauth;
    }

    public void setSmtpauth(String smtpauth) {
        this.smtpauth = smtpauth;
    }

    public String getSmtpport() {
        return smtpport;
    }

    public void setSmtpport(String smtpport) {
        this.smtpport = smtpport;
    }

    public String getSmtpsender() {
        return smtpsender;
    }

    public void setSmtpsender(String smtpsender) {
        this.smtpsender = smtpsender;
    }

    public String getSmtppassword() {
        return smtppassword;
    }

    public void setSmtppassword(String smtppassword) {
        this.smtppassword = smtppassword;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public String getMail_subject() {
        return mail_subject;
    }

    public void setMail_subject(String mail_subject) {
        this.mail_subject = mail_subject;
    }

    public String getMail_content() {
        return mail_content;
    }

    public void setMail_content(String mail_content) {
        this.mail_content = mail_content;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

}
