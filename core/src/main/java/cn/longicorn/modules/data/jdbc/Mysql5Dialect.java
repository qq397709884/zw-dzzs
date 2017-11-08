package cn.longicorn.modules.data.jdbc;

public class Mysql5Dialect implements IDialect {
	
	@Override
	public String getLimitString(String sql, long offset, long maxResults) {
		return sql + " limit " + offset + ',' + maxResults;
	}

}
