package com.org.utils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.org.controller.WxMenu;
import com.org.log.LogUtil;
import com.org.log.impl.LogUtilMg;
import com.org.util.CT;
import com.org.utils.http.HttpTool;
import com.org.utils.http.impl.HttpApacheClient;


public class WxUtil {
	private final static String TOKEN = "sandpay123"; // ���õ������ļ�
	private static String WX_TOKEN = "wxToken"; // ΢�Ŷ˵�token
	private static String WX_TICKET = "wxTicket"; // ΢�Ŷ˵�ticket
	private static int CACHE_TIME = 7000; // ΢�Ŷ˵�ticket

	public static void main(String[] args) throws InterruptedException {
		//autoRun();
	}

	public static boolean checkSignature(String signature, String timestamp,
			String nonce) {
		String[] paramArr = { TOKEN, timestamp, nonce };
		Arrays.sort(paramArr);

		String content = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);
		// String cipher = SHA1Util.hex_sha1(content);
		String cipher = SHA1Util.sha1(content);
		if (StringUtils.isEmpty(cipher)) {
			return false;
		} else {
			return cipher.equalsIgnoreCase(signature);
		}
	}

	public static String initToken() {
		JSONObject requestJson = new JSONObject();
		String grant_type = SmpPropertyUtil.getValue("wx", "grant_type");
		String remoteUrl = SmpPropertyUtil.getValue("wx", "wx_token_url");
		String appid = SmpPropertyUtil.getValue("wx", "appid");
		String secret = SmpPropertyUtil.getValue("wx", "secret");

		requestJson.put("grant_type", grant_type); // ΢�Žӿ�
		requestJson.put("appid", appid);
		requestJson.put("secret", secret);

		HttpTool http = new HttpApacheClient();
		JSONObject responseJson = http.httpPost(requestJson, remoteUrl, CT.ENCODE_UTF8);

		// TODO ��ŵ�memcache
//		try {
//			Memcache.getInstance().setValue(WX_TOKEN, CACHE_TIME, responseJson.getString("access_token"));
//			return responseJson.getString("access_token");
//		} catch (CacheClientException e) {
//			e.printStackTrace();
//		}
		return null;
	}

	/**
	 * ����token����ticket
	 * 
	 * @return
	 */
	public static boolean initTicket(String token) {
		JSONObject requestJson = new JSONObject();
		String remoteUrl = SmpPropertyUtil.getValue("wx", "wx_ticket_url");
		String type = SmpPropertyUtil.getValue("wx", "type_for_ticket");

		requestJson.put("access_token", token); // ΢�Žӿ�
		requestJson.put("type", type);

		HttpTool http = new HttpApacheClient();
		JSONObject responseJson = http.httpPost(requestJson, remoteUrl, CT.ENCODE_UTF8);

		if (0 == responseJson.getInt("errcode")) {
			// ��ȡ�ɹ�
			// TODO ��ŵ�memcache
//			try {
//				Memcache.getInstance().setValue(WX_TICKET, CACHE_TIME, responseJson.getString("ticket"));
//				return true;
//			} catch (CacheClientException e) {
//				e.printStackTrace();
				return false;
//			}
		} else {
			LogUtil.log(WxUtil.class, "initTicket ʧ�ܣ�" + responseJson.get("errmsg"), null, LogUtilMg.LOG_DEBUG, CT.LOG_PATTERN_NULL);
			return false;
		}
	}

	public static boolean init() {
		String token = initToken();
		boolean res = false;
		if (StringUtils.isNotEmpty(token)) {
			res = initTicket(token);
		}
		return res;
	}

	/**
	 * ��memcache��ȡtoken
	 * @return
	 */
	public static String getToken() {
		//
		return null;
		//return Memcache.getInstance().getValue(WX_TOKEN);
	}

	/**
	 * �ֶ���������token
	 * 
	 * @return
	 */
	public static String getTemporaryToken() {
		return "nYKE_6lhfOWxN2rrCSZPlMNhVMctLhMwddYA926KIzNIsRhNIrdRdw7QlVWBiR5OnIwNgfgA-MXWpgjhvydD55HDh3alPj5SPdWgBTszoaYTQMeAIACZV";
	}
	
	/**
	 * 
	 * @param timeInterval
	 */
	public static void autoRun() {
		// ͬʱ��һ����ʱ����,ÿ��Сʱִ��һ��
		Calendar calendar = Calendar.getInstance();

		Long timeInterval = Long.valueOf(SmpPropertyUtil.getValue("wx", "time_interval"));
		Date date = calendar.getTime(); // ��һ��ִ�ж�ʱ�����ʱ��
		Timer timer = new Timer();
		WxTimerTask task = new WxTimerTask();
		// ����ָ����������ָ����ʱ�俪ʼ�����ظ��Ĺ̶��ӳ�ִ�С�
		timer.schedule(task, date, timeInterval * 1000);
	}

	/**
	 * ����˵����ں����߷��ز˵�,������xml�õ����ݺ󣬸����û���������������ض���˵�
	 * 
	 * @param xmlJson
	 * @return
	 */
	public static String createMenu(JSONObject xmlJson) {
		String Content = xmlJson.getString("Content");
		String menuStr = matchMenu(Content);

		String ToUserName = xmlJson.getString("ToUserName");
		String FromUserName = xmlJson.getString("FromUserName");
		String CreateTime = xmlJson.getString("CreateTime");
		String MsgType = xmlJson.getString("MsgType");
		//String MsgId = xmlJson.getString("MsgId");

		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		sb.append("<ToUserName><![CDATA[").append(FromUserName).append("]]></ToUserName>");
		sb.append("<FromUserName><![CDATA[").append(ToUserName).append("]]></FromUserName>");
		sb.append("<CreateTime>").append(CreateTime).append("</CreateTime>");
		sb.append("<MsgType><![CDATA[").append(MsgType).append("]]></MsgType>");
		sb.append("<Content><![CDATA[").append(menuStr).append("]]></Content>");
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * �����û�����������жϣ� �Ƿ��ڲ˵���, ������򷵻ظò˵��������ڣ� �򷵻����в˵�
	 * 
	 * @param Content
	 * @return 
	 */
	public static String matchMenu(String content) {
		WxMenu wxMenu = WxMenu.getInstance();
		if (wxMenu.containsMenu(content)) {
			return wxMenu.get(content);
		}
		return wxMenu.getAll();
	}

	/**
	 * ����ǩ��, ΢�Žӿ�ǩ���õ� noncestr��timestamp������wx.config�е�nonceStr��timestamp��ͬ��
	 * @param timestamp
	 * @param nonceStr
	 * @param url
	 *            �����ǵ���JS�ӿ�ҳ�������URL
	 * @return
	 */
	public static String localSign(String timestamp, String noncestr, String url) {
		StringBuffer temp = new StringBuffer();
		//String ticket = Memcache.getInstance().getValue(WX_TICKET);
		String ticket = "";
		// jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg&noncestr=Wm3WZYTPz0wzccnW&timestamp=1414587457&url=http://mp.weixin.qq.com?params=value
		temp.append("jsapi_ticket=").append(ticket).append("&");
		temp.append("noncestr=").append(noncestr).append("&");
		temp.append("timestamp=").append(timestamp).append("&");
		temp.append("url=").append(url);

		return SHA1Util.sha1(temp.toString());
	}
/*
	private class TimerManager {

		// ʱ����
		private static final long PERIOD_DAY = 7000 * 1000;

		public TimerManager() {
			Calendar calendar = Calendar.getInstance();

			*//*** ����ÿ��2:00ִ�з��� ***//*
			calendar.set(Calendar.HOUR_OF_DAY, 2);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);

			Date date = calendar.getTime(); // ��һ��ִ�ж�ʱ�����ʱ��

			// �����һ��ִ�ж�ʱ�����ʱ�� С�� ��ǰ��ʱ��
			// ��ʱҪ�� ��һ��ִ�ж�ʱ�����ʱ�� ��һ�죬�Ա���������¸�ʱ���ִ�С��������һ�죬���������ִ�С�
			 Ҫ�ľ�������ִ��
			    if (date.before(new Date())) {
				    date = this.addDay(date, 1);
			    }
			

			Timer timer = new Timer();

			NFDFlightDataTimerTask task = new NFDFlightDataTimerTask();
			// ����ָ����������ָ����ʱ�俪ʼ�����ظ��Ĺ̶��ӳ�ִ�С�
			timer.schedule(task, date, PERIOD_DAY);
		}

		// ���ӻ��������
		public Date addDay(Date date, int num) {
			Calendar startDT = Calendar.getInstance();
			startDT.setTime(date);
			startDT.add(Calendar.DAY_OF_MONTH, num);
			return startDT.getTime();
		}

	}*/

	private static class WxTimerTask extends TimerTask {
		@Override
		public void run() {
			System.out.println("ִ�ж�ʱ��ȡ΢������");
			LogUtil.log(WxTimerTask.class, "ִ�ж�ʱ��ȡ΢������", null, LogUtilMg.LOG_DEBUG, CT.LOG_PATTERN_NULL);
			init();
		}
	}

	/*
	 * �����룺
	 * 
	 * -1 ϵͳ��æ����ʱ�뿪�����Ժ����� 0 ����ɹ� 40001
	 * ��ȡaccess_tokenʱAppSecret���󣬻���access_token��Ч
	 * ���뿪��������ȶ�AppSecret����ȷ�ԣ���鿴�Ƿ�����Ϊǡ���Ĺ��ںŵ��ýӿ� 40002 ���Ϸ���ƾ֤���� 40003
	 * ���Ϸ���OpenID���뿪����ȷ��OpenID�����û����Ƿ��ѹ�ע���ںţ����Ƿ����������ںŵ�OpenID 40004 ���Ϸ���ý���ļ�����
	 * 40005 ���Ϸ����ļ����� 40006 ���Ϸ����ļ���С 40007 ���Ϸ���ý���ļ�id 40008 ���Ϸ�����Ϣ���� 40009
	 * ���Ϸ���ͼƬ�ļ���С 40010 ���Ϸ��������ļ���С 40011 ���Ϸ�����Ƶ�ļ���С 40012 ���Ϸ�������ͼ�ļ���С 40013
	 * ���Ϸ���AppID���뿪���߼��AppID����ȷ�ԣ������쳣�ַ���ע���Сд 40014
	 * ���Ϸ���access_token���뿪��������ȶ�access_token����Ч�ԣ����Ƿ���ڣ�����鿴�Ƿ�����Ϊǡ���Ĺ��ںŵ��ýӿ� 40015
	 * ���Ϸ��Ĳ˵����� 40016 ���Ϸ��İ�ť���� 40017 ���Ϸ��İ�ť���� 40018 ���Ϸ��İ�ť���ֳ��� 40019 ���Ϸ��İ�ťKEY����
	 * 40020 ���Ϸ��İ�ťURL���� 40021 ���Ϸ��Ĳ˵��汾�� 40022 ���Ϸ����Ӳ˵����� 40023 ���Ϸ����Ӳ˵���ť���� 40024
	 * ���Ϸ����Ӳ˵���ť���� 40025 ���Ϸ����Ӳ˵���ť���ֳ��� 40026 ���Ϸ����Ӳ˵���ťKEY���� 40027 ���Ϸ����Ӳ˵���ťURL����
	 * 40028 ���Ϸ����Զ���˵�ʹ���û� 40029 ���Ϸ���oauth_code 40030 ���Ϸ���refresh_token 40031
	 * ���Ϸ���openid�б� 40032 ���Ϸ���openid�б��� 40033 ���Ϸ��������ַ������ܰ���uxxxx��ʽ���ַ� 40035
	 * ���Ϸ��Ĳ��� 40038 ���Ϸ��������ʽ 40039 ���Ϸ���URL���� 40050 ���Ϸ��ķ���id 40051 �������ֲ��Ϸ� 40117
	 * �������ֲ��Ϸ� 40118 media_id��С���Ϸ� 40119 button���ʹ��� 40120 button���ʹ��� 40121
	 * ���Ϸ���media_id���� 40132 ΢�źŲ��Ϸ� 40137 ��֧�ֵ�ͼƬ��ʽ 41001 ȱ��access_token���� 41002
	 * ȱ��appid���� 41003 ȱ��refresh_token���� 41004 ȱ��secret���� 41005 ȱ�ٶ�ý���ļ����� 41006
	 * ȱ��media_id���� 41007 ȱ���Ӳ˵����� 41008 ȱ��oauth code 41009 ȱ��openid 42001
	 * access_token��ʱ
	 * ������access_token����Ч�ڣ���ο�����֧��-��ȡaccess_token�У���access_token����ϸ����˵�� 42002
	 * refresh_token��ʱ 42003 oauth_code��ʱ 43001 ��ҪGET���� 43002 ��ҪPOST���� 43003
	 * ��ҪHTTPS���� 43004 ��Ҫ�����߹�ע 43005 ��Ҫ���ѹ�ϵ 44001 ��ý���ļ�Ϊ�� 44002 POST�����ݰ�Ϊ�� 44003
	 * ͼ����Ϣ����Ϊ�� 44004 �ı���Ϣ����Ϊ�� 45001 ��ý���ļ���С�������� 45002 ��Ϣ���ݳ������� 45003 �����ֶγ�������
	 * 45004 �����ֶγ������� 45005 �����ֶγ������� 45006 ͼƬ�����ֶγ������� 45007 ��������ʱ�䳬������ 45008
	 * ͼ����Ϣ�������� 45009 �ӿڵ��ó������� 45010 �����˵������������� 45015 �ظ�ʱ�䳬������ 45016 ϵͳ���飬�������޸�
	 * 45017 �������ֹ��� 45018 ���������������� 46001 ������ý������ 46002 �����ڵĲ˵��汾 46003 �����ڵĲ˵�����
	 * 46004 �����ڵ��û� 47001 ����JSON/XML���ݴ��� 48001
	 * api����δ��Ȩ����ȷ�Ϲ��ں��ѻ�øýӿڣ������ڹ���ƽ̨����-����������ҳ�в鿴�ӿ�Ȩ�� 50001 �û�δ��Ȩ��api 50002
	 * �û����ޣ�������Υ���ӿڱ���� 61451 ��������(invalid parameter) 61452 ��Ч�ͷ��˺�(invalid
	 * kf_account) 61453 �ͷ��ʺ��Ѵ���(kf_account exsited) 61454
	 * �ͷ��ʺ������ȳ�������(������10��Ӣ���ַ���������@��@��Ĺ��ںŵ�΢�ź�)(invalid kf_acount length) 61455
	 * �ͷ��ʺ��������Ƿ��ַ�(������Ӣ��+����)(illegal character in kf_account) 61456
	 * �ͷ��ʺŸ�����������(10���ͷ��˺�)(kf_account count exceeded) 61457 ��Чͷ���ļ�����(invalid
	 * file type) 61450 ϵͳ����(system error) 61500 ���ڸ�ʽ���� 61501 ���ڷ�Χ���� 9001001
	 * POST���ݲ������Ϸ� 9001002 Զ�˷��񲻿��� 9001003 Ticket���Ϸ� 9001004 ��ȡҡ�ܱ��û���Ϣʧ�� 9001005
	 * ��ȡ�̻���Ϣʧ�� 9001006 ��ȡOpenIDʧ�� 9001007 �ϴ��ļ�ȱʧ 9001008 �ϴ��زĵ��ļ����Ͳ��Ϸ� 9001009
	 * �ϴ��زĵ��ļ��ߴ粻�Ϸ� 9001010 �ϴ�ʧ�� 9001020 �ʺŲ��Ϸ� 9001021 �����豸�����ʵ���50%�����������豸
	 * 9001022 �豸���������Ϸ�������Ϊ����0������ 9001023 �Ѵ�������е��豸ID���� 9001024 һ�β�ѯ�豸ID�������ܳ���50
	 * 9001025 �豸ID���Ϸ� 9001026 ҳ��ID���Ϸ� 9001027 ҳ��������Ϸ� 9001028 һ��ɾ��ҳ��ID�������ܳ���10
	 * 9001029 ҳ����Ӧ�����豸�У����Ƚ��Ӧ�ù�ϵ��ɾ�� 9001030 һ�β�ѯҳ��ID�������ܳ���50 9001031 ʱ�����䲻�Ϸ�
	 * 9001032 �����豸��ҳ��İ󶨹�ϵ�������� 9001033 �ŵ�ID���Ϸ� 9001034 �豸��ע��Ϣ���� 9001035
	 * �豸����������Ϸ� 9001036 ��ѯ��ʼֵbegin���Ϸ�
	 */
}
