package com.org.socket;

import com.org.exception.SSocketConnTimeoutException;
import com.org.exception.SSocketException;

/**
 * 阻塞和非阻塞客户端由此接口派生
 * 
 * @since 2011-09-08 10:09:09
 * 
 */
public interface ISocket {
	
	/**
	 * @param hostName 主机
	 * @param port 端口
	 * @param timeoutSeconds 秒为单位
	 * @throws SSocketConnTimeoutException
	 * @throws EaiRuntimeException
	 */
	public void connect(String hostName,int port,int timeoutSeconds) throws SSocketConnTimeoutException,SSocketException;
	
	/**
	 * @return 读取字节数组
	 * 
	 * @throws SSocketException
	 */
	public byte[] read() throws SSocketException;
	
	/**
	 * 
	 * @param data 待发送的数据
	 * 
	 * @param flush 是否一次发送
	 * 
	 * @throws SSocketException
	 */
	public void write(byte[] data,boolean flush) throws SSocketException;
	
	public void close()throws SSocketException;
	
	public boolean isClose()throws SSocketException;
	

}
