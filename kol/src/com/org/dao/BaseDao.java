package com.org.dao;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.org.model.reflect.ReflectDbModel;
import com.org.util.StringUtil;
import com.org.utils.ByteUtil;
import com.org.utils.DesUtil;

// TODO 重新做分页
public class BaseDao {
	private static HikaricpMysql dataSource = HikaricpMysql.getInstance();
	
	/**
	 * 获取connection，同时关闭自动提交
	 * @return
	 */
	protected java.sql.Connection getConnection(){
		java.sql.Connection conn = dataSource.getConnection();
		return conn;
	}


	/**
	 * jdbc 存在中文乱码
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	protected java.sql.Connection getJDBCConnection() {
		try {
			Class.forName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
			String url="jdbc:mysql://114.80.215.196:3306/sq_zhouman10?user=sq_zhouman10&password=zhouman10";
			java.sql.Connection conn = DriverManager.getConnection(url);
			conn.setAutoCommit(false);
			return conn;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	protected <T> List<T> queryListByT(String sql, Map<Integer, Object> params, T entity)
			throws SQLException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		
		java.sql.Connection connection = null;
		List<T> list = new ArrayList<T>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try{
			connection = getConnection();
			ps = connection.prepareStatement(sql);
			if(params != null) {
				setStatmentParams(ps, params);
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			// 列数
			int columnCounts = rsmd.getColumnCount();
			//
			ReflectDbModel model = new ReflectDbModel();
			Method m = null;
			while (rs.next()) {
				// 这个地方相当于每用一次就new一次,否则数据会覆盖上一次的数据
				for (int i = 1; i <= columnCounts; i++) {
					initReflectDbModel(rs, model, i);
					if (model.getValue() != null && model.getValue() != "") {
						try {
							m = entity.getClass().getDeclaredMethod("set" + model.getKey(), model.getValue().getClass());
							m.invoke(entity, model.getValue());
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							System.out.println(e.getMessage() + ": NoSuchMethodException");
						}
					}
				}
				list.add(entity);
			}
		}finally{
			releaseAll(rs, ps, connection);
		}			
		return list;
	}
	
	protected <T> T queryByT(String sql, Map<Integer, Object> params, T entity)
			throws SQLException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		
		java.sql.Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try{
			connection = getConnection();
			ps = connection.prepareStatement(sql);
			setStatmentParams(ps, params);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			// 列数
			int columnCounts = rsmd.getColumnCount();
			//
			ReflectDbModel model = new ReflectDbModel();
			Method m = null;
			while (rs.next()) {
				// 这个地方相当于每用一次就new一次,否则数据会覆盖上一次的数据
				for (int i = 1; i <= columnCounts; i++) {
					initReflectDbModel(rs, model, i);
					if (model.getValue() != null && model.getValue() != "") {
						try {
							m = entity.getClass().getDeclaredMethod("set" + model.getKey(), model.getValue().getClass());
							m.invoke(entity, model.getValue());
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							System.out.println(e.getMessage() + ": NoSuchMethodException");
						}
					}
				}
			}
		}finally{
			releaseAll(rs, ps, connection);
		}			
		return entity;
	}
	
	/**
	 * 
	 * @param sql
	 * @param params
	 * @param collumToUpper 是否遵守驼峰
	 * @param secretColumn 是否有加密列的需求
	 * @return
	 * @throws SQLException
	 */
	protected JSONArray queryList(String sql, Map<Integer, Object> params, boolean collumToUpper, List<String> secretColumn) throws SQLException{
		JSONArray list = new JSONArray();
		java.sql.Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try{
			connection = getConnection();
			ps = connection.prepareStatement(sql);
			if(params != null){
				setStatmentParams(ps, params);
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			// 列数
			int columnCounts = rsmd.getColumnCount();
			//
			JSONObject jo = null;
			String key = "";
			String value = null;
			while (rs.next()) {
				jo = new JSONObject();
				for (int i = 1; i <= columnCounts; i++) {
					//  转实例名
					key = StringUtil.toEntityName(rsmd.getColumnName(i), collumToUpper);
					value = (rs.getObject(i) == null) ? "" : rs.getObject(i).toString();
					if(secretColumn != null && secretColumn.contains(key)){
						// 如果这列需要加密 
						byte[] valueByte;
						try {
							valueByte = DesUtil.encryptMode(value.getBytes("UTF-8"));
							// 从数据库取出前， 先执行加密
							value = ByteUtil.bytes2HexStr(valueByte);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							
						}
						
					}
					jo.put(key, value);
				}
				list.add(jo);
			}
		}finally{
			releaseAll(rs, ps, connection);
		}			
		return list;		
	}

	private void releaseAll(ResultSet rs, PreparedStatement ps,
			java.sql.Connection connection) throws SQLException {
		if(rs != null){
			rs.close();
		}
		if(ps != null){
			ps.close();
		}
		if(connection != null){
			connection.close();
		}
	}

	protected JSONArray queryList(String sql, Map<Integer, Object> params, List<String> secretColumn) throws SQLException{
		return queryList(sql, params, true, secretColumn);		
	}
	
	protected void setStatmentParams(PreparedStatement ps, Map<Integer, Object> params) throws SQLException{
		for (Iterator<Integer> iterator = params.keySet().iterator(); iterator
				.hasNext();) {
			Integer key = iterator.next();
			ps.setObject(key, params.get(key));
		}
	}

	protected static void initReflectDbModel(ResultSet rs, ReflectDbModel model,
			int index) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		String key = rsmd.getColumnName(index);
		// 转实例名
		key = StringUtil.toEntityName(rsmd.getColumnName(index), false);
		
		String paramType = rsmd.getColumnClassName(index);
		Object value = rs.getObject(index);

		// 首字母大写
		key = String.valueOf(key.charAt(0)).toUpperCase() + key.substring(1);
		paramType = paramType.substring(paramType.lastIndexOf(".") + 1);

		model.setKey(key);
		model.setParamType(paramType);
		model.setValue(value);
	}
}
