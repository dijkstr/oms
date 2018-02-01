package com.hundsun.boss.common;

import java.util.HashMap;
import java.util.Map;

public class ReportConfig {
    private String key;
    private String template;
    private Map<String, ReportTemplate> templates = new HashMap<String, ReportTemplate>();
    private String type;
    private String controller;
    private String service;
    private String smtphost;
    private String smtpauth;
    private String smtpport;
    private String smtpsender;
    private String smtppassword;   

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, ReportTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(Map<String, ReportTemplate> templates) {
        this.templates = templates;
    }

    public void addTemplate(String key, ReportTemplate template) {
        this.templates.put(key, template);
    }

    public String getType() {
        return type;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSmtphost() {
        return smtphost;
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

}
