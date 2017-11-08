package cn.longicorn.modules.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 标准化异常输出的格式
 * 警告以上级别，输出错误的时候会输出错误栈
 * @author zhuchanglin
 * 
 * @deprecated As of release 2.0.0-SNAPSHOT, replaced by local logger instance.
 */
@Deprecated  
public class LogUtils {

	/*
	 * Default info level logger defined in log4j.properties file.
	 */
	private static String DEFAULT_INFO_LOGGER = "rootLogger";

	public static void warn(Logger log, Throwable e, String... strings) {
		log = checkLogger(log);
		log.warn(getPattern(strings), e.getClass().getCanonicalName(), e.getMessage(), e);
	}

	public static void error(Logger log, Throwable e, String... strings) {
		log = checkLogger(log);
		log.error(getPattern(strings), e.getClass().getCanonicalName(), e.getMessage(), e);
	}

	public static void info(Logger log, Throwable e, String... strings) {
		log = checkLogger(log);
		log.info(getPattern(strings), e.getClass().getCanonicalName(), e.getMessage());
	}

	public static void debug(Logger log, Throwable e, String... strings) {
		log = checkLogger(log);
		log.debug(getPattern(strings), e.getClass().getCanonicalName(), e.getMessage());
	}

	private static Logger rootLog() {
		return LoggerFactory.getLogger(DEFAULT_INFO_LOGGER);
	}

	private static Logger checkLogger(Logger log) {
		return log == null ? rootLog() : log;
	}

	private static String getPattern(String[] strings) {
		StringBuilder pattern = new StringBuilder();
		if (strings.length > 0) {
			pattern.append(strings[0]).append(", ");
		}
		pattern.append("异常类型:{}, 错误信息:{}");
		return pattern.toString();
	}

}
