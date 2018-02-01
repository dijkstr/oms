package com.hundsun.boss.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.hundsun.boss.base.hibernate.IdEntity;

/**
 * 导出配置Entity
 */
@Entity
@Table(name = "report_config_xml")
public class ReportConfig extends IdEntity<ReportConfig> {

    private static final long serialVersionUID = 1L;
    private String config_key;
    private String file_type;
    private String file_name;
    private String template;
    private String template_content;
    private String service;
    private String permission;
    private String contract_id;

    public ReportConfig() {
        super();
    }

    public ReportConfig(String id) {
        this();
        this.id = id;
    }

    public String getConfig_key() {
        return config_key;
    }

    public void setConfig_key(String config_key) {
        this.config_key = config_key;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTemplate_content() {
        return template_content;
    }

    public void setTemplate_content(String template_content) {
        this.template_content = template_content;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

}
