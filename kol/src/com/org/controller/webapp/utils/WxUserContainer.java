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
 * ΢���û�����Ϣ�����ڻ����û��ĵ�ǰ�������û��顢�û�ID�ȡ�
 * @author Administrator
 *
 */
public class WxUserContainer {
	
	private static JSONObject groupInfo = new JSONObject();
	private static JSONObject wxUserList = new JSONObject();
	private static JSONArray localUserList = new JSONArray();
	
	private static JSONObject wxUserInfo = new JSONObject();
	
	
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
			System.out.println("��ѯ�û��б�������  �� " + requestJson.toString());
			wxUserList = http.wxHttpsPost(requestJson, remoteUrl);
			return wxUserList;
		}
		return wxUserList;
	}
	
	/**
	 * ��ȡ�����û��б�
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
	 * @openId ΢���û�ID
	 * @return
	 */
	public static JSONObject getUserGroup(String openid) {
		String token = Memcache.getInstance().getValue(WxUtil.WX_TOKEN_KEY);
		String remoteUrl = PropertyUtil.getValue("wx", "wx_get_user_group");
		remoteUrl = remoteUrl.concat(token);
		
		JSONObject requestJson = new JSONObject();
		requestJson.put("openid", openid);
		
		HttpTool http = new HttpUtil();
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
		
		// �Ȳ�ѯ�����е��û�list
		// �ӷ��ص�json�����л�ȡ��data
		JSONObject data = wxUserList.getJSONObject("data");
		// ��data�л�ȡ��openid����
		JSONArray openidList = data.getJSONArray("openid");
		JSONObject userBaseInfo = null;
		// ����ÿ��openid�� �ٲ�ѯ�������Ϣ����������
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
			// ��ȡ�û��Ļ�����Ϣ
			//userBaseInfo = getUserBaseInfo(openid);
			GROUP_USER_INFO.put(openid, userBaseInfo);
		}
		
		JSONObject toquery = new JSONObject();
		toquery.put("user_list", toqueryArray);
		
		// ��������ѯ�û���Ϣ
		JSONObject batchUserInfo = getBatchUserInfoFromWx(toquery);
		JSONArray userInfoList = batchUserInfo.getJSONArray("user_info_list");
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
	
	/**
	 * �Ȳ鱾�أ��ٲ�΢��
	 * @param openid
	 * @return
	 */
	public static JSONObject getUserBaseInfo(String openid) {
		String token = Memcache.getInstance().getValue(WxUtil.WX_TOKEN_KEY);
		String remoteUrl = PropertyUtil.getValue("wx", "wx_get_user_baseinfo");
		remoteUrl = remoteUrl.concat(token).concat("&openid=").concat(openid);
		
		JSONObject requestJson = new JSONObject();
		HttpTool http = new HttpUtil();
		LogUtil.log(WxUtil.class, "��ѯ�û�������Ϣ������  �� " + requestJson.toString(), null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_NULL);
		JSONObject userGroup = http.wxHttpsPost(requestJson, remoteUrl);
		LogUtil.log(WxUtil.class, "��ѯ�û�������Ϣ���󷵻�  �� " + userGroup.toString(), null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_NULL);
		return userGroup;
	}
	
	/**
	 * ����openid ��΢�Ų�ѯ�û�������Ϣ
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
		
		// ��url
		String token = Memcache.getInstance().getValue(WxUtil.WX_TOKEN_KEY);
		String remoteUrl = PropertyUtil.getValue("wx", "wx_get_batch_user_baseinfo");
		remoteUrl = remoteUrl.concat(token);
		
		// ��httppost��ѯ
		HttpTool http = new HttpUtil();
		LogUtil.log(WxUtil.class, "��ѯ�û�������Ϣ������  �� " + openidList.toString(), null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_NULL);
		JSONObject userGroup = http.wxHttpsPost(openidList, remoteUrl);
		LogUtil.log(WxUtil.class, "��ѯ�û�������Ϣ���󷵻�  �� " + userGroup.toString(), null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_NULL);
		// ����
		return userGroup;
	}
}
