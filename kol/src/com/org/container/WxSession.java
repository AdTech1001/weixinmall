package com.org.container;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于标记微信用户登录信息
 * @author Administrator
 */
public class WxSession {
	private WxSession(){}
	private static WxSession temp = null;
	private static Map<String, Object> map = new HashMap<String, Object>();

	public static WxSession getInstance(){
		if(temp == null) {
			temp = new WxSession();
		}
		return temp;
	}
	
	public void setAttribute(String key, Object value){
		map.put(key, value);
	}
	
	public Object getAttribute(String key){
		if(map.containsKey(key)) {
			return map.get(key);
		}
		return null;
	}
}
