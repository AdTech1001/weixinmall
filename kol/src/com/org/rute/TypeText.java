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

		return returnStr;
	}

	public JSONObject getXmlJson() {
		return xmlJson;
	}

	public void setXmlJson(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}

}
