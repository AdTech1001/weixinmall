package com.org.utils;

/**
 * �û�������
 * ��Ҫ���ڴ������û���ص����ݣ����紴��һ����ʱ�û�
 */
public class UserUtil {
	public static synchronized String randomLoginName(){
		String rn = DateUtil.getDateStringByFormat(DateUtil.DATE_FORMAT_SHORT_DATE);
		return rn;
	}
	
}