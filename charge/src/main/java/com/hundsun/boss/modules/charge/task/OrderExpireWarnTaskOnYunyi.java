package com.hundsun.boss.modules.charge.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hundsun.boss.common.report.ReportUtils;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.service.order.OrderExpireWarningService;
import com.hundsun.boss.modules.sys.service.SysConfigService;

public class OrderExpireWarnTaskOnYunyi {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private OrderExpireWarningService orderExpireWarningService;

    @SuppressWarnings({ "unchecked", "rawtypes", })
    protected void execute() {
        try {
            List<Map> list = orderExpireWarningService.queryOrderWarningListOnYunYi();
            if (!CommonUtil.isNullorEmpty(list)) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = new HashMap<String, String>();
                    map.put("id", "ffd3e38cca7b11e7a63600505682144b");
                    String mailto = sysConfigService.getValue("bos.mail.yunyimailto");
                    String isTest = sysConfigService.getValue("bos.mail.istest");
                    if (CommonUtil.isNullorEmpty(isTest) || isTest.equals("0")) {
                        mailto = sysConfigService.getValue("bos.mail.testmailto");
                    }
                    ReportUtils.sendMail(mailto.trim(), "", "", map, list.get(i), sysConfigService);
                }
                logger.info("合同到期提醒邮件发送成功");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
