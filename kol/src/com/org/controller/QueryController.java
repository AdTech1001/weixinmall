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

import com.org.Connection;
import com.org.common.CommonConstant;
import com.org.servlet.CommonController;
import com.org.servlet.SmpHttpServlet;
import com.org.utils.JSONUtils;
import com.org.utils.RequestUtils;
import com.org.utils.StringUtil;

@Controller
public class QueryController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = -6179658706114169700L;

	public void start(HttpServletRequest request,HttpServletResponse response) 
			throws UnsupportedEncodingException, IOException{
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

	private Log log = LogFactory.getLog(QueryController.class);

	@Override
	public void post(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
}
