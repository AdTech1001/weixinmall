package com.org.cron;

import org.springframework.core.task.TaskExecutor;

/**
 * 
 * 后台服务管理器
 */
public class CronServiceManager {
	
	private TaskExecutor taskExecutor;

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	/**
	 * 商户订单和支付跑批
	 */
	public void service_step0(){
//		CronService service = CronServiceFactory.getInstance().createCronService("cronOrderService");
//		taskExecutor.execute(service);
	}
	
	/**
	 * 缓存数据跑量更新处理
	 */
	public void service_step1(){
//		CronService service = CronServiceFactory.getInstance().createCronService("cronCacheManagerService");
//		taskExecutor.execute(service);
	}
	
	/**
	 * 用户常用支付工具分析
	 */
	public void doUserCommonUtilsWork(){
//		CronService service = CronServiceFactory.getInstance().createCronService("userCommonUtilManagerService");
//		taskExecutor.execute(service);
	}
	
	
	
}
