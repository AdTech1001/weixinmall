package com.org.controller.webapp.msgmanager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.controller.webapp.WxConstant;
import com.org.controller.webapp.utils.WxUserContainer;
import com.org.controller.webapp.utils.WxUtil;
import com.org.dao.CommonDao;
import com.org.util.SpringUtil;

/**
 * request from wx , type is "event"
 * @author Administrator
 *
 */
public class TypeEvent extends ServiceMessageManager implements Event, Callable<String> {
	private Log log = LogFactory.getLog(TypeEvent.class);
	private JSONObject xmlJson;

	public TypeEvent(JSONObject xmlJson) {
		this.xmlJson = xmlJson;
	}

	@Override
	public String call() throws Exception {
		String FromUserName = xmlJson.getString("FromUserName");
		String Event = xmlJson.getString("Event");
		String EventKey = xmlJson.getString("EventKey"); // 对应自定义的key 值
		log.info("EventKey ====>" + EventKey);
		// 拿事件类型 和 点击的按钮key值判断 可以决定业务类型
		if(Event.equals("CLICK")) {
			log.info("消息推送开始");
			JSONObject returns = null;
			JSONObject paramContent = new JSONObject();
			if(WxUtil.ENTER_CHATING_ROOM.equals(EventKey)) {
				// 加入聊天室
				WxUserContainer.joininChatingRoom(FromUserName);
				// 回复文本消息
				paramContent = getTextMessageJson("您已进入聊天室, 可以和大家聊天啦");
			} else if(WxUtil.EXIT_CHATING_ROOM.equals(EventKey)) {
				// 退出聊天室
				WxUserContainer.exitChatingRoom(FromUserName);
				// 回复文本消息
				paramContent = getTextMessageJson("您已退出聊天室");
			}
			
			paramContent.put("touser", FromUserName);
			returns = pushMessage(paramContent, "wx_send_message_by_service");
			if(returns != null && (returns.getInt("errcode")==0)) {
				log.info("消息推送成功");
			}
		} else if(Event.equals("unsubscribe")) {
			// 用户取消关注
			// TODO 更新用户关注状态 
			
		} else if(Event.equals("subscribe")) {
			// TODO 用户关注
			// 添加用户
			String addSql = "INSERT INTO wx_user_info (openid, nickname, sex, subscribe, subscribe_time) VALUES (?, ?, ?, ?, ?)";
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
			try {
				commonDao.addSingle(addSql, params);
			} catch (SQLException e) {
				e.printStackTrace();
				log.info("subscribe ====> 保存关注用户异常");
			}
		}
		return WxConstant.RETURN_SUCCESS;
	}
	
	@Override
	public String deal() {
		String FromUserName = xmlJson.getString("FromUserName");
		String Event = xmlJson.getString("Event");
		String EventKey = xmlJson.getString("EventKey"); // 对应自定义的key 值
		log.info("EventKey ====>" + EventKey);
		// 拿事件类型 和 点击的按钮key值判断 可以决定业务类型
		if(Event.equals("CLICK")) {
			log.info("消息推送开始");
			JSONObject returns = null;
			JSONObject paramContent = new JSONObject();
			if(WxUtil.ENTER_CHATING_ROOM.equals(EventKey)) {
				// 加入聊天室
				WxUserContainer.joininChatingRoom(FromUserName);
				// 回复文本消息
				paramContent = getTextMessageJson("您已进入聊天室, 可以和大家聊天啦");
			} else if(WxUtil.EXIT_CHATING_ROOM.equals(EventKey)) {
				// 退出聊天室
				WxUserContainer.exitChatingRoom(FromUserName);
				// 回复文本消息
				paramContent = getTextMessageJson("您已退出聊天室");
			}
			
			paramContent.put("touser", FromUserName);
			returns = pushMessage(paramContent, "wx_send_message_by_service");
			if(returns != null && (returns.getInt("errcode")==0)) {
				log.info("消息推送成功");
			}
		} else if(Event.equals("unsubscribe")) {
			// 用户取消关注
			// TODO 更新用户关注状态 
			
		} else if(Event.equals("subscribe")) {
			// TODO 用户关注
			// 添加用户
			String addSql = "INSERT INTO wx_user_info (openid, nickname, sex, subscribe, subscribe_time) VALUES (?, ?, ?, ?, ?)";
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
			try {
				commonDao.addSingle(addSql, params);
			} catch (SQLException e) {
				e.printStackTrace();
				log.info("subscribe ====> 保存关注用户异常");
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
