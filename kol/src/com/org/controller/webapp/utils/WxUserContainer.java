package com.org.controller.webapp.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.org.dao.CommonDao;
import com.org.log.LogUtil;
import com.org.log.impl.LogUtilMg;
import com.org.util.CT;
import com.org.util.SpringUtil;
import com.org.utils.PropertyUtil;
import com.org.utils.http.HttpTool;
import com.org.utils.http.impl.HttpUtil;

/**
 * 微信用户组信息。用于缓存用户的当前操作、用户组、用户ID等。
 * @author Administrator
 *
 */
public class WxUserContainer {
	
	private static JSONObject groupInfo = new JSONObject();
	private static JSONObject wxUserList = new JSONObject();
	private static JSONArray localUserList = new JSONArray();
	
	private static JSONObject wxUserInfo = new JSONObject();
	
	
	/**
	 * 组：用户：用户基本信息容器 
	 * 数据结构: 
	 * groups：
	 * 类型：json
	 * 值：     {groupid(string): userinfoArray (jsonarray)}
	 * 
	 * userinfoArray:
	 * 类型：array
	 * 值：   [userinfo(json)]
	 * 
	 * userinfo:
	 * 类型 json
	 * 值：  {nick: jurry, userid: xxxxx}
	 */
	private static JSONObject GROUP_USER_INFO = new JSONObject();
	
	/**
	 * 这种设计的假设是只有一个聊天室，后面可能会有多个的需求，实现起来相对复杂，所以暂时不实现
	 * 类型：json
	 * 值： {userid : operatetype}
	 */
	private static Map<String, Boolean> CHATING_USER_LIST = new HashMap<String, Boolean>();
	
	
	public static void joininChatingRoom(String openid){
		CHATING_USER_LIST.put(openid, true);
	}
	
	public static void exitChatingRoom(String openid){
		if(CHATING_USER_LIST.containsKey(openid)) {
			CHATING_USER_LIST.remove(openid);
			//CHATING_USER_LIST.put(openid, false);
		}
	}
	
	/**
	 * 获取聊天的人
	 * @return
	 */
	public static Map<String, Boolean> getChatingOpenidsMap(){
		return CHATING_USER_LIST;
	}
	
	/**
	 * 获取聊天的人
	 * @return
	 */
	public static JSONArray getChatingUser(){
		return JSONArray.fromObject(CHATING_USER_LIST.keySet());
	}
	
	/**
	 * 查询组id
	 * @return 当前所有的微信组
	 */
	public static synchronized JSONObject getGroupidList() {
		if(groupInfo == null || groupInfo.isEmpty()) {
			String token = Memcache.getInstance().getValue(WxUtil.WX_TOKEN_KEY);
			String remoteUrl = PropertyUtil.getValue("wx", "wx_get_groupid");
			remoteUrl = remoteUrl.concat(token);
			
			JSONObject requestJson = new JSONObject();
			
			HttpTool http = new HttpUtil();
			groupInfo = http.httpPost(requestJson, remoteUrl, CT.ENCODE_UTF8);
			return groupInfo;
		}
		return groupInfo;
	}
	
	/**
	 * 
	 * @return
	 */
	public static JSONObject getWxUserList() {
		if(wxUserList == null || wxUserList.isEmpty()) {
			String token = Memcache.getInstance().getValue(WxUtil.WX_TOKEN_KEY);
			String remoteUrl = PropertyUtil.getValue("wx", "wx_get_userid_list");
			remoteUrl = remoteUrl.concat(token);
			
			JSONObject requestJson = new JSONObject();
			
			HttpTool http = new HttpUtil();
			System.out.println("查询用户列表请求报文  ： " + requestJson.toString());
			wxUserList = http.wxHttpsPost(requestJson, remoteUrl);
			return wxUserList;
		}
		return wxUserList;
	}
	
	/**
	 * 获取本地用户列表
	 * @return
	 */
	public static JSONArray getLocalUserList() {
		// wx_user_info
		String sql = "select openid from wx_user_info";
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		localUserList = commonDao.queryJSONArray(sql, params, null);
		return localUserList;	
	}
	
	/**
	 * 
	 * @openId 微信用户ID
	 * @return
	 */
	public static JSONObject getUserGroup(String openid) {
		String token = Memcache.getInstance().getValue(WxUtil.WX_TOKEN_KEY);
		String remoteUrl = PropertyUtil.getValue("wx", "wx_get_user_group");
		remoteUrl = remoteUrl.concat(token);
		
		JSONObject requestJson = new JSONObject();
		requestJson.put("openid", openid);
		
		HttpTool http = new HttpUtil();
		LogUtil.log(WxUtil.class, "查询用户所在组请求报文  ： " + requestJson.toString(), null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_NULL);
		JSONObject userGroup = http.wxHttpsPost(requestJson, remoteUrl);
		LogUtil.log(WxUtil.class, "查询用户所在组请求报文  ： " + userGroup.toString(), null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_NULL);
		return userGroup;
	}
	
