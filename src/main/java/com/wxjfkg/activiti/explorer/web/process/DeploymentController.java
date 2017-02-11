package com.wxjfkg.activiti.explorer.web.process;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.wxjfkg.activiti.explorer.form.ProcessForm;
import com.wxjfkg.activiti.explorer.utils.Page;
import com.wxjfkg.activiti.explorer.utils.PageUtil;
import com.wxjfkg.activiti.explorer.web.base.AbstractController;

/**
 * 部署流程
 *
 * @author guorui
 */
@Controller
@RequestMapping(value = "/process")
public class DeploymentController extends AbstractController {

    RepositoryService repositoryService = processEngine.getRepositoryService();

    /**
     * 流程定义列表
     */
    @RequestMapping(value = "/process-list")
    public ModelAndView processList(ProcessForm form, HttpServletRequest request) {
    	Page<ProcessDefinition> page = new Page<ProcessDefinition>(PageUtil.PAGE_SIZE);
        int[] pageParams = PageUtil.init(page, request);
        
        ModelAndView mav = new ModelAndView("process/process-list");

        ProcessDefinitionQuery processQuery = repositoryService.createProcessDefinitionQuery();
        if(StringUtils.isNotBlank(form.getName())) {
        	processQuery = processQuery.processDefinitionNameLike(form.getName());
        }
        if(StringUtils.isNotBlank(form.getKey())) {
        	processQuery = processQuery.processDefinitionKey(form.getKey());
        }
        
		List<ProcessDefinition> processDefinitionList = processQuery.listPage(
				pageParams[0], pageParams[1]);
		
		page.setResult(processDefinitionList);
        page.setTotalCount(processQuery.count());
        mav.addObject("page", page);

        return mav;
    }

    /**
     * 部署流程资源
     */
    @RequestMapping(value = "/deploy")
    public String deploy(@RequestParam(value = "file", required = true) MultipartFile file) {

        // 获取上传的文件名
        String fileName = file.getOriginalFilename();

        try {
            // 得到输入流（字节流）对象
            InputStream fileInputStream = file.getInputStream();

            // 文件的扩展名
            String extension = FilenameUtils.getExtension(fileName);

            // zip或者bar类型的文件用ZipInputStream方式部署
            DeploymentBuilder deployment = repositoryService.createDeployment();
            if (extension.equals("zip") || extension.equals("bar")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment.addZipInputStream(zip);
            } else {
                // 其他类型的文件直接部署
                deployment.addInputStream(fileName, fileInputStream);
            }
            deployment.deploy();
        } catch (Exception e) {
            logger.error("error on deploy process, because of file input stream");
        }

        return "redirect:process-list";
    }

    /**
     * 读取流程资源
     *
     * @param processDefinitionId 流程定义ID
     * @param resourceName        资源名称
     */
    @RequestMapping(value = "/read-resource")
    public void readResource(@RequestParam("pdid") String processDefinitionId, @RequestParam("resourceName") String resourceName, HttpServletResponse response)
            throws Exception {
        ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId).singleResult();

        // 通过接口读取
        InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);

        // 输出资源内容到相应对象
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
    @RequestMapping(value = "/delete-deployment")
    public String deleteProcessDefinition(@RequestParam("deploymentId") String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
        return "redirect:process-list";
    }

}
