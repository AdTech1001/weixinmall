package com.org.services;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.Connection;
import com.org.common.CommonConstant;

/**
 * ����Դ����
 * @author Administrator
 *
 */
public class DataSourceContainer {
	protected Map<String, Connection<?>> sourceMap= new HashMap<String, Connection<?>>();
	protected static Connection<?> currentConnection = null;

	/**
	 * �л���ͬ����Դ����
	 * @param dataSourceId
	 * @return
	 */
	public Connection<?> switchConnectionType(String connectionName){
		if(sourceMap.containsKey(connectionName)){
			Connection<?> ds = sourceMap.get(connectionName);
			if(ds != null){
				//ConnectionManager.getInstance().setDataSource(ds);
				currentConnection = ds;
				log.info("�л�����Դ�������ͳɹ�:" + connectionName);
			} else {
				log.info("�л�����Դ��������ʧ�ܣ�����������Դ:" + connectionName);
			}
		}
		return currentConnection;
	}
	
	public Connection<?> getCurrentConnection(){
		if(currentConnection == null){
			currentConnection = sourceMap.get(CommonConstant.DB_MYSQL);
		}
		return currentConnection;
	}
	
	public Connection<?> getConnection(String key){
		return sourceMap.get(key);
	}
	
	public void initAllDataSource(){
//		HikaricpDataSourceService.getInstance().loadDataSource(CommonConstant.DB_HIKARICP, null);
//		MongodbDataSourceService.getInstance().loadDataSource(CommonConstant.DB_MONGO, null);
		HikaricpMysqlDataSourceService.getInstance().loadDataSource(CommonConstant.DB_MYSQL, null);
	}
	
	public static DataSourceContainer getInstance(){
		if(container == null){
			container = new DataSourceContainer();
		}
		return container;
	}
	
	public void store(String connectionName, Connection<?> con) {
		sourceMap.put(connectionName, con);
	}
	
	private DataSourceContainer(){}
	private Log log = LogFactory.getLog(DataSourceContainer.class);
	private static DataSourceContainer container = null;

	
}
