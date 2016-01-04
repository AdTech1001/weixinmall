package com.org.controller.webapp.msgmanager;

import java.util.Map;
import java.util.concurrent.Callable;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.controller.webapp.utils.WxUserContainer;
import com.org.controller.webapp.utils.WxUtil;

/**
 * request from wx , type is "news"
 * @author Administrator
 *
 */
public class TypeNews extends ServiceMessageManager implements Event, Callable<String> {
	private Log log = LogFactory.getLog(TypeNews.class);
	private JSONObject xmlJson;

	public TypeNews(JSONObject xmlJson) {
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
			JSONArray chatingUserArray = WxUserContainer.getChatingUser();
			// �����г�ȥ����Ϣ���Լ�
			chatingUserArray.remove(msgFromOpenid);
			log.info("openidList ====>" + chatingUserArray);
			
			String content = nick + "˵:\n"+xmlJson.getString("Content");
			// ÿ���˶�Ҫ�յ�ͬ��һ����Ϣ
			JSONObject paramContent = getTextMessageJson(content);
			// ��0��ʼ�ݹ鷢�ͣ�ʵ��Ⱥ��
			pushMassMessage(chatingUserArray, paramContent, 0);
		} else {
			returnStr = WxUtil.autoReply(xmlJson);
		}
		return returnStr;
	}
	
	public JSONObject getXmlJson() {
		return xmlJson;
	}

	public void setXmlJson(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}

	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
