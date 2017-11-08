package cn.longicorn.modules.utils;

/**
 * 毫秒级的计时工具
 */
public class StopWatch {
	private long startTime;

	public StopWatch() {
		startTime = System.currentTimeMillis();
	}

	public long getMillis() {
		return System.currentTimeMillis() - startTime;
	}

	public void reset() {
		startTime = System.currentTimeMillis();
	}
}
