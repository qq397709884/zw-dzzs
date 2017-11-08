package cn.longicorn.modules.log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.longicorn.modules.annotation.ThreadSafe;
import cn.longicorn.modules.log.writer.AsynWriter;
import cn.longicorn.modules.log.writer.IAsynWriter;

/**
 * Writer Pool管理以及缺省的任务dispatch算法
 */
@ThreadSafe
public class AsynWriterTemplate {

	private final List<IAsynWriter> writers = new ArrayList<IAsynWriter>();

	public AsynWriterTemplate(int writerCount, int consumersCountPerWriter) {
		for (int i = 0; i < writerCount; i++) {
			String writerName = "asynwriter" + i;
			IAsynWriter writer = new AsynWriter(writerName, consumersCountPerWriter);
			writers.add(writer);
		}
	}

	public void write(Serializable content) {
		IAsynWriter writer = getWriter(content);
		if (writer == null) {
			throw new IllegalStateException("writers未初始化，无法获得writer");
		}
		writer.write(content);
	}

	public synchronized void stop() {
		for (IAsynWriter writer : writers) {
			writer.stop();
		}
	}

	/**
	 * 该方法可被子类重载，替换掉该缺省算法
	 * @param content	待写入的内容, 具体算法可以以此作为writer选择条件
	 * @return	获取的writer
	 */
	protected IAsynWriter getWriter(Serializable content) {
		if (writers.size() == 0) {
			return null;
		}
		int hashCode = content.hashCode();
		int bucket = hashCode % writers.size();
		return writers.get(Math.abs(bucket));
	}

}
