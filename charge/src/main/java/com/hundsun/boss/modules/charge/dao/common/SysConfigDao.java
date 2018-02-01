package com.hundsun.boss.modules.charge.dao.common;

import java.util.List;
import java.util.Map;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.common.MailConfig;
import com.hundsun.boss.modules.charge.entity.setting.ReportConfig;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;

/**
 * 账单DAO接口
 */
@MyBatisDao
@SuppressWarnings("rawtypes")
public interface SysConfigDao {
    /**
     * 获取报表配置信息
     * 
     * @param str
     * @return
     */
    public ReportConfig getReportCongfig(String reportKey);

    /**
     * 
     * 获取系统配置键值
     * 
     * @param req
     * @return
     */
    public String getValue(String key);

    /**
     * 获取邮件配置信息
     * 
     * @param str
     * @return
     */
    public MailConfig getMailCongfig(String mailKey);
    
    public List<Map> queryOrderRelationList(Map map);

    /**
     * 获取各个部门
     * 
     * @return
     * @throws Exception
     */
    public List<Map> getOrderSource(DownloadForm downloadForm);

    /**
     * 按部门获取billId
     * 
     * @return
     * @throws Exception
     */
    public List<Map> getBillIds(DownloadForm downloadForm);

    /**
     * 按部门获取销售
     * 
     * @return
     * @throws Exception
     */
    public List<Map> getSalers(DownloadForm downloadForm);
}
