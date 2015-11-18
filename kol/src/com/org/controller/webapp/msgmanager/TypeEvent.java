package com.org.controller.webapp.msgmanager;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.controller.webapp.WxConstant;
import com.org.controller.webapp.utils.WxUserContainer;
import com.org.controller.webapp.utils.WxUtil;

/**
 * request from wx , type is "event"
 * @author Administrator
 *
 */
public class TypeEvent extends MessageManager implements Event {
	private Log log = LogFactory.getLog(TypeEvent.class);
	private JSONObject xmlJson;

	public TypeEvent(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}

	@Override
	public String deal() {
		String FromUserName = xmlJson.getString("FromUserName");
		String Event = xmlJson.getString("Event");
		String EventKey = xmlJson.getString("EventKey"); // ��Ӧ�Զ����key ֵ
		log.info("EventKey ====>" + EventKey);
		// ���¼����� �� ����İ�ťkeyֵ�ж� ���Ծ���ҵ������
		if(Event.equals("CLICK")) {
			log.info("��Ϣ���Ϳ�ʼ");
			JSONObject returns = null;
			JSONObject paramContent = new JSONObject();
			if(WxUtil.ENTER_CHATING_ROOM.equals(EventKey)) {
				// ����������
				WxUserContainer.joininChatingRoom(FromUserName);
				
				JSONObject contentTemp = new JSONObject();
				contentTemp.put("content", "���ѽ���������, ���Ժʹ��������");
				paramContent.put("touser", FromUserName);
				paramContent.put("msgtype", "text");
				paramContent.put("text", contentTemp);
				
				returns = pushMessage(paramContent, "wx_send_message_by_service");
			} else if(WxUtil.EXIT_CHATING_ROOM.equals(EventKey)) {
				// ����������
				WxUserContainer.exitChatingRoom(FromUserName);
				JSONObject contentTemp = new JSONObject();
				contentTemp.put("content", "�����˳�������");
				
				paramContent.put("touser", FromUserName);
				paramContent.put("msgtype", "text");
				paramContent.put("text", contentTemp);
			}
			
			returns = pushMessage(paramContent, "wx_send_message_by_service");
			if(returns != null && (returns.getInt("errcode")==0)) {
				log.info("��Ϣ���ͳɹ�");
			}
		}
		return WxConstant.RETURN_SUCCESS;
	}

	public JSONObject getXmlJson() {
		return xmlJson;
	}

	public void setXmlJson(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}
	
}
