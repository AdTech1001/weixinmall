package com.org.common;

import com.org.utils.PropertyUtil;

public class CommonConstant {
	public static String WX_TOKEN_KEY = "wxToken"+PropertyUtil.getValue("wx", "appid"); // 微信端的token key
	public static String UTF8 = "utf-8";
	public final static String DB_MONGO = "mongo";
	public final static String DB_MYSQL = "hikaricp-mysql";
	public final static String DB_HIKARICP = "hikaricp-oracle";
	public final static String RESP_CODE = "respCode";
	public final static String RESP_MSG = "respMsg";
	public final static String FILE_PATH = "filePath";
	public final static String FORM_PARAMS = "formParams";
	public static final Integer ASC = -1;
	//　如果分页的条数超过 50000了，将对查询进行优化，这个量是用于区分分页条数是否超标
	public static final int LARGE_RECORD = 50000;
	//public static final int LARGE_RECORD = 50000;
	public static final String CURRENT_CHANNEL_ID = "currentChannelId";
	/**
	 * 首页 index
	 */
	public static final String HOME = "6";
	/**
	 * 首页 index
	 */
	public static final String INDEX = "index";
	/**
	 * 生活 0
	 */
	public static final String LIFE = "0";
	/**
	 * 情感 1
	 */
	public static final String EMOTION = "1";
	/**
	 * 工作 2
	 */
	public static final String CAREER = "2";
	/**
	 * 其他 3
	 */
	public static final String OTHER = "3";
	/**
	 * 吐槽吧 4
	 */
	public static final String TU_CAO = "4";
	/**
	 * 纪念板 5
	 */
	public static final String COMMEMORATE_BOARD = "5";
	/**
	 * 
	 */
	public static final String CHANNEL_NAME = "channerName";
	/**
	 * 
	 */
	public static final String SERVLET = "servlet-dispatcher";
		
	/**
	 * 置顶帖
	 */
	public static final String TOP_TESTIMONIAL = "topTestimonal";
	/**
	 * 注册类型: 系统
	 */
	public static final String REGIST_TYPE_SYS = "0";
	/**
	 * 注册类型: 用户
	 */
	public static final String REGIST_TYPE_PERSON = "1";
	/**
	 * 微信用户session key
	 */
	public static final String WX_USER_SESSION = "wxuser";
	
}
