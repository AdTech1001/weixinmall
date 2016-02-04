package com.org.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;

import com.org.controller.webapp.utils.Memcache;
import com.org.controller.webapp.utils.WxUserUtil;
import com.org.controller.webapp.utils.WxUtil;
import com.org.services.WxUserService;
import com.org.servlet.SmpHttpServlet;
import com.org.util.SpringUtil;
import com.org.utils.PropertyUtil;
import com.org.utils.http.HttpTool;
import com.org.utils.http.impl.HttpUtil;

@Controller
public class WxUserController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = 2156792239072761671L;

	public WxUserController(){
		
	}
	
	/**
	 * ҳ���һ����ť��һ��Ϳ�ʼͬ���û���Ϣ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void initUserInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1 �Ȼ�ȡ��΢�Ź��ںŵ������û�openid
		JSONArray openidArray = WxUserUtil.getOpenidList();
		JSONArray userInfoList = WxUserUtil.getUserInfoByOpenidList(openidArray);
		// ����dao������뷽��
		WxUserService wxDao = (WxUserService)SpringUtil.getBean("wxUserService");
		wxDao.transactionSaveOrUpdate(userInfoList);
		return;
	}

	/**
	 * �Ѳ��ԣ�δʹ��
	 * ��ȡ���е���
	 * ʾ����{"groups":[{"id":0,"name":"δ����","count":13},{"id":1,"name":"������","count":0},{"id":2,"name":"�Ǳ���","count":0}]}
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getGroupidList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = Memcache.getInstance().getValue(WxUtil.WX_TOKEN_KEY);
		String remoteUrl = PropertyUtil.getValue("wx", "wx_get_groupid").concat(token);
		
		HttpTool http = new HttpUtil();
		//JSONObject groupInfo = http.httpPost(requestJson, remoteUrl, CommonConstant.UTF8);
		JSONObject groupInfo = http.wxHttpsGet(null, remoteUrl);
		request.setAttribute("groupid", groupInfo.toString());
		this.forward("/www/html/test_grouplist.jsp", request, response);
		return;
	}
	
	public void post(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
}
