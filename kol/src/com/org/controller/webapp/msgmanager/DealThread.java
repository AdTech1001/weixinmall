package com.org.controller.webapp.msgmanager;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.sf.json.JSONObject;

public class DealThread {
	private static ExecutorService executor =  Executors.newCachedThreadPool();
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run() {
				executor.shutdown();
			}
		});
	}

	/**
	 * 
	 * @param params ҵ�����
	 * @param claxx ҵ��ִ�ж���
	 * @throws Exception 
	 */
	public static void executeThread(JSONObject params, Callable<String> obj) throws Exception{
		Future<String> f = DealThread.dealCallable(obj);
		f.get();
	}

	/**
	 * 
	 * @param params ҵ�����
	 * @param claxx ҵ��ִ��ʵ��
	 * @throws Exception 
	 */
	public static void executeThread(JSONObject params, Class<Callable<String>> claxx) throws Exception{
		Callable<String> executeThread = null;
		try {
			// ���constructor ʵ�����ǿ��Ի��������ġ����Ա����ظ���ͨ������ȥ��ȡ
			Constructor<Callable<String>> c = claxx.getConstructor(JSONObject.class);
			executeThread = c.newInstance(params);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("���������̶߳���ʧ�ܣ�" + e.getMessage());
		}
		Future<String> f = DealThread.dealCallable(executeThread);
		f.get();
	}
	
	public static <T> Future<T> dealCallable(Callable<T> cc) throws InterruptedException, ExecutionException  {
		return executor.submit(cc);
	}

	public static void shutdown(){
		executor.shutdown();
	}
}
