package com.hundsun.boss.modules.sys.dao;

import java.util.List;
import java.util.Map;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.common.mail.MailConfig;
import com.hundsun.boss.modules.sys.entity.ReportConfig;

/**
 * 账单DAO接口
 */
@MyBatisDao
@SuppressWarnings("rawtypes")
public interface SysConfigMybatisDao {
    /**
     * 获取报表配置信息
     * 
     * @param str
     * @return
     */
    public ReportConfig getReportCongfig(String reportKey);

    /**
     * 按合同定制账单模板
     * 
     * @param contractId
     * @return
     */
    public ReportConfig getCustomizeBillTemplateConfig(String contractId);

    /**
     * 
     * 获取系统配置键值
     * 
     * @param key
     * @return
     */
    public String getValue(String key);

    /**
     * 获取邮件配置信息
     * 
     * @param mParams
     * @return
     */
    public List<MailConfig> getMailCongfig(Map mParams);

}
