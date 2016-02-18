package com.org.wx.utils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.caches.Memcache;
import com.org.common.CommonConstant;
import com.org.log.LogUtil;
import com.org.log.impl.LogUtilMg;
import com.org.model.WxAutoReply;
import com.org.util.CT;
import com.org.utils.HttpTool;
import com.org.utils.HttpUtil;
import com.org.utils.PropertyUtil;
import com.org.utils.SHA1Util;

public class WxUtil {
	private static Log log = LogFactory.getLog(WxUtil.class);
	private static Timer timer = new Timer();
	private static HttpTool http = new HttpUtil();
	
	public static final String ENTER_CHATING_ROOM = "enterChatingroom";
	public static final String EXIT_CHATING_ROOM = "exitChatingroom";
	
	private static final String WX_TICKET = "wxTicket"; // 微信端的ticket
	private static final String ACCESS_TOKEN_KEY = "access_token";
	private static final String LOCAL_TOKEN = PropertyUtil.getValue("wx", "local_token");
	private static final String GRANT_TYPE = PropertyUtil.getValue("wx", "grant_type");
	private static final String APPID = PropertyUtil.getValue("wx", "appid");
	private static final String SECRET = PropertyUtil.getValue("wx", "secret");
	private static final String TOKEN_URL = PropertyUtil.getValue("wx", "wx_token_url");
	private static final String TICKET_URL = PropertyUtil.getValue("wx", "wx_ticket_url");
	private static final String TICKET_TYPE = PropertyUtil.getValue("wx", "type_for_ticket");
	private static final String autoOpen = PropertyUtil.getValue("wx", "auto_open");
	private static final int timeInterval = Integer.valueOf(PropertyUtil.getValue("wx", "time_interval"));
	
	public static boolean checkSignature(String signature, String timestamp,
			String nonce) {
		String[] paramArr = { LOCAL_TOKEN, timestamp, nonce };
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
		requestJson.put("grant_type", GRANT_TYPE); // 微信接口
		requestJson.put("appid", APPID);
		requestJson.put("secret", SECRET);
		JSONObject responseJson = http.httpPost(requestJson, TOKEN_URL, CT.ENCODE_UTF8);

		// 存放到memcache
		log.info("存放前====> "+Memcache.getInstance().getValue(CommonConstant.WX_TOKEN_KEY));
		Memcache.getInstance().setValue(CommonConstant.WX_TOKEN_KEY, timeInterval, responseJson.getString(ACCESS_TOKEN_KEY));
		log.info("存放前====> "+Memcache.getInstance().getValue(CommonConstant.WX_TOKEN_KEY));
		return responseJson.getString(ACCESS_TOKEN_KEY);
	}

	/**
	 * 根据token请求ticket
	 * 
	 * @return
	 */
	public static boolean initTicket(String token) {
		JSONObject requestJson = new JSONObject();

		requestJson.put(ACCESS_TOKEN_KEY, token); // 微信接口
		requestJson.put("type", TICKET_TYPE);

		JSONObject responseJson = http.httpPost(requestJson, TICKET_URL, CT.ENCODE_UTF8);

		if (0 == responseJson.getInt("errcode")) {
			// 获取成功
			// 存放到memcache
			Memcache.getInstance().setValue(WX_TICKET, timeInterval, responseJson.getString("ticket"));
			return true;
		} else {
			LogUtil.log(WxUtil.class, "initTicket 失败：" + responseJson.get("errmsg"), null, LogUtilMg.LOG_DEBUG, CT.LOG_PATTERN_NULL);
			return false;
		}
	}

	public static boolean wxInit() {
		String token = initToken();
		log.info("wxInit token: "+token);
		boolean res = false;
		if (StringUtils.isNotEmpty(token)) {
			res = initTicket(token);
		}
		return res;
	}

	/**
	 * 从memcache获取token
	 * @return
	 */
	public static String getToken() {
		return Memcache.getInstance().getValue(CommonConstant.WX_TOKEN_KEY);
	}
	
