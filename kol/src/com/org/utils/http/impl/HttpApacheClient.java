package com.org.utils.http.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.org.log.LogUtil;
import com.org.log.impl.LogUtilMg;
import com.org.util.CT;
import com.org.util.JSONUtils;
import com.org.utils.http.HttpTool;

/**
 * @author Nano
 * 
 * HTTPClient �ķ�װ
 */
public class HttpApacheClient implements HttpTool{

	public String httpGet(String url,String charset) {
		String httpResult = "";
		AbstractHttpClient httpclient = null;
		try{
			HttpGet httpRequest = new HttpGet(url);
			
		    // ���� ��ʱ���� ����Ϊ��λ,����3����
            HttpParams params = new BasicHttpParams();  
            HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);  
            HttpConnectionParams.setSoTimeout(params, 45 * 1000);    
            HttpConnectionParams.setTcpNoDelay(params, true);
            HttpClientParams.setRedirecting(params, false);
       
            httpclient = new DefaultHttpClient(params); 
            HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(3,true);
            httpclient.setHttpRequestRetryHandler(retryHandler);
            
            
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){//����ɹ� 
				httpResult = EntityUtils.toString(httpResponse.getEntity(),charset);
				
				LogUtil.log(CT.LOG_CATEGORY_COMMUNICATION, "HttpApacheClient  httpGet ����ɹ� --> " + httpResult, null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_COMMUNICATION);	
				
			}else{
				int statusCode = httpResponse.getStatusLine().getStatusCode() ;	
				LogUtil.log(CT.LOG_CATEGORY_COMMUNICATION, "HttpApacheClient httpGet������Ӧ�� --> " + statusCode, null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_COMMUNICATION);	
			}
		}catch(Exception e){
			LogUtil.log(CT.LOG_CATEGORY_ERR, "httpGet����ʧ�� ", e, LogUtilMg.LOG_ERROR, CT.LOG_PATTERN_ERR);
		}finally{
			if(httpclient != null)httpclient.getConnectionManager().shutdown();  
		}
		return httpResult;
	}

	public String httpPost(String paramContent, String url,String charset) {
		String httpResult = "";
		AbstractHttpClient httpclient = null;
		try{
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>(); 
			String[] paramValues = paramContent.split("&");
			for (int i = 0; i < paramValues.length; i++) {
				String[] paramValue = paramValues[i].split("=");
				if(paramValue != null && paramValue.length == 2){
					params.add(new BasicNameValuePair(paramValue[0], paramValue[1]));					
				}else{
					LogUtil.log(CT.LOG_CATEGORY_ERR, "���ݽ����Ĳ����������Ϸ�,ԭʼ���� --> "+ paramValues[i], null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_ERR);
				}
			}
			
			HttpEntity httpentity = new UrlEncodedFormEntity(params, charset);
			httpRequest.setHeader("accept", "*/*");
			httpRequest.setHeader("connection", "Keep-Alive");
			httpRequest.setEntity(httpentity);
			
			//���ó�ʱ ����Ϊ��λ,��������
			HttpParams httpParams = new BasicHttpParams();  
	        HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);  
	        HttpConnectionParams.setSoTimeout(httpParams, 45 * 1000);    
	        HttpConnectionParams.setTcpNoDelay(httpParams, true);
	        HttpClientParams.setRedirecting(httpParams, false);
	        
		    httpclient = new DefaultHttpClient(httpParams);
		    HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(3,true);
            httpclient.setHttpRequestRetryHandler(retryHandler);
		   
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){//HttpStatus.SC_OK��ʾ���ӳɹ�
				 httpResult = EntityUtils.toString(httpResponse.getEntity(),charset);
				 LogUtil.log(CT.LOG_CATEGORY_COMMUNICATION, "HttpApacheClient  httpPost ����ɹ� --> " + httpResult, null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_COMMUNICATION);		
			}else{
				 int statusCode = httpResponse.getStatusLine().getStatusCode() ;
				 LogUtil.log(CT.LOG_CATEGORY_COMMUNICATION, "HttpApacheClient httpPost������Ӧ�� --> " + statusCode, null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_COMMUNICATION);	
			}
		}catch(Exception e){
			LogUtil.log(CT.LOG_CATEGORY_ERR, "httpPost ����ʧ�� ", e, LogUtilMg.LOG_ERROR, CT.LOG_PATTERN_ERR);
		}finally{
			if(httpclient != null)httpclient.getConnectionManager().shutdown();  
		}
		return httpResult;
	} 
	
	
	
	public JSONObject httpPost(JSONObject requestJson, String url,
			String charset) {
		JSONObject resultJson = null;
		AbstractHttpClient httpclient = null;
		try {
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			Iterator<String> it = requestJson.keySet().iterator();
			while(it.hasNext()){
				String name = it.next();
				params.add(new BasicNameValuePair(name,requestJson.getString(name)));
			}
			
			HttpEntity httpentity = new UrlEncodedFormEntity(params, charset);
			httpRequest.setHeader("accept", "*/*");
			httpRequest.setHeader("connection", "Keep-Alive");
			httpRequest.setEntity(httpentity);

			// ���ó�ʱ ����Ϊ��λ,��������
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
			HttpConnectionParams.setSoTimeout(httpParams, 45 * 1000);
			HttpConnectionParams.setTcpNoDelay(httpParams, true);
			HttpClientParams.setRedirecting(httpParams, false);

			httpclient = new DefaultHttpClient(httpParams);
			HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(
					3, true);
			httpclient.setHttpRequestRetryHandler(retryHandler);

			HttpResponse httpResponse = httpclient.execute(httpRequest);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {// HttpStatus.SC_OK��ʾ���ӳɹ�
				String httpResult = EntityUtils.toString(httpResponse.getEntity(),charset);

				resultJson = JSONUtils.getJsonFromString(httpResult);
//				LogUtil.log(CT.LOG_CATEGORY_COMMUNICATION,"HttpApacheClient  httpPost ����ɹ� --> " + resultJson,
//						null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_COMMUNICATION);
			} else {
				int statusCode = httpResponse.getStatusLine().getStatusCode();
				LogUtil.log(CT.LOG_CATEGORY_COMMUNICATION,
						"HttpApacheClient httpPost������Ӧ�� --> " + statusCode,
						null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_COMMUNICATION);
			}
		} catch (Exception e) {
			LogUtil.log(CT.LOG_CATEGORY_ERR, "httpPost ����ʧ�� ", e,LogUtilMg.LOG_ERROR, CT.LOG_PATTERN_ERR);
		} finally {
			if (httpclient != null){
				httpclient.getConnectionManager().shutdown();
			}
		}
		return resultJson;
	}

	public String simplePost(JSONObject jsonParam, String remoteUrl, String charSet){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String key ="";
		for (Iterator<?> iterator = jsonParam.keys(); iterator.hasNext();) {
			key = String.valueOf(iterator.next());
			params.add(new BasicNameValuePair(key,jsonParam.getString(key)));
		}

	    UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(params,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	    HttpPost post = new HttpPost(remoteUrl);
		post.setHeader("accept", "*/*");
	    post.setHeader("connection", "Keep-Alive");
	    post.setEntity(entity);
	    
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response = null;
	    HttpEntity httpEntity = null;
	    String result = "";
		try {
			response = client.execute(post);
			httpEntity = response.getEntity();
			result = EntityUtils.toString(httpEntity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return result;
	}
    
}
