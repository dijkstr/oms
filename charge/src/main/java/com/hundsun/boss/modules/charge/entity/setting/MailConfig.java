package com.hundsun.boss.modules.charge.entity.setting;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.hundsun.boss.base.hibernate.IdEntity;

/**
 * 邮件配置Entity
 */
@Entity
@Table(name = "mail_config_xml")
public class MailConfig extends IdEntity<MailConfig> {

    private static final long serialVersionUID = 1L;

    private String department_name;
    private String smtpauth;
    private String smtphost;
    private String smtpport;
    private String smtpsender;
    private String bcc;
    private String smtpuser;
    private String smtppassword;
    private String template;

    private String mail_subject;
    private String mail_content;

    private String office_id;

    public MailConfig() {
        super();
    }

    public MailConfig(String id) {
        this();
        this.id = id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getSmtpauth() {
        return smtpauth;
    }

    public void setSmtpauth(String smtpauth) {
        this.smtpauth = smtpauth;
    }

    public String getSmtphost() {
        return smtphost;
    }

    public void setSmtphost(String smtphost) {
        this.smtphost = smtphost;
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

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getSmtpuser() {
        return smtpuser;
    }

    public void setSmtpuser(String smtpuser) {
        this.smtpuser = smtpuser;
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

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

}
