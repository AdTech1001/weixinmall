package com.org.dao;

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
 * hikaricp数据源服务－oracle
 *
 */
public class HikaricpOracle {
	
	public static HikaricpOracle getInstance(){
		if(hds == null){
			hds = new HikaricpOracle();
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

	private static HikaricpOracle hds = null;
	
	private HikaricpOracle(){
		InputStream pin = null;
		Properties pro = new Properties();
		String fileName = "/WEB-INF/config/oracle_db.properties";
		try {
			pin = CommonContainer.getServletContext().getResourceAsStream(fileName);
			pro.load(pin);
		} catch (IOException e) {
			System.out.println("load properties error : " + fileName);
		}
		
		String driverClassName = pro.getProperty("hikaricp.jdbc.driverClassName");
		String jdbcUrl = pro.getProperty("hikaricp.url");
		String user = pro.getProperty("hikaricp.username");
		String password = pro.getProperty("hikaricp.password");
		String minimumIdle = pro.getProperty("hikaricp.minimumIdle");
		String maximumPoolSize = pro.getProperty("hikaricp.maximumPoolSize");
		String connectionTestQuery = pro.getProperty("hikaricp.connectionTestQuery");
		String connectionTimeout = pro.getProperty("hikaricp.connectionTimeout");
		String idleTimeout = pro.getProperty("hikaricp.idleTimeout");
		String maxLifetime = pro.getProperty("hikaricp.maxLifetime");
		
		HikariConfig config = new HikariConfig();

		config.setMaximumPoolSize(100);
		//config.setDataSourceClassName(driverClassName); mysql 
		config.setDriverClassName(driverClassName);
		config.setJdbcUrl(jdbcUrl);
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
