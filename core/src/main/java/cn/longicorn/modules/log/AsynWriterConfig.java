package cn.longicorn.modules.log;

public interface AsynWriterConfig {

	//消费线程中缓存的页记录上限，达到该上限必须处理
	public static int BUNDLE_MAX_SIZE = 100;

	//消费线程中缓存的页定时处理时间，如果有数据，定时时间到达必须处理
	public static int FLUSH_INTERVAL = 1; //second

}
