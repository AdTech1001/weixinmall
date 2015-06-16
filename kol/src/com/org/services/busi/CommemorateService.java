package com.org.services.busi;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.org.dao.CommonDao;
import com.org.util.SpringUtil;
import com.org.utils.DateUtil;

/**
 * ͨ��
 * @author Administrator
 *
 */
@Service
public class CommemorateService {
	private final String getCommemorateById = "select * from kol_commemorate_board where channel_id = ? and commemorate_date = ? order by id desc";
	private final String getCurrentCommemorate = "select * from kol_commemorate_board a left join kol_files b on a.file_id = b.id where a.commemorate_date = ? and a.top_times >= ? order by a.top_times desc limit ?";
	private final String getAllCommemorate = "select * from kol_commemorate_board order by id desc";
	private final String getLimitCommemorate = "select * from kol_commemorate_board a left join kol_files b on a.file_id = b.id order by a.id desc limit ?";
	private final String saveCommemorate = "insert into kol_commemorate_board (user_id, comments, file_id, commemorate_date, create_date, update_date) values (?, ?, ?, ?, ?, ?)";

	/**
	 * ��ѯָ����¼
	 * @param id
	 * @return
	 */
	public JSONArray getCommemorateById(String id){
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, id);
		params.put(2, DateUtil.getDateStringByFormat(DateUtil.yyyyMMdd));
		JSONArray testimonials = commonDao.queryJSONArray(getCommemorateById, params);
		return testimonials;
	}
	
	/**
	 * ��ѯ������ߵļ����
	 * @param id
	 * @return
	 */
	public JSONArray getCurrentCommemorate(String count, String topTimesGoal){
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, DateUtil.getDateStringByFormat(DateUtil.yyyyMMdd));
		params.put(2, Integer.valueOf(topTimesGoal));
		params.put(3, Integer.valueOf(count));
		JSONArray testimonials = commonDao.queryJSONArray(getCurrentCommemorate, params);
		return testimonials;
	}
	
	/**
	 * ��ѯ���м�¼, ���ǣ�Ĭ��ֻ��
	 * @param id
	 * @return
	 */
	public JSONArray getLimitCommemorate(String count){
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, Integer.valueOf(count));
		JSONArray commemorate = commonDao.queryJSONArray(getLimitCommemorate, params);
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
		try {
			res = commonDao.addSingle(saveCommemorate, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
}
