package com.org.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.org.exception.SvcException;

@Repository
public class CommonDao extends BaseDao {
	
	/**
	 * @param sql
	 * @param params
	 * @param secretColumn 加密字段列表。指定的列将被加密
	 * @return
	 * @throws SvcException
	 */
	public JSONObject querySingle(String sql, Map<Integer, Object> params, List<String> secretColumn)
			throws SvcException {
		JSONObject jo = null;
		try {
			JSONArray list = queryList(sql, params, secretColumn);
			if (list.size() > 1) {
				throw new SvcException(
						"Common Dao : result counts more than single");
			}
			if (list.size() <= 0) {
				return null;
			}
			return list.getJSONObject(0);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return jo;
	}

	public <T> T querySingle(Class<T> entityClass, String sql, Map<Integer, Object> params) throws SvcException {
		T entity = null;
		try {
			entity = entityClass.newInstance();
			T res = queryByT(sql, params, entity);
			return res;
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONArray queryJSONArray(String sql, Map<Integer, Object> params, List<String> secretColumn) {
		JSONArray list = new JSONArray();
		try {
			list = queryList(sql, params, secretColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return list;
	}

	public JSONArray queryJSONArray(String sql, List<String> secretColumn) {
		JSONArray list = null;
		try {
			list = queryList(sql, null, secretColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * @param entityClass
	 * @param sql
	 * @param params
	 *            ?,?,? {1:"...", 2:"...", 3:"..."}
	 * @return
	 */
	public <T> List<T> queryList(Class<T> entityClass, String sql,
			Map<Integer, Object> params) {
		T entity = null;
		try {
			entity = entityClass.newInstance();
			return queryListByT(sql, params, entity);
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param sql
	 * @param params
	 *            ?,?,? {1:"...", 2:"...", 3:"..."}
	 * @return
	 * @return
	 * @throws SQLException
	 * @throws SvcException
	 */
	public synchronized <T> boolean addSingle(String sql, Map<Integer, Object> params) {
		java.sql.Connection conn = getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			setStatmentParams(ps, params);
			ps.execute();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		} finally {
			try {
				if(conn != null) {
					conn.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * @param sql
	 * @param params
	 * @throws SQLException
	 */
	public synchronized boolean update(String sql, Map<Integer, Object> params)  {
		java.sql.Connection conn = getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			setStatmentParams(ps, params);
			ps.executeUpdate();
			conn.commit();
			return true;
		} catch (SQLException e1) {
			e1.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
		} finally {
			try {
				if(conn != null) {
					conn.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public JSONObject isExist(String sql, Map<Integer, Object> params, List<String> secretColumn) {
		JSONObject user = null;
		try {
			user = querySingle(sql, params, secretColumn);
		} catch (SvcException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	/**
	 * 事务插入。建议不大于10000条
	 * 参考backup文件，关于表不支持事务的解决方案
	 * @param sql
	 * @param paramsList
	 */
	public boolean transactionInsert(String sql, List<Map<Integer, Object>> paramsList){
		java.sql.Connection conn = getConnection();
		
		PreparedStatement ps = null;
		try {
			for (int i = 0; i < paramsList.size(); i++) {
				ps = conn.prepareStatement(sql);
				setStatmentParams(ps, paramsList.get(i));
				ps.executeUpdate();
			}
			conn.commit();
			return true;
		} catch (SQLException e1) {
			e1.printStackTrace();
			
			if(conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return false;
		} finally {
			try {
				if(conn != null) {
					conn.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Log log = LogFactory.getLog(CommonDao.class);
}
