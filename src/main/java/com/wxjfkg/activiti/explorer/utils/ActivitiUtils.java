package com.wxjfkg.activiti.explorer.utils;

import org.activiti.engine.ProcessEngine;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

/**
 * Activiti工具类
 *
 * @author guorui
 */
public class ActivitiUtils implements ApplicationContextAware {

	private static ProcessEngine processEngine;

	private static ApplicationContext appContext;

	/**
	 * 单例模式获取引擎对象
	 */
	public static ProcessEngine getProcessEngine() {
		if (processEngine == null) {
			/*
			 * 使用默认的配置文件名称（activiti.cfg.xml）创建引擎对象
			 */
			// processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault().buildProcessEngine();
			throw new IllegalArgumentException("工作流引擎初始化失败");
		}
		return processEngine;
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {

		ActivitiUtils.appContext = context;
		SpringProcessEngineConfiguration configuration = appContext
				.getBean(SpringProcessEngineConfiguration.class);
		Assert.notNull(configuration);
		processEngine = configuration.buildProcessEngine();
	}

}
