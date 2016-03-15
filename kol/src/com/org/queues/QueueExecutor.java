package com.org.queues;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 队列执行者, 用于处理队列中的任务
 * @author zhou_man
 * 2016年3月15日
 */
public class QueueExecutor implements Runnable{
	private static QueueExecutor queueExecutor;
	private static Log log = LogFactory.getLog(QueueExecutor.class);
	private QueueExecutor(){}
	
	public static void start(){
		if(queueExecutor == null) {
			queueExecutor = new QueueExecutor();
		}
		log.info("QueueExecutor 启动");
		new Thread(queueExecutor).start();
	}

	@Override
	public void run() {
		while(true) {
			// 加锁， 
			// 方案一，在循环中加锁
			// 方案二，在取任务的时候加锁，选择getTask()中加锁
			//synchronized (QueueContainer.queue) {
				// 如果队列空则阻塞
				MessageSendTask task = QueueContainer.getTask();
				if(task != null) {
					log.info("QueueExecutor 消化任务＝＝＝＝〉"+task.toString());
					task.sendByService();
				}
			//}
		}
	}
	
	
}
