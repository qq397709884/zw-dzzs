package cn.longicorn.modules.log2;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import cn.longicorn.modules.log.AsynWriterTemplate;
import cn.longicorn.modules.log.writer.StoreServiceFactory;
import cn.longicorn.modules.test.data.DbcpHelper;
import cn.longicorn.modules.utils.Threads;

public class AsynWriteTest {

	private static final Logger logger = LoggerFactory.getLogger(AsynWriteTest.class);

	private final static int DEFAULT_WRITER_COUNT = 2;

	private final static int DEFAULT_CONSUMERS_PER_WRITER = 4;

	private AsynWriterTemplate writerTemplate;

	public void init() {
		logger.debug("启动消息日志跟踪线程池");
		writerTemplate = new AsynWriterTemplate(DEFAULT_WRITER_COUNT, DEFAULT_CONSUMERS_PER_WRITER);
	}

	public void stop() {
		logger.debug("关闭消息日志跟踪线程池");
		if (writerTemplate != null) {
			writerTemplate.stop();
		}
		logger.debug("关闭消息日志跟踪线程池完成");
	}

	@Test
	public void mainTest() {
		MonitorLogDao dao = new MonitorLogDao();
		StoreServiceFactory.registerStoreService(MonitorLog.class.getCanonicalName(), dao);
		AsynWriteTest tester = new AsynWriteTest();
		tester.test();
		Threads.sleep(1, TimeUnit.SECONDS);
		tester.stop();
		shutdownDS();
	}

	public void test() {
		init();
		final int count = 1;
		MonitorLog[] logs = new MonitorLog[count];
		for (int i = 0; i < count; i++) {
			logs[i] = new MonitorLog();
			logs[i].setId(i);
			logs[i].setMessage(new StringBuffer("测试信息").append(i).toString());
		}

		for (MonitorLog log : logs) {
			writerTemplate.write(log);
		}
	}

	public void shutdownDS() {
		try {
			System.out.println(DbcpHelper.getDataSourceStats());
			DbcpHelper.shutdownDataSource();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
