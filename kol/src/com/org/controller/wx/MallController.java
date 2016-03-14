package com.org.controller.wx;

import java.util.UUID;

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
import com.org.model.wx.WxUser;
import com.org.servlet.SmpHttpServlet;
import com.org.utils.StringUtil;
import com.org.wx.utils.WxUserUtil;
import com.org.wx.utils.WxUtil;

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
		
		String timestamp = String.valueOf(StringUtil.getTimestamp()); // 必填，生成签名的时间戳
		String nonceStr = UUID.randomUUID().toString(); // 必填，签名，见附录1
		String url = request.getRequestURL().toString(); // 由于微信端是获取请求地址的全路径，包括参数(但是#及其后的字符不要)一起参与签名
		String queryString = request.getQueryString();
		url = url.concat("?").concat(queryString);
		url = url.split("#")[0]; // 所以这里要把参数拼接上，再去掉#及其后的数据
		
		String signature = WxUtil.localSign(timestamp, nonceStr, url).toLowerCase(); // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		String appid = WxUtil.getAppid();
		
		
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("nonceStr", nonceStr);
		request.setAttribute("signature", signature);
		request.setAttribute("appId", appid);
		
//		log.info("timestamp: "+timestamp);
//		log.info("nonceStr: "+nonceStr);
//		log.info("signature: "+signature);
//		log.info("appid: "+appid);
//		log.info("url: "+url);
//		log.info("queryString: "+queryString);
		
		this.forward("/mall/index.jsp", request, response);
		return;
	}
	
	public void pay(HttpServletRequest request, HttpServletResponse response) {
		log.info("pay ====> "+this.getParamMap(request).toString());
		
		request.setAttribute(CommonConstant.RESP_CODE, "10000");
		request.setAttribute(CommonConstant.RESP_MSG, "支付成功");
		this.forward("/manager/result.jsp", request, response);
		return;
	}
	
}
