package com.org.caches;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.interfaces.caches.Container;
import com.org.services.WxUserService;
import com.org.util.SpringUtil;
import com.org.wx.utils.WxUserUtil;

/**
 * �û���Ϣ���������ڻ����û���Ϣ
 * �������ֵ��һ�����û���Ϣ����JSONArray����һ����key(String) - value(JSONObject)��ʽ�ģ����ڴ��봦��
 * @author Administrator
 *
 */
public class WxUserContainer implements Container{
	private static Map<String, JSONObject> wxUserInfoMap;
	private static WxUserContainer temp;
	private WxUserContainer(){}

	public JSONArray getWxUserList(){
		// TODO 
		return null;
	}

	/**
	 * ��ʼ���û�������Ϣ
	 */
	public void init(){
		wxUserInfoMap = new HashMap<String, JSONObject>();
		WxUserService wxService = (WxUserService)SpringUtil.getBean("wxUserService");
		JSONArray array = wxService.queryAll(null);
		
		JSONObject temp = null;
		String key = null;
		if(array != null) {
			for (int i = 0; i < array.size(); i++) {
				temp = array.getJSONObject(i);
				key = temp.getString("openid");
				wxUserInfoMap.put(key, temp);
			}
			log.info("�ѳ�ʼ���û���Ϣ"+ array.size() +"��");
		} else {
			log.info("δ��ѯ���Ѵ����û�");
		}
	}

	/**
	 * ��ȡ�û���Ϣ, �ȴӻ����л�ȡ��û���ٴ����ݿ��ȡ
	 * @param openid
	 * @return
	 */
	public JSONObject getLocalUser(String openid){
		if(wxUserInfoMap.containsKey(openid)) {
			return wxUserInfoMap.get(openid);
		}

		log.info("getLocalUser ��������Ϣ��ִ��΢�Ų�ѯ");
		// ����openidȥ΢�Ų�ѯ�û���Ϣ
		JSONObject res = WxUserUtil.getUserBaseInfo(openid);
		// 
		WxUserService wxService = (WxUserService)SpringUtil.getBean("wxUserService");
		// ���棬���������ݿⱣ����û���Ϣ
		log.info("getLocalUser ΢�Ų�ѯ������Ϣ���浽����");
		JSONObject dbUserInfo = wxService.saveAndReturn(res);
		wxUserInfoMap.put(openid, dbUserInfo);
		return dbUserInfo;
	}
	
	/**
	 * ���������û���Ϣ��ֻ��ȫ�ֻ����û���Ϣ��ʱ����ã��������ڲ����û���Ϣ����
	 * @param args ���ݿ������û���Ϣ�б�
	public void cacheUserInfoArray(JSONArray args){
		wxUserInfoMap = new HashMap<String, JSONObject>();
		JSONObject temp = null;
		String keytemp = null;
		for (int i = 0; i < args.size(); i++) {
			temp = args.getJSONObject(i);
			keytemp = temp.getString("openid");
			wxUserInfoMap.put(keytemp, temp);
		}
	}
	*/
	
	/**
	 * ���浥���û���Ϣ
	 * @param args
	 */
	public void cacheUserInfo(JSONObject temp){
		if(temp != null) {
			String keytemp = temp.getString("openid");
			wxUserInfoMap.put(keytemp, temp);
		}
	}
	
	public static WxUserContainer getInstance(){
		if(temp == null) {
			temp = new WxUserContainer();
		}
		return temp;
	}
	
	private static Log log = LogFactory.getLog(WxUserContainer.class);
}
