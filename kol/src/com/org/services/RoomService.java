package com.org.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.org.dao.CommonDao;
import com.org.model.ChatingRoom;
import com.org.util.SpringUtil;

/**
 * 用于同步微信用户信息
 * @author Administrator
 *
 */
@Service
public class RoomService {
	// 
	private static String sql_query_by_id = "select * from wx_room where id = ?";
	
	private static String sql_query_all = "select * from wx_room";

/*	public JSONObject query(String roomId){
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(1, roomId);
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		JSONObject res = commonDao.querySingle(JSONObject.class, sql_query_by_id, params);
		return res;
	}*/
	
	public ChatingRoom query(Integer roomId){
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(1, roomId);
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		ChatingRoom res = commonDao.querySingle(ChatingRoom.class, sql_query_by_id, params);
		return res;
	}
	
	public List<ChatingRoom> queryRoomList(){
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		List<ChatingRoom> list = commonDao.queryList(ChatingRoom.class, sql_query_all, null);
		return list;
	}
}
