package cn.longicorn.modules.data;

import java.util.Map;
import java.util.Map.Entry;

import cn.longicorn.modules.annotation.NotThreadSafe;
import cn.longicorn.modules.data.jdbc.IDialect;

/**
 * SQL构建者, 当前仅生成局部的SQL子句
 */
@NotThreadSafe
public class SQLBuilder {

	private Map<String, String> sortMaps;

	private INamingStrategy namingStrategy;

	private IDialect dbDialect;

	private String sortCache;

	public SQLBuilder(Map<String, String> sortMaps) {
		this.sortMaps = sortMaps;
		this.namingStrategy = new EnhanceNamingStrategy();
	}

	public void setDialect(IDialect dialect) {
		this.dbDialect = dialect;
	}

	public void setNamingStrategy(INamingStrategy namingStrategy) {
		this.namingStrategy = namingStrategy;
	}

	public String build() {
		return getSortConditions();
	}

	public String buildLimitString(String sql, long offset, long maxResults) {
		if (this.dbDialect == null) {
			throw new IllegalStateException("You need set the database dialect for sql builder.");
		}
		return dbDialect.getLimitString(sql, offset, maxResults);
	}

	public String reBuild() {
		reset();
		return build();
	}

	public void reset() {
		sortCache = null;
	}

	public String getSortConditions() {
		if (sortMaps == null) {
			return "";
		}
		if (sortCache != null) {
			return sortCache;
		}
		StringBuilder conditions = new StringBuilder();
		for (Entry<String, String> entry : sortMaps.entrySet()) {
			conditions.append(",").append(namingStrategy.PropertyToColumn(entry.getKey()));
			conditions.append(" ").append(entry.getValue());
		}
		sortCache = conditions.deleteCharAt(0).length() > 0 ? conditions.insert(0, "order by ").toString() : "";
		return sortCache;
	}

}
