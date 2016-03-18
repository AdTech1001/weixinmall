package com.org.model.wx;

import java.util.ArrayList;
import java.util.List;

import com.org.interfaces.room.Room;
import com.org.wx.utils.MessageUtil;

public class ChatingRoom implements Room{
	private Long roomid;
	// ��������
	private String roomname;
	// �������� 
	private String roomtitle;
	// ģ��id
	private Long templateid;
	
	/**
	 * �û�openid����
	 */
	protected List<String> userList = new ArrayList<String>();

	public ChatingRoom() {
	}

	@Override
	public void sendToAll(WxUser wxUser, String content) {
		String nick = wxUser.getNickname();
		MessageUtil.sendToMulti(nick + ":\n" + content, userList);
	}
	
	public ChatingRoom(Long roomid, String roomname, String roomtitle) {
		this.roomid = roomid;
		this.roomname = roomname;
		this.roomtitle = roomtitle;
	}

	public void join(String openid){
		if(!userList.contains(openid)) {
			userList.add(openid);
			// ������Ҫ�Ǵ��ڴ��ж�ȡ��Ϣ���д�������ֻ�����ڴ��е��û�����״̬�����ݿ�״̬���£����ö�ʱ�������
		}
	}

	public void exit(String openid){
		if(!userList.contains(openid)) {
			userList.remove(openid);
		}
	}
	
	public List<String> getAllOpenid(){
		return userList;
	}

	public Long getRoomid() {
		return roomid;
	}

	public void setRoomid(Long roomid) {
		this.roomid = roomid;
	}

	public String getRoomname() {
		return roomname;
	}

	public void setRoomname(String roomname) {
		this.roomname = roomname;
	}

	public String getRoomtitle() {
		return roomtitle;
	}

	public void setRoomtitle(String roomtitle) {
		this.roomtitle = roomtitle;
	}

	public Long getTemplateid() {
		return templateid;
	}

	public void setTemplateid(Long templateid) {
		this.templateid = templateid;
	}
	
}
