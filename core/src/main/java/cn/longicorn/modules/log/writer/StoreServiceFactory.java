package cn.longicorn.modules.log.writer;

import java.util.HashMap;
import java.util.Map;

public final class StoreServiceFactory {

	private static Map<String, IStoreService> registeredStoreService = new HashMap<String, IStoreService>();

	public static IStoreService getStoreService(String entityClassName) {
		IStoreService service = registeredStoreService.get(entityClassName);
		if (service == null) {
			throw new RuntimeException("找不到可管理" + entityClassName + "对象的StoreService实例，请先注册它。");
		}
		return service;
	}

	/**
	 * 注册StoreService, 使用方根据entityClassName获得可以存储它的Service对象
	 * @param entityClassName		被注册的StoreService处理的实体类名称
	 * @param service				被注册的StoerService对象实例
	 */
	public static void registerStoreService(String entityClassName, IStoreService service) {
		registeredStoreService.put(entityClassName, service);
	}

}
