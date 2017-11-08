package cn.longicorn.modules.log2;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import cn.longicorn.modules.log.writer.IStoreService;
import cn.longicorn.modules.test.data.DbcpHelper;

public class MonitorLogDao implements IStoreService {

	private static DbcpHelper db = new DbcpHelper("jdbc:mysql://localhost:3306/sms", "root", "root");

	private static JdbcTemplate jdbcTemplate = new JdbcTemplate(db.getDataSouce());

	public void saveMonitorLog(MonitorLog[] logs) {

	}

	@Override
	public String getEntityClassName() {
		return MonitorLog.class.getCanonicalName();
	}

	@Override
	public void save(Serializable[] contents) {

	}

	@Override
	public void save(Serializable content) {
		String sql = "insert into test(name, remark) values(?, ?)";
		final MonitorLog log = (MonitorLog) content;
		jdbcTemplate.update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, log.getMessage());
				ps.setString(2, log.getMessage());
			}
		});
	}

	public void saveWithJdbc(Serializable content) {
		MonitorLog log = (MonitorLog) content;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = db.getConnection();
			conn.setAutoCommit(false);
			stmt = conn
					.prepareStatement("insert into test(name ,remark,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10) values(?, ? ,?,?,?,?,?,?,?,?,?,?)");
			stmt.setString(1, String.valueOf(log.getId()));
			stmt.setString(2, log.getMessage());
			stmt.setString(3, log.getMessage());
			stmt.setString(4, log.getMessage());
			stmt.setString(5, log.getMessage());
			stmt.setString(6, log.getMessage());
			stmt.setString(7, log.getMessage());
			stmt.setString(8, log.getMessage());
			stmt.setString(9, log.getMessage());
			stmt.setString(10, log.getMessage());
			stmt.setString(11, log.getMessage());
			stmt.setString(12, log.getMessage());
			stmt.addBatch();
			stmt.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}

	}

}
