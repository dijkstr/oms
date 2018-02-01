package com.hundsun.boss.modules.sys.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.common.workflow.Variable;
import com.hundsun.boss.common.workflow.WorkflowEntity;
import com.hundsun.boss.common.workflow.WorkflowUtils;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.form.FileForm;
import com.hundsun.boss.modules.sys.form.Source;
import com.hundsun.boss.modules.sys.form.Workflow;
import com.hundsun.boss.modules.sys.service.workflow.WorkflowProcessDefinitionService;
import com.hundsun.boss.modules.sys.service.workflow.WorkflowTraceService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/sys/workflow")
public class WorkflowController extends BaseController {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected WorkflowProcessDefinitionService workflowProcessDefinitionService;
    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected WorkflowTraceService traceService;
    protected static Map<String, ProcessDefinition> PROCESS_DEFINITION_CACHE = new HashMap<String, ProcessDefinition>();

    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "/modelList")
    public ModelAndView modelList(RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("modules/sys/modelList");

        List<org.activiti.engine.repository.Model> modelList = repositoryService.createModelQuery().orderByCreateTime().desc().list();
        for (org.activiti.engine.repository.Model model : modelList) {

            if (!repositoryService.createDeploymentQuery().deploymentName(model.getName()).list().isEmpty()) {
                model.setDeploymentId("1");
            }
        }

        mav.addObject("list", modelList);
        return mav;
    }

    /**
     * 创建模型
     */
    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "create")
    public void create(String name, String key, String description, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            org.activiti.engine.repository.Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            description = StringUtils.defaultString(description);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(name);
            modelData.setKey(StringUtils.defaultString(key));

            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

            response.sendRedirect(request.getContextPath() + "/service/editor?id=" + modelData.getId());
        } catch (Exception e) {
            addMessage(redirectAttributes, "创建模型失败：" + e.getMessage());
            logger.error("创建模型失败：", e);
        }
    }

    /**
     * 根据Model部署流程
     */
    @SuppressWarnings("rawtypes")
    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "deploy")
    public @ResponseBody Map deploy(@RequestParam String modelId, @RequestParam String processDefinitionName, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            org.activiti.engine.repository.Model modelData = repositoryService.getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            byte[] bpmnBytes = null;
            modelNode.put("properties", editProperty(modelNode.get("properties"), "name", processDefinitionName));
            modelNode.put("properties", editProperty(modelNode.get("properties"), "process_id", modelData.getName()));
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes)).deploy();

            result.put("result", "success");
            result.put("message", "部署成功！");
            addMessage(redirectAttributes, "部署成功，部署ID=" + deployment.getId());
        } catch (Exception e) {
            result.put("result", "fail");
            result.put("message", "部署失败！");
            addMessage(redirectAttributes, "根据模型部署流程失败：modelId={" + modelId + "}" + e.getMessage());
        }
        return result;
    }

    /**
     * 编辑Json 某属性的值
     * 
     * @param node JsonNode 对象
     * @param key 属性
     * @param value 要修改的值
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private JsonNode editProperty(JsonNode node, String key, String value) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Map result = mapper.readValue(node, Map.class);
        result.put(key, value);
        String jsonString = mapper.writeValueAsString(result);
        JsonNode jsonNode = mapper.readValue(jsonString, JsonNode.class);
        return jsonNode;
    }

    /**
     * 编辑模型
     */
    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "edit")
    public void edit(@RequestParam String modelId, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {

        try {
            response.sendRedirect(request.getContextPath() + "/service/editor?id=" + modelId);
        } catch (IOException e) {
            addMessage(redirectAttributes, "编辑失败！");
            e.printStackTrace();
        }
    }

    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "delete")
    public String deleteModel(@RequestParam String modelId, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        try {
            repositoryService.deleteModel(modelId);
            addMessage(redirectAttributes, "删除ID={" + modelId + "}模型成功");
        } catch (Exception e) {
            addMessage(redirectAttributes, "删除ID={" + modelId + "}模型失败");
        }
        return "redirect:" + Global.getAdminPath() + "/sys/workflow/modelList";
    }

    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "deployList")
    public String deployList(@RequestParam String modelName, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {

        List<Object[]> objects = new ArrayList<Object[]>();
        List<Deployment> deploymentList = repositoryService.createDeploymentQuery().deploymentName(modelName).orderByDeploymenTime().desc().list();

        for (Deployment deployment : deploymentList) {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
            objects.add(new Object[] { processDefinition, deployment });
        }
        model.addAttribute("modelName", modelName);
        model.addAttribute("objects", objects);
        return "modules/sys/deployList";
    }

    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "/form")
    public String form(Workflow workflow, Model model) {
        model.addAttribute("workflow", workflow);
        return "/modules/sys/modelForm";
    }

    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "/fileForm")
    public String saveFile(FileForm fileForm, Model model) {
        model.addAttribute("fileForm", fileForm);
        return "/modules/sys/importFileForm";
    }

    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "editForm")
    public String saveFile(@RequestParam String deploymentId, @RequestParam String resourceName, Source source, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("deploymentId", deploymentId);
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
        try {
            String resource = IOUtils.toString(repositoryService.getResourceAsStream(deploymentId, resourceName));
            source.setResource(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("deploymentName", deployment.getName());
        model.addAttribute("source", source);
        return "/modules/sys/editorSourceForm";
    }

    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "saveXml")
    public String saveXml(@RequestParam String deploymentId, Source source, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        try {
            repositoryService.createDeployment().name(source.getResourceName()).addString(source.getResourceName(), source.getResource()).deploy();
            addMessage(redirectAttributes, "保存成功");
        } catch (Exception e) {
            addMessage(redirectAttributes, "保存失败");
        }

        return "/modules/sys/modelList";
    }

    /**
     * 流程定义列表
     * 
     * @return
     */
    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "/processList")
    public ModelAndView processList() {
        ModelAndView mav = new ModelAndView("modules/sys/processList");

        /*
         * 保存两个对象，一个是ProcessDefinition（流程定义），一个是Deployment（流程部署）
         */
        List<Object[]> objects = new ArrayList<Object[]>();

        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            objects.add(new Object[] { processDefinition, deployment });
        }

        mav.addObject("objects", objects);

        return mav;
    }

    /**
     * 部署全部流程
     * 
     * @return
     * @throws Exception
     */
    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "/redeploy/all")
    public String redeployAll() throws Exception {
        workflowProcessDefinitionService.deployAllFromClasspath();
        return "redirect:" + Global.getAdminPath() + "/sys/workflow/processList";
    }

    /**
     * 导出model的xml文件
     */
    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "export")
    public void export(@RequestParam String modelId, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        try {
            org.activiti.engine.repository.Model modelData = repositoryService.getModel(modelId);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            IOUtils.copy(in, response.getOutputStream());
            String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.flushBuffer();
        } catch (Exception e) {
            logger.error("导出model的xml文件失败：modelId={}", modelId, e);
        }
    }

    /**
     * 读取xml，通过流程ID
     * 
     * @param resourceType 资源类型xml
     * @param deploymentId 流程ID
     * @param response
     * @throws Exception
     */
    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "/resource/xml/deployment")
    public void loadXML(@RequestParam String deploymentId, @RequestParam String resourceName, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) throws Exception {

        InputStream resourceAsStream = null;

        resourceAsStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    /**
     * 读取image，通过流程ID
     * 
     * @param resourceType 资源类型image
     * @param deploymentId 流程ID
     * @param response
     * @throws Exception
     */
    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "/resource/image/deployment")
    public void loadImage(@RequestParam String deploymentId, @RequestParam String resourceName, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream resourceAsStream = null;
        
        resourceAsStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    /**
     * 读取资源，通过流程ID
     * 
     * @param resourceType 资源类型(xml|image)
     * @param processInstanceId 流程实例ID
     * @param response
     * @throws Exception
     */
    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "/resource/process-instance")
    public void loadByProcessInstance(@RequestParam("type") String resourceType, @RequestParam("pid") String processInstanceId, HttpServletResponse response) throws Exception {
        InputStream resourceAsStream = null;
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
        String resourceName = "";
        if (resourceType.equals("image")) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resourceType.equals("xml")) {
            resourceName = processDefinition.getResourceName();
        }
        resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    /**
     * 删除部署的流程，级联删除流程实例
     * 
     * @param deploymentId 流程部署ID
     */
    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "/process/delete")
    public String delete(@RequestParam("deploymentId") String deploymentId, RedirectAttributes redirectAttributes) {
        try {
            repositoryService.deleteDeployment(deploymentId, true);
            addMessage(redirectAttributes, "删除部署ID为" + deploymentId + "的流程成功！");
        } catch (Exception e) {
            addMessage(redirectAttributes, "删除部署ID为" + deploymentId + "的流程失败！");
        }

        return "redirect:" + Global.getAdminPath() + "/sys/workflow/processList";
    }

    /**
     * 输出跟踪流程信息
     * 
     * @param processInstanceId
     * @return
     * @throws Exception
     */
    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "/process/trace")
    @ResponseBody
    public List<Map<String, Object>> traceProcess(@RequestParam("pid") String processInstanceId) throws Exception {
        List<Map<String, Object>> activityInfos = traceService.traceProcess(processInstanceId);
        return activityInfos;
    }

    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "/fileSave")
    public String deploy(@RequestParam(value = "file", required = false) MultipartFile file, RedirectAttributes redirectAttributes) {

        String fileName = file.getOriginalFilename();

        try {
            InputStream fileInputStream = file.getInputStream();

            String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("zip") || extension.equals("bar")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                repositoryService.createDeployment().addZipInputStream(zip).deploy();
            } else if (extension.equals("png")) {
                repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            } else if (fileName.indexOf("bpmn20.xml") != -1) {
                repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            } else if (extension.equals("bpmn")) {
                /*
                 * bpmn扩展名特殊处理，转换为bpmn20.xml
                 */
                String baseName = FilenameUtils.getBaseName(fileName);
                repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", fileInputStream).deploy();
                addMessage(redirectAttributes, "导入成功！");
            } else {
                addMessage(redirectAttributes, "导入失败！");
            }
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入失败！");
        }

        return "redirect:" + Global.getAdminPath() + "/sys/workflow/processList";
    }

    /**
     * 待办任务--Portlet
     */
    @RequestMapping(value = "/task/todo/list")
    @ResponseBody
    public List<Map<String, Object>> todoList(HttpSession session) throws Exception {
        User user = UserUtils.getUser();
        String userId = ObjectUtils.toString(user.getId());
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        // 已经签收的任务
        List<Task> todoList = taskService.createTaskQuery().taskAssignee(userId).active().list();
        for (Task task : todoList) {
            String processDefinitionId = task.getProcessDefinitionId();
            ProcessDefinition processDefinition = getProcessDefinition(processDefinitionId);

            Map<String, Object> singleTask = packageTaskInfo(sdf, task, processDefinition);
            singleTask.put("status", "todo");
            result.add(singleTask);
        }

        // 等待签收的任务
        List<Task> toClaimList = taskService.createTaskQuery().taskCandidateUser(userId).active().list();
        for (Task task : toClaimList) {
            String processDefinitionId = task.getProcessDefinitionId();
            ProcessDefinition processDefinition = getProcessDefinition(processDefinitionId);

            Map<String, Object> singleTask = packageTaskInfo(sdf, task, processDefinition);
            singleTask.put("status", "claim");
            result.add(singleTask);
        }

        return result;
    }

    private Map<String, Object> packageTaskInfo(SimpleDateFormat sdf, Task task, ProcessDefinition processDefinition) {
        Map<String, Object> singleTask = new HashMap<String, Object>();
        singleTask.put("id", task.getId());
        singleTask.put("name", task.getName());
        singleTask.put("createTime", sdf.format(task.getCreateTime()));
        singleTask.put("pdname", processDefinition.getName());
        singleTask.put("pdversion", processDefinition.getVersion());
        singleTask.put("pid", task.getProcessInstanceId());
        return singleTask;
    }

    private ProcessDefinition getProcessDefinition(String processDefinitionId) {
        ProcessDefinition processDefinition = PROCESS_DEFINITION_CACHE.get(processDefinitionId);
        if (processDefinition == null) {
            processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
            PROCESS_DEFINITION_CACHE.put(processDefinitionId, processDefinition);
        }
        return processDefinition;
    }

    /**
     * 激活流程实例
     */
    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "processdefinition/active")
    public String active(@RequestParam String processId, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        try {
            repositoryService.activateProcessDefinitionById(processId, true, null);
            addMessage(redirectAttributes, "已激活ID为[" + processId + "]的流程定义。");
        } catch (Exception e) {
            addMessage(redirectAttributes, "ID为[" + processId + "]的流程定义已激活。");
        }

        return "redirect:" + Global.getAdminPath() + "/sys/workflow/processList";
    }

    /**
     * 挂起流程实例
     */
    @RequiresPermissions("sys:workflow:edit")
    @RequestMapping(value = "processdefinition/suspend")
    public String suspend(@RequestParam String processId, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        try {
            repositoryService.suspendProcessDefinitionById(processId, true, null);
            addMessage(redirectAttributes, "已挂起ID为[" + processId + "]的流程定义。");
        } catch (Exception e) {
            addMessage(redirectAttributes, "ID为[" + processId + "]的流程定义已挂起。");
        }

        return "redirect:" + Global.getAdminPath() + "/sys/workflow/processList";
    }

    /**
     * 完成任务
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "complete/{id}", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String complete(@PathVariable("id") String taskId, Variable var) {
        try {
            Map<String, Object> variables = var.getVariableMap();
            taskService.complete(taskId, variables);
            return "success";
        } catch (Exception e) {
            logger.error("error on complete task {}, variables={}", new Object[] { taskId, var.getVariableMap(), e });
            return "error";
        }
    }

    /**
     * 签收任务
     */
    @RequestMapping(value = "claim/{id}")
    @ResponseBody
    public String claim(@PathVariable("id") String taskId, HttpSession session, RedirectAttributes redirectAttributes) {
        String userId = ObjectUtils.toString(UserUtils.getUser().getId());
        taskService.claim(taskId, userId);
        return "success";
    }

    /**
     * 显示流程图
     */
    @RequestMapping(value = "processPic")
    public void processPic(String procDefId, HttpServletResponse response) throws Exception {
        ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
        String diagramResourceName = procDef.getDiagramResourceName();
        InputStream imageStream = repositoryService.getResourceAsStream(procDef.getDeploymentId(), diagramResourceName);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    /**
     * 获取跟踪信息
     */
    @RequestMapping(value = "processMap")
    public String processMap(String processInstanceId, Model model) throws Exception {
        WorkflowEntity workflowEntity = WorkflowUtils.getWorkflowEntity(processInstanceId);
        String procDefId = workflowEntity.getProcessDefinition().getId();
        List<ActivityImpl> actImpls = new ArrayList<ActivityImpl>();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
        ProcessDefinitionImpl pdImpl = (ProcessDefinitionImpl) processDefinition;
        String processDefinitionId = pdImpl.getId();// 流程标识
        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinitionId);
        List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);
        for (String activeId : activeActivityIds) {
            for (ActivityImpl activityImpl : activitiList) {
                String id = activityImpl.getId();
                if (activityImpl.isScope()) {
                    if (activityImpl.getActivities().size() > 1) {
                        List<ActivityImpl> subAcList = activityImpl.getActivities();
                        for (ActivityImpl subActImpl : subAcList) {
                            String subid = subActImpl.getId();
                            if (activeId.equals(subid)) {// 获得执行到那个节点
                                actImpls.add(subActImpl);
                                break;
                            }
                        }
                    }
                }
                if (activeId.equals(id)) {// 获得执行到那个节点
                    actImpls.add(activityImpl);
                    System.out.println(id);
                }
            }
        }
        model.addAttribute("procDefId", procDefId);
        model.addAttribute("proInstId", processInstanceId);
        model.addAttribute("actImpls", actImpls);
        return "modules/sys/processMap";
    }
}
