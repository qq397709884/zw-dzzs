package cn.longicorn.modules.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.longicorn.modules.log.writer.IStoreService;
import cn.longicorn.modules.log.writer.StoreServiceFactory;

/**
 * 支持Springframework的StoreSerive注册器，在spring的applicationContext配置文件中定义
 * 该bean。它将自动注册所有IStoreService的派生类到StoreServiceRegister中。
 * 
 * <bean id="storeServiceRegister" class="cn.longicorn.modules.log.StoreSeriveRegister" />
 */
public final class StoreSeriveRegister implements ApplicationContextAware, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(StoreSeriveRegister.class);

	private ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, IStoreService> services = context.getBeansOfType(IStoreService.class);
		if (services != null && !services.isEmpty()) {
			for (String serviceName : services.keySet()) {
				IStoreService service = services.get(serviceName);
				StoreServiceFactory.registerStoreService(service.getEntityClassName(), service);
				logger.debug("Regsiter store-service {} for entity class {}", serviceName, service.getEntityClassName());
			}
		}
	}

}
