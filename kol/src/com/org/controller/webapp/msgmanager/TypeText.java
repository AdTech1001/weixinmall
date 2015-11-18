package com.org.controller.webapp.msgmanager;

import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.controller.webapp.utils.WxUserContainer;
import com.org.controller.webapp.utils.WxUtil;

/**
 * request from wx , type is "text"
 * @author Administrator
 *
 */
public class TypeText extends MessageManager implements Event {
	private Log log = LogFactory.getLog(TypeText.class);
	private JSONObject xmlJson;

	public TypeText(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}

	@Override
	public String deal() {
		String returnStr = "";
		// ����Ϣ����
		String msgFromOpenid = xmlJson.getString("FromUserName");
		// ����Ϣ�ߵ��ǳ�
		String nick = WxUserContainer.getUserBaseInfo(msgFromOpenid).getString("nickname") ;
		Map<String, Boolean> chatingUsersMap = WxUserContainer.getChatingOpenidsMap();
		// �ж����Ƿ���������
		if(chatingUsersMap.containsKey(msgFromOpenid) && chatingUsersMap.get(msgFromOpenid)) {
			// ֱ�ӻظ�success�� �ɿͷ��ӿ�ȥ������Ϣ
			returnStr = "success";
			// ���ǻ�ȡ���е��û�
			// JSONObject userListJson = WxUserContainer.getUserList();
			// JSONObject data = userListJson.getJSONObject("data");
			// JSONArray openidList = data.getJSONArray("openid");
			JSONArray chatingUserArray = WxUserContainer.getChatingUser();
			// �����г�ȥ����Ϣ���Լ�
			chatingUserArray.remove(msgFromOpenid);
			log.info("openidList ====>"+chatingUserArray);
			
			String content = nick + "˵:\n"+xmlJson.getString("Content");
			// ��0��ʼ�ݹ鷢�ͣ�ʵ��Ⱥ��
			pushMassMessage(msgFromOpenid, chatingUserArray, content, 0);
		} else {
			returnStr = WxUtil.createMenu(xmlJson);
		}
		return returnStr;
	}

	public JSONObject getXmlJson() {
		return xmlJson;
	}

	public void setXmlJson(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}
	
}
