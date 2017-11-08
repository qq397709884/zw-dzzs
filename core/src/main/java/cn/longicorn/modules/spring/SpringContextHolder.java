package cn.longicorn.modules.spring;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * This class store the spring framework's <code>ApplicationContext</code> in
 * a static member, which would be shared by all the class instance in the same
 * Java VM.
 * <p>If want to let the class working correctly, please put the class file in 
 * WEB-INF\classes or package the whole project to a jar file and then put it 
 * in the WEB-INF\jar path. The classload inheritance hierarchy of tomcat 6 is 
 * list below as a reference.
 * <pre>
 *     Bootstrap
 *         |
 *       System
 *         |
 *       Common
 *      /     \
 *   Webapp1  Webapp2 ...
 * </pre>
 * Define method in applicationContext.xml
 * <bean id="springContextHolder" class="cn.longicorn.modules.spring.SpringContextHolder" />
 * @author zhuchanglin
 */
public class SpringContextHolder implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextHolder.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		checkApplicationContext();
		return applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		checkApplicationContext();
		return (T) applicationContext.getBean(name);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T getBean(Class<T> clazz) {
		checkApplicationContext();
		Map beans = applicationContext.getBeansOfType(clazz);
		if (beans != null && !beans.isEmpty()) {
			for (Object o : beans.keySet()) {
				return (T) beans.get(o);
			}
		}
		return null;
	}

	private static void checkApplicationContext() {
		if (applicationContext == null) {
			throw new IllegalStateException("applicaitonContext not set, Please define the SpringContextHolder bean in applicationContext.xml.");
		}
	}
	
}
