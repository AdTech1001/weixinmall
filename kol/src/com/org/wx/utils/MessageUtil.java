package com.org.wx.utils;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.caches.Memcache;
import com.org.common.CommonConstant;
import com.org.utils.HttpTool;
import com.org.utils.HttpUtil;
import com.org.utils.PropertyUtil;

/**
 * �ͷ���Ϣ����
 * �ܹ���Ϣ��װ����Ϣ����
 * service message send manager interface 
 * @author Administrator
 *
 */
public class MessageUtil {
	private static HttpTool http = new HttpUtil();
	private static String url = PropertyUtil.getValue("wx", "wx_send_message_by_service");
	private static Log log = LogFactory.getLog(MessageUtil.class);
	protected MessageUtil(){}

	/**
	 * ������Ϣ��ָ���û�
	 * @param content ��ԭʼ���ı�
	 * @param openid 
	 * @return
	 */
	public static JSONObject sendToOne(String content, String openid){
		// paramContent json�壬�������û�openid��content��
		JSONObject paramContent = getTextMessageJson(content);
		paramContent.put("touser", openid);

		// ���ÿͷ��ӿڷ�����Ϣ 
		String urlTemp = url.concat(Memcache.getInstance().getValue(CommonConstant.WX_TOKEN_KEY));
		JSONObject returns = http.wxHttpsPost(paramContent, urlTemp);
		log.info("pushMessage returns====> " + returns);

		return returns;
	}

	public static void sendToMulti(String content, List<String> openidList){
		
		JSONObject paramContent = getTextMessageJson(content);
		String urlTemp = null;
		for (int i = 0; i < openidList.size(); i++) {
			if(StringUtils.isNotEmpty(openidList.get(i))){
				// ���ÿͷ��ӿڷ�����Ϣ 
				urlTemp = url.concat(Memcache.getInstance().getValue(CommonConstant.WX_TOKEN_KEY));
				paramContent.put("touser", openidList.get(i));
				
				JSONObject returns = http.wxHttpsPost(paramContent, urlTemp);
				log.info("pushMessage returns====> " + returns);
			}
		}
	}
	
	/**
	 * ������Ϣ.Ϊ������������
	 * @param paramContent json�壬�������û�openid��content��
	 * @return
	 */
	public static JSONObject pushMessage(JSONObject paramContent){

		// ���ÿͷ��ӿڷ�����Ϣ 
		String urlTemp = url.concat(Memcache.getInstance().getValue(CommonConstant.WX_TOKEN_KEY));
		log.info("pushMessage url====> " + urlTemp);
		
		
		HttpTool http = new HttpUtil();
		JSONObject returns = http.wxHttpsPost(paramContent, urlTemp);
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
	public static void pushMassMessage(JSONArray openidList, JSONObject paramContent, int nextOpenid){
		if(nextOpenid >= openidList.size()) {
			log.info("�ݹ����");
			return ;
		}
		
		String toOpenid = openidList.getString(nextOpenid);
		paramContent.put("touser", toOpenid);
		
		// ���ÿͷ��ӿڷ�����Ϣ 
		JSONObject pushResult = pushMessage(paramContent);
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
	public static JSONObject getTextMessageJson(String content){
		JSONObject contentTemp = new JSONObject();
		contentTemp.put("content", content);
		
		JSONObject paramContent = new JSONObject();
		paramContent.put("msgtype", "text");
		paramContent.put("text", contentTemp);
		return paramContent;
	}
	
	/**
	 * �༭ͼƬ��Ϣģ��
	 * @param mediaId 
	 * @return
	 */
	public static JSONObject getImageMessageJson(String mediaId){
		JSONObject imageTemp = new JSONObject();
		imageTemp.put("media_id", mediaId);
		
		JSONObject paramContent = new JSONObject();
		paramContent.put("msgtype", "image");
		paramContent.put("image", imageTemp);
		
		return paramContent;
	}
	
	/**
	 * �༭ͼ����Ϣģ��
	 * ֻ�ܷ���10����ʲô��˼��ÿ����ֻ�ܷ���10������
	 * @param articles : 
	 * "articles": [
		         {
		             "title":"Happy Day",
		             "description":"Is Really A Happy Day",
		             "url":"URL",
		             "picurl":"PIC_URL"
		         },
		         {
		             "title":"Happy Day",
		             "description":"Is Really A Happy Day",
		             "url":"URL",
		             "picurl":"PIC_URL"
		         }
	    ]
	 * @return
	 */
	public static JSONObject getNewsMessageJson(JSONArray articles){
		/*{
		    "msgtype":"news",
		    "news":{
		        "articles": [
		         {
		             "title":"Happy Day",
		             "description":"Is Really A Happy Day",
		             "url":"URL",
		             "picurl":"PIC_URL"
		         },
		         {
		             "title":"Happy Day",
		             "description":"Is Really A Happy Day",
		             "url":"URL",
		             "picurl":"PIC_URL"
		         }
		         ]
		    }
		}*/
		
		JSONObject news = new JSONObject();
		news.put("articles", articles);
		
		JSONObject paramContent = new JSONObject();
		paramContent.put("msgtype", "news");
		paramContent.put("news", news);
		return paramContent;
	}
}
