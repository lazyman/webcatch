package cn.com.lazyhome.webcatch.test;

import java.sql.*;

public class UpdateTest {
	public void runInsertDelete() {
		try {
			String sourceURL = "jdbc:h2:h2/bin/mydb";// H2 database
			String user = "sa";
			String key = "";
			try {
				Class.forName("org.h2.Driver");// H2 Driver
			} catch (Exception e) {
				e.printStackTrace();
			}
			Connection conn = DriverManager.getConnection(sourceURL, user, key);
			Statement stmt = conn.createStatement();
//			stmt.execute("CREATE TABLE mytable(name VARCHAR(100),sex VARCHAR(10))");
			stmt.executeUpdate("INSERT INTO mytable VALUES('Steven Stander','male')");
			stmt.executeUpdate("INSERT INTO mytable VALUES('Elizabeth Eames','female')");
			stmt.executeUpdate("DELETE FROM mytable WHERE sex='male'");
			stmt.close();
			conn.close();

		} catch (SQLException sqle) {
			System.err.println(sqle);
		}
	}

	public static void main(String args[]) {
		new UpdateTest().runInsertDelete();
	}
}