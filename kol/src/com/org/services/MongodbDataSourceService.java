package com.org.services;

import java.net.UnknownHostException;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.Connection;
import com.org.connection.MongoConnection;

/**
 * mongodb����Դ����
 * @author Administrator
 *
 */
public class MongodbDataSourceService extends CommonDataSourceService{
	
	public static MongodbDataSourceService getInstance(){
		if(mds == null){
			mds = new MongodbDataSourceService();
		}
		return mds;
	}
	
	@Override
	public Connection loadDataSourceByParam(JSONObject extendsParam) {
		log.info("MongodbDataSourceService: ��������ʼ��mongo����");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection loadDefaultDataSource() {
		log.info("MongodbDataSourceService: ����Ĭ��mongo����");
		// TODO
		String host = "172.28.250.86";
		int port = 27017;
		String databaseName = "sd_monitor_db";
		
		MongoConnection con = null;
		try {
			con = new MongoConnection(host, port, databaseName);
			//con.queryList(null, "spop");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return con;
	}
	
	
	private static MongodbDataSourceService mds = null;
	private MongodbDataSourceService(){
		super(MongodbDataSourceService.class);
	}
	private Log log = LogFactory.getLog(MongodbDataSourceService.class);
}
