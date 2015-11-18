package com.org.controller.webapp;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.org.controller.webapp.utils.Memcache;
import com.org.controller.webapp.utils.WxUserContainer;
import com.org.controller.webapp.utils.WxUtil;
import com.org.servlet.CommonController;
import com.org.servlet.SmpHttpServlet;
import com.org.util.CT;
import com.org.utils.SmpPropertyUtil;
import com.org.utils.StringUtil;
import com.org.utils.XmlUtils;
import com.org.utils.http.HttpTool;
import com.org.utils.http.impl.HttpApacheClient;

@Controller
public class WxController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = 2156792239072761671L;

	public WxController(){
		
	}
	
	public void toWxTest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("token" + this.getParamMap(request));
		
		this.forward("/www/html/wxtest.jsp", request, response);
		return;
	}
	
	private Log log = LogFactory.getLog(WxController.class);

	/**
	 * ��һ��΢����֤��ʱ����Ҫ�����ģ��. ����һ��ģ�巽��.
	 * @param request
	 * @param response
	 * @throws Exception
	public void validate(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("token: " + this.getParamMap(request));
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		
		boolean signResult = WxUtil.checkSignature(signature, timestamp, nonce);
		if(!signResult) {
			log.info("��ǩ����");
			return;
		}
		String echostr = request.getParameter("echostr");
		
		this.write(echostr, CT.ENCODE_UTF8, response);
		return;
	}
	*/
	
	/**
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void validate(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("token: " + this.getParamMap(request));
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		
		boolean signResult = WxUtil.checkSignature(signature, timestamp, nonce);
		if(!signResult) {
			log.info("��ǩ����");
			return;
		}
		
		JSONObject xmlJson = XmlUtils.getDocumentFromRequest(request);
		log.info("�յ�΢�ŷ���������Ϣ��xmlJson=====> " + xmlJson);
		// TODO ΢�ŷ��������͵����в����������������͡� ���ԣ� Ҫ����һ��·�ɹ���
		// xmlJson ��һ���ֶν� MsgType �� ���������кܶ��֣� event ��ʾ���¼�/ text ��ʾ���ı���Ϣ ��
		String returnStr = "";
		if(xmlJson.getString("MsgType").equals("event")) {
			returnStr = dealEvent(xmlJson);
		} else if(xmlJson.getString("MsgType").equals("text")) {
			// ����Ϣ����
			String msgFromOpenid = xmlJson.getString("FromUserName");
			// ����Ϣ�ߵ��ǳ�
			String nick = WxUserContainer.getUserBaseInfo(msgFromOpenid).getString("nickname") ;
			Map<String, Boolean> chatingUsersMap = WxUserContainer.getChatingOpenidsMap();
			// �ж����Ƿ���������
			if(chatingUsersMap.containsKey(msgFromOpenid) && chatingUsersMap.get(msgFromOpenid)) {
				// ֱ�ӻظ�success�� �ɿͷ��ӿ�ȥ������Ϣ
				returnStr = "success";
				// ���ǻ�ȡ���е��û�
				// JSONObject userListJson = WxUserContainer.getUserList();
				// JSONObject data = userListJson.getJSONObject("data");
				// JSONArray openidList = data.getJSONArray("openid");
				JSONArray chatingUserArray = WxUserContainer.getChatingUser();
				// �����г�ȥ����Ϣ���Լ�
				chatingUserArray.remove(msgFromOpenid);
				log.info("openidList ====>"+chatingUserArray);
				
				String content = nick + "˵:\n"+xmlJson.getString("Content");
				// ��0��ʼ�ݹ鷢�ͣ�ʵ��Ⱥ��
				pushMassMessage(msgFromOpenid, chatingUserArray, content, 0);
			} else {
				returnStr = WxUtil.createMenu(xmlJson);
			}
		}
		
		System.out.println("��Ӧ��: " + returnStr);
		this.write(returnStr, CT.ENCODE_UTF8, response);
		return;
	}
	
	private String dealEvent(JSONObject xmlJson) {
		String FromUserName = xmlJson.getString("FromUserName");
		String Event = xmlJson.getString("Event");
		String EventKey = xmlJson.getString("EventKey"); // ��Ӧ�Զ����key ֵ
		log.info("EventKey ====>" + EventKey);
		// ���¼����� �� ����İ�ťkeyֵ�ж� ���Ծ���ҵ������
		if(Event.equals("CLICK")) {
			if(WxUtil.ENTER_CHATING_ROOM.equals(EventKey)) {
				// ����������
				WxUserContainer.joininChatingRoom(FromUserName);
				pushMessage(FromUserName, "���ѽ���������, ���Ժʹ��������");
			} else if(WxUtil.EXIT_CHATING_ROOM.equals(EventKey)) {
				// ����������
				WxUserContainer.exitChatingRoom(FromUserName);
				pushMessage(FromUserName, "�����˳�������");
			}
		}
		return "";
	}
	
	/**
	 * �ݹ鷢����Ϣ���û��б��е��û�
	 * @param openidList
	 * @param content
	 * @param nextOpenid
	 * @return
	 */
	public void pushMassMessage(String msgFromOpenid, JSONArray openidList, String content, int nextOpenid){
		if(nextOpenid >= openidList.size()) {
			log.info("�ݹ����");
			return ;
		}
		String toOpenid = openidList.getString(nextOpenid);
		
		// ���ÿͷ��ӿڷ�����Ϣ 
		String url = SmpPropertyUtil.getValue("wx", "wx_send_message_by_service");
		url = url.concat(Memcache.getInstance().getValue(WxUtil.WX_TOKEN));
		
		JSONObject contentTemp = new JSONObject();
		contentTemp.put("content", content);
		// ����������
		WxUserContainer.joininChatingRoom(toOpenid);
		
		JSONObject paramContent = new JSONObject();
		paramContent.put("touser", toOpenid);
		paramContent.put("msgtype", "text");
		paramContent.put("text", contentTemp);
		
		HttpTool http = new HttpApacheClient();
		JSONObject pushResult = http.wxHttpsPost(paramContent, url);
		if(pushResult.getString("errcode").equals("0")) {
			// ���ͳɹ�
			log.info("�ͷ��ӿ���Ϣ���ͳɹ����û�id��" + toOpenid);
		}
		// ��һ��openid������
		nextOpenid ++;
		// �ݹ鷢��
		pushMassMessage(msgFromOpenid, openidList, content, nextOpenid);

	}
	
	/**
	 * ������Ϣ��ָ���û�
	 * @param toOpenid
	 * @param content
	 * @return
	 */
	public JSONObject pushMessage(String toOpenid, String content){

		// ���ÿͷ��ӿڷ�����Ϣ 
		String url = SmpPropertyUtil.getValue("wx", "wx_send_message_by_service");
		url = url.concat(Memcache.getInstance().getValue(WxUtil.WX_TOKEN));
		
		JSONObject contentTemp = new JSONObject();
		contentTemp.put("content", content);
		
		JSONObject paramContent = new JSONObject();
		paramContent.put("touser", toOpenid);
		paramContent.put("msgtype", "text");
		paramContent.put("text", contentTemp);
		
		HttpTool http = new HttpApacheClient();
		JSONObject returns = http.wxHttpsPost(paramContent, url);
		log.info("pushMessage returns====> " + returns);

		return returns;
	}
	/**
	 * ����ӿ���ɼ�µ�΢�ź�
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void token(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("token: " + this.getParamMap(request));
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		
		boolean signResult = WxUtil.checkSignature(signature, timestamp, nonce);
		if(!signResult) {
			log.info("��ǩ����");
			return;
		}
		
		JSONObject xmlJson = XmlUtils.getDocumentFromRequest(request);
		// {"ToUserName":"gh_b35778044c48","FromUserName":"oUwjLwpYv0pkCC424mOxcBG24CVY","CreateTime":"1446189462","MsgType":"text","Content":"Ŷ","MsgId":"6211336443510120964"}
		String returnStr = WxUtil.createMenu(xmlJson);
		
		System.out.println("��Ӧ��: " + returnStr);
		this.write(returnStr, CT.ENCODE_UTF8, response);
		return;
	}

	public void toTest(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("toTest: " + this.getParamMap(request));
		String token = WxUtil.getToken();
		String timestamp = String.valueOf(StringUtil.getTimestamp()); // �������ǩ����ʱ���
		String nonceStr = UUID.randomUUID().toString(); // ���ǩ��������¼1
		String url = request.getRequestURL().toString();
		String signature = WxUtil.localSign(timestamp, nonceStr, url); // �����Ҫʹ�õ�JS�ӿ��б�����JS�ӿ��б����¼2
		String appid = SmpPropertyUtil.getValue("wx", "appid");
		
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("nonceStr", nonceStr);
		request.setAttribute("url", url);
		request.setAttribute("signature", signature);
		request.setAttribute("cacheToken", token);
		request.setAttribute("appId", appid);
		this.forward("/www/html/wxtest.jsp", request, response);
		return;
	}
	
	public void getCacheToken(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String token = WxUtil.getToken();
		String timestamp = String.valueOf(StringUtil.getTimestamp()); // �������ǩ����ʱ���
		String nonceStr = UUID.randomUUID().toString(); // ���ǩ��������¼1
		String url = request.getRequestURL().toString();
		String signature = WxUtil.localSign(timestamp, nonceStr, url); // �����Ҫʹ�õ�JS�ӿ��б�����JS�ӿ��б����¼2
		
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("nonceStr", nonceStr);
		request.setAttribute("url", url);
		request.setAttribute("signature", signature);
		request.setAttribute("cacheToken", token);
		this.forward("/www/html/wxtest.jsp", request, response);
		return;
	}
	
	public void initBottomMenu(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		WxUtil.createBottomMenu();
		//this.forward("/www/html/wxtest.jsp", request, response);
		return;
	}
	
	public void deleteBottomMenu(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		WxUtil.deleteBottomMenu();
		//this.forward("/www/html/wxtest.jsp", request, response);
		return;
	}
	
	public void post(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
	}
	
}
