package com.org.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.org.controller.webapp.utils.WxUtil;
import com.org.servlet.CommonController;
import com.org.servlet.SmpHttpServlet;
import com.org.util.CT;
import com.org.utils.StringUtil;
import com.org.utils.XmlUtils;


@Controller
public class WxController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = 2156792239072761671L;

	public WxController(){
		
	}
	
	public void toWxTest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("token" + this.getParamMap(request));
		
		this.forward("/www/html/wxtest.jsp", request, response);
		return;
	}
	
	private Log log = LogFactory.getLog(WxController.class);
	
	public void validate(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("token: " + this.getParamMap(request));
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		
		boolean signResult = WxUtil.checkSignature(signature, timestamp, nonce);
		if(!signResult) {
			log.info("��ǩ����");
			return;
		}
		
		JSONObject xmlJson = XmlUtils.getDocumentFromRequest(request);
		// {"ToUserName":"gh_b35778044c48","FromUserName":"oUwjLwpYv0pkCC424mOxcBG24CVY","CreateTime":"1446189462","MsgType":"text","Content":"Ŷ","MsgId":"6211336443510120964"}
		String returnStr = WxUtil.createMenu(xmlJson);
		
		System.out.println("��Ӧ��: " + returnStr);
		this.write(returnStr, CT.ENCODE_UTF8, response);
		return;
	}

	public void toTest(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("toTest: " + this.getParamMap(request));
		String timestamp = String.valueOf(StringUtil.getTimestamp()); // �������ǩ����ʱ���
		String noncestr = UUID.randomUUID().toString(); // ���ǩ��������¼1
		String url = request.getRequestURL().toString();
		String signature = WxUtil.localSign(timestamp, noncestr, url); // �����Ҫʹ�õ�JS�ӿ��б�����JS�ӿ��б����¼2
		
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("noncestr", noncestr);
		request.setAttribute("url", url);
		request.setAttribute("signature", signature);
		
		this.forward("/www/html/wxtest.jsp", request, response);
		return;
	}
	
	public void getCacheToken(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String token = WxUtil.getToken();
		String timestamp = String.valueOf(StringUtil.getTimestamp()); // �������ǩ����ʱ���
		String nonceStr = UUID.randomUUID().toString(); // ���ǩ��������¼1
		String url = request.getRequestURL().toString();
		String signature = WxUtil.localSign(timestamp, nonceStr, url); // �����Ҫʹ�õ�JS�ӿ��б�����JS�ӿ��б����¼2
		
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("nonceStr", nonceStr);
		request.setAttribute("url", url);
		request.setAttribute("signature", signature);
		request.setAttribute("cacheToken", token);
		this.forward("/www/html/wxtest.jsp", request, response);
		return;
	}
	
	public void post(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
	}
	
}
