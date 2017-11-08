package cn.longicorn.modules.log.writer;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import cn.longicorn.modules.annotation.NotThreadSafe;

/**
 * 准备写入目标存储区域的数据集合，注意应该由一个线程持有，
 * 不支持多线程访问。
 */
@NotThreadSafe
public class RecordBundle {

	private List<Serializable> records;

	private int processTimes = 0;

	private int size;

	private Date nextFlushTime;

	private int maxSize;

	private int flushInterval;

	public RecordBundle(int flushInterval, int maxSize) {
		this.flushInterval = flushInterval;
		this.maxSize = maxSize;
		this.records = new LinkedList<Serializable>();
	}

	public List<Serializable> getRecords() {
		return records;
	}

	public int size() {
		return size;
	}

	public Date getFlushTime() {
		return nextFlushTime;
	}

	public void reset() {
		records.clear();
		processTimes = 0;
		size = 0;
	}

	public void add(Serializable node) {
		if (size++ == 0) {
			setNextFlushTimeFromNow();
		}
		records.add(node);
	}

	/**
	 * 持有的数据是否超时，超时则必须写出
	 */
	public boolean timeout() {
		return size() > 0 && getFlushTime().before(new Date());
	}

	public boolean overflow() {
		return size() >= maxSize;
	}

	public boolean needFlush() {
		return timeout() || overflow();
	}

	public String getRecordClassCanonicalName() {
		return size() > 0 ? records.get(0).getClass().getCanonicalName() : "";
	}

	public void addProcessTimes() {
		processTimes++;
	}

	public int getProcessTimes() {
		return processTimes;
	}

	private void setNextFlushTimeFromNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.SECOND, flushInterval);
		nextFlushTime = calendar.getTime();
	}

}
