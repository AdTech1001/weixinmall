package com.org.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.org.interfaces.controller.CommonController;
import com.org.servlet.SmpHttpServlet;
import com.org.utils.FileUploadUtil;

@Controller
public class UploadController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = 2156792239072761671L;

	public UploadController(){
		
	}
	
	private Log log = LogFactory.getLog(UploadController.class);

	@Override
	public void post(HttpServletRequest request, HttpServletResponse response) {
		log.info("UploadController.......");
		// 同源策略不允许读取XXX上的远程资源
		// 告诉浏览器，这个资源是运行远程所有域名访问的。当然，此处的*也可以替换为指定的域名，出于安全考虑，建议将*替换成指定的域名
		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			FileUploadUtil.uploadFile(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
}
