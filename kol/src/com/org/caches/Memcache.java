package com.org.caches;

import java.util.HashMap;
import java.util.Map;

public class Memcache {
	
	//private Log log = LogFactory.getLog(Memcache.class);
	private static Memcache memcache = null;
	private static Map<String, String> container = new HashMap<String, String>();
	
	private Memcache(){
	}
	
	public String getValue(String key) {
		return container.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param time ÃëÊý
	 * @param value
	 * @throws CacheClientException
	 */
	public void setValue(String key, String value) {
		container.put(key, value);
	}
	
	/**
	 * 
	 * @param key
	 * @param time ÃëÊý
	 * @param value
	 * @throws CacheClientException
	 */
	public void setValue(String key, int time, String value) {
		container.put(key, value);
	}
	
	public static Memcache getInstance(){
		if(memcache == null) {
				memcache = new Memcache();
		}
		return memcache;
	}
	
}
