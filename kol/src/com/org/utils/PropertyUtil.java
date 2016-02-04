package com.org.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
/**
 * 
 * .properties file include 
 *
 */
public class PropertyUtil {
	private static Log log = LogFactory.getLog(PropertyUtil.class);
	
	public static Map<String,Properties>  proMaps = new HashMap<String, Properties>();
	
	public static void initProperties(ServletContext webContext){
		InputStream in = null;
		String coreFile = "/WEB-INF/config/core.properties";
		String simpleCoreFileName = "core";
		try {
			String propertiesMode= "model_type";
			String propertiesPath = "properties_path";
			String propertiesFilePrefix = "properties_prefix";
			String propertiesFileNames = "properties_file_names";
			String connSymbol = "_";
			in = webContext.getResourceAsStream(coreFile);
			if(in != null){
				Properties coreProperty = new Properties();
				coreProperty.load(in);
				proMaps.put(simpleCoreFileName, coreProperty);
				String mode = coreProperty.getProperty(propertiesMode);
				String path = coreProperty.getProperty(propertiesPath);
				String prefix = coreProperty.getProperty(propertiesFilePrefix);
				prefix = StringUtils.isEmpty(prefix) ? "" : prefix + connSymbol;
				
				String files = coreProperty.getProperty(propertiesFileNames);
				if(files != null){
					String[] names = files.split(",");
					for (int i = 0; i < names.length; i++) {
						String file = path + "/" + prefix + names[i].trim()+ connSymbol + mode +".properties";
						String simpleName = names[i].trim();
						InputStream pin = null;
						try{						
							pin = webContext.getResourceAsStream(file);
							if(pin == null){
								file = path + "/" + prefix + names[i].trim()+".properties";
								pin = webContext.getResourceAsStream(file);
							}
							Properties pro = new Properties();
							pro.load(pin);
							proMaps.put(simpleName, pro);
							log.info("加载配置参数文件"+file+"成功....");
						}catch (IOException e) {
							log.info("加载配置参数文件"+file+"失败,原因:"+ e.getMessage());
					    }finally{
							if(pin  != null)
								try {
									pin.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
						}
					}
				}
			}
			log.info("加载配置参数文件"+coreFile+"成功....");
		}catch (IOException e) {
			log.info("加载配置配置参数文件"+coreFile+"失败,原因:"+ e.getMessage());
	    } finally{
			if(in  != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static Properties getProperties(String name){
		if(proMaps != null && proMaps.size() > 0){
			return proMaps.get(name);
		}
		return null;
	}
	
	public static String getValue(String name,String key){
		if(proMaps != null && proMaps.size() > 0){
			Properties pro =  proMaps.get(name);
			if(pro != null && pro.containsKey(key)){				
				return pro.getProperty(key);
			}else{
				log.info("查找文件名["+name+"]失败,原因:找不到对应配置文件");
				return null;				
			}
		}
		return null;
	}
	
	public static void setPropertiesSetValue(String name, String key, String value){
		if(proMaps != null && proMaps.size() > 0){
			Properties pro =  proMaps.get(name);
			if(pro != null){
				pro.setProperty(key, value);
			}else{
				log.info("查找文件名["+name+"]失败,原因:找不到对应配置文件");
			}
		}
	}
	
	public static String getValue(String name,String key,String defaultValue){
		if(proMaps != null && proMaps.size() > 0){
			Properties pro =  proMaps.get(name);
			if(pro != null && pro.containsKey(key)){				
				return pro.getProperty(key,defaultValue);
			}else{
				log.info("查找文件名["+name+"]失败,原因:找不到对应配置文件");
				return defaultValue;				
			}
		}
		return defaultValue;
	}
	
	public static boolean isProductMode(){
		String modeType = getValue("core", "model_type");
		if("product".equalsIgnoreCase(modeType)){
			return true;
		}else {
			return false;
		}
	}
	
	

}
