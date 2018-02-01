package com.hundsun.boss.common.workflow;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.hundsun.boss.common.utils.SpringContextHolder;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 
 *
 * @author HenryYan
 */
public class WorkflowUtils {
    
    private static Logger logger = LoggerFactory.getLogger(WorkflowUtils.class);

    /**
     * 转换流程节点类型为中文说明
     * 
     * @param type 英文名称
     * @return 翻译后的中文名称
     */
    public static String parseToZhType(String type) {
        Map<String, String> types = Maps.newHashMap();
        types.put("userTask", "用户任务");
        types.put("serviceTask", "系统任务");
        types.put("startEvent", "开始节点");
        types.put("endEvent", "结束节点");
        types.put("exclusiveGateway", "条件判断节点(系统自动根据条件处理)");
        types.put("inclusiveGateway", "并行处理任务");
        types.put("callActivity", "子流程");
        return types.get(type) == null ? type : types.get(type);
    }

    /**
     * 导出图片文件到硬盘
     *
     * @return 文件的全路径
     */
    public static String exportDiagramToFile(RepositoryService repositoryService, ProcessDefinition processDefinition, String exportDir) throws IOException {
        String diagramResourceName = processDefinition.getDiagramResourceName();
        String key = processDefinition.getKey();
        int version = processDefinition.getVersion();
        String diagramPath = "";

        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), diagramResourceName);
        byte[] b = new byte[resourceAsStream.available()];

        @SuppressWarnings("unused")
        int len = -1;
        resourceAsStream.read(b, 0, b.length);

        // create file if not exist
        String diagramDir = exportDir + "/" + key + "/" + version;
        File diagramDirFile = new File(diagramDir);
        if (!diagramDirFile.exists()) {
            diagramDirFile.mkdirs();
        }
        diagramPath = diagramDir + "/" + diagramResourceName;
        File file = new File(diagramPath);

        // 文件存在退出
        if (file.exists()) {
            // 文件大小相同时直接返回否则重新创建文件(可能损坏)
            logger.debug("diagram exist, ignore... : {}", diagramPath);
            return diagramPath;
        } else {
            file.createNewFile();
        }

        logger.debug("export diagram to : {}", diagramPath);

        // write bytes to file
        FileUtils.writeByteArrayToFile(file, b, true);
        return diagramPath;
    }

    public static WorkflowEntity getWorkflowEntity(String processInstanceId) {
        RuntimeService runtimeService = SpringContextHolder.getBean(RuntimeService.class);
        TaskService taskService = SpringContextHolder.getBean(TaskService.class);
        HistoryService historyService = SpringContextHolder.getBean(HistoryService.class);
        RepositoryService repositoryService = SpringContextHolder.getBean(RepositoryService.class);

        WorkflowEntity workflowEntity = new WorkflowEntity();
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        workflowEntity.setTasks(tasks);
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (historicProcessInstance != null) {
            workflowEntity.setHistoricProcessInstance(historicProcessInstance);
            workflowEntity.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult());
        } else {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
            workflowEntity.setProcessInstance(processInstance);
            workflowEntity.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult());
        }
        workflowEntity.setHistoricTaskInstances(historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByHistoricTaskInstanceEndTime().asc().list());
        workflowEntity.setHistoricVariableInstances(historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list());
        workflowEntity.setComments(taskService.getProcessInstanceComments(processInstanceId));
        return workflowEntity;
    }

    public static void claim(String processInstanceId) {
        TaskService taskService = SpringContextHolder.getBean(TaskService.class);
        //如果没有签收，签收
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        if (task != null && StringUtils.isEmpty(task.getAssignee())) {
            taskService.claim(task.getId(), ObjectUtils.toString(UserUtils.getUser().getId()));
        }
    }

}
