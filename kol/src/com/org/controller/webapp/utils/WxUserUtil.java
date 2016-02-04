package com.org.controller.webapp.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.utils.PropertyUtil;
import com.org.utils.http.HttpTool;
import com.org.utils.http.impl.HttpUtil;

public class WxUserUtil {
	public static String WX_TOKEN_KEY = "wxToken"+PropertyUtil.getValue("wx", "appid"); // 微信端的token key
	private static Log log = LogFactory.getLog(WxUserUtil.class);
	private static HttpTool http = new HttpUtil();
	
	public static final String ENTER_CHATING_ROOM = "enterChatingroom";
	public static final String EXIT_CHATING_ROOM = "exitChatingroom";
	
	private static final String WX_GET_BATCH_USER_INFO = PropertyUtil.getValue("wx", "wx_get_batch_user_baseinfo");

	/**
	 * 获取userList 
	 * 数据示例：{"total":13,"count":13,"data":{"openid":["osp6swkdED28-Bo9k0zV1TBfAVwM","osp6swsMgghqE9kB-58Jd_411bCg","osp6swk9_DfmWZ093q8ShHchMjdM","osp6swvOvVa1aXcjbFGui0Ur88V4","osp6swkQTyVxg6GhdBhlad9FKZBU","osp6swlzafjgKrXIJXTW9q5RiV74","osp6swoC_jT04slc3TKFddSgO45c","osp6swpoMllDnL5hsshCXklZXsVg","osp6swnBLWkDJWivjzzvg4wi_fZY","osp6swnoZMzfQAuaaiVr1p90W2qk","osp6swlTQrl7KzkgaQTsjPcTIrB8","osp6swrNZiWtEuTy-Gj1cBVA1l38","osp6swkgabGhn7_Jqt7zGU90095A"]},"next_openid":"osp6swkgabGhn7_Jqt7zGU90095A"}
	 */
	public static JSONArray getOpenidList() {
		String token = Memcache.getInstance().getValue(WX_TOKEN_KEY);
		String url = PropertyUtil.getValue("wx", "wx_get_userid_list").concat(token);
		JSONObject resultJson = http.wxHttpsGet(null, url);
		// 第一次查询请求得到的数组
		JSONArray openidArray = resultJson.getJSONObject("data").getJSONArray("openid");
		// 判断是否还需要再查询
		// 先获取总数
		int total = resultJson.getInt("total");
		// 再获取本次查询获得的数，并且在后面的循环中，以此数为基础，将查询得到的结果，累加
		int count = resultJson.getInt("count");
		// 如果总数大于 10000，表示还有未查的
		if(total > 10000) {
			// 只要
			while (count < total) {
				String nextOpenid = resultJson.getString("next_openid");
				// 如果当前循环累计的总数，小于实际用户总数，则需要再发起查询
				// https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID1
				String urlTemp = url.concat("&next_openid=").concat(nextOpenid);
				JSONObject resultTemp = http.wxHttpsGet(null, urlTemp);
				
				int countTemp = resultTemp.getInt("count");
				if(countTemp > 0) {
					JSONArray userArrayTemp = resultTemp.getJSONObject("data").getJSONArray("openid");
					// 再添加进去 
					openidArray.add(userArrayTemp);
					count += countTemp;
				}
			}
		}
		return openidArray;
	}
	
	/**
	 * 获取批量查询用户信息
	 * 返回数据示例：{"total":13,"count":13,"data":{"openid":["osp6swkdED28-Bo9k0zV1TBfAVwM","osp6swsMgghqE9kB-58Jd_411bCg","osp6swk9_DfmWZ093q8ShHchMjdM","osp6swvOvVa1aXcjbFGui0Ur88V4","osp6swkQTyVxg6GhdBhlad9FKZBU","osp6swlzafjgKrXIJXTW9q5RiV74","osp6swoC_jT04slc3TKFddSgO45c","osp6swpoMllDnL5hsshCXklZXsVg","osp6swnBLWkDJWivjzzvg4wi_fZY","osp6swnoZMzfQAuaaiVr1p90W2qk","osp6swlTQrl7KzkgaQTsjPcTIrB8","osp6swrNZiWtEuTy-Gj1cBVA1l38","osp6swkgabGhn7_Jqt7zGU90095A"]},"next_openid":"osp6swkgabGhn7_Jqt7zGU90095A"}
	 * @param request
	 * @param response
	 */
	public static JSONArray getUserInfoByOpenidList(JSONArray openidArray) {
		String token = Memcache.getInstance().getValue(WX_TOKEN_KEY);
		String url = WX_GET_BATCH_USER_INFO.concat(token);
		
		JSONArray paramArray = new JSONArray();
		JSONObject singleTemp = new JSONObject();
		
		JSONArray userArray = new JSONArray();
		
		for (int i = 0; i < openidArray.size(); i++) {
			if((i % 100) == 0) {
				singleTemp = new JSONObject();
			}
			singleTemp.put("openid", openidArray.getString(i));
			singleTemp.put("lang", "zh-CN");
			paramArray.add(singleTemp);
			

			if(paramArray.size() == 100 || i == (openidArray.size() -1)) {
				JSONObject paramJsonObj = new JSONObject();
				paramJsonObj.put("user_list", paramArray);
				JSONObject resultTemp = http.wxHttpsPost(paramJsonObj, url);
				userArray.addAll(resultTemp.getJSONArray("user_info_list"));
			}
			
		}
			
		
		
		/*for (int i = 0; i < openidArray.size(); i++) {
			singleTemp.put("openid", openidArray.getString(i));
			singleTemp.put("lang", "zh-CN");
			paramArray.add(singleTemp);
		}
		JSONObject paramJsonObj = new JSONObject();
		paramJsonObj.put("user_list", paramArray);
		JSONObject resultTemp = http.wxHttpsPost(paramJsonObj, url);
		
		JSONArray userArray = resultTemp.getJSONArray("user_info_list");*/
		log.debug("getUserInfoByOpenidList: "+userArray.toString());
		return userArray;
	}
	
	public void getUserBaseInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String openid = request.getParameter("openid");
		WxUserContainer.getUserBaseInfo(openid);
		return;
	}
	
}
