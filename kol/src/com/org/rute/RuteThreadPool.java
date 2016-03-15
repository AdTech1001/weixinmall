package com.org.rute;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 所有进入的请求，先交给本类
 * @author Administrator
 *
 */
public class RuteThreadPool {
	/**
	 * 该线程池中将存放所有接入的请求，分散请求处理压力
	 * 创建一个可缓存的线程池。如果线程池的大小超过了处理任务所需要的线程，那么就会回收部分空闲（60秒不执行任务）的线程，当任务数增加时，此线程池又可以智能的添加新线程来处理任务。此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统（或者说JVM）能够创建的最大线程大小。
	 */
	private static ExecutorService rute =  Executors.newCachedThreadPool();
	private static Log log = LogFactory.getLog(RuteThreadPool.class);
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run() {
				rute.shutdown();
				log.info("关闭线程池");
			}
		});
	}

	public static <T> Future<T> submit(Callable<T> callable) throws InterruptedException, ExecutionException  {
		return rute.submit(callable);
	}
	
	public static void shutdown(){
		rute.shutdown();
	}
}
