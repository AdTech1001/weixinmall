package com.org.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.org.dao.CommonDao;
import com.org.util.SpringUtil;
import com.org.utils.DateUtil;

/**
 * 商品信息管理
 * @author Administrator
 *
 */
@Service
public class ProductService {
	
//  这种方法是用ifnull函数判断，但是ifnull只认null 不认''， 所以要保证进入的参数值要么有值，要么是null， 空字符串一定要转换成null
	private static String sql_save_or_update = "insert into wx_user_info (openid, nickname, sex, subscribe, subscribe_time, headimgurl, country, province, city) values (?, ?, ?, ?, ?, ?, ?, ?, ?)"
			+ "on duplicate key update "
			+ "nickname = ifnull(?, nickname), "
			+ "sex = ifnull(?, sex), "
			+ "subscribe = ifnull(?, subscribe), "
			+ "subscribe_time = ifnull(?, subscribe_time), "
			+ "headimgurl = ifnull(?, headimgurl), "
			+ "country = ifnull(?, country), "
			+ "province = ifnull(?, province), "
			+ "city = ifnull(?, city) ";
	
	private static String sql_save = "insert into wx_user_info (openid, nickname, sex, subscribe, subscribe_time, headimgurl, country, province, city) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static String sql_update = "update wx_user_info set nickname= ?, sex= ?, "
			+ "subscribe= ?, subscribe_time= ?, headimgurl= ?, country= ?, province= ?, city= ? where openid =?";
	
	private static String sql_query_by_id = "select * from wx_product_info where id = ?";
	
	private static String sql_query_all = "select * from wx_product_info";
	
	public JSONObject query(Integer id){
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(1, id);
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		JSONObject res = commonDao.querySingle(JSONObject.class, sql_query_by_id, params);
		return res;
	}
	
	/**
	 * 查询所有
	 * @return 所有商品列表
	 */
	public JSONArray queryAll(){
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		JSONArray res = commonDao.queryJSONArray(sql_query_all);
		return res;
	}
	
	/**
	 * 保存并返回查询结果
	 * @return
	 */
	public JSONObject saveAndReturn(JSONObject userInfoFromWx){
		//TODO
		return null;
	}
	
	/**
	 * 插入
	 * @param openid 微信openid
	 * @param nickname 微信昵称
	 * @param sex 微信性别
	 * @param subscribe_time 关注时间
	 * @param subscribe 是否关注 0:未关注  1 已关注
	 * @param headimgurl 头像
	 * @param country 国家
	 * @param province 省份
	 * @param city 城市
	 * @return
	 */
	public boolean save (String openid, String nickname, String sex, String subscribe_time, 
			String subscribe, String headimgurl, String country, String province, String city){
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(1, openid);
		params.put(2, nickname);
		params.put(3, sex);
		params.put(4, subscribe_time);
		params.put(5, subscribe);
		params.put(6, headimgurl);
		params.put(7, country);
		params.put(8, province);
		params.put(9, city);
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		boolean res = commonDao.addSingle(sql_save, params);
		return res;
	}
	
	/**
	 * 更新
	 * @param openid 微信openid
	 * @param nickname 微信昵称
	 * @param sex 微信性别
	 * @param subscribe_time 关注时间
	 * @param subscribe 是否关注 0:未关注  1 已关注
	 * @param headimgurl 头像
	 * @param country 国家
	 * @param province 省份
	 * @param city 城市
	 * @return
	 */
	public boolean update (String openid, String nickname, String sex, String subscribe_time, 
			String subscribe, String headimgurl, String country, String province, String city){
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(1, nickname);
		params.put(2, sex);
		params.put(3, subscribe_time);
		params.put(4, subscribe);
		params.put(5, headimgurl);
		params.put(6, country);
		params.put(7, province);
		params.put(8, city);
		params.put(9, openid);
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		boolean res = commonDao.update(sql_update, params);
		return res;
	}
	


