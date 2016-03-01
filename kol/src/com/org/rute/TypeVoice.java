package com.org.rute;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.interfaces.rute.Business;

/**
 * request from wx , type is "news"
 * @author Administrator
 *
 */
public class TypeVoice implements Business<String> {
	private Log log = LogFactory.getLog(this.getClass().getName());
	private JSONObject xmlJson;

	public TypeVoice(JSONObject xmlJson) {
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
