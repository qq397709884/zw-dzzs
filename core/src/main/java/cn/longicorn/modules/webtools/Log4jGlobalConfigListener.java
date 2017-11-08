package cn.longicorn.modules.webtools;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import cn.longicorn.modules.utils.Log4jWebConfigurer;

public class Log4jGlobalConfigListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent event) {
		Log4jWebConfigurer.initLogging(event.getServletContext());
	}

	public void contextDestroyed(ServletContextEvent event) {
		Log4jWebConfigurer.shutdownLogging(event.getServletContext());
	}

}
