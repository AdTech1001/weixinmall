package com.org.controller.wx;

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
		String str = "{\"user_list\":[{\"openid\":\"obS_VjrUEPN20UyxF29rUiLeeJfU\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjutmfO4y4cDA3ZgVJ--AAgc\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjpxYHn_8l6WmIMY1YWQpJ08\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjmjiJWktMDMaU89GEycDzgk\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjnOXTOp-EOIRQ83VnfRbDyA\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjuLq-FEPYN4tLw8dUZYIzRY\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjrWJfYX6OUey2EoFDIjX1qo\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjgMeNRpVJYPhRPGNUCbC6tI\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjoZFtgXqsd5f2LJpT3GO9_Q\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjvw43AxrfKR_OhfEbAXUxsM\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjk7r1qJsRDwY7uOPDzYLvrM\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjnxtR47bDWl0T6jveRVwd_k\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjkrwUM6AqGUr85Xybec5UQw\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjgs1_Zi40c69OHtcLQlrR1o\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjhLyTwpsl5G14bzh3XHuRkM\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjrbQAQ_G46jwoCGnptbivbY\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjl9O_Z76hBU39rYAkqty6V8\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjkiuih5ECBK7WjmoVmPcJ28\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjh-0XOEMZjsF6xUZ13hH5bQ\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjvL4gSSOMw3sqFCzT_ZzHjo\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjkTEbKSxYdxu5f_zeQClMts\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjrmo8WIz7lVKgU8cdPmfG8Y\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjsiuBf7eT_foD8qGHECLUfU\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjvHnx-nXXV2qn-62NROyP2o\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjq7IefZJqTWmGrn3dVMembQ\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjlr9Wkxkr7kZG3Bo0TPs01Y\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vji6axRcWn4aJ2oSME5JKB1I\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjvMfYa_nuwxPVZg2A_AI5B8\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjmtXhEKFHbeCrESzlVvnwMI\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjjnocWe7q4TdZUWsMJ4b_Eo\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjiLTZ4TgY4Zae1tS5NgpbiQ\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjrbpVBZ_2pdMxvIUm0IPs-U\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjsXU95-qYiXIPtd465Np-q0\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjkWfu7ym_R9X8wjCsIJEaKI\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjnGqAejTQvK-hef283O2_lo\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjuAdwnUE4s1I_x99gNgKId8\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjio1t19HJKKAvb6YYQmr3Ew\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjlJXfbsVO16STAliehsN9Vg\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjiR8X_ZGm5lK9LDJqERHUng\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjtd2N8gJLJjk2oUhwaHDPSg\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjmsz74vmvatT_jAX8_hIfTg\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjteZkLUODXweYXRcLZkSpC0\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjgeUtxHMEi8VfG4uzXl6X8s\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjtoibiIbeQ9OUvbrWsJj9V0\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjo9iETaTCBJwT37z2B1CkLA\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjjimWYWjfTrFYJgvLcYPwto\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjmqlVlY4glmsxNvbIKceye4\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjqwctIXSmlBvdh5ulH2vOgs\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjnpowC-7FOUyR_UHmey7zQc\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjlRkZm_abQLFfCgNFswpTNw\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjt76dMQvhu9yMqK5GgpSwoc\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjtlsz299xKM5HPYaWuaEHL4\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjnwGhBN-jOw1ddgAzPgyupM\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjqF7k3P-N-uqflAGeygEnVY\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjj4OjSsRPjNgPm-Cd8XmmWw\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjoM2fPgEjDDpnfHO3Axcy_c\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjuT5y9RYBQ_NoPwMRqehiiM\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjlqNIdzep4r4ET7KSJZ5Sac\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjsWi01p5A6jWzAgAeWfEZgo\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjsIqjCRIjjG1B1SIc1HarSI\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjgSi-O1AA5marlutOly2LgY\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjiPjN2byQuUDUIc_EsJgpJQ\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjkmGO8g89kAaCZ2f2xgGT5s\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjiPLrWRWtC9z4SLwkjaUgf0\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjta8NCn-ZjvFHE24eLuJ-oU\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjtov6k-k5qGFft_iioY1YJo\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjm-GJoCMN0YB9SkmTGP5bug\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjl9JDfocQRz_UWaoy1eHo1o\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjm5w9M0g6GWe3_hRhOY-qHg\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjv9cGnqImRhYtVAggVGeq2g\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjim5He-E7YB9ZgZSUN13mlw\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjhNXbnkOpzn_lhfq28IEdWQ\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjnfOpqdqX41ct-_Y3vqKYsA\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjgvlhhHGjJlEP6p-zJxIsec\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjnZ49BpvjUmwqJwwsIG2n_o\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjpe7nKFtiBfoGJZMZZoy3Cg\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjlbGmKusAEmWjT4VPYP8d1g\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjurex4ehpooaREBfYZ6aZaw\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjuKpCLgfPvJmfbbK8OOy9Vo\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjpjdh5SRI23HXVk4bGx_QSs\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjupP656BwiRsNQQWFS4k9E4\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjrXpg7qhHqP2_o1GcqgRzxY\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjsGTtH2Ub03BC8Zo0SjIWho\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjs9Fxx02hFNsQSEee7W6H4c\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjuk-STH6IOOgV3hUTHTtpX8\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjnY_KObkrN7RJ3etHYFHfN0\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vju73__fIQRyUmukQPTSQhWQ\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjqg83wmlhFhK-ohoRuX770k\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjqmR5wueILjFkLRrPb_F5cs\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjnRSMJcI_FvToWsSqgiAb8A\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjorOPAuoH39osn_wX-0-TCU\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjq3eVOgZAK4QDwaP_qI_oyM\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjjUwchKc1QNb44A0t3J4epc\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjp4LpzBUS_-_QZxucZPL-VQ\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjtCuItjSN9ggpZs5vPPDMww\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjmktcAMDwTpo64onHAiyD9E\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjlRt4uH97IqBs4xymQDAoHA\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjlTJ_IR-xllGJtGTqe5kh0M\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_Vjj40X1D2LFWCmLIjGWsmdzY\",\"lang\":\"zh-CN\"},{\"openid\":\"obS_VjhzX79FR-N4Tlgffk27asWA\",\"lang\":\"zh-CN\"}]}";
		String token = Memcache.getInstance().getValue(CommonConstant.WX_TOKEN_KEY);
		String url = PropertyUtil.getValue("wx", "wx_get_batch_user_baseinfo").concat(token);
		HttpTool http = new HttpUtil();
		
		JSONObject temp = JSONObject.fromObject(str);
		JSONObject resultTemp = http.wxHttpsPost(temp, url);
		System.out.println(resultTemp.getJSONArray("user_info_list"));
		try {
			this.write(resultTemp.getJSONArray("user_info_list").toString(), CommonConstant.UTF8, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
