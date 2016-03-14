package com.org.services.busi;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.org.dao.CommonDao;
import com.org.util.SpringUtil;

/**
 * @author Administrator
 *
 */
@Service
public class JsService {
	private final String sql_insert = "insert into kol_operate_tracking (user_id, browser_name, browser_version, operate_name, operate_date, current_page, user_agent, local_addr, remote_addr) values (?,?,?,?,?,?,?,?,?)";
	public JSONObject getUserTrackingInfo(String userId){
		return null;
	}
	
	public void save(String userId, String browserName, String browserVersion,
			String operateName, String operateDateTime, String currentPage,
			String userAgent, String localAddr, String remoteAddr) {
		
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, userId);
		params.put(2, browserName);
		params.put(3, browserVersion);
		params.put(4, operateName);
		params.put(5, operateDateTime);
		params.put(6, currentPage);
		params.put(7, userAgent);
		params.put(8, localAddr);
		params.put(9, remoteAddr);
		
		JSONObject res = new JSONObject();
		commonDao.addSingle(sql_insert, params);
		
	}
}
