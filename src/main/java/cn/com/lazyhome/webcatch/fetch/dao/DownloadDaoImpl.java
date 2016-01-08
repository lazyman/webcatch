package cn.com.lazyhome.webcatch.fetch.dao;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.lazyhome.webcatch.fetch.UrlPage;
import cn.com.lazyhome.webcatch.fetch.util.DBUtil;

public class DownloadDaoImpl implements DownloadDao {
	private static Log logger = LogFactory.getLog(DownloadDaoImpl.class);

	public List<UrlPage> loadDownloaderProfile() throws SQLException {
		logger.trace("DownloadDao.getDownloaderProfile start...");
		
		// update profile set status = STATUS_DOWNLOADING where status = STATUS_NEW
		String sql = "update profile set status = ? where status = ?";
		
		Vector<String> params = new Vector<String>();
		params.add(UrlPage.STATUS_DOWNLOADING);
		params.add(UrlPage.STATUS_NEW);
		
		DBUtil.getInstance().executeByStrParams(sql, params);
		
		return getDownloaderProfile();
		
//		logger.trace("DownloadDao.getDownloaderProfile end.");
	}
	
	public List<UrlPage> getDownloaderProfile() throws SQLException {
		logger.trace("DownloadDao.getDownloaderProfile start...");
		
		// select * from profile where status = STATUS_DOWNLOADING
		String sql = "select * from profile where status = ?";
		
		Connection conn = DBUtil.getInstance().getConnection();
		
		ResultSetHandler<List<UrlPage>> handler = new BeanListHandler<UrlPage>(UrlPage.class);
		
		QueryRunner run = new QueryRunner();
		try{
			List<UrlPage> pages = run.query(conn, sql, handler, UrlPage.STATUS_DOWNLOADING);

		    return pages;
		} finally {
		    conn.close(); 
		}
		
//		logger.trace("DownloadDao.getDownloaderProfile end.");
	}

	public void save(HashMap<String, UrlPage> resources) throws SQLException {
		logger.trace("DownloadDao.analyzed start...");
		
		Connection conn = DBUtil.getInstance().getConnection();
		QueryRunner run = new QueryRunner( );
		
		Iterator<Entry<String, UrlPage>> i = resources.entrySet().iterator();
		while(i.hasNext()) {
			UrlPage url = i.next().getValue();
			run.update(conn, "INSERT INTO profile (url, refer, status, level) VALUES (?,?,?,?)",
					url.getUrl(), url.getRefer(), url.getStatus(), url.getLevel());
		}
		
		conn.close();
		
		// TODO Auto-generated method stub
		
		logger.trace("DownloadDao.analyzed end.");
	}

	public void fetched(URL url) throws SQLException {
		logger.trace("DownloadDao.fetched start...");
		
		// update profile set status = UrlPage.STATUS_FETCHED where url.status = STATUS_DOWNLOADING

		String sql = "update profile set status = ? where url=?";
		
		Vector<String> params = new Vector<String>();
		params.add(UrlPage.STATUS_FETCHED);
		params.add(url.toString());
		
		DBUtil.getInstance().executeByStrParams(sql, params);
		
		
		logger.trace("DownloadDao.fetched end.");
	}

	public void analyzing(URL url) throws SQLException {
		logger.trace("DownloadDao.analyzing start...");
		
		// update profile set status = UrlPage.STATUS_ANALYZING where url.status = STATUS_FETCHED

		String sql = "update profile set status = ? where url = ?";
		
		Vector<String> params = new Vector<String>();
		params.add(UrlPage.STATUS_ANALYZING);
		params.add(url.toString());
		
		DBUtil.getInstance().executeByStrParams(sql, params);
		
		logger.trace("DownloadDao.analyzing end.");
	}

	public void analyzed(URL url) throws SQLException {
		logger.trace("DownloadDao.analyzed start...");
		
		// update profile set status = UrlPage.STATUS_ANALYZED where url.status = STATUS_ANALYZING

		String sql = "update profile set status = ? where url = ?";
		
		Vector<String> params = new Vector<String>();
		params.add(UrlPage.STATUS_ANALYZED);
		params.add(url.toString());
		
		DBUtil.getInstance().executeByStrParams(sql, params);
		
		logger.trace("DownloadDao.analyzed end.");
	}

}
