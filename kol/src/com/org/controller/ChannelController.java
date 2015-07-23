package com.org.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.org.common.CommonConstant;
import com.org.services.busi.ChannelService;
import com.org.services.busi.CommemorateService;
import com.org.servlet.CommonController;
import com.org.servlet.SmpHttpServlet;
import com.org.util.SpringUtil;
import com.org.utils.SmpPropertyUtil;

@Controller
public class ChannelController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = 2156792239072761671L;

	public ChannelController(){
		
	}
	
	public void home(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		log.info("home������" );
		HttpSession session = request.getSession();
		session.setAttribute(CommonConstant.CHANNEL_NAME, "��ҳ");
		session.setAttribute(CommonConstant.CURRENT_CHANNEL_ID, CommonConstant.HOME);
		
		String t_limit = "10";
		String topTimesGoal = SmpPropertyUtil.getValue("business", "topTimesGoal");
		ChannelService channelService = (ChannelService) SpringUtil.getBean("channelService");
		JSONArray testimonialsArray = channelService.getTestimonialsByChannelId(null, t_limit);
		
		// �����ĵ�һ��
		CommemorateService commemorateService = (CommemorateService) SpringUtil.getBean("commemorateService");
		JSONArray commemorateArray = commemorateService.getCurrentCommemorate("1", topTimesGoal);
		if(commemorateArray.size() > 0){
			request.setAttribute("commemorate", commemorateArray.getJSONObject(0));
		}
		
		// �����ѯ������Զֻ��100�����ݣ�ÿ�з�25������
		request.setAttribute("testimonialsArray", testimonialsArray);
		
		this.forward("/home.jsp", request, response);
		return;
	}
	
	public void life(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		log.info("life������" );
		HttpSession session = request.getSession();
		session.setAttribute(CommonConstant.CHANNEL_NAME, "����");
		session.setAttribute(CommonConstant.CURRENT_CHANNEL_ID, CommonConstant.LIFE);
		
		ChannelService channelService = (ChannelService) SpringUtil.getBean("channelService");
		JSONArray testimonialsArray = channelService.getTestimonialsByChannelId(CommonConstant.LIFE);
		request.setAttribute("testimonialsArray", testimonialsArray);

		this.forward("/channel/life.jsp", request, response);
		return;
	}
	
	public void emotion(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		log.info("emotion������" );
		HttpSession session = request.getSession();
		session.setAttribute(CommonConstant.CHANNEL_NAME, "���");
		session.setAttribute(CommonConstant.CURRENT_CHANNEL_ID, CommonConstant.EMOTION);
		
		ChannelService channelService = (ChannelService) SpringUtil.getBean("channelService");
		JSONArray testimonialsArray = channelService.getTestimonialsByChannelId(CommonConstant.EMOTION);
		request.setAttribute("testimonialsArray", testimonialsArray);

		// ����Ǳ���Ҫ���ڴ����session��, ��ʹ��session. ����ֻҪʹ��request������
		// ���ʹ��request, ��ֻ��forward��ҳ��, �����õ�����

		this.forward("/channel/emotion.jsp", request, response);
		return;
	}
	
	public void career(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		log.info("career������" );
		HttpSession session = request.getSession();
		session.setAttribute(CommonConstant.CHANNEL_NAME, "ְ��");
		session.setAttribute(CommonConstant.CURRENT_CHANNEL_ID, CommonConstant.CAREER);
		
		ChannelService channelService = (ChannelService) SpringUtil.getBean("channelService");
		JSONArray testimonialsArray = channelService.getTestimonialsByChannelId(CommonConstant.CAREER);
		request.setAttribute("testimonialsArray", testimonialsArray);

		//this.redirect("/channel/career.jsp", response);
		this.forward("/channel/career.jsp", request, response);
		return;
	}
	
	public void other(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		log.info("other������" );
		HttpSession session = request.getSession();
		session.setAttribute(CommonConstant.CHANNEL_NAME, "����");
		session.setAttribute(CommonConstant.CURRENT_CHANNEL_ID, CommonConstant.OTHER);
		
		ChannelService channelService = (ChannelService) SpringUtil.getBean("channelService");
		JSONArray testimonialsArray = channelService.getTestimonialsByChannelId(CommonConstant.OTHER);
		request.setAttribute("testimonialsArray", testimonialsArray);

		this.forward("/channel/other.jsp", request, response);
		return;
	}
	
	/*
	 * �����
	 */
	public void commemorateBoard(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		log.info("����塣����" );
		HttpSession session = request.getSession();
		session.setAttribute(CommonConstant.CHANNEL_NAME, "�����");
		session.setAttribute(CommonConstant.CURRENT_CHANNEL_ID, CommonConstant.COMMEMORATE_BOARD);
		
		CommemorateService commemorateService = (CommemorateService) SpringUtil.getBean("commemorateService");
		JSONArray resultArray = commemorateService.getLimitCommemorate("50");
		request.setAttribute("commemorateArray", resultArray);

		this.forward("/channel/commemorateBoard.jsp", request, response);
		return;
	}
	
	private Log log = LogFactory.getLog(ChannelController.class);

	@Override
	public void post(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
	}
}
