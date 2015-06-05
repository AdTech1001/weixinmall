package com.org.remote;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.Connection;
import com.org.common.CommonConstant;
import com.org.utils.JSONUtils;
import com.org.utils.RequestUtils;

// TODO 
public class RemoteServiceImpl implements RemoteService {
	//private final static DataSourceContainer dsc = DataSourceContainer.getInstance();
	private Log log = LogFactory.getLog(RemoteServiceImpl.class);

	@Override
	public String access(String request) {
		// һ����������Դ

		JSONObject requestJson = JSONUtils.getJsonFromString(request);
		
		JSONObject result = RequestUtils.precheckParmas(requestJson);
		if(! result.getString(CommonConstant.RESP_CODE).equals("10000")){
			log.info(result.getString(CommonConstant.RESP_CODE) +": " + result.getString(CommonConstant.RESP_MSG));
			return result.toString();
		}

		String identityFlag = requestJson.getString("identityFlag");
		// ������ݣ�·�ɵ�ָ�������ݿ�
		Connection con = null;
//		if(identityFlag.equals("monitor")){
//			con = dsc.getConnection(CommonConstant.DB_MONGO);
//		} else {
//			con = dsc.getConnection(CommonConstant.DB_HIKARICP);
//		}
		
		// ����ִ�в�ѯ
		result = executeQuery(requestJson, con);
		
		// ������������
		return result.toString();
	}

	private JSONObject executeQuery(JSONObject requestJson, Connection con) {
		// TODO 
		return null;
	}

}
