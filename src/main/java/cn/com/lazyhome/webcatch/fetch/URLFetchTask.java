package cn.com.lazyhome.webcatch.fetch;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import cn.com.lazyhome.webcatch.fetch.dao.DownloadDao;
import cn.com.lazyhome.webcatch.fetch.dao.DownloadDaoImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class URLFetchTask implements Runnable {
	private static Log logger = LogFactory.getLog(URLFetchTask.class);
	
	private Queue<UrlPage> taskQueue;
	private LinkedList<UrlPage> finishedQueue;
	
	final Downloader downloader = new DownloaderImpl();
	final DownloadDao downloadDao = new DownloadDaoImpl();
	
	public URLFetchTask(Queue<UrlPage> queue, UrlPage entryPage, LinkedList<UrlPage> finishedQueue) {
		super();
		this.taskQueue = queue;
		if(queue.isEmpty()) {
			queue.add(entryPage);
		}
		
		this.finishedQueue = finishedQueue;
	}


	public void run() {
		while (true) {
			synchronized (taskQueue) {
				while (!taskQueue.isEmpty()) {
					try {
						logger.info("URLFetchTask Queue is not empty");
						taskQueue.wait(1000);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
				
				// 从数据库查找URL，并塞进队列
				try {
					List<UrlPage> profiles = downloadDao.loadDownloaderProfile();
					logger.info("新增URL数量：" + profiles.size());
					
					taskQueue.addAll(profiles);
					
					taskQueue.notify();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

}
