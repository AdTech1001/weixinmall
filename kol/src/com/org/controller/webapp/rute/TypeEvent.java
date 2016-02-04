package com.org.controller.webapp.rute;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.controller.WxConstant;
import com.org.controller.webapp.utils.WxUserContainer;
import com.org.controller.webapp.utils.WxUtil;
import com.org.dao.CommonDao;
import com.org.util.SpringUtil;
import com.org.utils.DateUtil;

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
		String EventKey = xmlJson.getString("EventKey"); // 对应自定义的key 值
		log.info("EventKey ====>" + EventKey);
		// 拿事件类型 和 点击的按钮key值判断 可以决定业务类型
		if(Event.equals("CLICK")) {
			log.info("消息推送开始");
			JSONObject returns = null;
			String content = "";
			if(WxUtil.ENTER_CHATING_ROOM.equals(EventKey)) {
				// 加入聊天室
				WxUserContainer.joininChatingRoom(FromUserName);
				// 回复文本消息
				content = "您已进入聊天室, 可以和大家聊天啦";
			} else if(WxUtil.EXIT_CHATING_ROOM.equals(EventKey)) {
				// 退出聊天室
				WxUserContainer.exitChatingRoom(FromUserName);
				// 回复文本消息
				content = "您已退出聊天室";
			}
			
			returns = MessageUtil.sendToOne(content, FromUserName);
			if(returns != null && (returns.getInt("errcode")==0)) {
				log.info("消息推送成功");
			}
		} else if(Event.equals("unsubscribe") || Event.equals("subscribe")) {
			
			CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
			Map<Integer, Object> queryParams = new HashMap<Integer, Object>();
			queryParams.put(1, FromUserName);
			
			JSONObject actual = WxUserContainer.getUserBaseInfo(FromUserName);
			String subscribe = actual.getString("subscribe");
			String subscribe_time = DateUtil.getDateStringByFormat(DateUtil.yyyyMMddHHmmss);
			String nickname = actual.getString("nickname");
			String sex = actual.getString("sex");
			
			String addSql = "insert into wx_user_info (openid, nickname, sex, subscribe, subscribe_time) VALUES (?, ?, ?, ?, ?)";
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(1, FromUserName); // 
			params.put(2, nickname);
			params.put(3, sex);
			params.put(4, subscribe);
			params.put(5, subscribe_time);
			commonDao.addSingle(addSql, params);
			
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
