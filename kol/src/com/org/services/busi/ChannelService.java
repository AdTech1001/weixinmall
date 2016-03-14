package com.org.services.busi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.org.common.CommonConstant;
import com.org.container.CommonContainer;
import com.org.dao.CommonDao;
import com.org.util.SpringUtil;

/**
 * 通用
 * @author Administrator
 *
 */
@Service
public class ChannelService {
	private final static List<String> secretColumn = new ArrayList<String>();
	// 查询置顶的
	private String getTestimonialsByChannelId_from_to = "";
	
	static {
		secretColumn.add("id");
	}
	
	
	public JSONArray getTestimonialsByChannelId(String channelId, String limit){
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, channelId);
		params.put(2, Integer.valueOf(limit));
		String getTestimonialsByChannelId_limit = "select (select count(1) from kol_comment c where c.testimonials_id = a.id) comment_counts, a.*, b.file_path from kol_testimonials a left join kol_testimonials_files b on a.file_id=b.id where channel_id = ? and a.is_top is null order by a.id desc limit ?";
		if(StringUtils.isEmpty(channelId)){
			params = new HashMap<Integer, Object>();
			getTestimonialsByChannelId_limit = "select (select count(1) from kol_comment c where c.testimonials_id = a.id) comment_counts, a.*, b.file_path from kol_testimonials a left join kol_testimonials_files b on a.file_id=b.id where a.is_top is null order by a.id desc limit ?";
			params.put(1, Integer.valueOf(limit));
		}
		JSONArray testimonials = commonDao.queryJSONArray(getTestimonialsByChannelId_limit, params, secretColumn);
		JSONArray topTestimonials = getTopedTestimonialsByChannelId(channelId);
		testimonials.addAll(0, topTestimonials);
		return testimonials;
	}
	
	/**
	 * 查置顶帖
	 * @param channelId
	 * @param limit
	 * @return
	 */
	public JSONArray getTopedTestimonialsByChannelId(String channelId){
		// 先看看内存中有没有存放数据
		Object dataTemp = CommonContainer.getData(CommonConstant.TOP_TESTIMONIAL+channelId);
		JSONArray testimonials = null;
		if(dataTemp != null){
			testimonials = (JSONArray)dataTemp;
			return testimonials;
		}
		String getTopedTestimonialsByChannelId_limit = "select (select count(1) from kol_comment c where c.testimonials_id = a.id) comment_counts, a.*, b.file_path from kol_testimonials a left join kol_testimonials_files b on a.file_id=b.id where channel_id = ? and a.is_top='0' order by a.id desc limit 3";
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, channelId);
		if(StringUtils.isEmpty(channelId)){
			params = new HashMap<Integer, Object>();
			getTopedTestimonialsByChannelId_limit = "select (select count(1) from kol_comment c where c.testimonials_id = a.id) comment_counts, a.*, b.file_path from kol_testimonials a left join kol_testimonials_files b on a.file_id=b.id where a.is_top='0' order by a.id desc limit 3";
		}
		testimonials = commonDao.queryJSONArray(getTopedTestimonialsByChannelId_limit, params, secretColumn);
		CommonContainer.saveData(CommonConstant.TOP_TESTIMONIAL+channelId, testimonials);
		return testimonials;
	}
	
	/**
	 * 分页查询 TODO
	 * @param channelId
	 * @param limitFrom
	 * @param limitTo
	 * @return
	 */
	public JSONArray getTestimonialsByChannelId(String channelId, String limitFrom, String limitTo){
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, channelId);
		JSONArray testimonials = commonDao.queryJSONArray(getTestimonialsByChannelId_from_to, params, secretColumn);
		return testimonials;
	}
}
