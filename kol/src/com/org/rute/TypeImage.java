package com.org.rute;

import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.org.caches.WxUserContainer;
import com.org.controller.WxConstant;
import com.org.interfaces.rute.Business;
import com.org.wx.utils.MessageUtil;
import com.org.wx.utils.WxUtil;

/**
 * request from wx , type is "image"
 * @author Administrator
 *
 */
public class TypeImage implements Business<String> {
	//private Log log = LogFactory.getLog(TypeText.class);
	private JSONObject xmlJson;

	public TypeImage(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}

	@Override
	public String call() {
		/*// 发消息的人
		String msgFromOpenid = xmlJson.getString("FromUserName");
		// 消息要反馈的对象列表
		JSONArray chatingUserArray = WxUserContainer.getChatingUser();
		// 发消息者的昵称
		Map<String, Boolean> chatingUsersMap = WxUserContainer.getChatingOpenidsMap();
		// 判断下是否在聊天室
		String returnStr = "";
		if(chatingUsersMap.containsKey(msgFromOpenid) && chatingUsersMap.get(msgFromOpenid)) {
			returnStr = WxConstant.RETURN_SUCCESS;
			// 从组中除去发信息者自己
			chatingUserArray.remove(msgFromOpenid);
			// 发消息者的昵称
			// TODO 图文消息是没办法添加这个nick的
			String nick = "test";
			
			String mediaId = nick + ":\n"+xmlJson.getString("MediaId");
			JSONObject paramContent = MessageUtil.getImageMessageJson(mediaId);
			
			MessageUtil.pushMassMessage(chatingUserArray, paramContent, 0);
		} else {
			returnStr = WxUtil.autoReply(xmlJson);
		}
		return returnStr;*/

		// TODO 
		return null;
	}

	public JSONObject getXmlJson() {
		return xmlJson;
	}

	public void setXmlJson(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}
	
}
