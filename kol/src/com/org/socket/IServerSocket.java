package com.org.socket;

import com.org.exception.SSocketException;


/**
 * socket 服务接口
 * 
 * 阻塞和非阻塞服务由此接口派生
 * 
 * @since 2011-09-08 10:09:09
 *
 */
public interface IServerSocket extends Runnable{
	
	 public void service();
	 
	 public void close() throws SSocketException;
	 
	 public void start();
	 
	 public String getServerInfo()throws SSocketException;
	 

}
