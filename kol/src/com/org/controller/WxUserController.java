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
	 * 页面放一个按钮，一点就开始同步用户信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void initUserInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1 先获取本微信公众号的所有用户openid
		JSONArray openidArray = WxUserUtil.getOpenidList();
		JSONArray userInfoList = WxUserUtil.getUserInfoByOpenidList(openidArray);
		// 调用dao事务插入方法
		WxUserService wxDao = (WxUserService)SpringUtil.getBean("wxUserService");
		wxDao.transactionSaveOrUpdate(userInfoList);
		return;
	}

	/**
	 * 已测试，未使用
	 * 获取所有的组
	 * 示例：{"groups":[{"id":0,"name":"未分组","count":13},{"id":1,"name":"黑名单","count":0},{"id":2,"name":"星标组","count":0}]}
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
