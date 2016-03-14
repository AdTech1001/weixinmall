package com.org.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import com.org.common.CommonConstant;
import com.org.common.PageConstant;
import com.org.common.UserConstant;
import com.org.interfaces.controller.CommonController;
import com.org.services.busi.FileService;
import com.org.services.busi.TestimonialsService;
import com.org.servlet.SmpHttpServlet;
import com.org.util.SpringUtil;
import com.org.utils.FileUploadUtil;

@Controller
public class TestimonialsController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = -2554067244094241952L;

	public void saveContents(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{

		JSONObject sessionUser = (JSONObject)request.getSession().getAttribute(UserConstant.SESSION_USER);
		String userId = sessionUser.getString("id");
		
		String fileId = "";
		
		// 保存图片信息
		JSONObject uploadResult = null;
		try {
			uploadResult = FileUploadUtil.uploadFile(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(uploadResult == null || uploadResult.isEmpty()) {
			request.setAttribute(CommonConstant.RESP_CODE, "SYS002");
			request.setAttribute(CommonConstant.RESP_MSG, "文件上传失败:系统异常");
			this.forward(PageConstant.ERROR, request, response);
			return;
		}
		
		JSONObject formParams = uploadResult.getJSONObject(CommonConstant.FORM_PARAMS);
		String contents = formParams.getString("testimonialsContent");
		String channelId = formParams.getString("channelId");
		String title = formParams.getString("testimonialsTitle");
		String filePath = formParams.getString(CommonConstant.FILE_PATH);
		
		if (StringUtils.isEmpty(filePath)) {
			request.setAttribute(CommonConstant.RESP_CODE, uploadResult.getString(CommonConstant.RESP_CODE));
			request.setAttribute(CommonConstant.RESP_MSG, uploadResult.getString(CommonConstant.RESP_MSG));
			this.forward(PageConstant.ERROR, request, response);
			return;
		}
		
		FileService fService = (FileService)SpringUtil.getBean("fileService");
		JSONObject saveFileRes = fService.saveContents(userId, filePath);
		String cc = saveFileRes.getString(CommonConstant.RESP_CODE);
		if("10000".equals(cc)) {
			JSONObject _temp = saveFileRes.getJSONObject("lastInsert");
			fileId = _temp.getString("id");
		}
		
		// 保存言论信息
		TestimonialsService tService = (TestimonialsService)SpringUtil.getBean("testimonialsService");
		tService.saveContents(userId, contents, channelId, title, fileId);
		// TODO 判断结果是否成功. 如果成功，跳转首页
		response.sendRedirect("/channel/home.do");
		return;
	}
	
	public void saveContentsNoPic(HttpServletRequest request,HttpServletResponse response) 
			throws UnsupportedEncodingException, IOException{

		JSONObject sessionUser = (JSONObject)request.getSession().getAttribute(UserConstant.SESSION_USER);
		String userId = sessionUser.getString("id");
		
		String contents = request.getParameter("testimonialsContent");
		String channelId = request.getParameter("channelId");
		String title = request.getParameter("testimonialsTitle");
		
		TestimonialsService tService = (TestimonialsService)SpringUtil.getBean("testimonialsService");
		
		tService.saveContents(userId, contents, channelId, title, "");
		response.sendRedirect("/channel/home.do");
		return;
	}
	
	//private Log log = LogFactory.getLog(TestimonialsController.class);

	@Override
	public void post(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
}
