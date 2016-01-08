package cn.com.lazyhome.webcatch.fetch.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h2.jdbcx.JdbcConnectionPool;

import cn.com.lazyhome.webcatch.fetch.UrlPage;

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
//		Connection connection = DriverManager.getConnection(url, username, password);
		JdbcConnectionPool cp = JdbcConnectionPool.create(url,username,password);
		Connection connection = cp.getConnection();
		
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
		for(int parameterIndex=0; parameterIndex< size; parameterIndex++) {
			prestmt.setString(parameterIndex+1, params.get(parameterIndex));
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
	public void executeByStrParams(String sql, List<String> params) throws SQLException {
		// TODO Auto-generated method stub
		Connection conn = getConnection();
		
		executeByStrParams(conn, sql, params);
	}
	
	public Object[] queryByStrParams(String sql, String... params) throws SQLException {
		Connection conn = getConnection();
		
		ResultSetHandler<Object[]> handler = new ResultSetHandler<Object[]>() {
		    public Object[] handle(ResultSet rs) throws SQLException {
		        if (!rs.next()) {
		            return null;
		        }
		    
		        ResultSetMetaData meta = rs.getMetaData();
		        int cols = meta.getColumnCount();
		        Object[] result = new Object[cols];

		        for (int i = 0; i < cols; i++) {
		            result[i] = rs.getString(i + 1);
		        }

		        return result;
		    }
		};
		
		QueryRunner run = new QueryRunner();
		try{
			Object[] result = run.query(conn, sql, handler, params);
		        // do something with the result
		        
		    return result;
		} finally {
		    conn.close(); 
		}
		
	}
	

	public List<UrlPage> queryBeanByStrParams(String sql, String... params) throws SQLException {
		Connection conn = getConnection();
		
		ResultSetHandler<List<UrlPage>> handler = new BeanListHandler<UrlPage>(UrlPage.class);
		
		QueryRunner run = new QueryRunner();
		try{
			List<UrlPage> pages = run.query(conn, sql, handler, params);

		    return pages;
		} finally {
		    conn.close(); 
		}
		
	}

}
