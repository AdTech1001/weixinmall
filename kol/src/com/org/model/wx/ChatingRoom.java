package com.org.model.wx;

import java.util.ArrayList;
import java.util.List;

public class ChatingRoom {
	private Integer roomid;
	// ��������
	private String roomname;
	// �������� 
	private String roomtitle;
	/**
	 * �û�openid����
	 */
	private List<String> userList = new ArrayList<String>();
	
	//private Map<String, List<String>> roomMap = new HashMap<String, List<String>>(); 

	public ChatingRoom() {
		
	}
	
	public ChatingRoom(Integer roomid, String roomname, String roomtitle) {
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

	public Integer getRoomid() {
		return roomid;
	}

	public void setRoomid(Integer roomid) {
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
	
}
