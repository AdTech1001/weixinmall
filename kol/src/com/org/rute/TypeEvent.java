package com.org.rute;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.caches.RoomContainer;
import com.org.controller.WxConstant;
import com.org.interfaces.rute.Business;
import com.org.model.WxUser;
import com.org.services.WxUserService;
import com.org.util.SpringUtil;
import com.org.utils.DateUtil;
import com.org.wx.utils.MessageUtil;
import com.org.wx.utils.WxUserUtil;
import com.org.wx.utils.WxUtil;

/**
 * request from wx , type is "event"
 * @author Administrator
 *
 */
public class TypeEvent implements Business<String> {
	private Log log = LogFactory.getLog(TypeEvent.class);
	private JSONObject xmlJson;

	public TypeEvent(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}

	@Override
	public String call() throws Exception {
		String FromUserName = xmlJson.getString("FromUserName");
		String Event = xmlJson.getString("Event");
		String EventKey = xmlJson.getString("EventKey"); // ��Ӧ�Զ����key ֵ
		// ���¼����� �� ����İ�ťkeyֵ�ж� ���Ծ���ҵ������
		if(Event.equals("CLICK")) {
			log.info("��Ϣ���Ϳ�ʼ");
			JSONObject returns = null;
			String content = "";
			WxUser wxUser = new WxUser(FromUserName);
			if(WxUtil.ENTER_CHATING_ROOM.equals(EventKey)) {
				// ����������
				wxUser.joininChatingRoom(RoomContainer.DEFAULT_ROOM_ID);
				// �ظ��ı���Ϣ
				content = "���ѽ���������, ���Ժʹ��������";
			} else if(WxUtil.EXIT_CHATING_ROOM.equals(EventKey)) {
				// �˳�������
				wxUser.exitChatingRoom();
				// �ظ��ı���Ϣ
				content = "�����˳�������";
			}
			
			returns = MessageUtil.sendToOne(content, FromUserName);
			if(returns != null && (returns.getInt("errcode")==0)) {
				log.info("��Ϣ���ͳɹ�");
			}
		} else if(Event.equals("unsubscribe") || Event.equals("subscribe")) {
			
			JSONObject actual = WxUserUtil.getUserBaseInfo(FromUserName);
			String subscribe = actual.containsKey("subscribe") ? actual.getString("subscribe") : null;
			String subscribe_time = DateUtil.getDateStringByFormat(DateUtil.yyyyMMddHHmmss);
			String nickname = actual.containsKey("nickname") ? actual.getString("nickname") : null;
			String sex = actual.containsKey("sex") ? actual.getString("sex") : null;
			String headimgurl = actual.containsKey("headimgurl") ? actual.getString("headimgurl") : null;
			String country = actual.containsKey("country") ? actual.getString("country") : null;
			String province = actual.containsKey("province") ? actual.getString("province") : null;
			String city = actual.containsKey("city") ? actual.getString("city") : null;
			
			WxUserService uService = (WxUserService)SpringUtil.getBean("wxUserService");
			uService.saveOrUpdate(FromUserName, nickname, sex, subscribe_time, subscribe, headimgurl, country, province, city);

		}
		// 
		return WxConstant.RETURN_SUCCESS;
	}
	
	public JSONObject getXmlJson() {
		return xmlJson;
	}

	public void setXmlJson(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}
	
}
