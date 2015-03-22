package com.org.services.busi;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.org.common.CommonConstant;
import com.org.common.UserConstant;
import com.org.dao.CommonDao;
import com.org.util.MD5;
import com.org.util.SpringUtil;
import com.org.utils.UserUtil;

/**
 * ͨ��
 * @author Administrator
 *
 */
@Service
public class UserService {
	public JSONObject checkLogin(String loginName, String LoginPwd){
		// �Ȳ�session��û���û���
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(loginName) || StringUtils.isEmpty(LoginPwd)){
			result.put("respCode", "10001");
			result.put("respMsg", "�û��������벻��Ϊ��");
			System.out.println("�û��������벻��Ϊ��");
			return result;
		}
		
		JSONObject fullUserInfo = searchFullUserInfo(loginName);
		if(fullUserInfo == null || fullUserInfo.isNullObject()){
			result.put("respCode", "10002");
			result.put("respMsg", "�û�������");
			System.out.println("�û�������");
			return result;
		}
		if(!MD5.getMD5(LoginPwd).equals(fullUserInfo.getString("password"))){
			result.put("respCode", "10003");
			result.put("respMsg", "�������");
			return result;
		}
		result.put("respCode", "10000");
		result.put("respMsg", "��¼�ɹ�");
		result.put("fullUserInfo", fullUserInfo);
		return result;
	}
	
	/**
	 * ��ҵ�û���¼У��
	 * @param loginName
	 * @param LoginPwd
	 * @return
	 */
	public JSONObject checkLoginComp(String loginName, String LoginPwd){
		// �Ȳ�session��û���û���
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(loginName) || StringUtils.isEmpty(LoginPwd)){
			result.put("respCode", "10001");
			result.put("respMsg", "�û��������벻��Ϊ��");
			System.out.println("�û��������벻��Ϊ��");
			return result;
		}
		
		JSONObject fullUserInfo = searchFullUserInfoComp(loginName);
		if(fullUserInfo == null || fullUserInfo.isNullObject()){
			result.put("respCode", "10002");
			result.put("respMsg", "�û�������");
			System.out.println("�û�������");
			return result;
		}
		if(!MD5.getMD5(LoginPwd).equals(fullUserInfo.getString("password"))){
			result.put("respCode", "10003");
			result.put("respMsg", "�������");
			return result;
		}
		result.put("respCode", "10000");
		result.put("respMsg", "��¼�ɹ�");
		result.put("fullUserInfo", fullUserInfo);
		return result;
	}
	
	public JSONObject searchFullUserInfo(String loginName){
		String sql = "select * from plat_user_extends_info e right join plat_user_info u on e.login_name = u.login_name where u.login_name = ?"; 
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		//String sql = "select * from plat_user_extends_info where login_name = ?";
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, loginName);
		//params.put(2, MD5.getMD5(LoginPwd));
		JSONObject fullUserInfo = commonDao.isExist(sql, params);
		return fullUserInfo;
	}

	public JSONObject searchFullUserInfoComp(String loginName){
		String sql = "select * from plat_company_extends_info e right join plat_company_info u on e.login_name = u.login_name where u.login_name = ?"; 
		CommonDao commonDao = (CommonDao)SpringUtil.getBean("commonDao");
		//String sql = "select * from plat_user_extends_info where login_name = ?";
		Map<Integer , Object> params = new HashMap<Integer, Object>();
		params.put(1, loginName);
		//params.put(2, MD5.getMD5(LoginPwd));
		JSONObject fullUserInfo = commonDao.isExist(sql, params);
		return fullUserInfo;
	}

	
	/**
	 * ��ȡ�ѷ���ְλ
	 * @param loginName
	 * @return
	 */
	public JSONArray getAllPublishedPosition(String loginName){
		// TODO 
		return null;
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
		try {
			success = commonDao.addSingle(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			res.put(CommonConstant.RESP_CODE, "DB00001");
			res.put(CommonConstant.RESP_MSG, "���ݿⱣ���쳣");
		}
		return success;
	}
	
	public JSONObject createTempUser(){
		JSONObject user = new JSONObject();
		String loginName = UserUtil.randomLoginName(), mail ="", mobile="", registType="", nickName="", pwd="";
		user.put(UserConstant.LOGIN_NAME, loginName);
		user.put(UserConstant.MAIL, mail);
		user.put(UserConstant.MOBILE, mobile);
		user.put(UserConstant.REGIST_TYPE, registType);
		user.put(UserConstant.NICK, nickName);
		user.put(UserConstant.PWD, pwd);
		
		saveUser(loginName, mail, mobile, registType, nickName, pwd);
		//ͬʱ�����û�
		return user;
	}
}
