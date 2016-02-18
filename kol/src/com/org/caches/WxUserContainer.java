package com.org.caches;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.org.model.WxUser;
import com.org.services.WxUserService;
import com.org.util.SpringUtil;
import com.org.wx.utils.WxUserUtil;

/**
 * 用户信息容器。用于缓存用户信息
 * 存放两组值，一组是用户信息数组JSONArray，另一组是key(String) - value(JSONObject)形式的，便于代码处理
 * @author Administrator
 *
 */
public class WxUserContainer {
	private WxUserContainer(){}
	private static Map<String, JSONObject> wxUserInfoMap = new HashMap<String, JSONObject>();
	private static Map<String, WxUser> wxUserInfoObject = new HashMap<String, WxUser>();

	public static JSONArray getWxUserList(){
		// TODO 
		return null;
	}

	/**
	 * 获取用户信息, 先从缓存中获取，没有再从数据库获取
	 * @param openid
	 * @return
	 */
	public static JSONObject getLocalUser(String openid){
		if(wxUserInfoMap.containsKey(openid)) {
			return wxUserInfoMap.get(openid);
		}
		// 根据openid去微信查询用户信息
		JSONObject res = WxUserUtil.getUserBaseInfo(openid);
		// 
		WxUserService wxService = (WxUserService)SpringUtil.getBean("wxUserService");
		// 保存，并返回数据库保存的用户信息
		JSONObject dbUserInfo = wxService.saveAndReturn(res);
		wxUserInfoMap.put(openid, dbUserInfo);
		return dbUserInfo;
	}
	
	/**
	 * 缓存所有用户信息，只在全局缓存用户信息的时候调用，不适用于部分用户信息缓存
	 * @param args 数据库查出的用户信息列表
	 */
	public static void cacheUserInfoArray(JSONArray args){
		JSONObject temp = null;
		String keytemp = null;
		for (int i = 0; i < args.size(); i++) {
			temp = args.getJSONObject(i);
			keytemp = temp.getString("openid");
			wxUserInfoMap.put(keytemp, temp);
		}
	}
	
	/**
	 * 缓存单个用户信息
	 * @param args
	 */
	public static void cacheUserInfo(JSONObject temp){
		if(temp != null) {
			String keytemp = temp.getString("openid");
			wxUserInfoMap.put(keytemp, temp);
		}
	}
}
