package cn.com.lazyhome.webcatch.fetch.dao;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.lazyhome.webcatch.fetch.UrlPage;

public class DownloadDaoImpl implements DownloadDao {
	private static Log logger = LogFactory.getLog(DownloadDaoImpl.class);

	public List<UrlPage> loadDownloaderProfile() {
		logger.trace("DownloadDao.getDownloaderProfile start...");
		
		// TODO Auto-generated method stub
		// update profile set status = STATUS_DOWNLOADING where status = STATUS_NEW
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

	public void fetched(URL url) {
		logger.trace("DownloadDao.fetched start...");
		
		// TODO Auto-generated method stub
		// update profile set status = UrlPage.STATUS_FETCHED where url.status = STATUS_DOWNLOADING
		
		
		logger.trace("DownloadDao.fetched end.");
	}

	public void analyzing(URL url) {
		logger.trace("DownloadDao.analyzing start...");
		
		// TODO Auto-generated method stub
		// update profile set status = UrlPage.STATUS_DOWNLOADING where url.status = STATUS_FETCHED
		
		logger.trace("DownloadDao.analyzing end.");
	}

	public void analyzed(URL url) {
		logger.trace("DownloadDao.analyzed start...");
		
		// TODO Auto-generated method stub
		// update profile set status = UrlPage.STATUS_ANALYZED where url.status = STATUS_DOWNLOADING
		
		logger.trace("DownloadDao.analyzed end.");
	}

}
