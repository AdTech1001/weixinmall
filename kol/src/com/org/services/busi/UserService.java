package com.org.services.busi;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.org.common.CommonConstant;
import com.org.common.UserConstant;
import com.org.dao.CommonDao;
import com.org.exception.SvcException;
import com.org.util.MD5;
import com.org.util.SpringUtil;
import com.org.utils.UserUtil;

/**
 * 通用
 * @author Administrator
 *
 */
@Service
public class UserService {
	private static String getUserByLoginName = "select * from kol_user where login_name = ?";
	
	public JSONObject checkLogin(String loginName, String LoginPwd){
		// 先查session有没有用户　
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(loginName) || StringUtils.isEmpty(LoginPwd)){
			result.put("respCode", "10001");
			result.put("respMsg", "用户名　密码不能为空");
			System.out.println("用户名　密码不能为空");
			return result;
		}
		
		JSONObject fullUserInfo = searchFullUserInfo(loginName);
		if(fullUserInfo == null || fullUserInfo.isNullObject()){
			result.put("respCode", "10002");
			result.put("respMsg", "用户不存在");
			System.out.println("用户不存在");
			return result;
		}
		if(!MD5.getMD5(LoginPwd).equals(fullUserInfo.getString("password"))){
			result.put("respCode", "10003");
			result.put("respMsg", "密码错误");
			return result;
		}
		result.put("respCode", "10000");
		result.put("respMsg", "登录成功");
		result.put("fullUserInfo", fullUserInfo);
		return result;
	}
	
	/**
	 * 企业用户登录校验
	 * @param loginName
	 * @param LoginPwd
	 * @return
	 */
	public JSONObject checkLoginComp(String loginName, String LoginPwd){
		// 先查session有没有用户　
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(loginName) || StringUtils.isEmpty(LoginPwd)){
			result.put("respCode", "10001");
			result.put("respMsg", "用户名　密码不能为空");
			System.out.println("用户名　密码不能为空");
			return result;
		}
		
		JSONObject fullUserInfo = searchFullUserInfoComp(loginName);
		if(fullUserInfo == null || fullUserInfo.isNullObject()){
			result.put("respCode", "10002");
			result.put("respMsg", "用户不存在");
			System.out.println("用户不存在");
			return result;
		}
		if(!MD5.getMD5(LoginPwd).equals(fullUserInfo.getString("password"))){
			result.put("respCode", "10003");
			result.put("respMsg", "密码错误");
			return result;
		}
		result.put("respCode", "10000");
		result.put("respMsg", "登录成功");
		result.put("fullUserInfo", fullUserInfo);
		return result;
	}
	
	public JSONObject searchFullUserInfo(String loginName){
		//String sql = "select * from plat_user_extends_info e right join plat_user_info u on e.login_name = u.login_name where u.login_name = ?"; 
		//CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		//String sql = "select * from plat_user_extends_info where login_name = ?";
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, loginName);
		//params.put(2, MD5.getMD5(LoginPwd));
//		JSONObject fullUserInfo = commonDao.isExist(sql, params);
//		return fullUserInfo;
		return null;
	}

	public JSONObject searchFullUserInfoComp(String loginName){
		//String sql = "select * from plat_company_extends_info e right join plat_company_info u on e.login_name = u.login_name where u.login_name = ?"; 
		//CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		//String sql = "select * from plat_user_extends_info where login_name = ?";
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, loginName);
		//params.put(2, MD5.getMD5(LoginPwd));
//		JSONObject fullUserInfo = commonDao.isExist(sql, params);
//		return fullUserInfo;
		return null;
	}

	
	/**
	 * 获取已发布职位
	 * @param loginName
	 * @return
	 */
	public JSONArray getAllPublishedPosition(String loginName){
		// TODO 
		return null;
	}
	
	public JSONObject getUserByLoginName(String loginName){
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, loginName);
		
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		JSONObject res = new JSONObject();
		try {
			res = commonDao.querySingle(getUserByLoginName, params, null);
		} catch (SvcException e) {
			e.printStackTrace();
			res.put(CommonConstant.RESP_CODE, "DB00001");
			res.put(CommonConstant.RESP_MSG, "数据库查询异常");
		}
		return res;
	}
	
	public boolean saveUser(String loginName, String mail, String mobile, String registType, String nickName, String pwd){
		String sql = "insert into kol_user (login_name, mail, mobile, regist_type, nick_name, pwd) values (?,?,?,?,?,?)";
		
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, loginName);
		params.put(2, mail);
		params.put(3, mobile);
		params.put(4, registType);
		params.put(5, nickName);
		params.put(6, pwd);
		
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		JSONObject res = new JSONObject();
		boolean success = false;
		success = commonDao.addSingle(sql, params);
		return success;
	}
	
	public JSONObject createTempUser(){
		JSONObject user = new JSONObject();
		String loginName = UserUtil.randomLoginName(), mail ="", mobile="", registType="0", nickName="", pwd="";
		user.put(UserConstant.LOGIN_NAME, loginName);
		user.put(UserConstant.MAIL, mail);
		user.put(UserConstant.MOBILE, mobile);
		user.put(UserConstant.REGIST_TYPE, registType);
		user.put(UserConstant.NICK, nickName);
		user.put(UserConstant.PWD, pwd);
		user.put(UserConstant.USERID, 9999);
		
		//saveUser(loginName, mail, mobile, registType, nickName, pwd);
		//同时保存用户
		return user;
	}
}
