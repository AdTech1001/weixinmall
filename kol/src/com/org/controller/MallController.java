package com.org.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.org.common.CommonConstant;
import com.org.container.WxSession;
import com.org.interfaces.controller.CommonController;
import com.org.model.WxUser;
import com.org.servlet.SmpHttpServlet;
import com.org.utils.HttpTool;
import com.org.utils.HttpUtil;
import com.org.wx.utils.WxUtil;

@Controller
public class MallController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = -892141492706657148L;
	private Log log = LogFactory.getLog(MallController.class);

	public MallController(){
	}
	
	public void post(HttpServletRequest request, HttpServletResponse response) {
		log.info(this.getParamMap(request).toString());
		// {state=123, code=031bc730cca7eb3ab51ba8322130801I}
		// 用户授权。
		String code = request.getParameter("code");
		String secret = WxUtil.getSecret();
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx6f9dc2bfd436b651&secret="+secret+"&code="+code+"&grant_type=authorization_code";
		HttpTool http = new HttpUtil();
		JSONObject res = http.wxHttpsGet(null, url);
		log.info(res.toString());
		// {"access_token":"OezXcEiiBSKSxW0eoylIeG_LpV4TpnX-BxNbAVVAasaRyPm55zyI9CKaVNciQOEw8iu_pEDXCiBKbbSJbzzqarhyfecqXoplnmCl7HsBiWFARy1Ob3MealEkubEDs8KHeRbAr5Awrvr7RR3i5t24GA","expires_in":7200,"refresh_token":"OezXcEiiBSKSxW0eoylIeG_LpV4TpnX-BxNbAVVAasaRyPm55zyI9CKaVNciQOEwUHOmtG9PkoiFUefqTDaX00sVqxhfoyE-jbYDCIjldLBnZvj1QP0gGev-Tw2BWQWTdIOnZ9EQDB0Oi0w2ZlT0lA","openid":"osp6swrNZiWtEuTy-Gj1cBVA1l38","scope":"snsapi_base"}
		
		String openid = res.getString("openid");
		
		
		WxUser wxUser = checkLogin();
		/*if(wxUser == null) {
			log.info("微信自动登录功能失败，页面跳转登录页面，请用户用手机号登录");
			this.forward("/user/relogin.jsp", request, response);
			return;
		}*/
		this.forward("/mall/index.jsp", request, response);
		return;
	}
	
	private WxUser checkLogin(){
		WxSession session = WxSession.getInstance();
		Object userTemp = null;
		for (int i = 0; i < 10; i++) {
			// 循环10次，每次等待0.5秒，相当于等待5秒
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 如果0.5秒后再查询不为空，则break
			userTemp = session.getAttribute(CommonConstant.WX_USER_SESSION);
			if(userTemp != null) {
				break;
			}
		}
		if(userTemp != null) {
			return (WxUser)userTemp;
		}
		return null;
	}
}
