package com.hundsun.boss.util;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hundsun.boss.common.Formatter;
import com.hundsun.boss.common.Mail;
import com.hundsun.boss.common.MailConfig;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.entity.setting.ReportConfig;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillSearchForm;
import com.hundsun.boss.modules.charge.service.common.SysConfigService;
import com.hundsun.boss.modules.charge.service.finance.FinSummaryService;

/**
 * 导出utils类
 * 
 * @author whl
 *
 */

@SuppressWarnings({ "unused" })
public class ReportUtils {
    private ReportUtils() {
    };

    /**
     * 获取邮件的配置对象
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static MailConfig getMailConfig(String key, SysConfigService sysConfigService) throws Exception {
        MailConfig mailConfig = sysConfigService.getMailCongfig(key);
        Mail mail = new Mail();
        mail.setSubject(mailConfig.getMail_subject());
        String content = StringEscapeUtils.unescapeHtml(mailConfig.getMail_content());
        mail.setContent(content);
        //        mail.addBCCs(mailConfig.getBcc());
        mailConfig.setMail(mail);
        return mailConfig;
    }

}