	public static String getSecret() {
		return SECRET;
	}

	/**
	 * 
	 * @param timeInterval
	 */
	public static void autoRun() {
		if(autoOpen.equals("false")) {
			return;
		}
		// 同时启一个定时任务,每两小时执行一次
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime(); // 第一次执行定时任务的时间
		WxTimerTask task = new WxTimerTask();
		// 安排指定的任务在指定的时间开始进行重复的固定延迟执行。
		timer.schedule(task, date, timeInterval * 1000);
	}
	
	public static void cancelAutoRun(){
		timer.cancel();
	}

	public static String replyStr(String replyMsg, JSONObject xmlJson) {
		String CreateTime = String.valueOf(new Date().getTime());
		String ToUserName = xmlJson.getString("ToUserName");
		String FromUserName = xmlJson.getString("FromUserName");
		
		String MsgType = "text";
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		sb.append("<ToUserName><![CDATA[").append(FromUserName).append("]]></ToUserName>");
		sb.append("<FromUserName><![CDATA[").append(ToUserName).append("]]></FromUserName>");
		sb.append("<CreateTime>").append(CreateTime).append("</CreateTime>");
		sb.append("<MsgType><![CDATA[").append(MsgType).append("]]></MsgType>");
		sb.append("<Content><![CDATA[").append(replyMsg).append("]]></Content>");
		sb.append("</xml>");
		return sb.toString();
	}
	
