package com.hundsun.boss.modules.charge.workflow.servicetask;

import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hundsun.boss.modules.charge.common.ChargeConstant;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.dao.audit.OrderInfoAuditDao;
import com.hundsun.boss.modules.charge.dao.order.OrderInfoDao;
import com.hundsun.boss.modules.charge.entity.audit.OrderInfoAudit;
import com.hundsun.boss.modules.charge.entity.order.OrderInfo;
import com.hundsun.boss.modules.charge.service.audit.OrderInfoAuditService;
import com.hundsun.boss.modules.charge.service.order.OrderInfoService;

@Component("contractServiceTask")
public class ContractServiceTask {

    @Autowired
    OrderInfoAuditService orderInfoAuditService;
    @Autowired
    OrderInfoAuditDao orderInfoAuditDao;

    @Autowired
    OrderInfoService orderInfoService;
    @Autowired
    OrderInfoDao orderInfoDao;

    @Autowired
    TaskService taskService;

    /**
     * 点击审核通过按钮执行此过程
     * 
     * @param execution
     */
    public void verifyPass(DelegateExecution execution) {
        // 获取工作流中的变量
        String contractId = (String) execution.getVariable("contractId");
        String wfProcessKey = (String) execution.getVariable("wfProcessKey");
        String orderStatus = ChargeConstant.ORDER_STATUS_VERIFIED;
        List<OrderInfo> orderInfos = orderInfoService.findByContractId(contractId);
        if (!CommonUtil.isNullorEmpty(orderInfos)) {
            orderInfos.get(0).setOrder_status(orderStatus);
            orderInfoDao.save(orderInfos.get(0));
        }

        // 修改工作流的状态和备注
        OrderInfoAudit orderInfoAudit = orderInfoAuditService.getByContractProcessKey(wfProcessKey);
        orderInfoAudit.setProcessStatus(orderStatus);
        orderInfoAudit.setRemarks("审核通过");
        orderInfoAuditDao.save(orderInfoAudit);
    }

    /**
     * 点击审核不通过按钮执行此过程
     * 
     * @param execution
     */
    public void verifyFail(DelegateExecution execution) {
        String contractId = (String) execution.getVariable("contractId");
        String wfProcessKey = (String) execution.getVariable("wfProcessKey");
        String rejectReason = (String) execution.getVariable("rejectReason");
        String orderStatus = ChargeConstant.ORDER_STATUS_FAIL;
        List<OrderInfo> orderInfos = orderInfoService.findByContractId(contractId);
        if (!CommonUtil.isNullorEmpty(orderInfos)) {
            orderInfos.get(0).setOrder_status(orderStatus);
            orderInfoDao.save(orderInfos.get(0));
        }
        // 修改工作流的状态和备注
        OrderInfoAudit orderInfoAudit = orderInfoAuditService.getByContractProcessKey(wfProcessKey);
        orderInfoAudit.setProcessStatus(orderStatus);
        orderInfoAudit.setRemarks(rejectReason);
        orderInfoAuditDao.save(orderInfoAudit);
    }
}
