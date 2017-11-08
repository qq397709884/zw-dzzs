package cn.longicorn.modules.log.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import cn.longicorn.modules.utils.Assert;

public class WriteWorker implements IWriteWorker {

	private static final Logger logger = LoggerFactory.getLogger(WriteWorker.class);

	/** 一个bundle重试处理的次数最多为10个，超过如果仍然未成功处理，就丢弃  */
	private static final int MAX_PROCESS_TIMES_WITH_A_BUNDLE = 10;

	private RecordBundle bundle;

	@Override
	public void bindBundle(RecordBundle bundle) {
		Assert.notNull(bundle);
		//TODO 这里最好深度克隆一份，允许参数bundle引用的内存立即释放给其他线程继续使用
		this.bundle = bundle;
	}

	@Override
	public void run() {
		write();
	}

	@Override
	public void write() {
		if (bundle.getProcessTimes() >= MAX_PROCESS_TIMES_WITH_A_BUNDLE) {
			logger.error("数据集存储失败，已经超过最大尝试次数");
			return;
		}
		if (bundle.size() > 0 || (bundle.getRecords() != null && bundle.size() > 0)) {
			Serializable[] contents = new Serializable[bundle.getRecords().size()];
			bundle.getRecords().toArray(contents);
			bundle.addProcessTimes();
			for (Serializable content : contents) {
				//逐条保存，每次都需要查询对应的存储管理实体
				String entityClassName = content.getClass().getCanonicalName();
				try {
					StoreServiceFactory.getStoreService(entityClassName).save(content);
				} catch (java.lang.Throwable e) {
					logger.error("{}对象写入失败, 丢弃, 失败原因：{}", entityClassName, e.getMessage());
				}
			}
		}
	}

}
