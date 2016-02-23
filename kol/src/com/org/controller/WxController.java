package com.org.controller;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.org.common.CommonConstant;
import com.org.interfaces.controller.CommonController;
import com.org.interfaces.rute.Business;
import com.org.rute.RuteAdapter;
import com.org.rute.RuteThreadPool;
import com.org.servlet.SmpHttpServlet;
import com.org.util.CT;
import com.org.utils.DateUtil;
import com.org.utils.HttpTool;
import com.org.utils.HttpUtil;
import com.org.utils.StringUtil;
import com.org.utils.XmlUtils;
import com.org.wx.utils.WxUtil;

@Controller
public class WxController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = 2156792239072761671L;
	private Log log = LogFactory.getLog(WxController.class);

	public WxController(){
	}
	
	/**
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void rute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("token: " + this.getParamMap(request));
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		
		boolean signResult = WxUtil.checkSignature(signature, timestamp, nonce);
		if(!signResult) {
			log.info("验签错误");
			return;
		}
		String echostr = request.getParameter("echostr");
		if(StringUtils.isNotEmpty(echostr)) {
			// 表示是首次验签
			this.write(echostr, CT.ENCODE_UTF8, response);
			return;
		}
		
		JSONObject xmlJson = XmlUtils.getDocumentFromRequest(request);
		log.info("收到微信服务器的消息：xmlJson=====> " + xmlJson);
		
		String result = dealBusiness(xmlJson);
		this.write(result, CommonConstant.UTF8, response);
		return;
	}
	
	private String dealBusiness(JSONObject xmlJson){
		Business<String> event = RuteAdapter.adapter(xmlJson);
		String dateStr = DateUtil.getyyyyMMddHHmmss();
		try {
			Future<String> result = RuteThreadPool.submit(event);
			if(result != null) {
				return result.get();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.info(dateStr + "--> dealBusiness InterruptedException: " + e.getMessage());
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.info(dateStr + "--> dealBusiness ExecutionException: " + e.getMessage());
		}
		return WxUtil.replyStr("系统出现异常，请联系管理员，错误时间：" + dateStr, xmlJson);
	}
	
	// TODO 测试用的
	public void getCacheToken(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = WxUtil.getToken();
		String timestamp = String.valueOf(StringUtil.getTimestamp()); // 必填，生成签名的时间戳
		String nonceStr = UUID.randomUUID().toString(); // 必填，签名，见附录1
		String url = request.getRequestURL().toString();
		String signature = WxUtil.localSign(timestamp, nonceStr, url); // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("nonceStr", nonceStr);
		request.setAttribute("url", url);
		request.setAttribute("signature", signature);
		request.setAttribute("cacheToken", token);
		this.forward("/www/html/wxtest.jsp", request, response);
		return;
	}
	
	public void initMenu(HttpServletRequest request, HttpServletResponse response) {
		String resMsg = WxUtil.createMenu();
		log.info("initMenu ====> " + resMsg);
		request.setAttribute(CommonConstant.RESP_MSG, resMsg);
		//this.forward("/www/html/wxtest.jsp", request, response);
		return;
	}
	
	public void deleteBottomMenu(HttpServletRequest request, HttpServletResponse response) {
		WxUtil.deleteBottomMenu();
		//this.forward("/www/html/wxtest.jsp", request, response);
		return;
	}
	
	/**
	 * 申请开通摇一摇
	 * @param request
	 * @param response
	 */
	public void applicationYao(HttpServletRequest request, HttpServletResponse response) {
		
		JSONObject param = new JSONObject();
		param.put("name", "jurimengs");
		param.put("phone_number", "17749753878");
		param.put("email", "17749753878@qq.com");
		param.put("industry_id", "1204"); // 机构组织     其他组织     不需要资质文件 
		param.put("qualification_cert_urls", new JSONArray()); // 机构组织     其他组织     不需要资质文件 
		param.put("apply_reason", "test"); // 机构组织     其他组织     不需要资质文件 
		
		String url = "https://api.weixin.qq.com/shakearound/account/register?access_token=".concat(WxUtil.getToken());
		HttpTool http = new HttpUtil();
		JSONObject res = http.wxHttpsPost(param, url);
		log.info("申请开通摇一摇：" + res.toString());
		return;
	}
	
	/**
	 * 申请开通摇一摇 查询审核状态
	 * @param request
	 * @param response
	 */
	public void applicationYaoResult(HttpServletRequest request, HttpServletResponse response) {
		
		JSONObject param = new JSONObject();
		param.put("name", "jurimengs");
		param.put("phone_number", "17749753878");
		param.put("email", "17749753878@qq.com");
		param.put("industry_id", "1204"); // 机构组织     其他组织     不需要资质文件 
		param.put("qualification_cert_urls", new JSONArray()); // 机构组织     其他组织     不需要资质文件 
		param.put("apply_reason", "test"); // 机构组织     其他组织     不需要资质文件 
		
		String url = "https://api.weixin.qq.com/shakearound/account/auditstatus?access_token=".concat(WxUtil.getToken());
		HttpTool http = new HttpUtil();
		JSONObject res = http.wxHttpsGet(null, url);
		res.put("backup", "审核状态。0：审核未通过、1：审核中、2：审核已通过；审核会在三个工作日内完成");
		log.info("查询审核状态：" + res.toString());
		// 0：审核未通过、1：审核中、2：审核已通过；审核会在三个工作日内完成
		try {
			this.write(res, CommonConstant.UTF8, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	public void post(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
}
