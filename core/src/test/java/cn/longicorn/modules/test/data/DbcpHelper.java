package cn.longicorn.modules.test.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

/**
 * 简单的DBCP数据库连接池辅助类，一般用于测试用例或独立的客户端Java应用
 */
public final class DbcpHelper {

	private static final Logger logger = LoggerFactory.getLogger(DbcpHelper.class);

	private static DataSource dataSource;

	public Connection getConnection() {
		Connection con = null;
		if (dataSource != null) {
			try {
				con = dataSource.getConnection();
				con.setAutoCommit(false);
			} catch (SQLException e) {
				logger.error("获取数据库连接出现错误, {}", e.getMessage());
			}
		}
		return con;
	}

	public DbcpHelper(String connectURI, String userName, String passwd) {
		initDS(connectURI, userName, passwd);
	}

	public DbcpHelper(String connectURI, String username, String pswd, String driverClass, int initialSize,
			int maxActive, int maxIdle, int maxWait) {
		initDS(connectURI, username, pswd, driverClass, initialSize, maxActive, maxIdle, maxWait);
	}
	
	public DataSource getDataSouce() {
		return dataSource;
	}

	//FOR LOCAL TEST
	private static void initDS(String connectURI, String userName, String passwd) {
		initDS(connectURI, userName, passwd, "com.mysql.jdbc.Driver", 8, 100, 30, 10000);
	}

	/**
	 * 指定所有参数连接数据源
	 *
	 * @param connectURI 数据库
	 * @param username 用户名
	 * @param pswd 密码
	 * @param driverClass 数据库连接驱动名
	 * @param initialSize 初始连接池连接个数
	 * @param maxActive 最大激活连接数
	 * @param maxIdle 最大闲置连接数
	 * @param maxWait 获得连接的最大等待毫秒数
	 */
	private static void initDS(String connectURI, String username, String pswd, String driverClass, int initialSize,
			int maxActive, int maxIdle, int maxWait) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverClass);
		ds.setUsername(username);
		ds.setPassword(pswd);
		ds.setUrl(connectURI);
		ds.setInitialSize(initialSize);
		ds.setMaxActive(maxActive);
		ds.setMaxIdle(maxIdle);
		ds.setMaxWait(maxWait);
		dataSource = ds;
	}

	public static Map<String, Integer> getDataSourceStats() throws SQLException {
		BasicDataSource bds = (BasicDataSource) dataSource;
		Map<String, Integer> map = new HashMap<String, Integer>(2);
		map.put("active", bds.getNumActive());
		map.put("idle", bds.getNumIdle());
		return map;
	}

	public static void shutdownDataSource() throws SQLException {
		BasicDataSource bds = (BasicDataSource) dataSource;
		bds.close();
	}

}