package com.hundsun.boss.modules.charge.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hundsun.boss.common.report.ReportUtils;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.modules.charge.service.order.OrderExpireWarningService;
import com.hundsun.boss.modules.sys.service.SysConfigService;

public class OrderExpireWarnTask {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private OrderExpireWarningService orderExpireWarningService;

    @SuppressWarnings({ "unchecked", "rawtypes", })
    protected void execute() {
        try {
            List<Map> list = orderExpireWarningService.queryOrderWarningList();
            if (!CommonUtil.isNullorEmpty(list)) {
                for (int i = 0; i < list.size(); i++) {
                    Map varibles = new HashMap();
                    varibles.put("contractid", list.get(i).get("contract_id"));
                    String orderEndDate = (String) list.get(i).get("order_end_date");
                    Date date = (Date) Formatter.parseUtilDate(Formatter.DATE_FORMAT1, orderEndDate);
                    String dateString = Formatter.formatDate(Formatter.DATE_FORMAT2, date);
                    varibles.put("order_end_date", dateString);
                    varibles.put("customer_id", list.get(i).get("customer_id"));
                    varibles.put("customer_name", list.get(i).get("customer_name"));

                    Map map = new HashMap<String, String>();
                    map.put("id", "7dd8c6e114c94c87807ea2b46b8f28ed");

                    String mailto = "";
                    if (!CommonUtil.isNullorEmpty(list.get(i).get("email"))) {
                        mailto = (String) list.get(i).get("email");
                    }
                    String isTest = sysConfigService.getValue("bos.mail.istest");
                    if (CommonUtil.isNullorEmpty(isTest) || isTest.equals("0")) {
                        mailto = sysConfigService.getValue("bos.mail.testmailto");
                    }
                    if (!CommonUtil.isNullorEmpty(mailto)) {
                        ReportUtils.sendMail(mailto.trim(), "", "", map, varibles, sysConfigService);
                    }
                }
                logger.info("合同到期提醒邮件发送成功");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
