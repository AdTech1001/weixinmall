package com.org.cron;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.exception.BusinessException;
/**
 * 定时服务的抽象
 */
public abstract class CronService implements Runnable {

	private Log log = LogFactory.getLog(CronServiceFactory.class);
	
	protected String cronDate;
	
	protected String cronDateTime;
	
	public String getCronDate() {
		return cronDate;
	}

	public void setCronDate(String cronDate) {
		this.cronDate = cronDate;
	}

	public String getCronDateTime() {
		return cronDateTime;
	}

	public void setCronDateTime(String cronDateTime) {
		this.cronDateTime = cronDateTime;
	}

	public abstract void service() throws BusinessException;
	
	
	public void run() {
		try{
			log.info("开始执行服务("+this.getClass().getName()+")");
			this.service();
			log.info("结束执行服务("+this.getClass().getName()+")");
		}catch(BusinessException ce){
			ce.printStackTrace();
		}
	}

}
