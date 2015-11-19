package com.org.controller.webapp.msgmanager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.controller.webapp.utils.Memcache;
import com.org.controller.webapp.utils.WxUtil;
import com.org.utils.SmpPropertyUtil;
import com.org.utils.http.HttpTool;
import com.org.utils.http.impl.HttpApacheClient;

/**
 * �����ظ���Ϣ����ӿ�
 * passive reply message manager interface 
 * @author Administrator
 *
 */
// TODO 
public class PassiveReplyMessageManager {
	private Log log = LogFactory.getLog(PassiveReplyMessageManager.class);
	protected PassiveReplyMessageManager(){}

	/**
	 * ������Ϣ��ָ���û�
	 * @param toOpenid
	 * @param content
	 * @return
	 */
	public JSONObject pushMessage(JSONObject paramContent, String urlKey){

		// ���ÿͷ��ӿڷ�����Ϣ 
		String url = SmpPropertyUtil.getValue("wx", urlKey);
		url = url.concat(Memcache.getInstance().getValue(WxUtil.WX_TOKEN));
		
		HttpTool http = new HttpApacheClient();
		JSONObject returns = http.wxHttpsPost(paramContent, url);
		log.info("pushMessage returns====> " + returns);

		return returns;
	}

	/**
	 * �ݹ鷢����Ϣ���û��б��е��û�
	 * @param openidList
	 * @param content
	 * @param nextOpenid
	 * @return
	 */
	public void pushMassMessage(JSONArray openidList, JSONObject paramContent, int nextOpenid){
		if(nextOpenid >= openidList.size()) {
			log.info("�ݹ����");
			return ;
		}
		
		String toOpenid = openidList.getString(nextOpenid);
		paramContent.put("touser", toOpenid);
		
		// ���ÿͷ��ӿڷ�����Ϣ 
		String url = SmpPropertyUtil.getValue("wx", "wx_send_message_by_service");
		url = url.concat(Memcache.getInstance().getValue(WxUtil.WX_TOKEN));
		
		HttpTool http = new HttpApacheClient();
		JSONObject pushResult = http.wxHttpsPost(paramContent, url);
		if(pushResult.getString("errcode").equals("0")) {
			// ���ͳɹ�
			log.info("�ͷ��ӿ���Ϣ���ͳɹ����û�id��" + toOpenid);
		}
		// ��һ��openid������
		nextOpenid ++;
		// �ݹ鷢��
		pushMassMessage(openidList, paramContent, nextOpenid);

	}
	
	/**
	 * �༭�ı���Ϣģ��
	 * @param content
	 * @return
	 */
	public JSONObject getTextMessageJson(String content){
		JSONObject contentTemp = new JSONObject();
		contentTemp.put("content", content);
		
		JSONObject paramContent = new JSONObject();
		paramContent.put("msgtype", "text");
		paramContent.put("text", contentTemp);
		return paramContent;
	}
	
	/**
	 * �༭ͼ����Ϣģ��
	 * @return
	 */
	public JSONObject getNewsMessageJson(String content){
		JSONObject contentTemp = new JSONObject();
		contentTemp.put("content", content);
		
		JSONObject paramContent = new JSONObject();
		paramContent.put("msgtype", "text");
		paramContent.put("text", contentTemp);
		return paramContent;
	}
}
