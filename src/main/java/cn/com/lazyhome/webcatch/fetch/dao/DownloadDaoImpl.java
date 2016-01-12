package cn.com.lazyhome.webcatch.fetch.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.lazyhome.webcatch.fetch.UrlPage;
import cn.com.lazyhome.webcatch.fetch.util.DBUtil;

public class DownloadDaoImpl implements DownloadDao {
	private static Log logger = LogFactory.getLog(DownloadDaoImpl.class);

	public List<UrlPage> loadDownloaderProfile() throws SQLException {
		logger.trace("DownloadDao.getDownloaderProfile start...");

		Connection conn = DBUtil.getInstance().getConnection();
		QueryRunner run = new QueryRunner( );
		String sql = "update profile set status = ? where status = ?";
		
		List<UrlPage> pages = getDownloaderProfile();
		// update profile set status = STATUS_DOWNLOADING where status = STATUS_NEW
		
		// 已经取出的URL置为下载中STATUS_DOWNLOADING
		run.update(conn, sql, UrlPage.STATUS_DOWNLOADING, UrlPage.STATUS_NEW);
		
		conn.close();
		
		
		return pages;
		
//		logger.trace("DownloadDao.getDownloaderProfile end.");
	}
	
	public List<UrlPage> getDownloaderProfile() throws SQLException {
		logger.trace("DownloadDao.getDownloaderProfile start...");
		
		// select * from profile where status = STATUS_DOWNLOADING
		String sql = "select top 100 url, max(level) level, status from profile where status = ? group by url";
		
		Connection conn = DBUtil.getInstance().getConnection();
		
		ResultSetHandler<List<UrlPage>> handler = new BeanListHandler<UrlPage>(UrlPage.class);
		
		QueryRunner run = new QueryRunner();
		try{
			List<UrlPage> pages = run.query(conn, sql, handler, UrlPage.STATUS_NEW);

		    return pages;
		} finally {
		    conn.close(); 
		}
		
//		logger.trace("DownloadDao.getDownloaderProfile end.");
	}

	public UrlPage find(String url, int level) throws SQLException {
		logger.trace("DownloadDao.find start...");
		
		Connection conn = DBUtil.getInstance().getConnection();
		QueryRunner run = new QueryRunner( );
		ResultSetHandler<UrlPage> handler = new BeanHandler<UrlPage>(UrlPage.class);
		
		UrlPage page = run.query(conn, "select * from profile where url=? and level=?", handler, url, level);
		
		conn.close();
		
		logger.trace("DownloadDao.find end.");
		
		return page;
	}

	public UrlPage find(UrlPage url) throws SQLException {
		UrlPage page = find(url.getUrl(), url.getLevel());
		
		return page;
	}

	public void addNew(UrlPage url) throws SQLException {
		logger.trace("DownloadDao.addNew start...");
		
		Connection conn = DBUtil.getInstance().getConnection();
		QueryRunner run = new QueryRunner( );
		
		run.update(conn, "INSERT INTO profile (url, refer, status, level) VALUES (?,?,?,?)",
				url.getUrl(), url.getRefer(), url.getStatus(), url.getLevel());
		
		conn.close();
		
		
		logger.trace("DownloadDao.addNew end.");
	}

	public void update(UrlPage url) throws SQLException {
		logger.trace("DownloadDao.update start...");
		
		Connection conn = DBUtil.getInstance().getConnection();
		QueryRunner run = new QueryRunner( );
		
		run.update(conn, "update profile set refer=?, status=?, level=? where url=?",
				 url.getRefer(), url.getStatus(), url.getLevel(), url.getUrl());
		
		conn.close();
		
		
		logger.trace("DownloadDao.update end.");
	}
	
	public void save(HashMap<String, UrlPage> resources) throws SQLException {
		logger.trace("DownloadDao.save start...");
		
		Iterator<Entry<String, UrlPage>> i = resources.entrySet().iterator();
		while(i.hasNext()) {
			UrlPage url = i.next().getValue();
			if(!exist(url)) {
				addNew(url);
			}
		}
		
		logger.trace("DownloadDao.save end.");
	}

	private boolean exist(UrlPage url) throws SQLException {
		UrlPage page = find(url);
		
		return page != null;
	}

	public void markFetched(String url, int level) throws SQLException {
		logger.trace("DownloadDao.fetched start...");
		
		markStatus(url, level, UrlPage.STATUS_FETCHED);
		
		logger.trace("DownloadDao.fetched end.");
	}

	public void markAnalyzing(String url, int level) throws SQLException {
		logger.trace("DownloadDao.analyzing start...");
		
		markStatus(url, level, UrlPage.STATUS_ANALYZING);
		
		logger.trace("DownloadDao.analyzing end.");
	}

	public void markAnalyzed(String url, int level) throws SQLException {
		logger.trace("DownloadDao.analyzed start...");
		
		markStatus(url, level, UrlPage.STATUS_ANALYZED);
		
		logger.trace("DownloadDao.analyzed end.");
	}

	public void markDownloading(String url, int level) throws SQLException {
		markStatus(url, level, UrlPage.STATUS_DOWNLOADING);
	}

	public void markStatus(String url, int level, String status) throws SQLException {
		logger.trace("DownloadDao.marked start...");
		
		Connection conn = DBUtil.getInstance().getConnection();
		QueryRunner run = new QueryRunner( );
		
		run.update(conn, "update profile set status=? where level=? and url=?",
				status, level, url);
		
		conn.close();
		
		
		logger.trace("DownloadDao.update end.");
	}

	public void initURLstatus() throws SQLException {
		Connection conn = DBUtil.getInstance().getConnection();
		QueryRunner run = new QueryRunner( );
		String sql = "update profile set status = ? where status = ?";
		
		// 初始化，把所有上次未下载完成的URL全部置为新的URL
		run.update(conn, sql, UrlPage.STATUS_NEW, UrlPage.STATUS_DOWNLOADING);
		
		conn.close();
	}
}
