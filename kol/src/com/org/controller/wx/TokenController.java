package com.org.controller.wx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.org.common.CommonConstant;
import com.org.interfaces.controller.CommonController;
import com.org.servlet.SmpHttpServlet;
import com.org.util.PropertiesUtil;
import com.org.wx.utils.WxUtil;

@Controller
public class TokenController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = 2156792239072761671L;
	private Log log = LogFactory.getLog(getClass().getName());

	public TokenController(){
	}
	
	public void post(HttpServletRequest request, HttpServletResponse response) {
		log.info("请求加载token");
		String secret = request.getParameter("sec");
		if(secret.endsWith(PropertiesUtil.getProperties("supersecret", "wx"))){
			boolean res = WxUtil.wxInit();
			if(res) {
				this.write("初始成功", CommonConstant.UTF8, response);
				return;
			}
			this.write("初始失败", CommonConstant.UTF8, response);
			return;
		}
		this.write("口令错误", CommonConstant.UTF8, response);
		return;
	}
	
}
