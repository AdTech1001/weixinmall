package com.org.rute;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * ���н���������Ƚ�������
 * @author Administrator
 *
 */
public class RuteThreadPool {
	/**
	 * ���̳߳��н�������н�������󣬷�ɢ������ѹ��
	 * ����һ���ɻ�����̳߳ء�����̳߳صĴ�С�����˴�����������Ҫ���̣߳���ô�ͻ���ղ��ֿ��У�60�벻ִ�����񣩵��̣߳�������������ʱ�����̳߳��ֿ������ܵ�������߳����������񡣴��̳߳ز�����̳߳ش�С�����ƣ��̳߳ش�С��ȫ�����ڲ���ϵͳ������˵JVM���ܹ�����������̴߳�С��
	 */
	private static ExecutorService rute =  Executors.newCachedThreadPool();
	private static Log log = LogFactory.getLog(RuteThreadPool.class);
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run() {
				rute.shutdown();
				log.info("�ر��̳߳�");
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
