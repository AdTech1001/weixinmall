package com.org.utils;


public class ClassUtil {
	public static Class<?> getClass(String className){
		Class<?> res = null;
		try {
			res = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	public static Object newInstance(String clazz) {
		try {
			Object o = Class.forName(clazz).newInstance();
			return o;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
