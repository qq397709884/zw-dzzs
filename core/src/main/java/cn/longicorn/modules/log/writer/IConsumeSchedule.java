package cn.longicorn.modules.log.writer;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;

public interface IConsumeSchedule extends Runnable {
	
	public void consume() throws Exception;

	public void setQueue(BlockingQueue<Serializable> queue);

	public void tryStop();
	
	/**
	 * 设置Consumer的名称
	 */
	public void setName(String name);

	/**
	 * 获取Consumer的名称
	 */
	public String getName();

}