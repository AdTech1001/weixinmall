package com.org.model.wx;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.org.util.MD5;
import com.org.utils.PropertyUtil;

public class WxAutoReply {
	private static WxAutoReply wxReply = null;
	private static Map<String, String> replyMap = new HashMap<String, String>();
	private boolean isInit = false;
	private static String replyWords = "";
	
	public static WxAutoReply getInstance() {
		if(wxReply == null) {
			wxReply = new WxAutoReply();
		}
		return wxReply;
	}
	
	private WxAutoReply(){
		init();
	}
	
	public boolean containsMenu(String key) {
		return replyMap.containsKey(MD5.getMD5(key));
	}
	
	public void put(String key, String value){
		replyMap.put(MD5.getMD5(key), value);
	}
	
	public String get(String key){
		return replyMap.get(MD5.getMD5(key));
	}
	
	public String getReplyWords(){
		return replyWords;
	}
	
	public void init() {
		if(!isInit) {
			//String menuStr = SmpPropertyUtil.getValue("wx_auto_reply", "menu");
			Properties p = PropertyUtil.getProperties("wx_auto_reply");
			Object o = null;
			String pKey = null;
			String pVal = null;
			StringBuffer all = new StringBuffer();
			for (Iterator<?> iterator = p.keySet().iterator(); iterator.hasNext();) {
				o = iterator.next();
				if(o != null) {
					pKey = o.toString();
					pVal = p.getProperty(pKey);
					
					StringBuffer linkTemp = new StringBuffer();
					linkTemp.append("<a href='").append(pVal).append("'>").append(pKey).append("</a>");
					replyMap.put(MD5.getMD5(pKey), linkTemp.toString());
					all.append(linkTemp.toString()).append("\n");
				}
			}
			replyWords = all.substring(0, all.length() - 1);
			/*
			JSONObject menuJson = JSONObject.fromObject(menuStr);
			Object keytemp = null;
			String valTemp = null;
			StringBuffer all = new StringBuffer();
			for (Iterator<?> iterator = menuJson.keys(); iterator.hasNext();) {
				keytemp = iterator.next();
				if(keytemp != null) {
					StringBuffer linkTemp = new StringBuffer();
					valTemp = menuJson.getString(keytemp.toString());
					linkTemp.append("<a href='").append(valTemp).append("'>").append(keytemp).append("</a>");
					replyMap.put(MD5.getMD5(keytemp.toString()), linkTemp.toString());
					all.append(linkTemp.toString()).append("\n");
				}
			}
			replyWords = all.substring(0, all.length() - 1);*/
		}
		isInit = true;
	}
}
