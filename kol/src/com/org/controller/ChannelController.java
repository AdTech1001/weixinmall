package com.org.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.org.common.CommonConstant;
import com.org.interfaces.controller.CommonController;
import com.org.services.busi.ChannelService;
import com.org.services.busi.CommemorateService;
import com.org.servlet.SmpHttpServlet;
import com.org.util.SpringUtil;
import com.org.utils.PropertyUtil;

@Controller
public class ChannelController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = 2156792239072761671L;

	public ChannelController(){
		
	}
	
	public void home(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		log.info("home。。。" );
		HttpSession session = request.getSession();
		session.setAttribute(CommonConstant.CHANNEL_NAME, "首页");
		session.setAttribute(CommonConstant.CURRENT_CHANNEL_ID, CommonConstant.HOME);
		
		String topTimesGoal = PropertyUtil.getValue("business", "topTimesGoal");
		ChannelService channelService = (ChannelService) SpringUtil.getBean("channelService");
		String t_limit = "10";
		JSONArray testimonialsArray = channelService.getTestimonialsByChannelId(null, t_limit);
		
		// 纪念板的第一个
		CommemorateService commemorateService = (CommemorateService) SpringUtil.getBean("commemorateService");
		JSONArray commemorateArray = commemorateService.getCurrentCommemorate("1", topTimesGoal);
		if(commemorateArray.size() > 0){
			request.setAttribute("commemorate", commemorateArray.getJSONObject(0));
		}
		
		// 假设查询到的永远只有100条数据，每列分25条数据
		request.setAttribute("testimonialsArray", testimonialsArray);
		
		this.forward("/home.jsp", request, response);
		return;
	}
	
	public void life(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		log.info("life。。。" );
		HttpSession session = request.getSession();
		session.setAttribute(CommonConstant.CHANNEL_NAME, "生活");
		session.setAttribute(CommonConstant.CURRENT_CHANNEL_ID, CommonConstant.LIFE);
		
		ChannelService channelService = (ChannelService) SpringUtil.getBean("channelService");
		String t_limit = "10";
		JSONArray testimonialsArray = channelService.getTestimonialsByChannelId(CommonConstant.LIFE, t_limit);
		
		request.setAttribute("testimonialsArray", testimonialsArray);

		this.forward("/channel/life.jsp", request, response);
		return;
	}
	
	public void emotion(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		log.info("emotion。。。" );
		HttpSession session = request.getSession();
		session.setAttribute(CommonConstant.CHANNEL_NAME, "情感");
		session.setAttribute(CommonConstant.CURRENT_CHANNEL_ID, CommonConstant.EMOTION);
		
		ChannelService channelService = (ChannelService) SpringUtil.getBean("channelService");
		String t_limit = "10";
		JSONArray testimonialsArray = channelService.getTestimonialsByChannelId(CommonConstant.EMOTION, t_limit);
		
		request.setAttribute("testimonialsArray", testimonialsArray);

		// 如果是必需要长期存放于session的, 则使用session. 否则只要使用request就行了
		// 如果使用request, 则只能forward到页面, 才能拿到数据

		this.forward("/channel/emotion.jsp", request, response);
		return;
	}
	
	public void career(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		log.info("career。。。" );
		HttpSession session = request.getSession();
		session.setAttribute(CommonConstant.CHANNEL_NAME, "职场");
		session.setAttribute(CommonConstant.CURRENT_CHANNEL_ID, CommonConstant.CAREER);
		
		ChannelService channelService = (ChannelService) SpringUtil.getBean("channelService");
		String t_limit = "10";
		JSONArray testimonialsArray = channelService.getTestimonialsByChannelId(CommonConstant.CAREER, t_limit);
		
		request.setAttribute("testimonialsArray", testimonialsArray);

		//this.redirect("/channel/career.jsp", response);
		this.forward("/channel/career.jsp", request, response);
		return;
	}
	
	public void other(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		log.info("other。。。" );
		HttpSession session = request.getSession();
		session.setAttribute(CommonConstant.CHANNEL_NAME, "其他");
		session.setAttribute(CommonConstant.CURRENT_CHANNEL_ID, CommonConstant.OTHER);
		
		ChannelService channelService = (ChannelService) SpringUtil.getBean("channelService");
		String t_limit = "10";
		JSONArray testimonialsArray = channelService.getTestimonialsByChannelId(CommonConstant.OTHER, t_limit);
		
		request.setAttribute("testimonialsArray", testimonialsArray);

		this.forward("/channel/other.jsp", request, response);
		return;
	}
	
	/*
	 * 纪念板
	 */
	public void commemorateBoard(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		log.info("纪念板。。。" );
		HttpSession session = request.getSession();
		session.setAttribute(CommonConstant.CHANNEL_NAME, "纪念板");
		session.setAttribute(CommonConstant.CURRENT_CHANNEL_ID, CommonConstant.COMMEMORATE_BOARD);
		
		CommemorateService commemorateService = (CommemorateService) SpringUtil.getBean("commemorateService");
		JSONArray resultArray = commemorateService.getLimitCommemorate("50");
		request.setAttribute("commemorateArray", resultArray);

		this.forward("/channel/commemorateBoard.jsp", request, response);
		return;
	}
	
	/*
	 * 友链
	 */
	public void friendLink(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		log.info("友链。。。" );
		HttpSession session = request.getSession();
		session.setAttribute(CommonConstant.CHANNEL_NAME, "友情链接");
		//session.setAttribute(CommonConstant.CURRENT_CHANNEL_ID, CommonConstant.COMMEMORATE_BOARD);
		
//		CommemorateService commemorateService = (CommemorateService) SpringUtil.getBean("commemorateService");
//		JSONArray resultArray = commemorateService.getLimitCommemorate("50");
//		request.setAttribute("commemorateArray", resultArray);
		
		this.forward("/channel/friendLink.jsp", request, response);
		return;
	}
	
	private Log log = LogFactory.getLog(ChannelController.class);

	@Override
	public void post(HttpServletRequest request, HttpServletResponse response) {
		
	}
}