	/**
	 * 这个动作一定要在初始化微信key之后执行。否则无效
	 * 根据一些基本信息去完善用户的信息结构
	 */
	public static void initUserInfo() {
		
		// 先查询到所有的用户list
		// 从返回的json数据中获取到data
		JSONObject data = wxUserList.getJSONObject("data");
		// 从data中获取到openid数组
		JSONArray openidList = data.getJSONArray("openid");
		JSONObject userBaseInfo = null;
		// 遍历每个openid， 再查询其基本信息，缓存起来
		String openid = "";
		// 
		JSONArray toqueryArray = new JSONArray();
		JSONObject temp;
		for (int i = 0; i < openidList.size(); i++) {
			openid = openidList.getString(i);
			if(!localUserList.contains(openid)) {
				temp = new JSONObject();
				temp.put("openid", openid);
				temp.put("lang", "zh-CN");
				toqueryArray.add(temp);
			}
			// 获取用户的基本信息
			//userBaseInfo = getUserBaseInfo(openid);
			GROUP_USER_INFO.put(openid, userBaseInfo);
		}
		
		JSONObject toquery = new JSONObject();
		toquery.put("user_list", toqueryArray);
		
		// 再批量查询用户信息
		JSONObject batchUserInfo = getBatchUserInfoFromWx(toquery);
		JSONArray userInfoList = batchUserInfo.getJSONArray("user_info_list");
	}
	
	/**
	 * 从本地缓存中获取用户基本信息，如果没有再去查询，查询后，缓存起来
	 * @param openid
	 * @return
	 */
	public static JSONObject getUserBaseInfoFromLocal(String openid) {
		//
		if(GROUP_USER_INFO.containsKey(openid)) {
			return GROUP_USER_INFO.getJSONObject(openid);
		}
		JSONObject baseInfo = getUserBaseInfo(openid);
		GROUP_USER_INFO.put(openid, baseInfo);
		return baseInfo;
	}
	
	/**
	 * 先查本地，再查微信
	 * @param openid
	 * @return
	 */
	public static JSONObject getUserBaseInfo(String openid) {
		String token = Memcache.getInstance().getValue(WxUtil.WX_TOKEN_KEY);
		String remoteUrl = PropertyUtil.getValue("wx", "wx_get_user_baseinfo");
		remoteUrl = remoteUrl.concat(token).concat("&openid=").concat(openid);
		
		JSONObject requestJson = new JSONObject();
		HttpTool http = new HttpUtil();
		LogUtil.log(WxUtil.class, "查询用户基本信息请求报文  ： " + requestJson.toString(), null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_NULL);
		JSONObject userGroup = http.wxHttpsPost(requestJson, remoteUrl);
		LogUtil.log(WxUtil.class, "查询用户基本信息请求返回  ： " + userGroup.toString(), null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_NULL);
		return userGroup;
	}
	
	/**
	 * 根据openid 向微信查询用户基本信息
	 * @param openidList
	 *  {
		    "user_list": [
		        {
		            "openid": "otvxTs4dckWG7imySrJd6jSi0CWE", 
		            "lang": "zh-CN"
		        }, 
		        {
		            "openid": "otvxTs_JZ6SEiP0imdhpi50fuSZg", 
		            "lang": "zh-CN"
		        }
		    ]
		}
	 * @return
	 */
	public static JSONObject getBatchUserInfoFromWx(JSONObject openidList) {
		
		// 组url
		String token = Memcache.getInstance().getValue(WxUtil.WX_TOKEN_KEY);
		String remoteUrl = PropertyUtil.getValue("wx", "wx_get_batch_user_baseinfo");
		remoteUrl = remoteUrl.concat(token);
		
		// 调httppost查询
		HttpTool http = new HttpUtil();
		LogUtil.log(WxUtil.class, "查询用户基本信息请求报文  ： " + openidList.toString(), null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_NULL);
		JSONObject userGroup = http.wxHttpsPost(openidList, remoteUrl);
		LogUtil.log(WxUtil.class, "查询用户基本信息请求返回  ： " + userGroup.toString(), null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_NULL);
		// 返回
		return userGroup;
	}
}
