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
 * �û���Ϣ���������ڻ����û���Ϣ
 * �������ֵ��һ�����û���Ϣ����JSONArray����һ����key(String) - value(JSONObject)��ʽ�ģ����ڴ��봦��
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
	 * ��ȡ�û���Ϣ, �ȴӻ����л�ȡ��û���ٴ����ݿ��ȡ
	 * @param openid
	 * @return
	 */
	public static JSONObject getLocalUser(String openid){
		if(wxUserInfoMap.containsKey(openid)) {
			return wxUserInfoMap.get(openid);
		}
		// ����openidȥ΢�Ų�ѯ�û���Ϣ
		JSONObject res = WxUserUtil.getUserBaseInfo(openid);
		// 
		WxUserService wxService = (WxUserService)SpringUtil.getBean("wxUserService");
		// ���棬���������ݿⱣ����û���Ϣ
		JSONObject dbUserInfo = wxService.saveAndReturn(res);
		wxUserInfoMap.put(openid, dbUserInfo);
		return dbUserInfo;
	}
	
	/**
	 * ���������û���Ϣ��ֻ��ȫ�ֻ����û���Ϣ��ʱ����ã��������ڲ����û���Ϣ����
	 * @param args ���ݿ������û���Ϣ�б�
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
	 * ���浥���û���Ϣ
	 * @param args
	 */
	public static void cacheUserInfo(JSONObject temp){
		if(temp != null) {
			String keytemp = temp.getString("openid");
			wxUserInfoMap.put(keytemp, temp);
		}
	}
}
