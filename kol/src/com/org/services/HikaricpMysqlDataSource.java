package com.org.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.org.container.CommonContainer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * hikaricp数据源服务
 * @author Administrator
 *
 */
public class HikaricpMysqlDataSource {
	
	public static HikaricpMysqlDataSource getInstance(){
		if(hds == null){
			hds = new HikaricpMysqlDataSource();
		}
		return hds;
	}
	
	public Connection getConnection(){
		try {
			return template.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static HikaricpMysqlDataSource hds = null;
	
	private HikaricpMysqlDataSource(){
		Properties pro = new Properties();
		try {
			String fileName = "/WEB-INF/config/mysql_db.properties";
			InputStream in = CommonContainer.getServletContext().getResourceAsStream(fileName);
			pro.load(in);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		String driverClassName = pro.getProperty("driverClassName");
		String serverName = pro.getProperty("serverName");
		String port = pro.getProperty("port");
		String databaseName = pro.getProperty("databaseName");
		String user = pro.getProperty("user");
		String password = pro.getProperty("password");
		String connectionTimeout = pro.getProperty("connectionTimeout");
		String maxLifetime = pro.getProperty("maxLifetime");
		String idleTimeout = pro.getProperty("idleTimeout");
		String maximumPoolSize = pro.getProperty("maximumPoolSize");
		String minimumIdle = pro.getProperty("minimumIdle");
		String connectionTestQuery = pro.getProperty("connectionTestQuery");
		
		HikariConfig config = new HikariConfig();

		config.setMaximumPoolSize(100);
		config.setDataSourceClassName(driverClassName);
		config.addDataSourceProperty("serverName", serverName);
		config.addDataSourceProperty("port", port);
		config.addDataSourceProperty("databaseName", databaseName);
		config.addDataSourceProperty("user", user);
		config.addDataSourceProperty("password", password);
		config.addDataSourceProperty("useUnicode", "true");
		config.addDataSourceProperty("characterEncoding", "utf8");
		
		config.setConnectionTimeout(Long.valueOf(connectionTimeout));
		config.setIdleTimeout(Long.valueOf(idleTimeout));
		config.setMaxLifetime(Long.valueOf(maxLifetime));
		config.setMaximumPoolSize(Integer.valueOf(maximumPoolSize));
		config.setMinimumIdle(Integer.valueOf(minimumIdle));
		config.setConnectionTestQuery(connectionTestQuery);
		
		HikariDataSource temp = new HikariDataSource(config);
		this.template = temp;
	}
	private DataSource template;
	//private Log log = LogFactory.getLog(HikaricpMysqlDataSourceService.class);
}
