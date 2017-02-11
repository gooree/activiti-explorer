package com.wxjfkg.activiti.explorer.web.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wxjfkg.activiti.explorer.utils.Page;
import com.wxjfkg.activiti.explorer.utils.PageUtil;
import com.wxjfkg.activiti.explorer.utils.UserUtil;
import com.wxjfkg.activiti.explorer.web.base.AbstractController;

/**
 * 任务控制器
 *
 * @author guorui
 */
@Controller
@RequestMapping(value = "/task")
public class TaskController extends AbstractController {

    private static String TASK_LIST = "redirect:/task/list";

    /**
     * 读取启动流程的表单字段
     */
    @RequestMapping(value = "/list")
    public ModelAndView todoTasks(HttpServletRequest request, HttpSession session) throws Exception {
        String viewName = "task/task-list";
        ModelAndView mav = new ModelAndView(viewName);
        User user = UserUtil.getUserFromSession(session);

        /*// 读取直接分配给当前人或者已经签收的任务
        List<Task> doingTasks = taskService.createTaskQuery().taskAssignee(user.getId()).list();

        // 等待签收的任务
        List<Task> waitingClaimTasks = taskService.createTaskQuery().taskCandidateUser(user.getId()).list();

        // 合并两种任务
        List<Task> allTasks = new ArrayList<Task>();
        allTasks.addAll(doingTasks);
        allTasks.addAll(waitingClaimTasks);*/
        Page<Task> page = new Page<Task>(PageUtil.PAGE_SIZE);
        int[] pageParams = PageUtil.init(page, request);
        
        TaskQuery taskQuery = taskService.createTaskQuery();
        // 5.16版本可以使用一下代码待办查询
        List<Task> tasks = null;
		try {
			tasks = taskQuery.taskCandidateOrAssigned(user.getId()).listPage(pageParams[0], pageParams[1]);
		} catch (Exception ex) {
			tasks = new ArrayList<Task>();
		}
		page.setResult(tasks);
        page.setTotalCount(taskQuery.count());
        mav.addObject("page", page);
        return mav;
    }

    /**
     * 签收任务
     */
    @RequestMapping(value = "/claim/{id}")
    public String claim(@PathVariable("id") String taskId, HttpSession session, RedirectAttributes redirectAttributes) {
        String userId = UserUtil.getUserFromSession(session).getId();
        taskService.claim(taskId, userId);
        redirectAttributes.addFlashAttribute("message", "任务已签收");
        return TASK_LIST;
    }

    /**
     * 读取用户任务的表单字段
     */
    @RequestMapping(value = "/getform/{taskId}")
    public ModelAndView readTaskForm(@PathVariable("taskId") String taskId) throws Exception {
        String viewName = "task/task-form";
        ModelAndView mav = new ModelAndView(viewName);
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        if (taskFormData.getFormKey() != null) {
            Object renderedTaskForm = formService.getRenderedTaskForm(taskId);
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            mav.addObject("task", task);
            mav.addObject("taskFormData", renderedTaskForm);
            mav.addObject("hasFormKey", true);
        } else {
            mav.addObject("taskFormData", taskFormData);
        }
        return mav;
    }

    /**
     * 读取启动流程的表单字段
     */
    @RequestMapping(value = "/complete/{taskId}")
    public String completeTask(@PathVariable("taskId") String taskId, HttpServletRequest request) throws Exception {
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        String formKey = taskFormData.getFormKey();
        // 从请求中获取表单字段的值
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        Map<String, String> formValues = new HashMap<String, String>();

        if (StringUtils.isNotBlank(formKey)) { // formkey表单
            Map<String, String[]> parameterMap = request.getParameterMap();
            Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
            for (Entry<String, String[]> entry : entrySet) {
                String key = entry.getKey();
                formValues.put(key, entry.getValue()[0]);
            }
        } else { // 动态表单
            for (FormProperty formProperty : formProperties) {
                if (formProperty.isWritable()) {
                    String value = request.getParameter(formProperty.getId());
                    formValues.put(formProperty.getId(), value);
                }
            }
        }
        formService.submitTaskFormData(taskId, formValues);
        return TASK_LIST;
    }

}
