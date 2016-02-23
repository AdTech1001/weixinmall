package com.org.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;

import com.org.caches.CacheManager;
import com.org.container.CommonContainer;
import com.org.utils.PropertyUtil;
import com.org.wx.utils.WxUtil;

public class ContextLoaderListener implements ServletContextListener{

	private static Log log = LogFactory.getLog(ContextLoaderListener.class);
	
	private ContextLoader contextLoader;
	
	public void contextDestroyed(ServletContextEvent arg0) {
		// 取消timer
		WxUtil.cancelAutoRun();
		//
		if (this.contextLoader != null) {
			this.contextLoader.closeWebApplicationContext(arg0.getServletContext());
			//KestrelSub.getInstance().stop();
		}
	}
	
	protected ContextLoader createContextLoader() {
		return new ContextLoader();
	}

	public ContextLoader getContextLoader() {
		return contextLoader;
	}

	public void contextInitialized(ServletContextEvent arg0) {
		log.info("Integrate Smp With Spring Container Begin....");
		
		ServletContext servletContext =	arg0.getServletContext();
	    PropertyUtil.initProperties(servletContext);
	    CommonContainer.saveContext(servletContext);
	    
		/* 6.init spring context */
		this.contextLoader = createContextLoader();
		//WebApplicationContext context = this.contextLoader.initWebApplicationContext(servletContext);
		this.contextLoader.initWebApplicationContext(servletContext);
		
		//SpringUtil.initApplicationContext(context);
		/* 7.init smp context */
//		SmpContext sc = SmpContext.getInstance();
//		sc.initApplicationContext(context,servletContext);	
	    /* 8. load cache */
	   
	    /*10. start socket server */
//	    IServerSocket serverSocket = (IServerSocket)sc.getBean(CT.SPRING_BEANNAME_JNIOSERVERSOCKET);//SPRING_BEANNAME_JNIOSERVERSOCKET,SPRING_BEANNAME_SERVERSOCKET
//	    serverSocket.start();
		
		log.info("Integrate Smp With Spring Container End....");
		
		/*11. start kestrel message */
		//KestrelSub.getInstance().start();
		
		/*11. test data 
			MongoDbTestServcie mongodbSvc = (MongoDbTestServcie)sc.getBean("MongoDbTestSvc");
			mongodbSvc.test();
		*/
		
	    // 开启定时获取token任务
	    WxUtil.autoRun();
		
	    // 初始化聊天室房间信息
	    // RoomContainer.initRoom();
	    
	    // 初始化缓存信息
	    CacheManager.getInstance().initAllContainer();
	    
		/*12. init guice with mybatis */
		/*log.info("Integrate Smp With Guice Container Begin....");
		sc.initGuice(GuiceHelper.createInjector(new SmpMyBatisModule(CT.SMP_GUICE_DSTYPE_C3P0,CT.SMP_GUICE_MYBATIS_EID)));
		log.info("Integrate Smp With Guice Container End....");
		
		try {
			TestSvc testSvc = (TestSvc)SmpContextUtils.getSmpContext().getInstance(TestSvc.class);
			testSvc.test(StringUtil.getUUIDValue());	
		} catch (SmpException e) {
			e.printStackTrace();
		}*/
		// 初始化数据源
		//DataSourceContainer.getInstance().initAllDataSource();
	}
	
	

}
