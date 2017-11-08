package cn.longicorn.modules.log.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cn.longicorn.modules.log.AsynWriterConfig;
import cn.longicorn.modules.utils.Threads;

public class ConsumeSchedule implements IConsumeSchedule {

	private static final Logger logger = LoggerFactory.getLogger(ConsumeSchedule.class);

	private BlockingQueue<Serializable> queue;

	private RecordBundle bundle;

	private String name;

	private ExecutorService writeWorkExcutorService;

	private volatile boolean isShutdown;

	protected void init() {
		if (queue == null || name == null) {
			logger.error("ConsumerSchedule start error, please set name and queue first.");
			throw new RuntimeException("ConsumerSchedule start error, please check innerElement.");
		}
		bundle = new RecordBundle(AsynWriterConfig.FLUSH_INTERVAL, AsynWriterConfig.BUNDLE_MAX_SIZE);
		writeWorkExcutorService = Executors.newSingleThreadExecutor();
	}

	@Override
	public void run() {
		init();
		logger.debug("{}启动", getName());
		try {
			while ((!Thread.currentThread().isInterrupted() && !isShutdown) || queue.size() > 0) {
				consume();
			}
			logger.debug("{}收到退出信号", getName());
		} catch (InterruptedException e) {
			logger.debug("{}收到中断异常信号", getName());
		} finally {
			flush();
			Threads.gracefulShutdown(writeWorkExcutorService, 10, 2, TimeUnit.SECONDS);
		}
		logger.debug("{}已退出", getName());
	}

	@Override
	public void consume() throws InterruptedException {
		if (bundle.needFlush()) {
			flush();
		}
		Serializable node = queue.poll(100, TimeUnit.MICROSECONDS);
		if (node != null) {
			bundle.add(node);
		}
	}

	@Override
	public void setQueue(BlockingQueue<Serializable> queue) {
		this.queue = queue;
	}

	@Override
	public void tryStop() {
		isShutdown = true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	private void flush() {
		if (bundle.size() <= 0) {
			return;
		}
		try {
			IWriteWorker writeWorker = new WriteWorker();
			//如果bindBundle的具体实现未对bundle深度克隆，则写入线程必须同步执行，否则bundle需要同步
			//执行，即下面的get方法等待。否则，当前消费线程会继续消费，破坏bundle对象，导致数据丢失。
			writeWorker.bindBundle(bundle);
			writeWorkExcutorService.submit(writeWorker).get(60, TimeUnit.SECONDS);
			bundle.reset();
		} catch (InterruptedException e) {
			logger.warn("{}中任务执行失败，操作被中断，不再恢复重试", getName());
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			logger.warn("{}中任务执行异常，{}", getName(), e.getMessage(), e);
		} catch (TimeoutException e) {
			logger.warn("{}中任务执行超时，{}", getName(), e.getMessage());
		}
	}

}
