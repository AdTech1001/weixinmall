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

import com.org.common.CommonConstant;
import com.org.interfaces.controller.CommonController;
import com.org.servlet.SmpHttpServlet;
import com.org.utils.JSONUtils;
import com.org.utils.RequestUtils;
import com.org.utils.StringUtil;

@Controller
public class QueryController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = -6179658706114169700L;

	public void start(HttpServletRequest request,HttpServletResponse response) 
			throws UnsupportedEncodingException, IOException{
		/* 1.获得商户端请求的值  默认设置数据处理成功 */
		Map<String,String> paramMap = RequestUtils.getParamMap(request);
		log.info("收到远程请求参数：　" + StringUtil.mapStringToString(paramMap));
		
		JSONObject requestJson = JSONUtils.getJsonFromStrStrMap(paramMap);
		JSONObject result = RequestUtils.precheckParmas(requestJson);
		if(! result.getString(CommonConstant.RESP_CODE).equals("10000")){
			response.getOutputStream().write(result.toString().getBytes("UTF-8"));
			log.info(result.getString(CommonConstant.RESP_CODE) +": " + result.getString(CommonConstant.RESP_MSG));
			return ;
		}
		
		//Connection con = null;
		//if(con == null){
			// 尝试从请求中获取数据库参数，动态加载数据源，成功后返回con
			
		//}
		
		// 二、执行查询
		//result = con.executeQuery(requestJson);
		
		// 三、返回数据
		log.info("远程请求返回数据：　" + result.toString());
		response.getOutputStream().write(result.toString().getBytes("UTF-8"));
		return;
	}

	private Log log = LogFactory.getLog(QueryController.class);

	@Override
	public void post(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
}
