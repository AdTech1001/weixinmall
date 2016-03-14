package com.org.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;

import com.org.interfaces.controller.CommonController;
import com.org.servlet.SmpHttpServlet;
import com.org.utils.DateUtil;

/**
 * HTML5 ÏûÏ¢ÍÆËÍ
 * @author Administrator
 *
 */
@Controller
public class MessagePushController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = -2554067244094241952L;

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void push(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		try {
			response.setHeader("Content-Type", "text/event-stream");
			response.setHeader("Cache-Control", "no-cache");
			response.setCharacterEncoding ("UTF-8");
			
			String date = DateUtil.getDateStringByFormat(DateUtil.DATE_FORMAT_DATE);
			JSONObject temp = new JSONObject();
			temp.put("date", date);
			temp.put("id", "aa");
			
			response.getWriter().println("data:"+temp);
			response.getWriter().println();
			response.getWriter().flush();
		} catch (Exception e) {
		}
		
//		Enumeration<String> headers = request.getHeaderNames();
//		while (headers.hasMoreElements()) {
//			String name = (String) headers.nextElement();
//			System.out.println(name+":"+request.getHeader(name));	
//		}
//		System.out.println("--------------------------------");
//		response.setHeader("Content-Type", "text/event-stream");
//		response.setHeader("Cache-Control", "no-cache");
//		response.setCharacterEncoding ("UTF-8");
//		String id = new Date().toString();
//		response.getWriter().println("id:");
//		response.getWriter().println("data:server-sent event is working.");
//		response.getWriter().println("data:test server-sent event multi-line data");
//		response.getWriter().println();
//		response.getWriter().flush();
		
	}

	@Override
	public void post(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
}
