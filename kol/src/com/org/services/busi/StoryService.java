package com.org.services.busi;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.org.dao.CommonDao;
import com.org.util.SpringUtil;
import com.org.utils.DateUtil;

/**
 * @author Administrator
 */
@Service
public class StoryService {
	private String sql_insert = "insert into wx_story (templateid, rolename, message, comments, createtime) values (?,?,?,?)";
	
	public boolean saveStory(Long templateid, String rolename, String message, String comments){
		
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(1, templateid);
		params.put(2, rolename);
		params.put(3, message);
		params.put(4, comments);
		params.put(5, DateUtil.getDateStringByFormat(DateUtil.yyyyMMddHHmmss));
		
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		boolean success = commonDao.addSingle(sql_insert, params);
		return success;
	}
}
