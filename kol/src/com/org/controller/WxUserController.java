package com.org.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;

import com.org.caches.Memcache;
import com.org.common.CommonConstant;
import com.org.interfaces.controller.CommonController;
import com.org.servlet.SmpHttpServlet;
import com.org.utils.HttpTool;
import com.org.utils.HttpUtil;
import com.org.utils.PropertyUtil;
import com.org.wx.utils.WxUserUtil;
import com.org.wx.utils.WxUtil;

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
		String resMsg = WxUserUtil.synchUserInfo();
		request.setAttribute(CommonConstant.RESP_MSG, resMsg);
		this.forward("/manager/result.jsp", request, response);
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
		String token = Memcache.getInstance().getValue(CommonConstant.WX_TOKEN_KEY);
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
