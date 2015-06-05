package com.org.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.org.Connection;
import com.org.common.CommonConstant;
import com.org.utils.JSONUtils;
import com.org.utils.RequestUtils;
import com.org.utils.StringUtil;

@Controller
@RequestMapping("/query")
public class QueryController {
	
	private Log log = LogFactory.getLog(QueryController.class);
	@RequestMapping("/executeQuery")
	public void start(HttpServletRequest request,HttpServletResponse response) 
			throws UnsupportedEncodingException, IOException{
		// һ����������Դ
		/* 0. �������ݲ�����  */
		response.setHeader("Pragma","no-cache"); 
		response.setHeader("Cache-Control","no-cache"); 
		response.setDateHeader("Expires", 0); 
		/* 1.����̻��������ֵ  Ĭ���������ݴ���ɹ� */
		Map<String,String> paramMap = RequestUtils.getParamMap(request);
		log.info("�յ�Զ�������������" + StringUtil.mapStringToString(paramMap));
		
		JSONObject requestJson = JSONUtils.getJsonFromStrStrMap(paramMap);
		JSONObject result = RequestUtils.precheckParmas(requestJson);
		if(! result.getString(CommonConstant.RESP_CODE).equals("10000")){
			response.getOutputStream().write(result.toString().getBytes("UTF-8"));
			log.info(result.getString(CommonConstant.RESP_CODE) +": " + result.getString(CommonConstant.RESP_MSG));
			return ;
		}
		
		String identityFlag = requestJson.getString("identityFlag");
		// ������ݣ�·�ɵ�ָ�������ݿ�
		Connection con = null;
		if(con == null){
			// ���Դ������л�ȡ���ݿ��������̬��������Դ���ɹ��󷵻�con
			
		}
		
		// ����ִ�в�ѯ
		result = con.executeQuery(requestJson);
		
		// ������������
		log.info("Զ�����󷵻����ݣ���" + result.toString());
		response.getOutputStream().write(result.toString().getBytes("UTF-8"));
		return;
	}

}
