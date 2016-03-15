package com.org.queues;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.caches.Memcache;
import com.org.common.CommonConstant;
import com.org.utils.HttpTool;
import com.org.utils.HttpUtil;
import com.org.utils.PropertyUtil;
import com.org.wx.utils.MessageUtil;

import net.sf.json.JSONObject;

/**
 * 发送信息任务
 * @author zhou_man
 *
 * 2016年3月15日
 */
public class MessageSendTask {
	
	private static String url = PropertyUtil.getValue("wx", "wx_send_message_by_service");
	private static Log log = LogFactory.getLog(MessageUtil.class);
	
	private JSONObject paramContent;
	private String openid;
	
	/**
	 * 调用客服接口发送信息
	 */
	public void sendByService(){
		String urlTemp = url.concat(Memcache.getInstance().getValue(CommonConstant.WX_TOKEN_KEY));
		paramContent.put("touser", openid);
		HttpTool http = new HttpUtil();
		JSONObject returns = http.wxHttpsPost(paramContent, urlTemp);
		log.info("MessageSendTask sendByService ====> " + returns);
	}
	
	public MessageSendTask(JSONObject paramContent, String openid) {
		this.paramContent = paramContent;
		this.openid = openid;
	}

	public JSONObject getParamContent() {
		return paramContent;
	}
	public void setParamContent(JSONObject paramContent) {
		this.paramContent = paramContent;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	
}
