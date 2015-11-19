package com.org.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.org.services.busi.ChannelService;
import com.org.servlet.CommonController;
import com.org.servlet.SmpHttpServlet;
import com.org.util.SpringUtil;


@Controller
public class DreamsController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = 8632438856419845609L;

	public void wish(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		log.info("wish������" );
		//HttpSession session = request.getSession(true);
		ChannelService channelService = (ChannelService) SpringUtil.getBean("channelService");
		String limit = "20";
		JSONArray testimonialsArray = channelService.getTestimonialsByChannelId(null, limit);
		// �����ѯ������Զֻ��100�����ݣ�ÿ�з�25������
		request.setAttribute("testimonialsArray", testimonialsArray);
		
		this.redirect("/index.jsp", response);
		return;
	}
	
	private Log log = LogFactory.getLog(DreamsController.class);

	@Override
	public void post(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
}
