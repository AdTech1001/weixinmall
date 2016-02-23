package com.org.caches;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CacheManager {
	
	private Log log = LogFactory.getLog(Memcache.class);
	private static CacheManager memcache = null;
	
	private CacheManager(){
	}
	
	public static CacheManager getInstance(){
		if(memcache == null) {
				memcache = new CacheManager();
		}
		return memcache;
	}
	
	public void initAllContainer(){
		ProductContainer.getInstance().init();
		RoomContainer.getInstance().init();
		WxUserContainer.getInstance().init();
		log.info("initAllContainer Íê³É");
	}
}
