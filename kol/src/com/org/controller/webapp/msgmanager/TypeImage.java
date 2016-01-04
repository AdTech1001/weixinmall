package com.org.controller.webapp.msgmanager;

import java.util.Map;
import java.util.concurrent.Callable;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.org.controller.webapp.WxConstant;
import com.org.controller.webapp.utils.WxUserContainer;
import com.org.controller.webapp.utils.WxUtil;

/**
 * request from wx , type is "image"
 * @author Administrator
 *
 */
public class TypeImage extends ServiceMessageManager implements Event, Callable<String> {
	//private Log log = LogFactory.getLog(TypeText.class);
	private JSONObject xmlJson;

	public TypeImage(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}

	@Override
	public String deal() {
		// ����Ϣ����
		String msgFromOpenid = xmlJson.getString("FromUserName");
		// ��ϢҪ�����Ķ����б�
		JSONArray chatingUserArray = WxUserContainer.getChatingUser();
		// ����Ϣ�ߵ��ǳ�
		Map<String, Boolean> chatingUsersMap = WxUserContainer.getChatingOpenidsMap();
		// �ж����Ƿ���������
		String returnStr = "";
		if(chatingUsersMap.containsKey(msgFromOpenid) && chatingUsersMap.get(msgFromOpenid)) {
			returnStr = WxConstant.RETURN_SUCCESS;
			// �����г�ȥ����Ϣ���Լ�
			chatingUserArray.remove(msgFromOpenid);
			// ����Ϣ�ߵ��ǳ�
			String nick = WxUserContainer.getUserBaseInfoFromLocal(msgFromOpenid).getString("nickname") ;
			
			String mediaId = nick + ":\n"+xmlJson.getString("MediaId");
			JSONObject paramContent = getImageMessageJson(mediaId);
			
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
