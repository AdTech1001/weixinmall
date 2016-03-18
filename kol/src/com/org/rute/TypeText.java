package com.org.rute;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.caches.RoomContainer;
import com.org.caches.WxUserContainer;
import com.org.interfaces.room.Room;
import com.org.interfaces.rute.Business;
import com.org.model.wx.WxUser;
import com.org.wx.utils.WxUtil;

/**
 * request from wx , type is "text"
 * @author Administrator
 *
 */
public class TypeText implements Business<String> {
	private Log log = LogFactory.getLog(TypeText.class);
	private JSONObject xmlJson;

	public TypeText(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}

	@Override
	public String call() {
		// ����Ϣ����
		String msgFromOpenid = xmlJson.getString("FromUserName");
		WxUser wxUser = WxUserContainer.getInstance().getLocalUser(msgFromOpenid);
		String returnStr = "";
		Integer roomid = wxUser.getRoomId();
		if(roomid != null) {
			// �������������
			Room cr = RoomContainer.getInstance().getRoomById(roomid);
			if(cr != null) {
				// ���͵Ĳ�����Ӧ�����ɷ������
				cr.sendToAll(wxUser, xmlJson.getString("Content"));
			} else {
				log.info("���䲻����: " + roomid);
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
