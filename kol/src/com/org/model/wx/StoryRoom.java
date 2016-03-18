package com.org.model.wx;

import com.org.services.busi.StoryService;
import com.org.util.SpringUtil;
import com.org.wx.utils.MessageUtil;

public class StoryRoom extends ChatingRoom{
	// 
	private boolean over;

	@Override
	public void sendToAll(WxUser wxUser, String content) {
		// 故事模式，要将用户发送的信息保存下来，
		// TODO 保存
		
		// 这里的名字要用故事名
		String rolename = wxUser.getStoryName();
		StoryService storyService = (StoryService)SpringUtil.getBean("storyService");
		
		storyService.saveStory(getTemplateid(), rolename, content, "");
		MessageUtil.sendToMulti(rolename + ":\n" + content, userList);
	}

	public StoryRoom() {
	}
	
	public StoryRoom(Long roomid, String roomname, String roomtitle) {
		super(roomid, roomname, roomtitle);
	}
	
}
