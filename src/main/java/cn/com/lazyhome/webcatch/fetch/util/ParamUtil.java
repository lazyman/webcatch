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
	 * �������ļ�����������Ϣ
	 * @return 
	 */
	public Properties loadParam() {
		logger.debug("�������ò���");
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
			logger.error("�������ò���ʧ��", e);
		}
		
		return config;
	}
	
	/**
	 * ��һ����������ʼ��������Ϣ������Ϣ�洢�����ݿ�
	 */
	public void initParam() {
		
	}
	
	/**
	 * 
	 * @return
	 */
	public Properties getParam() {
		logger.debug("��ȡϵͳ����");
		
		Properties fileConfig = new Properties();
		try {
			logger.debug("�������ò���");
			fileConfig.load(this.getClass().getResourceAsStream( CONFIG_FILE ));
			
			// �����ݿ��ȡϵͳ���������Ϊ����ʹ���ļ��е�Ĭ�ϲ���
			String driver = fileConfig.getProperty("config.db.driverClass");
			String url = fileConfig.getProperty("config.db.url");
			String username = fileConfig.getProperty("config.db.username");
			String password = fileConfig.getProperty("config.db.password");
			
			DBUtil dbUtil = DBUtil.getInstance(driver, url, username, password);

			Connection conn = dbUtil.getConnection();
			String sql = "select * from params";
			PreparedStatement prest = conn.prepareStatement(sql);
			
		} catch (IOException e) {
			logger.error("�������ò���ʧ��", e);
		} catch (SQLException e) {
			logger.error("�����ݿ��ȡ���ò���ʧ��", e);
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