	/**
	 * 定义菜单公众号在线返回菜单,解析完xml得到数据后，根据用户输入的内容来返回定义菜单
	 * 
	 * @param xmlJson
	 * @return
	 */
	public static String autoReply(JSONObject xmlJson) {
		String Content = xmlJson.getString("Content");
		String menuStr = matchContent(Content);

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
	 * 根据用户输入的内容判断， 是否在菜单中, 如果在则返回该菜单项，如果不在， 则返回所有菜单
	 * 
	 * @param Content
	 * @return 
	 */
	public static String matchContent(String content) {
		WxAutoReply autoReply = WxAutoReply.getInstance();
		if (autoReply.containsMenu(content)) {
			return autoReply.get(content);
		}
		return autoReply.getReplyWords();
	}

	/**
	 * 本地签名, 微信接口签名用的 noncestr和timestamp必须与wx.config中的nonceStr和timestamp相同。
	 * @param timestamp
	 * @param nonceStr
	 * @param url
	 *            必须是调用JS接口页面的完整URL
	 * @return
	 */
	public static String localSign(String timestamp, String noncestr, String url) {
		StringBuffer temp = new StringBuffer();
		String ticket = Memcache.getInstance().getValue(WX_TICKET);
		// jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg&noncestr=Wm3WZYTPz0wzccnW&timestamp=1414587457&url=http://mp.weixin.qq.com?params=value
		temp.append("jsapi_ticket=").append(ticket).append("&");
		temp.append("noncestr=").append(noncestr).append("&");
		temp.append("timestamp=").append(timestamp).append("&");
		temp.append("url=").append(url);

		return SHA1Util.sha1(temp.toString());
	}
	
	public static void deleteBottomMenu() {
		String token = Memcache.getInstance().getValue(CommonConstant.WX_TOKEN_KEY);
		// 先删除：
		StringBuffer deleteUrl = new StringBuffer(PropertyUtil.getValue("wx", "wx_delete_bottommenu_url"));
		deleteUrl.append(token);
		String res = http.httpGet(deleteUrl.toString(), CT.ENCODE_UTF8);
		System.out.println("=====delete> "+res);
	}
		
	public static String createMenu() {
		String submenuKey = "sub_button";
		String token = Memcache.getInstance().getValue(CommonConstant.WX_TOKEN_KEY);

		JSONArray menuArray = new JSONArray();
		Properties p = PropertyUtil.getProperties("wx_botton_menu");
		String[] menuKeys = {"menua", "menub", "menuc"};
		for (int i = 0; i < menuKeys.length; i++) {
			// 一级
			String menuKeyTemp = menuKeys[i];
			String menuValTemp = p.getProperty(menuKeyTemp);
			
			if(StringUtils.isNotEmpty(menuValTemp)) {
				JSONObject submenuJson = JSONObject.fromObject(menuValTemp);
				JSONArray submenuArray = new JSONArray();
				String[] submenuKeys = {"suba","subb","subc","subd","sube"};
				for (int j = 0; j < submenuKeys.length; j++) {
					String submenu = p.getProperty(menuKeyTemp+"_"+submenuKeys[j]);
					if(StringUtils.isNotEmpty(submenu)) {
						// 如果不为空，
						submenuArray.add(submenu);
					}
				}
				if(! submenuArray.isEmpty()) {
					submenuJson.put(submenuKey, submenuArray);
				}
				menuArray.add(submenuJson);
			}
		}
		JSONObject menuJson = new JSONObject();
		menuJson.put("button", menuArray);
		
		StringBuffer createUrl = new StringBuffer(PropertyUtil.getValue("wx", "wx_create_bottommenu_url"));
		createUrl.append(token);

		JSONObject responseJson = http.wxHttpsPost(menuJson, createUrl.toString());

		if (0 == responseJson.getInt("errcode")) {
			return "菜单初始化成功";
		} else {
			return "菜单初始化失败：" + responseJson.get("errmsg");
		}
	
	}
	
	private static class WxTimerTask extends TimerTask {
		@Override
		public void run() {
			boolean res = WxUtil.wxInit();
			LogUtil.log(WxTimerTask.class, "执行定时获取微信任务", null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_NULL);

			boolean anyTimes = Boolean.valueOf(PropertyUtil.getValue("wx", "create_menu_by_interval"));
			// 定时任务中，是否执行初始化菜单，和同步用户基本信息
			if(anyTimes && res) {
				// 初始化菜单
				createMenu();
			}
			
			if(res) {
				// 初始化用户基本信息， 一定是在token初始化完毕后执行
				WxUserUtil.initUserInfo();
			}
		}
	}

	/*
	 * 错误码：
	 * 
	 * -1 系统繁忙，此时请开发者稍候再试 0 请求成功 40001
	 * 获取access_token时AppSecret错误，或者access_token无效
	 * 。请开发者认真比对AppSecret的正确性，或查看是否正在为恰当的公众号调用接口 40002 不合法的凭证类型 40003
	 * 不合法的OpenID，请开发者确认OpenID（该用户）是否已关注公众号，或是否是其他公众号的OpenID 40004 不合法的媒体文件类型
	 * 40005 不合法的文件类型 40006 不合法的文件大小 40007 不合法的媒体文件id 40008 不合法的消息类型 40009
	 * 不合法的图片文件大小 40010 不合法的语音文件大小 40011 不合法的视频文件大小 40012 不合法的缩略图文件大小 40013
	 * 不合法的AppID，请开发者检查AppID的正确性，避免异常字符，注意大小写 40014
	 * 不合法的access_token，请开发者认真比对access_token的有效性（如是否过期），或查看是否正在为恰当的公众号调用接口 40015
	 * 不合法的菜单类型 40016 不合法的按钮个数 40017 不合法的按钮个数 40018 不合法的按钮名字长度 40019 不合法的按钮KEY长度
	 * 40020 不合法的按钮URL长度 40021 不合法的菜单版本号 40022 不合法的子菜单级数 40023 不合法的子菜单按钮个数 40024
	 * 不合法的子菜单按钮类型 40025 不合法的子菜单按钮名字长度 40026 不合法的子菜单按钮KEY长度 40027 不合法的子菜单按钮URL长度
	 * 40028 不合法的自定义菜单使用用户 40029 不合法的oauth_code 40030 不合法的refresh_token 40031
	 * 不合法的openid列表 40032 不合法的openid列表长度 40033 不合法的请求字符，不能包含uxxxx格式的字符 40035
	 * 不合法的参数 40038 不合法的请求格式 40039 不合法的URL长度 40050 不合法的分组id 40051 分组名字不合法 40117
	 * 分组名字不合法 40118 media_id大小不合法 40119 button类型错误 40120 button类型错误 40121
	 * 不合法的media_id类型 40132 微信号不合法 40137 不支持的图片格式 41001 缺少access_token参数 41002
	 * 缺少appid参数 41003 缺少refresh_token参数 41004 缺少secret参数 41005 缺少多媒体文件数据 41006
	 * 缺少media_id参数 41007 缺少子菜单数据 41008 缺少oauth code 41009 缺少openid 42001
	 * access_token超时
	 * ，请检查access_token的有效期，请参考基础支持-获取access_token中，对access_token的详细机制说明 42002
	 * refresh_token超时 42003 oauth_code超时 43001 需要GET请求 43002 需要POST请求 43003
	 * 需要HTTPS请求 43004 需要接收者关注 43005 需要好友关系 44001 多媒体文件为空 44002 POST的数据包为空 44003
	 * 图文消息内容为空 44004 文本消息内容为空 45001 多媒体文件大小超过限制 45002 消息内容超过限制 45003 标题字段超过限制
	 * 45004 描述字段超过限制 45005 链接字段超过限制 45006 图片链接字段超过限制 45007 语音播放时间超过限制 45008
	 * 图文消息超过限制 45009 接口调用超过限制 45010 创建菜单个数超过限制 45015 回复时间超过限制 45016 系统分组，不允许修改
	 * 45017 分组名字过长 45018 分组数量超过上限 46001 不存在媒体数据 46002 不存在的菜单版本 46003 不存在的菜单数据
	 * 46004 不存在的用户 47001 解析JSON/XML内容错误 48001
	 * api功能未授权，请确认公众号已获得该接口，可以在公众平台官网-开发者中心页中查看接口权限 50001 用户未授权该api 50002
	 * 用户受限，可能是违规后接口被封禁 61451 参数错误(invalid parameter) 61452 无效客服账号(invalid
	 * kf_account) 61453 客服帐号已存在(kf_account exsited) 61454
	 * 客服帐号名长度超过限制(仅允许10个英文字符，不包括@及@后的公众号的微信号)(invalid kf_acount length) 61455
	 * 客服帐号名包含非法字符(仅允许英文+数字)(illegal character in kf_account) 61456
	 * 客服帐号个数超过限制(10个客服账号)(kf_account count exceeded) 61457 无效头像文件类型(invalid
	 * file type) 61450 系统错误(system error) 61500 日期格式错误 61501 日期范围错误 9001001
	 * POST数据参数不合法 9001002 远端服务不可用 9001003 Ticket不合法 9001004 获取摇周边用户信息失败 9001005
	 * 获取商户信息失败 9001006 获取OpenID失败 9001007 上传文件缺失 9001008 上传素材的文件类型不合法 9001009
	 * 上传素材的文件尺寸不合法 9001010 上传失败 9001020 帐号不合法 9001021 已有设备激活率低于50%，不能新增设备
	 * 9001022 设备申请数不合法，必须为大于0的数字 9001023 已存在审核中的设备ID申请 9001024 一次查询设备ID数量不能超过50
	 * 9001025 设备ID不合法 9001026 页面ID不合法 9001027 页面参数不合法 9001028 一次删除页面ID数量不能超过10
	 * 9001029 页面已应用在设备中，请先解除应用关系再删除 9001030 一次查询页面ID数量不能超过50 9001031 时间区间不合法
	 * 9001032 保存设备与页面的绑定关系参数错误 9001033 门店ID不合法 9001034 设备备注信息过长 9001035
	 * 设备申请参数不合法 9001036 查询起始值begin不合法
	 */
}
