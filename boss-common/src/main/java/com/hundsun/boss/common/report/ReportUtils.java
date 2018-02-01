package com.hundsun.boss.common.report;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import com.hundsun.boss.common.mail.Mail;
import com.hundsun.boss.common.mail.MailConfig;
import com.hundsun.boss.common.mail.MailSender;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.CommonUtils;
import com.hundsun.boss.modules.sys.service.SysConfigService;

/**
 * 导出utils类
 * 
 * @author whl
 *
 */

@SuppressWarnings({ "rawtypes" })
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
    public static List<MailConfig> getMailConfig(Map map, SysConfigService sysConfigService) throws Exception {
        List<MailConfig> mailConfigs = sysConfigService.getMailCongfig(map);
        MailConfig mailConfig = null;
        for (int i = 0; i < mailConfigs.size(); i++) {
            mailConfig = mailConfigs.get(i);
            Mail mail = new Mail();
            mail.setSubject(mailConfig.getMail_subject());
            String content = StringEscapeUtils.unescapeHtml(mailConfig.getMail_content());
            mail.setContent(content);
            if (!CommonUtil.isNullorEmpty(mailConfig.getCc())) {
                mail.addCCs(mailConfig.getCc());
            }
            if (!CommonUtil.isNullorEmpty(mailConfig.getBcc())) {
                mail.addBCCs(mailConfig.getBcc());
            }
            mailConfig.setMail(mail);
        }
        return mailConfigs;
    }

    /**
     * 发送邮件
     * 
     * @param mailto
     * @param mailcc
     * @param mailbcc
     * @param map
     * @param varibles
     * @param sysConfigService
     * @throws Exception
     */
    public static void sendMail(String mailto, String mailcc, String mailbcc, Map map, Map varibles, SysConfigService sysConfigService) throws Exception {

        MailConfig mailConfig = getMailConfig(map, sysConfigService).get(0);
        MailSender mailSender = new MailSender(mailConfig);
        Mail mail = mailConfig.getMail();
        mail.injectVarible(varibles);
        mail.addSenders(mailConfig.getSmtpsender(), sysConfigService.getValue("sendNickName"));
        // 设置收信人
        mail.addRecipients(mailto);
        // 抄送
        if (!CommonUtils.isNullorEmpty(mailcc)) {
            mail.addCCs(mailcc);
        }
        // 密抄
        if (!CommonUtils.isNullorEmpty(mailbcc)) {
            mail.addBCCs(mailbcc);
        }
        String isTest = sysConfigService.getValue("bos.mail.istest");
        String testmailto = sysConfigService.getValue("bos.mail.testmailto");
        mailSender.sendMail(mail, isTest, testmailto);
    }
}
