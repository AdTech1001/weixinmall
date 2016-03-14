package com.org.rute;

import java.util.List;

import net.sf.json.JSONObject;

import com.org.caches.RoomContainer;
import com.org.caches.WxUserContainer;
import com.org.interfaces.rute.Business;
import com.org.model.wx.ChatingRoom;
import com.org.model.wx.WxUser;
import com.org.wx.utils.MessageUtil;
import com.org.wx.utils.WxUtil;

/**
 * request from wx , type is "text"
 * @author Administrator
 *
 */
public class TypeText implements Business<String> {
	//private Log log = LogFactory.getLog(TypeText.class);
	private JSONObject xmlJson;

	public TypeText(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}

	@Override
	public String call() {
		// 发消息的人
		String msgFromOpenid = xmlJson.getString("FromUserName");
		WxUser wxUser = WxUserContainer.getInstance().getLocalUser(msgFromOpenid);
		
		String returnStr = "";
		Integer roomid = wxUser.getRoomId();
		if(wxUser.getRoomId() != null) {
			// 如果在聊天室中
			ChatingRoom cr = RoomContainer.getInstance().getRoomById(roomid);
			if(cr != null) {
				// 这应该丟给一个队列处理
				String nick = wxUser.getNickname();
				String content = nick + ":\n" + xmlJson.getString("Content");
				
				List<String> openidList = cr.getAllOpenid();
				MessageUtil.sendToMulti(content, openidList);
			}
		} else {
			returnStr = WxUtil.autoReply(xmlJson);
		}
//		// 消息要反馈的对象列表
//		JSONArray chatingUserArray = WxUserContainer.getChatingUser();
//		// 发消息者的昵称
//		Map<String, Boolean> chatingUsersMap = WxUserContainer.getChatingOpenidsMap();
//		// 判断下是否在聊天室
//		String returnStr = "";
//		if(chatingUsersMap.containsKey(msgFromOpenid) && chatingUsersMap.get(msgFromOpenid)) {
//			returnStr = WxConstant.RETURN_SUCCESS;
//			// 从组中除去发信息者自己
//			//chatingUserArray.remove(msgFromOpenid);
//			// 发消息者的昵称
//			String nick = WxUserContainer.getUserBaseInfoFromLocal(msgFromOpenid).getString("nickname") ;
//			String content = nick + ":\n" + xmlJson.getString("Content");
//			JSONObject paramContent = MessageUtil.getTextMessageJson(content);
//			// 这里应该奖消息发送放到线程池中处理
//			MessageUtil.pushMassMessage(chatingUserArray, paramContent, 0);
//		} else {
//			returnStr = WxUtil.autoReply(xmlJson);
//		}
//		return returnStr;

		// TODO 
		return "";
	}

	public JSONObject getXmlJson() {
		return xmlJson;
	}

	public void setXmlJson(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}

}
