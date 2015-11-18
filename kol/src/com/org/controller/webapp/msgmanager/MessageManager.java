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
 * manager all the message send interface 
 * @author Administrator
 *
 */
public class MessageManager {
	private Log log = LogFactory.getLog(MessageManager.class);
	protected MessageManager(){}

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
}
