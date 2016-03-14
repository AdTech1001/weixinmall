package com.org.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.org.common.UserConstant;
import com.org.interfaces.controller.CommonController;
import com.org.services.busi.JsService;
import com.org.servlet.SmpHttpServlet;
import com.org.util.SpringUtil;
import com.org.utils.DateUtil;

/**
 * ÓÃ»§²Ù×÷¸ú×Ù
 * @author Administrator
 *
 */
@Controller
public class JsController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = 2156792239072761671L;
	public JsController(){
		
	}
	
	@Override
	public void post(HttpServletRequest request, HttpServletResponse response) {
	}
	
	public void jsListener(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("jsListener...");
		String userId = "";
		if(request.getSession(true).getAttribute(UserConstant.SESSION_USER) != null) {
			JSONObject user = (JSONObject) request.getSession(true).getAttribute(UserConstant.SESSION_USER);
			userId = user.getString("id");
		}
		
		String browserName = request.getParameter("browserName");
		String browserVersion = request.getParameter("browserVersion");
		String operateName = request.getParameter("operate");
		String operateDateTime = DateUtil.getDateStringByFormat(DateUtil.yyyyMMddHHmmss);
		String currentPage = request.getParameter("currentPage");
		String userAgent = request.getParameter("userAgent");
		String localAddr = request.getLocalAddr();
		String remoteAddr =  request.getRemoteAddr();
		
		JsService js = (JsService) SpringUtil.getBean("jsService");
		js.save(userId, browserName, browserVersion, operateName, operateDateTime, 
				currentPage, userAgent, localAddr, remoteAddr);
		
	}
	
	private Log log = LogFactory.getLog(JsController.class);
}
