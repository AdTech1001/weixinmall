package com.org.controller.webapp.rute;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.controller.webapp.WxConstant;
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
		String EventKey = xmlJson.getString("EventKey"); // ��Ӧ�Զ����key ֵ
		log.info("EventKey ====>" + EventKey);
		// ���¼����� �� ����İ�ťkeyֵ�ж� ���Ծ���ҵ������
		if(Event.equals("CLICK")) {
			log.info("��Ϣ���Ϳ�ʼ");
			JSONObject returns = null;
			String content = "";
			if(WxUtil.ENTER_CHATING_ROOM.equals(EventKey)) {
				// ����������
				WxUserContainer.joininChatingRoom(FromUserName);
				// �ظ��ı���Ϣ
				content = "���ѽ���������, ���Ժʹ��������";
			} else if(WxUtil.EXIT_CHATING_ROOM.equals(EventKey)) {
				// �˳�������
				WxUserContainer.exitChatingRoom(FromUserName);
				// �ظ��ı���Ϣ
				content = "�����˳�������";
			}
			
			returns = MessageUtil.sendToOne(content, FromUserName);
			if(returns != null && (returns.getInt("errcode")==0)) {
				log.info("��Ϣ���ͳɹ�");
			}
		} else if(Event.equals("unsubscribe") || Event.equals("subscribe")) {
			
			CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
			String sql = "select 1 from wx_user_info where openid = ?";
			Map<Integer, Object> queryParams = new HashMap<Integer, Object>();
			queryParams.put(1, FromUserName);
			
			JSONObject actual = WxUserContainer.getUserBaseInfo(FromUserName);
			String subscribe = actual.getString("subscribe");
			String subscribe_time = DateUtil.getDateStringByFormat(DateUtil.yyyyMMddHHmmss);
			
			if(commonDao.isExist(sql, queryParams, null) != null) {
				// update
				String updateSql = "update wx_user_info set subscribe=?, subscribe_time=? where openid =?";
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(1, subscribe); // 
				params.put(2, subscribe_time);
				params.put(3, FromUserName);
				commonDao.update(updateSql, params);
			} else {
				// insert 
				String nickname = actual.getString("nickname");
				String sex = actual.getString("sex");
				
				String addSql = "insert into wx_user_info (openid, nickname, sex, subscribe, subscribe_time) VALUES (?, ?, ?, ?, ?)";
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(1, FromUserName); // 
				params.put(2, nickname);
				params.put(3, sex);
				params.put(4, subscribe);
				params.put(5, subscribe_time);
				try {
					commonDao.addSingle(addSql, params);
				} catch (SQLException e) {
					e.printStackTrace();
					log.info("subscribe ====> �����ע�û��쳣");
				}
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
