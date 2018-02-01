package com.hundsun.boss.base.service;

import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hundsun.boss.common.workflow.WorkflowUtils;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 计费合同审核Service
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Component
public class WorkflowBaseService extends BaseService {

    @Autowired
    public RuntimeService runtimeService;
    @Autowired
    public TaskService taskService;
    @Autowired
    public HistoryService historyService;
    @Autowired
    public IdentityService identityService;

    /**
     * 开启流程实例，传入参数: Entity, 和流程定义的编号
     * 
     * @param id
     * @param userId
     * @param processDefinitionKey
     * @return
     */
    public ProcessInstance startProcessInstance(String id, String processDefinitionKey) {
        ProcessInstance processInstance = null;
        try {
            String businessKey = id;
            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中

            identityService.setAuthenticatedUserId(UserUtils.getUser().getId());
            // 返回创建成功后的流程实例对象
            processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return processInstance;
    }

    /**
     * 完成任务
     * 
     * @param orderInfoAudit
     * @param form
     */
    public void completeTask(String processInstanceId, String remarks, Map params) {

        try {
            WorkflowUtils.claim(processInstanceId);
            Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
            taskService.addComment(task.getId(), processInstanceId, remarks);
            taskService.complete(task.getId(), params);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 删除流程实例
     * 
     * @param processInstanceId
     */
    public void deleteProcessInstance(String processInstanceId) {
        try {
            runtimeService.deleteProcessInstance(processInstanceId, "");
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            historyService.deleteHistoricProcessInstance(processInstanceId);
        }
    }

    public ProcessInstance getProcessInstance(String businessKey, String processDefinitionKey) {
        return runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey, processDefinitionKey).singleResult();
    }

    public String getProcessInstanceId(String businessKey, String processDefinitionKey) {
        return runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey, processDefinitionKey).singleResult().getProcessInstanceId();
    }

}
