package com.org.controller.webapp.utils;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.org.log.LogUtil;
import com.org.log.impl.LogUtilMg;
import com.org.util.CT;
import com.org.utils.SmpPropertyUtil;
import com.org.utils.http.HttpTool;
import com.org.utils.http.impl.HttpApacheClient;

/**
 * ΢���û�����Ϣ�����ڻ����û��ĵ�ǰ�������û��顢�û�ID�ȡ�
 * @author Administrator
 *
 */
public class WxUserContainer {
	private static JSONObject groupInfo = new JSONObject();
	private static JSONObject userList = new JSONObject();
	
	/**
	 * �飺�û����û�������Ϣ���� 
	 * ���ݽṹ: 
	 * groups��
	 * ���ͣ�json
	 * ֵ��     {groupid(string): userinfoArray (jsonarray)}
	 * 
	 * userinfoArray:
	 * ���ͣ�array
	 * ֵ��   [userinfo(json)]
	 * 
	 * userinfo:
	 * ���� json
	 * ֵ��  {nick: jurry, userid: xxxxx}
	 */
	private static JSONObject GROUP_USER_INFO = new JSONObject();
	
	/**
	 * ������Ƶļ�����ֻ��һ�������ң�������ܻ��ж��������ʵ��������Ը��ӣ�������ʱ��ʵ��
	 * ���ͣ�json
	 * ֵ�� {userid : operatetype}
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
	 * ��ȡ�������
	 * @return
	 */
	public static Map<String, Boolean> getChatingOpenidsMap(){
		return CHATING_USER_LIST;
	}
	
	/**
	 * ��ȡ�������
	 * @return
	 */
	public static JSONArray getChatingUser(){
		return JSONArray.fromObject(CHATING_USER_LIST.keySet());
	}
	
	/**
	 * ��ѯ��id
	 * @return ��ǰ���е�΢����
	 */
	public static synchronized JSONObject getGroupidList() {
		if(groupInfo == null || groupInfo.isEmpty()) {
			String token = Memcache.getInstance().getValue(WxUtil.WX_TOKEN);
			String remoteUrl = SmpPropertyUtil.getValue("wx", "wx_get_groupid");
			remoteUrl = remoteUrl.concat(token);
			
			JSONObject requestJson = new JSONObject();
			
			HttpTool http = new HttpApacheClient();
			groupInfo = http.httpPost(requestJson, remoteUrl, CT.ENCODE_UTF8);
			return groupInfo;
		}
		return groupInfo;
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized JSONObject getUserList() {
		if(userList == null || userList.isEmpty()) {
			String token = Memcache.getInstance().getValue(WxUtil.WX_TOKEN);
			String remoteUrl = SmpPropertyUtil.getValue("wx", "wx_get_userid_list");
			remoteUrl = remoteUrl.concat(token);
			
			JSONObject requestJson = new JSONObject();
			
			HttpTool http = new HttpApacheClient();
			System.out.println("��ѯ�û��б�������  �� " + requestJson.toString());
			userList = http.wxHttpsPost(requestJson, remoteUrl);
			return userList;
		}
		return userList;
	}
	
	/**
	 * 
	 * @openId ΢���û�ID
	 * @return
	 */
	public static JSONObject getUserGroup(String openid) {
		String token = Memcache.getInstance().getValue(WxUtil.WX_TOKEN);
		String remoteUrl = SmpPropertyUtil.getValue("wx", "wx_get_user_group");
		remoteUrl = remoteUrl.concat(token);
		
		JSONObject requestJson = new JSONObject();
		requestJson.put("openid", openid);
		
		HttpTool http = new HttpApacheClient();
		LogUtil.log(WxUtil.class, "��ѯ�û�������������  �� " + requestJson.toString(), null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_NULL);
		JSONObject userGroup = http.wxHttpsPost(requestJson, remoteUrl);
		LogUtil.log(WxUtil.class, "��ѯ�û�������������  �� " + userGroup.toString(), null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_NULL);
		return userGroup;
	}
	
	/**
	 * �������һ��Ҫ�ڳ�ʼ��΢��key֮��ִ�С�������Ч
	 * ����һЩ������Ϣȥ�����û�����Ϣ�ṹ
	 */
	public static void initUserInfo() {
		//GROUP_USER_INFO
		// �Ȳ�ѯ�����е��û�list
		JSONObject userList = getUserList();
		// �ӷ��ص�json�����л�ȡ��data
		JSONObject data = userList.getJSONObject("data");
		// ��data�л�ȡ��openid����
		JSONArray openidList = data.getJSONArray("openid");
		JSONObject userBaseInfo = null;
		// ����ÿ��openid�� �ٲ�ѯ�������Ϣ����������
		String openid = "";
		for (int i = 0; i < openidList.size(); i++) {
			openid = openidList.getString(i);
			// ��ȡ�û��Ļ�����Ϣ
			// TODOӦ��Ҫ�ڻ�ȡ����Ϣ�󣬱��浽���ݿ�
			userBaseInfo = getUserBaseInfo(openid);
			GROUP_USER_INFO.put(openid, userBaseInfo);
		}
	}
	
	/**
	 * �ӱ��ػ����л�ȡ�û�������Ϣ�����û����ȥ��ѯ����ѯ�󣬻�������
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
	
	public static JSONObject getUserBaseInfo(String openid) {
		String token = Memcache.getInstance().getValue(WxUtil.WX_TOKEN);
		String remoteUrl = SmpPropertyUtil.getValue("wx", "wx_get_user_baseinfo");
		remoteUrl = remoteUrl.concat(token).concat("&openid=").concat(openid);
		
		JSONObject requestJson = new JSONObject();
		HttpTool http = new HttpApacheClient();
		LogUtil.log(WxUtil.class, "��ѯ�û�������Ϣ������  �� " + requestJson.toString(), null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_NULL);
		JSONObject userGroup = http.wxHttpsPost(requestJson, remoteUrl);
		LogUtil.log(WxUtil.class, "��ѯ�û�������Ϣ���󷵻�  �� " + userGroup.toString(), null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_NULL);
		return userGroup;
	}
}
