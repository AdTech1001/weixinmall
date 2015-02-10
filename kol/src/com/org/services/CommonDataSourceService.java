package com.org.services;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.Connection;

/**
 * hikaricp����Դ����
 * @author Administrator
 *
 */
public abstract class CommonDataSourceService {
	/**
	 * ���¼�������Դ
	 * @param dataSourceName
	 * @param extendsParam ������Ϣ��
	 */
	public Connection loadDataSource(String dataSourceName, JSONObject extendsParam){
		// ִ��reload����
		log.info("����"+dataSourceName+"����Դ");
		Connection ds = null;
		if(extendsParam == null || extendsParam.isNullObject()){
			ds = loadDefaultDataSource();
		} else {
			// ��extendsParam��������Դ
			ds = loadDataSourceByParam(extendsParam);
		}
		// �洢��ǰsource������
		DataSourceContainer.getInstance().store(dataSourceName, ds);
		return ds;
	}
	
	public abstract Connection loadDataSourceByParam(JSONObject extendsParam);

	public abstract Connection loadDefaultDataSource();
	
	public CommonDataSourceService(Class<?> childClazz){
		System.out.println("new child created : " + childClazz.getName());
		this.childClazzs.add(childClazz);
	}
	private List<Class<?>> childClazzs = new ArrayList<Class<?>>();
	private Log log = LogFactory.getLog(CommonDataSourceService.class);
}
