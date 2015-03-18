package cn.com.lazyhome.webcatch.fetch.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ���ݿ⹤����
 * @author dch
 *
 */
public class DBUtil {
	private static Log logger = LogFactory.getLog(DBUtil.class);
	private static DBUtil util;
	
	// ���ݿ�������Ϣ
	/**
	 * ���ݿ�����
	 */
	private String driver;
	/**
	 * ���ݿ�URL
	 */
	private String url;
	/**
	 * ���ݿ��û���
	 */
	private String username;
	/**
	 * ���ݿ�����
	 */
	private String password;

	public static DBUtil getInstance() {
		if(util == null) {
			util = new DBUtil();
		}
		
		return util;
	}
	public static DBUtil getInstance(String dbdriver, String dburl, String dbusername, String dbpassword) {
		if(util == null) {
			util = new DBUtil();
		}
		
		util.setDriver(dbdriver);
		util.setUrl(dburl);
		util.setUsername(dbusername);
		util.setPassword(dbpassword);
		
		try {
			util.getConnection().close();
			logger.info("�������ݿ�ɹ�.....");
		} catch (SQLException e) {
			logger.error("DBUtil.getInstance SQLException", e);
			
			util = null;
		}
		
		return util;
	}
	
	/**
	 * ��ȡ���ݿ�����
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			logger.warn("�������ݿ�����ʧ��", e);
			util = null;
		}
		Connection connection = DriverManager.getConnection(url, username, password);
		
		return connection;
	}
	
	/**
	 * �ر����ݿ�����
	 * @param conn ���ݿ�����
	 * @param prestmt SQL���
	 */
	public void closeConnection(Connection conn, PreparedStatement prestmt) {
		try {
			if(prestmt != null) {
				prestmt.close();
			}
			
			if(conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			// �����ݿ��쳣��NullPoint�쳣
			logger.error("�ر����Ӵ���", e);
		}
	}
	
	/**
	 * ִ��sql��ֻ�ܴ���string���������޸���
	 * @param sql Ҫִ�е�sql���
	 * @param params �����б�����Ϊstring�����޸���
	 * @throws SQLException 
	 */
	public void executeByStrParams(Connection conn, String sql, List<String> params) throws SQLException {
		PreparedStatement prestmt = conn.prepareStatement(sql);
		int size = params.size();
		for(int parameterIndex=1; parameterIndex< size; parameterIndex++) {
			prestmt.setString(parameterIndex, params.get(parameterIndex));
		}
		prestmt.execute();
		
		prestmt.close();
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
