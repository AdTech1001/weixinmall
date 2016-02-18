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
	 */
	private static ExecutorService rute =  Executors.newCachedThreadPool();
	private Log log = LogFactory.getLog(RuteThreadPool.class);
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run() {
				rute.shutdown();
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
