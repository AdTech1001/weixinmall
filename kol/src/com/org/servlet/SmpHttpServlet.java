package com.org.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.org.log.LogUtil;
import com.org.log.impl.LogUtilMg;
import com.org.util.CT;

/**
 * 
 * 封装Servlet共用功能
 *
 */
public class SmpHttpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void forward(String targetUrl,HttpServletRequest request, HttpServletResponse response){
		RequestDispatcher rd = request.getRequestDispatcher(targetUrl);
		try {
			rd.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void redirect(String targetUrl,HttpServletResponse response) throws Exception{
			response.sendRedirect(targetUrl);
	}
	
	protected void write(JSONObject noticeData,String charsetName,HttpServletResponse response)throws Exception{
		ServletOutputStream out = null;
		try{
			out = response.getOutputStream();
			
			out.write(noticeData.toString().getBytes(charsetName));
			
			out.flush();
		}catch(Exception e){
			LogUtil.log(CT.LOG_CATEGORY_ERR, "输出流写数据过程失败,原因：" + e.getMessage(), e, LogUtilMg.LOG_ERROR, CT.LOG_CATEGORY_ERR);
			throw e;
		}finally{
			
		}
	}
	
	protected void writePage(String page,String charsetName,HttpServletResponse response)throws Exception{
		PrintWriter out = null;
		try{
			response.setContentType("text/html;charset="+charsetName);
			
			out = response.getWriter();
			
			out.println(page);
			
			out.flush();
			
			response.setStatus(HttpServletResponse.SC_OK);
		}catch(Exception e){
			LogUtil.log(CT.LOG_CATEGORY_ERR, "输出流写页面过程失败,原因：" + e.getMessage(), e, LogUtilMg.LOG_ERROR, CT.LOG_CATEGORY_ERR);
			throw e;
		}finally{
			
		}
	}
	
	protected void write(String noticeData,String charsetName,HttpServletResponse response) throws IOException {
		ServletOutputStream out = null;
		out = response.getOutputStream();
		out.write(noticeData.getBytes(charsetName));
		out.flush();
	}
	
//	暂时不知道为什么要try catch
//	protected void write(String noticeData,String charsetName,HttpServletResponse response)throws Exception{
//		ServletOutputStream out = null;
//		try{
//			out = response.getOutputStream();
//			
//			out.write(noticeData.getBytes(charsetName));
//			
//			out.flush();
//		}catch(Exception e){
//			LogUtil.log(CT.LOG_CATEGORY_ERR, "输出流写数据过程失败,原因：" + e.getMessage(), e, LogUtilMg.LOG_ERROR, CT.LOG_CATEGORY_ERR);
//			throw e;
//		}finally{
//			
//		}
//	}
	
	protected void write(JSONObject data,String dataKey,String format,String charset,HttpServletResponse response) throws Exception {
		PrintWriter out = null;
		try{
			String rdata = data.toString();
			response.setStatus(HttpServletResponse.SC_OK);
			if("json".equalsIgnoreCase(format)){
				response.setContentType("text/plain;charset="+charset);
			}else if("xml".equalsIgnoreCase(format)){
				response.setContentType("text/xml;charset="+charset);
			
				Iterator<?> its = data.getJSONObject(dataKey).keys();
				rdata = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root><"+dataKey;
				
				while(its.hasNext()){
					Object nextObj = its.next();
					if(nextObj != null) {
						String attrName = nextObj.toString();
						String attrValue = data.getString(attrName);
						rdata =rdata+" "+attrName+"="+attrValue;
					}
				}
				rdata =rdata+ " /></root>";
				
			}else{
				response.setContentType("text/plain;charset="+charset);
			}
			response.setCharacterEncoding(charset);
			
			out = response.getWriter();
		
			out.write(rdata);
			
			out.flush();
		}catch(Exception e){
			LogUtil.log(CT.LOG_CATEGORY_ERR, "输出流写数据过程失败,原因：" + e.getMessage(), e, LogUtilMg.LOG_ERROR, CT.LOG_PATTERN_ERR);
			throw e;
		}finally{
			
		}	
	
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String,String> getParamMap(HttpServletRequest request){
		Map<String,String> paramMap = new HashMap<String, String>();
		Enumeration<String> enumeration = request.getParameterNames();
		while(enumeration.hasMoreElements()){
			String name = enumeration.nextElement();
            paramMap.put(name, request.getParameter(name));
		}
		return paramMap;
	}
	
	protected String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	//判断客户端请求行为
	protected String[] checkClientRequest(HttpServletRequest request)throws Exception{
		String[] clients = new String[2];
		String userAgent = request.getHeader("User-Agent");
		if(userAgent.contains(CT.CLIENT_INFO_IS_BROWSER)){
			clients[0] =  CT.CLIENT_INFO_IS_BROWSER;
		}else if(userAgent.contains(CT.CLIENT_INFO_IS_JAVA)){
			clients[0] =  CT.CLIENT_INFO_IS_JAVA;
		}else{
			clients[0] =  CT.CLIENT_INFO_IS_UNKNOW;
		}
		
		if(userAgent.contains(CT.CLIENT_INFO_IS_BROWSER_IE)){
			clients[1] =  CT.CLIENT_INFO_IS_BROWSER_IE;
		}else if(userAgent.contains(CT.CLIENT_INFO_IS_BROWSER_CHROME)){
			clients[1] =  CT.CLIENT_INFO_IS_BROWSER_CHROME;
		}else if(userAgent.contains(CT.CLIENT_INFO_IS_BROWSER_FIREFOX)){
			clients[1] =  CT.CLIENT_INFO_IS_BROWSER_FIREFOX;
		}else if(userAgent.contains(CT.CLIENT_INFO_IS_BROWSER_OPERA)){
			clients[1] =  CT.CLIENT_INFO_IS_BROWSER_OPERA;
		}else if(userAgent.contains(CT.CLIENT_INFO_IS_BROWSER_SAFARI)){
			clients[1] =  CT.CLIENT_INFO_IS_BROWSER_SAFARI;
		}else{
			clients[1] =  CT.CLIENT_INFO_IS_BROWSER_UNKNOW;
		}
		
	    return clients;
	}
	
	public String checkDevice(HttpServletRequest request){
		String userAgent =request.getHeader("user-agent");
		String sUserAgent= userAgent == null ? "" : userAgent.toLowerCase();
		int bIsIpad= sUserAgent.indexOf("ipad");
		int bIsIphoneOs= sUserAgent.indexOf("iphone os");
		int bIsMidp= sUserAgent.indexOf("midp");
		int bIsUc7= sUserAgent.indexOf("rv:1.2.3.4");
		int bIsUc= sUserAgent.indexOf("ucweb");
		int bIsAndroid= sUserAgent.indexOf("android");
		int bIsCE= sUserAgent.indexOf("windows ce");
		int bIsWinPhone= sUserAgent.indexOf("windows mobile");
		if ((bIsIpad + bIsIphoneOs + bIsMidp + bIsUc7 + bIsUc + bIsAndroid + bIsCE + bIsWinPhone)>0) {
			return "WAP";
		} else {
			return "PC";
		}
	}
}
