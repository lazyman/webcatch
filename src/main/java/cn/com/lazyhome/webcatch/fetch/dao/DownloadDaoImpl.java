package cn.com.lazyhome.webcatch.fetch.dao;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

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
	
	public List<UrlPage> getDownloaderProfile() {
		logger.trace("DownloadDao.getDownloaderProfile start...");
		
		// TODO Auto-generated method stub
		// select * from profile where status = STATUS_DOWNLOADING
		return null;
		
//		logger.trace("DownloadDao.getDownloaderProfile end.");
	}

	public void analyzed(HashMap<String, UrlPage> resources) {
		logger.trace("DownloadDao.analyzed start...");
		
		// TODO Auto-generated method stub
		
		logger.trace("DownloadDao.analyzed end.");
	}

	public void fetched(URL url) throws SQLException {
		logger.trace("DownloadDao.fetched start...");
		
		// update profile set status = UrlPage.STATUS_FETCHED where url.status = STATUS_DOWNLOADING

		String sql = "update profile set status = ? where url.status = ?";
		
		Vector<String> params = new Vector<String>();
		params.add(UrlPage.STATUS_FETCHED);
		params.add(UrlPage.STATUS_DOWNLOADING);
		
		DBUtil.getInstance().executeByStrParams(sql, params);
		
		
		logger.trace("DownloadDao.fetched end.");
	}

	public void analyzing(URL url) throws SQLException {
		logger.trace("DownloadDao.analyzing start...");
		
		// update profile set status = UrlPage.STATUS_ANALYZING where url.status = STATUS_FETCHED

		String sql = "update profile set status = ? where url.status = ?";
		
		Vector<String> params = new Vector<String>();
		params.add(UrlPage.STATUS_ANALYZING);
		params.add(UrlPage.STATUS_FETCHED);
		
		DBUtil.getInstance().executeByStrParams(sql, params);
		
		logger.trace("DownloadDao.analyzing end.");
	}

	public void analyzed(URL url) throws SQLException {
		logger.trace("DownloadDao.analyzed start...");
		
		// update profile set status = UrlPage.STATUS_ANALYZED where url.status = STATUS_ANALYZING

		String sql = "update profile set status = ? where url.status = ?";
		
		Vector<String> params = new Vector<String>();
		params.add(UrlPage.STATUS_ANALYZED);
		params.add(UrlPage.STATUS_ANALYZING);
		
		DBUtil.getInstance().executeByStrParams(sql, params);
		
		logger.trace("DownloadDao.analyzed end.");
	}

}
