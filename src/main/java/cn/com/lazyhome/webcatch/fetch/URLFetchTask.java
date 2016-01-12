package cn.com.lazyhome.webcatch.fetch;

import java.sql.SQLException;
import java.util.List;
import java.util.Queue;

import cn.com.lazyhome.webcatch.fetch.dao.DownloadDao;
import cn.com.lazyhome.webcatch.fetch.dao.DownloadDaoImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class URLFetchTask implements Runnable {
	private static Log logger = LogFactory.getLog(URLFetchTask.class);
	
	private Queue<UrlPage> queue;
	
	final Downloader downloader = new DownloaderImpl();
	final DownloadDao downloadDao = new DownloadDaoImpl();
	
	public URLFetchTask(Queue<UrlPage> queue, UrlPage entryPage) {
		super();
		this.queue = queue;
		if(queue.isEmpty()) {
			queue.add(entryPage);
		}
	}


	public void run() {
		while (true) {
			synchronized (queue) {
				while (queue.size() > 0) {
					try {
						logger.info("Queue is not empty");
						queue.wait();
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
				
				// 从数据库查找URL，并塞进队列
				try {
					List<UrlPage> profiles = downloadDao.loadDownloaderProfile();
					logger.info("新增URL数量：" + profiles.size());
					
					queue.addAll(profiles);
					
					queue.notify();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

}
