package com.org.services;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.org.dao.CommonDao;
import com.org.model.wx.WxUser;
import com.org.util.Base64;
import com.org.util.SpringUtil;
import com.org.utils.DateUtil;

/**
 * 用于同步微信用户信息
 * @author Administrator
 *
 */
@Service
public class WxUserService {
	// 这种方式是要用if函数判断的，感觉开销稍微有点大，
//	private static String sql_insert = "insert into wx_user_info (openid, nickname, sex, subscribe, subscribe_time, headimgurl, country, province, city) values (?, ?, ?, ?, ?, ?, ?, ?, ?)"
//			+ "on duplicate key update "
//			+ "nickname = IF((ISNULL(?) || LENGTH(?)<=0), nickname, '?'), "
//			+ "sex = IF((ISNULL(?) || LENGTH(?)<=0), sex, '?'), "
//			+ "subscribe = IF((ISNULL(?) || LENGTH(?)<=0), subscribe, '?'), "
//			+ "subscribe_time = IF((ISNULL(?) || LENGTH(?)<=0), subscribe_time, '?'), "
//			+ "headimgurl = IF((ISNULL(?) || LENGTH(?)<=0), headimgurl, '?'), "
//			+ "country = IF((ISNULL(?) || LENGTH(?)<=0), country, '?'), "
//			+ "province = IF((ISNULL(?) || LENGTH(?)<=0), province, '?'), "
//			+ "city = IF((ISNULL(?) || LENGTH(?)<=0), city, '?') ";
	
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
	
	private static String sql_query_by_openid = "select * from wx_user_info where openid = ?";
	
	// 0:未关注  1 已关注
	private static String sql_query_by_subscribe = "select * from wx_user_info where subscribe = ?";
	
	private static String sql_query_all = "select * from wx_user_info";
	
	public WxUser query(String openid){
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(1, openid);
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		WxUser res = commonDao.querySingle(WxUser.class, sql_query_by_openid, params);
		// 由于用户名的特殊，在存储到数据库的时候，都是使用的base64转码字符
		// 所以在取出的时候，要再base64解码
		String nickname = res.getNickname();
		try {
			nickname = new String(Base64.decode(nickname), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.info("Base64解码失败: " + nickname);
			e.printStackTrace();
		}
		res.setNickname(nickname);
		return res;
	}
	
	/**
	 * 查询所有
	 * @param subscribe 是否关注 0:未关注  1 已关注 ; 如果参数为null, 则表示查询所有
	 * @return
	 */
	public List<WxUser> queryAll(String subscribe){
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		//JSONArray res = new JSONArray();
		List<WxUser> res = null;
		if(StringUtils.isEmpty(subscribe)) {
			//res = commonDao.queryJSONArray(sql_query_all);.
			res = commonDao.queryList(WxUser.class, sql_query_all, null);
		} else {
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(1, subscribe);
			//res = commonDao.queryJSONArray(sql_query_by_subscribe, params);
			res = commonDao.queryList(WxUser.class, sql_query_by_subscribe, params);
		}
		
		WxUser temp = null;
		for (int i = 0; i < res.size(); i++) {
			temp = res.get(i);
			String nickname = temp.getNickname();
			if(StringUtils.isNotEmpty(nickname)) {
				try {
					nickname = new String(Base64.decode(nickname), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					log.info("queryAll Base64解码失败: " + nickname);
					e.printStackTrace();
				}
				temp.setNickname(nickname);
			} else {
				log.info("queryAll nickname 为空: " + temp.getOpenid());
			}
		}
		return res;
	}
	
	/**
	 * 保存并返回查询结果
	 * @param userInfoFromWx 调用微信接口查询得到的用户信息
	 * @return
	 */
	public WxUser saveAndReturn(JSONObject userInfoFromWx){

		String openid = userInfoFromWx.getString("openid");
		String nickname = userInfoFromWx.getString("nickname");
		//nickname = nickname.replace("🌻", "*");
		try {
			nickname = Base64.encode(nickname.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.info("saveAndReturn" + nickname + "转码Base64失败，使用截除方案" );
			nickname = matchStr(nickname);
		}
		
		
		String sex = userInfoFromWx.getString("sex");
		String subscribe_time = DateUtil.getyyyyMMddHHmmss();
		String subscribe = userInfoFromWx.getString("subscribe");
		String headimgurl = userInfoFromWx.getString("headimgurl");
		String country = userInfoFromWx.getString("country");
		String province = userInfoFromWx.getString("province");
		String city = userInfoFromWx.getString("city");
		boolean saveRes = save(openid, nickname, sex, subscribe_time, subscribe, headimgurl, country, province, city);
		
		WxUser result = null;
		if(saveRes) {
			result = query(openid);
		}
		return result;
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
		
		try {
			nickname = Base64.encode(nickname.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.info("saveOrUpdate" + nickname + "转码Base64失败，使用截除方案" );
			nickname = matchStr(nickname);
		}
		
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
			try {
				nickname = Base64.encode(nickname.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				log.info("transactionSaveOrUpdate" + nickname + "转码Base64失败，使用截除方案" );
				nickname = matchStr(nickname);
			}
			
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
		try {
			String ss = Base64.encode(str.getBytes("UTF-8"));
			System.err.println(ss);
			byte[] ssaa = Base64.decode(ss);
			System.err.println(new String(ssaa, "UTF-8"));
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*Pattern pattern = Pattern.compile(pattrn);
		Matcher matcher = pattern.matcher(str);
		
		StringBuffer sbr = new StringBuffer();
		while (matcher.find()) {
			sbr.append(matcher.group());
		}
		String res = sbr.toString();
		System.err.println(res);*/
	}
	
	private Log log = LogFactory.getLog(WxUserService.class);
	// String str = "中是H★ 要 H★KかS  Micky ߌ»Icey  Storm hߔ®"; // 测试字符串
	private static String pattrn = "[\u4e00-\u9fa5]+|[a-z]+|[A-Z]+| |[0-9]+";
}

