package com.org.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.org.common.CommonConstant;
import com.org.common.PageConstant;
import com.org.common.UserConstant;
import com.org.interfaces.controller.CommonController;
import com.org.services.busi.CommemorateService;
import com.org.services.busi.FileService;
import com.org.services.busi.TestimonialsService;
import com.org.servlet.SmpHttpServlet;
import com.org.util.SpringUtil;
import com.org.utils.FileUploadUtil;

/**
 * �����
 * @author Administrator
 *
 */
@Controller
public class CommemorateController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = -3498132823103396194L;
	//private static AtomicInteger topTimes = new AtomicInteger(0);

	public void addCommemorate(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		HttpSession session = request.getSession();

		JSONObject sessionUser = (JSONObject)session.getAttribute(UserConstant.SESSION_USER);
		String userId = sessionUser.getString(UserConstant.USERID);
		String comments = request.getParameter("comments");
		// TODO �ϴ�ͼƬ�Ȳ���
		//String fileId = request.getParameter("picPath");
		String fileId = "";
		String commemorateDate = request.getParameter("commemorateDate");
		CommemorateService service = (CommemorateService)SpringUtil.getBean("commemorateService");
		service.save(userId, comments, fileId, commemorateDate);
		
		this.forward("/channel/commemorateBoard.do", request, response);
		return;
	}
	

	public void saveContents(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{

		JSONObject sessionUser = (JSONObject)request.getSession().getAttribute(UserConstant.SESSION_USER);
		String userId = sessionUser.getString("id");
		
		String fileId = "";
		
		// ����ͼƬ��Ϣ
		JSONObject uploadResult = null;
		try {
			uploadResult = FileUploadUtil.uploadFile(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(uploadResult == null || uploadResult.isEmpty()) {
			request.setAttribute(CommonConstant.RESP_CODE, "SYS002");
			request.setAttribute(CommonConstant.RESP_MSG, "�ļ��ϴ�ʧ��:ϵͳ�쳣");
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
		
		// ����������Ϣ
		TestimonialsService tService = (TestimonialsService)SpringUtil.getBean("testimonialsService");
		tService.saveContents(userId, contents, channelId, title, fileId);
		// TODO �жϽ���Ƿ�ɹ�. ����ɹ�����ת��ҳ
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
	
	

	public void toAddPage(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		//JSONObject sessionUser = (JSONObject)session.getAttribute(UserConstant.SESSION_USER);
//			String userId = sessionUser.getString("id");
		this.forward("/channel/addCommemorate.jsp", request, response);
		return;
	}
	
	public void topOnce(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		//HttpSession session = request.getSession();
		//JSONObject sessionUser = (JSONObject)session.getAttribute(UserConstant.SESSION_USER);
		//String userId = sessionUser.getString("id");
		// ����ɲ������ַ�ʽ
//		if(topTimes.addAndGet(1) > 100){
//		}
		// ����ֻҪһ��һ�����Ӿͺ���
		String aimid = request.getParameter("id");
		CommemorateService service = (CommemorateService)SpringUtil.getBean("commemorateService");
		//JSONObject res = service.getCommemorateById(aimid);
		boolean topRes = service.addOneTop(aimid);
		
		String res = "";
		if(topRes){
			log.info("top success������" );
			res = "success";
		}
		this.write(res, CommonConstant.UTF8, response);
		return;
	}
	
	private Log log = LogFactory.getLog(CommemorateController.class);

	@Override
	public void post(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
}
