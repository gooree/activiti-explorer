package com.wxjfkg.activiti.explorer.web.base;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxjfkg.activiti.explorer.service.WorkflowProcessDefinitionService;
import com.wxjfkg.activiti.explorer.service.WorkflowTraceService;
import com.wxjfkg.activiti.explorer.utils.ActivitiUtils;

/**
 * 抽象Controller，提供一些基础的方法、属性
 *
 * @author guorui
 */
public abstract class AbstractController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected ProcessEngine processEngine = null;
    protected RepositoryService repositoryService;
    protected RuntimeService runtimeService;
    protected TaskService taskService;
    protected HistoryService historyService;
    protected IdentityService identityService;
    protected ManagementService managementService;
    protected FormService formService;
    protected ProcessEngineConfiguration processEngineConfiguration;
    
    protected WorkflowProcessDefinitionService workflowProcessDefinitionService;
    protected WorkflowTraceService traceService;

    public AbstractController() {
        super();
        processEngine = ActivitiUtils.getProcessEngine();
        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();
        taskService = processEngine.getTaskService();
        historyService = processEngine.getHistoryService();
        identityService = processEngine.getIdentityService();
        managementService = processEngine.getManagementService();
        formService = processEngine.getFormService();
        processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        
        workflowProcessDefinitionService = new WorkflowProcessDefinitionService();
        workflowProcessDefinitionService.setHistoryService(historyService);
        workflowProcessDefinitionService.setRepositoryService(repositoryService);
        workflowProcessDefinitionService.setRuntimeService(runtimeService);
        
        traceService = new WorkflowTraceService();
        traceService.setIdentityService(identityService);
        traceService.setRepositoryService(repositoryService);
        traceService.setRuntimeService(runtimeService);
        traceService.setTaskService(taskService);
    }

}
