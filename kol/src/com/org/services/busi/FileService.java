package com.org.services.busi;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.org.common.CommonConstant;
import com.org.dao.CommonDao;
import com.org.exception.SvcException;
import com.org.util.SpringUtil;

/**
 * @author Administrator
 *
 */
@Service
public class FileService {
	private final String sql_insert = "insert into kol_testimonials_files (user_id, file_path) values (?,?)";
	private final String sql_getLastInsert = "select * from kol_testimonials_files order by id desc limit 1";
	
	public synchronized JSONObject saveContents(String userId, String filePath){
		//String createDate = DateUtil.getDate(DateUtil.DATE_FORMAT_SHORT_DATE);
		
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, Integer.valueOf(userId));
		params.put(2, filePath);
		
		JSONObject res = new JSONObject();
		commonDao.addSingle(sql_insert, params);
		JSONObject lastInsert = getFileLastInsert();

		res.put(CommonConstant.RESP_CODE, "10000");
		res.put("lastInsert", lastInsert);
		return res;
	}
	
	public JSONObject getFileLastInsert(){
		//String createDate = DateUtil.getDate(DateUtil.DATE_FORMAT_SHORT_DATE);
		
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		
		JSONObject res = new JSONObject();
		try {
			res = commonDao.querySingle(sql_getLastInsert, params, null);
		} catch (SvcException e) {
			e.printStackTrace();
			res.put(CommonConstant.RESP_CODE, "DB00002");
			res.put(CommonConstant.RESP_MSG, "数据库查询结果异常");
		}
		return res;
	}
	
}
