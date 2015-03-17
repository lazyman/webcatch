package cn.com.lazyhome.webcatch.fetch.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ParamUtil {
	private static Log logger = LogFactory.getLog(ParamUtil.class);
	
	public static final String CONFIG_FILE = "/app.properties";
	private String baseDir;

	/**
	 * 从配置文件加载配置信息
	 * @return 
	 */
	public Properties loadParam() {
		logger.debug("加载配置参数");
		Properties config = new Properties();
		
//		String configFile;
		try {
			logger.debug(CONFIG_FILE);
			baseDir = System.getProperty("user.dir");
			if(logger.isDebugEnabled()) {
				logger.debug("base dir:\t" + baseDir);
			}
			
			config.load(this.getClass().getResourceAsStream( CONFIG_FILE ));
			
			logger.trace(config);
		} catch (IOException e) {
			logger.error("加载配置参数失败", e);
		}
		
		return config;
	}
	
	/**
	 * 第一次启动，初始化配置信息，把信息存储到数据库
	 */
	public void initParam() {
		
	}
	
	/**
	 * 
	 * @return
	 */
	public Properties getParam() {
		logger.debug("获取系统参数");
		
		Properties fileConfig = new Properties();
		try {
			logger.debug("加载配置参数");
			fileConfig.load(this.getClass().getResourceAsStream( CONFIG_FILE ));
			
			// 从数据库获取系统参数，如果为空则使用文件中的默认参数
			String driver = fileConfig.getProperty("config.db.driverClass");
			String url = fileConfig.getProperty("config.db.url");
			String username = fileConfig.getProperty("config.db.username");
			String password = fileConfig.getProperty("config.db.password");
			
			DBUtil dbUtil = DBUtil.getInstance(driver, url, username, password);

			Connection conn = dbUtil.getConnection();
			String sql = "select * from params";
			PreparedStatement prest = conn.prepareStatement(sql);
			
		} catch (IOException e) {
			logger.error("加载配置参数失败", e);
		} catch (SQLException e) {
			logger.error("从数据库读取配置参数失败", e);
		}
		
		return fileConfig;
		
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}
}
