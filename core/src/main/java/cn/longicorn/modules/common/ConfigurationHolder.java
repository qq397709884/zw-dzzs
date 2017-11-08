package cn.longicorn.modules.common;

import cn.longicorn.modules.utils.PropertiesLoader;

/**
 * 新版ConfigurationHolder,基于Properties文件而非XML，
 * 不再依赖Apache Common Configuration这么庞大的库了。
 *
 * 约束：
 * 1 对Spring profile的支持仅限于通过System property方式获取当前激活的profile
 * 2 仅支持固定的性文件名，不能通过配置或参数传递任意的文件名
 * 3 Spring profile支持，支持development、sandbox以及其他。
 *
 * @author zhuchanglin
 * @since 2013-06-11
 */
public class ConfigurationHolder {

    /** 文件内容加载到Properties中后，将被关闭释放 */
    private PropertiesLoader p;

    private static class SingletonHolder {
        private static ConfigurationHolder uniqueInstance;
        private static String APP_PROPERTIES_FILE_NAME = "application.properties";
        private static String APP_PROPERTIES_FILE_NAME_DEV = "application.dev.properties";
        private static String APP_PROPERTIES_FILE_NAME_SANDBOX = "application.sandbox.properties";

        static {
            uniqueInstance = new ConfigurationHolder();
            init();
        }

        static void init() {
            String activeProfile = System.getProperty("spring.profiles.active");
            if ("development".equalsIgnoreCase(activeProfile)) {
                uniqueInstance.p = new PropertiesLoader(APP_PROPERTIES_FILE_NAME, APP_PROPERTIES_FILE_NAME_DEV);
            } else if ("sandbox".equalsIgnoreCase(activeProfile)) {
                uniqueInstance.p = new PropertiesLoader(APP_PROPERTIES_FILE_NAME, APP_PROPERTIES_FILE_NAME_SANDBOX);
            } else {
                uniqueInstance.p = new PropertiesLoader(APP_PROPERTIES_FILE_NAME);
            }
        }
    }

    public synchronized static void reload() {
        SingletonHolder.init();
    }

    public static PropertiesLoader getInstance() {
        return SingletonHolder.uniqueInstance.p;
    }

}