	/**
	 * 事务 已有数据更新，无则插入； 没值的字段一定要是null不能是""
	 * @param openid 微信openid
	 * @param nickname 微信昵称
	 * @param sex 微信性别
	 * @param subscribe_time 关注时间
	 * @param subscribe 是否关注 0:未关注  1 已关注
	 * @param headimgurl 头像
	 * @param country 国家
	 * @param province 省份
	 * @param city 城市
	 * @return
	 */
	public boolean saveOrUpdate (String openid, String nickname, String sex, String subscribe_time, 
			String subscribe, String headimgurl, String country, String province, String city){
		
		Map<Integer, Object> params = null;
		params = new HashMap<Integer, Object>();
		params.put(1, openid);
		params.put(2, nickname);
		params.put(3, sex);
		params.put(4, subscribe);
		params.put(5, subscribe_time);
		params.put(6, headimgurl);
		params.put(7, country);
		params.put(8, province);
		params.put(9, city);
		
		params.put(10, nickname);
		params.put(11, sex);
		params.put(12, subscribe);
		params.put(13, subscribe_time);
		params.put(14, headimgurl);
		params.put(15, country);
		params.put(16, province);
		params.put(17, city);
			
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		boolean res = commonDao.addSingle(sql_save_or_update, params);
		return res;
	}

	/**
	 * 事务 已有数据更新，无则插入
	 * @param openid 微信openid
	 * @param nickname 微信昵称
	 * @param sex 微信性别
	 * @param subscribe_time 关注时间
	 * @param subscribe 是否关注 0:未关注  1 已关注
	 * @param headimgurl 头像
	 * @param country 国家
	 * @param province 省份
	 * @param city 城市
	 * @return
	 */
	public boolean transactionSaveOrUpdate (JSONArray userInfoList){
		JSONObject userTemp = null;
		Map<Integer, Object> params = null;
		List<Map<Integer, Object>> paramsList = new ArrayList<Map<Integer,Object>>();
		for (int i = 0; i < userInfoList.size(); i++) {
			userTemp = userInfoList.getJSONObject(i);
			String openid = userTemp.getString("openid");
			String nickname = userTemp.getString("nickname");
			log.info("transactionSaveOrUpdate " + i +": "+ openid + " ; old nickname : " + nickname);
			nickname = matchStr(nickname);
			
			String sex = userTemp.getString("sex");
			String subscribe_time = DateUtil.getyyyyMMddHHmmss();
			String subscribe = userTemp.getString("subscribe");
			String headimgurl = userTemp.getString("headimgurl");
			String country = userTemp.getString("country");
			String province = userTemp.getString("province");
			String city = userTemp.getString("city");


			params = new HashMap<Integer, Object>();
			params.put(1, openid);
			params.put(2, nickname);
			params.put(3, sex);
			params.put(4, subscribe);
			params.put(5, subscribe_time);
			params.put(6, headimgurl);
			params.put(7, country);
			params.put(8, province);
			params.put(9, city);
			
			params.put(10, nickname);
			params.put(11, sex);
			params.put(12, subscribe);
			params.put(13, subscribe_time);
			params.put(14, headimgurl);
			params.put(15, country);
			params.put(16, province);
			params.put(17, city);
			paramsList.add(params);
		}
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		boolean res = commonDao.transactionInsert(sql_save_or_update, paramsList);
		return res;
	}
	
	private String matchStr(String from){
		Pattern pattern = Pattern.compile(pattrn);
		Matcher matcher = pattern.matcher(from);
		
		StringBuffer sbr = new StringBuffer();
		while (matcher.find()) {
		    //matcher.appendReplacement(sbr, "*");
			sbr.append(matcher.group());
		}
		String res = sbr.toString();
		log.info("matchStr new nickname : " + res);
		return res;
	}
	
	public static void main(String[] args) {
		String str = "中是H★ 要 H★KかS  Micky ߌ»Icey  Storm hߔ®";
		
		Pattern pattern = Pattern.compile(pattrn);
		Matcher matcher = pattern.matcher(str);
		
		StringBuffer sbr = new StringBuffer();
		while (matcher.find()) {
			sbr.append(matcher.group());
		}
		String res = sbr.toString();
		System.err.println(res);
	}
	
	private Log log = LogFactory.getLog(ProductService.class);
	// String str = "中是H★ 要 H★KかS  Micky ߌ»Icey  Storm hߔ®"; // 测试字符串
	private static String pattrn = "[\u4e00-\u9fa5]+|[a-z]+|[A-Z]+| |[0-9]+";
}

