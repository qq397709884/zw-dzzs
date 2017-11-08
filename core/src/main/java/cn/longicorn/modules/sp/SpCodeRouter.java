package cn.longicorn.modules.sp;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SpCodeRouter {

	private final Map<SpCode, String> routeTable = new TreeMap<>();
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock r = rwl.readLock();
	private final Lock w = rwl.writeLock();

	/**
	 * 当帐号重新分配的时候，可以重新调rebuild
	 * @param accounts		注册帐号列表
	 */
	public void rebuild(List<SpAccount> accounts) {
		w.lock();
		try {
			routeTable.clear();
			for (SpAccount account : accounts) {
				List<SpCode> codes = account.buildAllSpCode();
				for (SpCode code : codes) {
					routeTable.put(code, account.accountCode());
				}
			}
		} finally {
			w.unlock();
		}
	}

	public boolean isExsit(SpCode spCode) {
		String ret = null;
		r.lock();
		try {
			ret = routeTable.get(spCode);
		} finally {
			r.unlock();
		}
		return ret != null;
	}

	public String getAccount(SpCode spCode) {
		String ret = null;
		r.lock();
		try {
			ret = routeTable.get(spCode);
		} finally {
			r.unlock();
		}
		return ret;
	}

}
