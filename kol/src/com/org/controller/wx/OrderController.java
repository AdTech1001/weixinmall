package com.org.controller.wx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.org.caches.ProductContainer;
import com.org.common.CommonConstant;
import com.org.interfaces.controller.CommonController;
import com.org.servlet.SmpHttpServlet;

@Controller
public class OrderController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = -892141492706657148L;
	private Log log = LogFactory.getLog(OrderController.class);

	public OrderController(){
	}
	
	public void post(HttpServletRequest request, HttpServletResponse response) {}
	
	public void directBuy(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		log.info("order -> buy ====> "+this.getParamMap(request).toString());
		String productid = request.getParameter("productid");
		
		if(StringUtils.isEmpty(productid)) {
			request.setAttribute(CommonConstant.RESP_CODE, "error");
			request.setAttribute(CommonConstant.RESP_MSG, "请确认是否选中商品");
			this.forward("/manager/result.jsp", request, response);
			return;
		}
		
		// 获取商品列表
		JSONObject product = ProductContainer.getInstance().getById(Integer.valueOf(productid));
		session.setAttribute("product", product); 
		
		// 生成订单
		// TODO 这里要考虑做分布式系统,调用订单系统接口，完成订单的创建
		String orderId = "524522111";
		session.setAttribute("orderId", orderId);
		
		// 拿到商品信息后，直接到支付页面
		this.forward("/mall/pay.jsp", request, response);
		return;
	}
	
}
