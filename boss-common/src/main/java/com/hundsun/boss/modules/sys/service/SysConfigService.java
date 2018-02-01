package com.hundsun.boss.modules.sys.service;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.mail.MailConfig;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.sys.dao.SysConfigDao;
import com.hundsun.boss.modules.sys.dao.SysConfigMybatisDao;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.entity.Sysconfig;

/**
 * 账单Service
 */
@Component
@Transactional(readOnly = false)
@SuppressWarnings("rawtypes")
public class SysConfigService extends BaseService {

    @Autowired
    private SysConfigMybatisDao sysConfigMybatisDao;
    @Autowired
    private SysConfigDao sysconfigDao;

    /**
     * 获取导出配置信息
     */
    public ReportConfig getReportCongfig(String reportKey) {
        return sysConfigMybatisDao.getReportCongfig(reportKey);
    }

    /**
     * 按合同定制账单模板
     * 
     * @param contract
     * @return
     */
    public ReportConfig getCustomizeBillTemplateConfig(String contract) {
        return sysConfigMybatisDao.getCustomizeBillTemplateConfig(contract);
    }

    /**
     * 获取属性配置信息
     */
    public String getValue(String key) {
        return sysConfigMybatisDao.getValue(key);
    }

    /**
     * 获取邮件配置信息
     */
    public List<MailConfig> getMailCongfig(Map map) {
        return sysConfigMybatisDao.getMailCongfig(map);
    }

    /**
     * 获取系统参数配置
     * 
     * @param receiveid
     * @return
     */
    public Sysconfig get(String prop_name) {
        return sysconfigDao.get(prop_name);
    }

    /**
     * 查询系统配置
     * 
     * @param page
     * @param tsysconfigForm
     * @return
     */
    public Page<Sysconfig> find(Page<Sysconfig> page, Sysconfig tsysconfig) {
        DetachedCriteria dc = sysconfigDao.createDetachedCriteria();
        //系统参数名称
        if (StringUtils.isNotEmpty(tsysconfig.getProp_name())) {
            dc.add(Restrictions.like("prop_name", "%" + tsysconfig.getProp_name() + "%"));
        }
        dc.addOrder(Order.desc("prop_name"));
        return sysconfigDao.find(page, dc);
    }

    /**
     * 保存系统参数配置
     * 
     * @param tsysconfig
     */
    @Transactional(readOnly = false)
    public void save(Sysconfig tsysconfig) {
        sysconfigDao.save(tsysconfig);
    }

    /**
     * 删除系统参数配置
     * 
     * @param prop_name
     */
    @Transactional(readOnly = false)
    public void delete(String prop_name) {
        sysconfigDao.deleteById(prop_name);
    }
}
