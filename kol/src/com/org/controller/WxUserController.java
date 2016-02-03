package com.org.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;

import com.org.controller.webapp.utils.WxUserContainer;
import com.org.servlet.SmpHttpServlet;

@Controller
public class WxUserController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = 2156792239072761671L;

	public WxUserController(){
		
	}
	
	public void getGroupidList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject responseJson = WxUserContainer.getGroupidList();
		request.setAttribute("groupid", responseJson.toString());
		System.out.println("getGroupid"+ responseJson);
		this.forward("/www/html/test_grouplist.jsp", request, response);
		return;
	}
	
	public void getUserList(HttpServletRequest request, HttpServletResponse response) {}
	
	
	public void getUserBaseInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String openid = request.getParameter("openid");
		JSONObject baseinfo = WxUserContainer.getUserBaseInfo(openid);
		request.setAttribute("baseinfo", baseinfo);
		this.forward("/www/html/test_userbaseinfo.jsp", request, response);
		return;
	}
	
	public void post(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
}
