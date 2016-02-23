package com.org.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.org.caches.ProductContainer;
import com.org.common.CommonConstant;
import com.org.interfaces.controller.CommonController;
import com.org.model.WxUser;
import com.org.servlet.SmpHttpServlet;
import com.org.wx.utils.WxUserUtil;

@Controller
public class MallController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = -892141492706657148L;
	private Log log = LogFactory.getLog(MallController.class);

	public MallController(){
	}
	
	/**
	 * 先授权，得到用户信息后，再进入商城首页
	 */
	public void post(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		log.info(this.getParamMap(request).toString());
		// 用户授权。
		String code = request.getParameter("code");
		// 获取用户信息
		String openid = WxUserUtil.oauth(code);
		WxUser wxUser = new WxUser(openid);
		session.setAttribute(CommonConstant.WX_USER_SESSION, wxUser);
		
		// 获取商品列表
		JSONArray productList = ProductContainer.getInstance().getAll();
		session.setAttribute("productList", productList);
		
		this.forward("/mall/index.jsp", request, response);
		return;
	}
	
	public void pay(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		log.info("pay ====> "+this.getParamMap(request).toString());
		
		request.setAttribute(CommonConstant.RESP_CODE, "10000");
		request.setAttribute(CommonConstant.RESP_MSG, "支付成功");
		this.forward("/manager/result.jsp", request, response);
		return;
	}
	
}
