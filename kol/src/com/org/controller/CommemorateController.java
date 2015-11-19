package com.org.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.org.common.CommonConstant;
import com.org.common.UserConstant;
import com.org.services.busi.CommemorateService;
import com.org.servlet.CommonController;
import com.org.servlet.SmpHttpServlet;
import com.org.util.SpringUtil;

/**
 * 纪念板
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
		// TODO 上传图片先不管
		//String fileId = request.getParameter("fileId");
		String fileId = "";
		String commemorateDate = request.getParameter("commemorateDate");
		CommemorateService service = (CommemorateService)SpringUtil.getBean("commemorateService");
		service.save(userId, comments, fileId, commemorateDate);
		
		this.forward("/channel/commemorateBoard.do", request, response);
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
		// 量大可采用这种方式
//		if(topTimes.addAndGet(1) > 100){
//		}
		// 现在只要一个一个增加就好了
		String aimid = request.getParameter("id");
		CommemorateService service = (CommemorateService)SpringUtil.getBean("commemorateService");
		//JSONObject res = service.getCommemorateById(aimid);
		boolean topRes = service.addOneTop(aimid);
		
		String res = "";
		if(topRes){
			log.info("top success。。。" );
			res = "success";
		}
		this.write(res, CommonConstant.ENCODE_DEFAULT, response);
		return;
	}
	
	private Log log = LogFactory.getLog(CommemorateController.class);

	@Override
	public void post(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
}
