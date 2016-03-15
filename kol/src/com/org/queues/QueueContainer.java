package com.org.queues;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 队列执行者, 用于处理队列中的任务
 * @author zhou_man
 * 2016年3月15日
 */
public class QueueContainer{
	private static Log log = LogFactory.getLog(QueueContainer.class);
	private QueueContainer(){}
	protected static LinkedBlockingQueue<MessageSendTask> queue = new LinkedBlockingQueue<MessageSendTask>();

	/**
	 * 如果队列满，则阻塞
	 * @param task
	 */
	public static void addTask(MessageSendTask task){
		try {
			log.info("QueueExecutor 添加任务＝＝＝＝〉"+task.toString());
			queue.put(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 如果队列空，则阻塞
	 * @param task
	 */
	public static MessageSendTask getTask(){
		try {
			synchronized(queue){
				return queue.take();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
