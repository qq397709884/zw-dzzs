package cn.longicorn.modules.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtilsTest {

	private static final Logger logger = LoggerFactory.getLogger(LogUtilsTest.class);

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		LogUtils.debug(null, new RuntimeException("debug"));
		LogUtils.info(null, new RuntimeException("info"));
		LogUtils.warn(logger, new RuntimeException("warn"));
		LogUtils.error(logger, new RuntimeException("error"));
		LogUtils.error(logger, new RuntimeException("error"), "用户保存失败");
	}

}
