package com.org.common;

import com.org.utils.PropertyUtil;

public class CommonConstant {
	public static String WX_TOKEN_KEY = "wxToken"+PropertyUtil.getValue("wx", "appid"); // ΢�Ŷ˵�token key
	public static String UTF8 = "utf-8";
	public final static String DB_MONGO = "mongo";
	public final static String DB_MYSQL = "hikaricp-mysql";
	public final static String DB_HIKARICP = "hikaricp-oracle";
	public final static String RESP_CODE = "respCode";
	public final static String RESP_MSG = "respMsg";
	public final static String FILE_PATH = "filePath";
	public final static String FORM_PARAMS = "formParams";
	public static final Integer ASC = -1;
	//�������ҳ���������� 50000�ˣ����Բ�ѯ�����Ż�����������������ַ�ҳ�����Ƿ񳬱�
	public static final int LARGE_RECORD = 50000;
	//public static final int LARGE_RECORD = 50000;
	public static final String CURRENT_CHANNEL_ID = "currentChannelId";
	/**
	 * ��ҳ index
	 */
	public static final String HOME = "6";
	/**
	 * ��ҳ index
	 */
	public static final String INDEX = "index";
	/**
	 * ���� 0
	 */
	public static final String LIFE = "0";
	/**
	 * ��� 1
	 */
	public static final String EMOTION = "1";
	/**
	 * ���� 2
	 */
	public static final String CAREER = "2";
	/**
	 * ���� 3
	 */
	public static final String OTHER = "3";
	/**
	 * �²۰� 4
	 */
	public static final String TU_CAO = "4";
	/**
	 * ����� 5
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
	 * �ö���
	 */
	public static final String TOP_TESTIMONIAL = "topTestimonal";
	/**
	 * ע������: ϵͳ
	 */
	public static final String REGIST_TYPE_SYS = "0";
	/**
	 * ע������: �û�
	 */
	public static final String REGIST_TYPE_PERSON = "1";
	/**
	 * ΢���û�session key
	 */
	public static final String WX_USER_SESSION = "wxuser";
	
}
