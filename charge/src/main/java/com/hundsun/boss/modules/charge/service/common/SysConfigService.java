package com.hundsun.boss.modules.charge.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.MailConfig;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.dao.common.SysConfigDao;
import com.hundsun.boss.modules.charge.entity.setting.ReportConfig;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 账单Service
 */
@Component
@Transactional(readOnly = false)
@SuppressWarnings("rawtypes")
public class SysConfigService extends BaseService {

    @Autowired
    private SysConfigDao sysConfigDao;

    /**
     * 获取导出配置信息
     */
    public ReportConfig getReportCongfig(String reportKey) {
        return sysConfigDao.getReportCongfig(reportKey);
    }

    /**
     * 获取属性配置信息
     */
    public String getValue(String key) {
        return sysConfigDao.getValue(key);
    }
    
    /**
     * 获取属性配置信息
     */
    public List<Map> queryOrderRelationList(Map map) {
        return sysConfigDao.queryOrderRelationList(map);
    }
    

    /**
     * 获取邮件配置信息
     */
    public MailConfig getMailCongfig(String mailKey) {
        return sysConfigDao.getMailCongfig(mailKey);
    }

    public List<Map> getOrderSource(DownloadForm downloadForm) {
        if (CommonUtil.isNullorEmpty(downloadForm.getDept())) {
            User currentUser = UserUtils.getUser();
            downloadForm.setDept(getDept(currentUser, "office", ""));
        }
        return sysConfigDao.getOrderSource(downloadForm);
    }

    public List<Map> getBillIds(DownloadForm downloadForm) {
        return sysConfigDao.getBillIds(downloadForm);
    }

    public List<Map> getSalers(DownloadForm downloadForm) {
        return sysConfigDao.getSalers(downloadForm);
    }

}
