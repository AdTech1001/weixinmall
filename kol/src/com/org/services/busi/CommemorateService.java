package com.org.services.busi;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.org.dao.CommonDao;
import com.org.exception.SvcException;
import com.org.util.SpringUtil;
import com.org.utils.DateUtil;

/**
 * 通用
 * @author Administrator
 *
 */
@Service
public class CommemorateService {
	private final String getCommemorateById = "select * from kol_commemorate_board where id = ? and commemorate_date = ? order by id desc";
	private final String getCurrentCommemorate = "select a.id, a.user_id, a.comments, a.view_times, a.create_date, a.update_date, a.commemorate_date, a.top_times, a.file_id, b.file_path from kol_commemorate_board a left join kol_files b on a.file_id = b.id where a.commemorate_date = ? and a.top_times >= ? order by a.top_times desc limit ?";
	//private final String getAllCommemorate = "select * from kol_commemorate_board order by id desc";
	private final String getLimitCommemorate = "select a.id, a.user_id, a.comments, a.view_times, a.create_date, a.update_date, a.commemorate_date, a.top_times, a.file_id, b.file_path from kol_commemorate_board a left join kol_files b on a.file_id = b.id order by a.id desc limit ?";
	private final String saveCommemorate = "insert into kol_commemorate_board (user_id, comments, file_id, commemorate_date, create_date, update_date) values (?, ?, ?, ?, ?, ?)";
	private final String addOneTop = "update kol_commemorate_board set top_times=top_times+1 where id =?";
	
	/**
	 * 查询指定记录
	 * @param id
	 * @return
	 */
	public JSONObject getCommemorateById(String id){
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, id);
		params.put(2, DateUtil.getDateStringByFormat(DateUtil.yyyyMMdd));
		JSONObject res = new JSONObject();
		try {
			res = commonDao.querySingle(getCommemorateById, params, null);
		} catch (SvcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 查询指定记录
	 * @param id
	 * @return
	 */
	public synchronized boolean addOneTop(String id){
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, id);
		boolean res = false;
		res = commonDao.addSingle(addOneTop, params);
		return res;
	}
	
	/**
	 * 查询当天最高的纪念板
	 * @param id
	 * @return
	 */
	public JSONArray getCurrentCommemorate(String count, String topTimesGoal){
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, DateUtil.getDateStringByFormat(DateUtil.yyyyMMdd));
		params.put(2, Integer.valueOf(topTimesGoal));
		params.put(3, Integer.valueOf(count));
		JSONArray testimonials = commonDao.queryJSONArray(getCurrentCommemorate, params, null);
		return testimonials;
	}
	
	/**
	 * 查询所有记录, 但是，默认只查
	 * @param id
	 * @return
	 */
	public JSONArray getLimitCommemorate(String count){
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, Integer.valueOf(count));
		JSONArray commemorate = commonDao.queryJSONArray(getLimitCommemorate, params, null);
		return commemorate;
	}

	public boolean save(String userId, String comments, String fileId, String commemorateDate) {
		String createDate = DateUtil.getDateStringByFormat(DateUtil.yyyyMMddHHmmss);
		
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, userId);
		params.put(2, comments);
		params.put(3, fileId);
		params.put(4, commemorateDate);
		params.put(5, createDate);
		params.put(6, createDate);
		
		boolean res = false;
		res = commonDao.addSingle(saveCommemorate, params);
		return res;
	}
}
