package com.org.model.wx;

import com.org.services.busi.StoryService;
import com.org.util.SpringUtil;
import com.org.wx.utils.MessageUtil;

public class StoryRoom extends ChatingRoom{
	// 
	private boolean over;

	@Override
	public void sendToAll(WxUser wxUser, String content) {
		// ����ģʽ��Ҫ���û����͵���Ϣ����������
		// TODO ����
		
		// ���������Ҫ�ù�����
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
