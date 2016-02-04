package com.org.utils.http;

import net.sf.json.JSONObject;

public interface HttpTool {
	
	public String httpGet(String url,String charset);
	
	public String httpPost(String paramContent, String url,String charset) ;
	
	public JSONObject httpPost(JSONObject paramContent, String url,String charset) ;
	
	public JSONObject wxHttpsPost(JSONObject paramContent, String url) ;

	public JSONObject wxHttpsPost(String paramContent, String url) ;

	public JSONObject wxHttpsGet(String paramContent, String url) ;

	public String simplePost(JSONObject jsonParam, String remoteUrl, String charSet) ;
	
}
