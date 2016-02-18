package com.org.rute;

import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.caches.WxUserContainer;
import com.org.interfaces.rute.Business;
import com.org.wx.utils.MessageUtil;
import com.org.wx.utils.WxUtil;

/**
 * request from wx , type is "news"
 * @author Administrator
 *
 */
public class TypeNews implements Business<String> {
	private Log log = LogFactory.getLog(TypeNews.class);
	private JSONObject xmlJson;

	public TypeNews(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}

	@Override
	public String call() {
		// TODO 
		return null;
	}
	
	public JSONObject getXmlJson() {
		return xmlJson;
	}

	public void setXmlJson(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}
	
}
