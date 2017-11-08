package cn.longicorn.modules.log.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import cn.longicorn.modules.annotation.ThreadSafe;
import cn.longicorn.modules.utils.Threads;

@ThreadSafe
public class AsynWriter implements IAsynWriter {

	private static final Logger logger = LoggerFactory.getLogger(AsynWriter.class);

	private BlockingQueue<Serializable> dataQueue;

	private ExecutorService consumerExcutorService;

	private List<IConsumeSchedule> consumers = new ArrayList<IConsumeSchedule>();

	private String name;

	private boolean isShutdown;

	public AsynWriter(String name, int consumersCountPerWriter) {
		this.name = name;
		dataQueue = new LinkedBlockingQueue<Serializable>();
		consumerExcutorService = Executors.newFixedThreadPool(consumersCountPerWriter);
		initConsumer(consumersCountPerWriter);
	}

	private void initConsumer(int consumersCountPerWriter) {
		for (int i = 0; i < consumersCountPerWriter; i++) {
			IConsumeSchedule consumer = new ConsumeSchedule();
			String consumerName = new StringBuffer().append(name).append("_consumer").append(i).toString();
			consumer.setName(consumerName);
			consumer.setQueue(dataQueue);

			consumerExcutorService.submit(consumer);
			consumers.add(consumer);
		}
	}

	@Override
	public synchronized void stop() {
		logger.debug("{}准备退出", getName());
		isShutdown = true;
		for (IConsumeSchedule consumer : consumers) {
			consumer.tryStop();
		}
		Threads.gracefulShutdown(consumerExcutorService, 30, 10, TimeUnit.SECONDS);
		if (dataQueue.size() > 0) {
			logger.warn("{}退出完成，队列剩余记录数{}", getName(), dataQueue.size());
		}
	}
	
	@Override
	public void write(Serializable content) {
		synchronized (this) {
			if (isShutdown) {
				throw new IllegalStateException(name + "已经关闭，不能写入");
			}
		}
		dataQueue.add(content);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}