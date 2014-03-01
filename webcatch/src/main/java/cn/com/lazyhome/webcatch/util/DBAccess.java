package cn.com.lazyhome.webcatch.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.lazyhome.webcatch.CatchMain;

/**
 * ���ݿⳣ�ò���
 * @author rainbow
 *
 */
public class DBAccess {
	private static final Log logger = LogFactory.getLog(DBAccess.class);
	
	/**
	 * �������ݿ�����
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		//�����ļ�Ŀ¼���������Ŀ¼
		String sourceURL = "jdbc:h2:tcp://localhost/~/h2/mydb";// H2 database
		String user = "sa";
		String password = "";
		try {
			Class.forName("org.h2.Driver");// H2 Driver
		} catch (Exception e) {
			e.printStackTrace();
		}
		Connection conn = DriverManager.getConnection(sourceURL, user, password);
		
		return conn;
	}
	
	public static void close(Connection conn) {
		try {
			DbUtils.close(conn);
		} catch (SQLException e) {
			logger.error("close conn", e);
		}
	}
}
