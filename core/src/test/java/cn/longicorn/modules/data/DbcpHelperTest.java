package cn.longicorn.modules.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import cn.longicorn.modules.test.data.DbcpHelper;

public class DbcpHelperTest {

	public static void main(String args[]) {
		DbcpHelper db = new DbcpHelper("jdbc:mysql://localhost:3306/sms", "root", "root");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = db.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from test limit 1");
			System.out.println("Results:");
			int numcols = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= numcols; i++) {
					System.out.print(" " + rs.getString(i) + " ");
				}
			}
			System.out.println(DbcpHelper.getDataSourceStats());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (stmt != null) stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (conn != null) conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (db != null) DbcpHelper.shutdownDataSource();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
