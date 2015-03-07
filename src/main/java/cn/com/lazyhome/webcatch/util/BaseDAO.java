package cn.com.lazyhome.webcatch.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

public abstract class BaseDAO {
	protected Connection conn;
	protected static String driverClass;
	protected static String user;
	protected static String url;
	protected static String password;
	static {
		// 从数据库配置文件导入配置参数
		Properties p = new Properties();
		try {
			p.load(BaseDAO.class.getResourceAsStream("/dbconfig.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		driverClass = p.getProperty("driverClass");
		user = p.getProperty("user");
		url = p.getProperty("url");
		password = p.getProperty("password");
		// 加载驱动类
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BaseDAO() {
		openConnection();
	}

	/** * 开启连接 */
	protected void openConnection() {
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** * 关闭连接 */
	public void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void executeUpdate(String sql) {
		Statement stm = null;
		try {
			stm = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			stm.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected ResultSet executeQuery(String sql) {
		Statement stm = null;
		try {
			stm = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet rs = null;
		try {
			rs = stm.executeQuery(sql);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return rs;
	}

	@SuppressWarnings("unchecked")
	protected abstract List resultSetToList(ResultSet rs);
}


//<span>import</span> <span>java.io.IOException</span><span>;</span><span>import</span> <span>java.sql.Connection</span><span>;</span><span>import</span> <span>java.sql.DriverManager</span><span>;</span><span>import</span> <span>java.sql.ResultSet</span><span>;</span><span>import</span> <span>java.sql.SQLException</span><span>;</span><span>import</span> <span>java.sql.Statement</span><span>;</span><span>import</span> <span>java.util.List</span><span>;</span><span>import</span> <span>java.util.Properties</span><span>;</span>&nbsp;<span>public</span> <span>abstract</span> <span>class</span> BaseDAO <span>{</span>&nbsp;	<span>protected</span> <span>Connection</span> conn<span>;</span>	<span>protected</span> <span>static</span> <span>String</span> driverClass<span>;</span>	<span>protected</span> <span>static</span> <span>String</span> user<span>;</span>	<span>protected</span> <span>static</span> <span>String</span> url<span>;</span>	<span>protected</span> <span>static</span> <span>String</span> password<span>;</span>&nbsp;	<span>static</span> <span>{</span>		<span>//从数据库配置文件导入配置参数</span>		<span>Properties</span> p <span>=</span> <span>new</span> <span>Properties</span><span>(</span><span>)</span><span>;</span>		<span>try</span> <span>{</span>			p.<span>load</span><span>(</span>BaseDAO.<span>class</span>.<span>getResourceAsStream</span><span>(</span><span>"/dbconfig.properties"</span><span>)</span><span>)</span><span>;</span>		<span>}</span> <span>catch</span> <span>(</span><span>IOException</span> e1<span>)</span> <span>{</span>			e1.<span>printStackTrace</span><span>(</span><span>)</span><span>;</span>		<span>}</span>		driverClass <span>=</span> p.<span>getProperty</span><span>(</span><span>"driverClass"</span><span>)</span><span>;</span>		user <span>=</span> p.<span>getProperty</span><span>(</span><span>"user"</span><span>)</span><span>;</span>		url <span>=</span> p.<span>getProperty</span><span>(</span><span>"url"</span><span>)</span><span>;</span>		password <span>=</span> p.<span>getProperty</span><span>(</span><span>"password"</span><span>)</span><span>;</span>		<span>//加载驱动类</span>		<span>try</span> <span>{</span>			<span>Class</span>.<span>forName</span><span>(</span><span>"com.mysql.jdbc.Driver"</span><span>)</span><span>;</span>		<span>}</span> <span>catch</span> <span>(</span><span>ClassNotFoundException</span> e<span>)</span> <span>{</span>			<span>// TODO Auto-generated catch block</span>			e.<span>printStackTrace</span><span>(</span><span>)</span><span>;</span>		<span>}</span>	<span>}</span>	<span>public</span> BaseDAO<span>(</span><span>)</span><span>{</span>		openConnection<span>(</span><span>)</span><span>;</span>	<span>}</span>&nbsp;	<span>/**	 * 开启连接	 */</span>	<span>protected</span> <span>void</span> openConnection<span>(</span><span>)</span><span>{</span>		<span>try</span> <span>{</span>			conn <span>=</span> <span>DriverManager</span>.<span>getConnection</span><span>(</span>url,user,password<span>)</span><span>;</span>		<span>}</span> <span>catch</span> <span>(</span><span>SQLException</span> e<span>)</span> <span>{</span>			<span>// TODO Auto-generated catch block</span>			e.<span>printStackTrace</span><span>(</span><span>)</span><span>;</span>		<span>}</span>	<span>}</span>	<span>/**	 * 关闭连接	 */</span>	<span>public</span> <span>void</span> closeConnection<span>(</span><span>)</span><span>{</span>		<span>if</span><span>(</span>conn<span>!=</span><span>null</span><span>)</span><span>{</span>			<span>try</span> <span>{</span>				conn.<span>close</span><span>(</span><span>)</span><span>;</span>			<span>}</span> <span>catch</span> <span>(</span><span>SQLException</span> e<span>)</span> <span>{</span>				<span>// TODO Auto-generated catch block</span>				e.<span>printStackTrace</span><span>(</span><span>)</span><span>;</span>			<span>}</span>		<span>}</span>&nbsp;	<span>}</span>&nbsp;	<span>protected</span> <span>void</span> executeUpdate<span>(</span><span>String</span> sql<span>)</span><span>{</span>		<span>Statement</span> stm <span>=</span> <span>null</span><span>;</span>		<span>try</span> <span>{</span>			stm <span>=</span> conn.<span>createStatement</span><span>(</span><span>)</span><span>;</span>		<span>}</span> <span>catch</span> <span>(</span><span>SQLException</span> e<span>)</span> <span>{</span>			e.<span>printStackTrace</span><span>(</span><span>)</span><span>;</span>		<span>}</span>		<span>try</span> <span>{</span>			stm.<span>execute</span><span>(</span>sql<span>)</span><span>;</span>		<span>}</span> <span>catch</span> <span>(</span><span>SQLException</span> e<span>)</span> <span>{</span>			e.<span>printStackTrace</span><span>(</span><span>)</span><span>;</span>		<span>}</span>&nbsp;	<span>}</span>	<span>protected</span> <span>ResultSet</span> executeQuery<span>(</span><span>String</span> sql<span>)</span><span>{</span>		<span>Statement</span> stm <span>=</span> <span>null</span><span>;</span>		<span>try</span> <span>{</span>			stm <span>=</span> conn.<span>createStatement</span><span>(</span><span>)</span><span>;</span>		<span>}</span> <span>catch</span> <span>(</span><span>SQLException</span> e<span>)</span> <span>{</span>			e.<span>printStackTrace</span><span>(</span><span>)</span><span>;</span>		<span>}</span>		<span>ResultSet</span> rs <span>=</span> <span>null</span><span>;</span>		<span>try</span> <span>{</span>			rs <span>=</span> stm.<span>executeQuery</span><span>(</span>sql<span>)</span><span>;</span>		<span>}</span> <span>catch</span> <span>(</span><span>SQLException</span> e1<span>)</span> <span>{</span>			e1.<span>printStackTrace</span><span>(</span><span>)</span><span>;</span>		<span>}</span>		<span>return</span> rs<span>;</span>	<span>}</span>&nbsp;	@SuppressWarnings<span>(</span><span>"unchecked"</span><span>)</span>	<span>protected</span> <span>abstract</span> <span>List</span> resultSetToList<span>(</span><span>ResultSet</span> rs<span>)</span><span>;</span>&nbsp;<span>}</span>                                            <span>                            <br>                    </span>                                            <span>转自：http://abowow.com/2009/04/an-light-substitution-for-hibernate/</span>                </p><p></p>