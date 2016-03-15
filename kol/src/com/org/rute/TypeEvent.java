package com.org.rute;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.caches.RoomContainer;
import com.org.caches.WxUserContainer;
import com.org.controller.wx.WxConstant;
import com.org.interfaces.rute.Business;
import com.org.model.wx.WxUser;
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
		// 拿事件类型 和 点击的按钮key值判断 可以决定业务类型
		if(Event.equals("CLICK")) {
			log.info("CLICK 事件处理");
			String EventKey = xmlJson.getString("EventKey"); // 对应自定义的key 值
			JSONObject returns = null;
			String content = "";
			WxUser wxUser = WxUserContainer.getInstance().getLocalUser(FromUserName);
			
			if(WxUtil.ENTER_CHATING_ROOM.equals(EventKey)) {
				// 加入聊天室
				if(wxUser.getRoomId() != null) {
					wxUser.exitChatingRoom();
				}
				wxUser.joininChatingRoom(RoomContainer.DEFAULT_ROOM_ID);
				// 回复文本消息
				content = "您已进入聊天室, 可以和大家聊天啦";
			} else if(WxUtil.EXIT_CHATING_ROOM.equals(EventKey)) {
				// 退出聊天室
				wxUser.exitChatingRoom();
				// 回复文本消息
				content = "您已退出聊天室";
			} else if(WxUtil.ENTER_STORE_ROOM.equals(EventKey)) {
				// 进入故事模式
				if(wxUser.getRoomId() != null) {
					wxUser.exitChatingRoom();
				}
				wxUser.joininChatingRoom(RoomContainer.STORY_ROOM_ID);
				// 回复文本消息
				content = "您已进入故事会";
			} else if(WxUtil.EXIT_STORE_ROOM.equals(EventKey)) {
				// 退出聊天室
				wxUser.exitChatingRoom();
				// 回复文本消息
				content = "您已退出故事会";
			}
			
			returns = MessageUtil.sendToOne(content, FromUserName);
			if(returns != null && (returns.getInt("errcode")==0)) {
				log.info("消息推送成功");
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
		} else if(Event.equals("LOCATION")) {
			// {"ToUserName":"gh_b4c1774a1ef7","FromUserName":"osp6swrNZiWtEuTy-Gj1cBVA1l38","CreateTime":"1458029241","MsgType":"event","Event":"LOCATION","Latitude":"31.166275","Longitude":"121.389099","Precision":"30.000000"}
			// 经度
			String Latitude = xmlJson.getString("Latitude");
			// 纬度
			String Longitude = xmlJson.getString("Longitude");
			// 精度 基本没用
			//String Precision = xmlJson.getString("Precision");
			
			StringBuffer temp = new StringBuffer();
			temp.append("您的当前位置:\n");
			temp.append("经度:").append(Latitude);
			temp.append("纬度:").append(Longitude);
			JSONObject returns = MessageUtil.sendToOne(temp.toString(), FromUserName);
			if(returns != null && (returns.getInt("errcode")==0)) {
				log.info("消息推送成功");
			}
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
