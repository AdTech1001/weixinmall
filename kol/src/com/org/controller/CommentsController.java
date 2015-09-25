package com.org.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.org.services.busi.CommentsService;
import com.org.servlet.CommonController;
import com.org.servlet.SmpHttpServlet;
import com.org.util.SpringUtil;
import com.org.utils.ByteUtil;
import com.org.utils.DesUtil;

@Controller
public class CommentsController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = 3288382584901361068L;

	public void saveComments(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		response.setHeader("Pragma","no-cache"); 
		response.setHeader("Cache-Control","no-cache"); 
		response.setDateHeader("Expires", 0); 

		//HttpSession session = request.getSession(true);
		//JSONObject sessionUser = (JSONObject)session.getAttribute(UserConstant.SESSION_USER);
		
		/* 1.����̻��������ֵ  Ĭ���������ݴ���ɹ� */
		String testimonialsId = request.getParameter("testimonialsId");
		byte[] srcBytes = DesUtil.decryptMode(ByteUtil.hex2Bytes(testimonialsId));
		testimonialsId = new String(srcBytes, "UTF-8");
		
		String commentContent = request.getParameter("commentContent");
//		String userId = sessionUser.getString("id");
		String userId = "1";
		
		CommentsService commentsService = (CommentsService)SpringUtil.getBean("commentsService");
		commentsService.saveComments(testimonialsId, commentContent, userId);
		
		this.forward("/comments/queryComments.do", request, response);
	}
	
	public void queryComments(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
//		HttpSession session = request.getSession(true);
//		JSONObject sessionUser = (JSONObject)session.getAttribute(UserConstant.SESSION_USER);
//			String userId = sessionUser.getString("id");
		
//		String isTop = request.getParameter("isTop");// ʵ�ʲ�����ֵ��testimonialsId��3dex����
		String testimonialsId = request.getParameter("testimonialsId");// ʵ�ʲ�����ֵ��testimonialsId��3dex����
		byte[] srcBytes = DesUtil.decryptMode(ByteUtil.hex2Bytes(testimonialsId));
		testimonialsId = new String(srcBytes, "UTF-8");
		
		CommentsService commentsService = (CommentsService)SpringUtil.getBean("commentsService");
		JSONObject res = new JSONObject();
//		if(StringUtils.isNotEmpty(isTop) && isTop.equals("0")) {
//			// �ö���
//			res = commentsService.getCommentsByTopTesTimonialId(testimonialsId);
//		} else {
			res = commentsService.getCommentsByTesTimonialId(testimonialsId);
//		}
		
		request.setAttribute("res", res);

		this.forward("/comments/comments.jsp", request, response);
		return;
	}
	
	private Log log = LogFactory.getLog(CommentsController.class);

	@Override
	public void post(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
	}
}
