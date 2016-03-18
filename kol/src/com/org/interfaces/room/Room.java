package com.org.interfaces.room;

import com.org.model.wx.WxUser;

public interface Room {
	public void sendToAll(WxUser wxUser, String content);
	public void join(String openid);
	public void exit(String openid);
}
