package cn.com.lazyhome.webcatch.fetch;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.lazyhome.webcatch.fetch.dao.DownloadDao;
import cn.com.lazyhome.webcatch.fetch.dao.DownloadDaoImpl;

public class DownTask implements Runnable {
	private static Log logger = LogFactory.getLog(DownTask.class);
	
	private Queue<UrlPage> queue;
	final Downloader downloader = new DownloaderImpl();
	final DownloadDao downloadDao = new DownloadDaoImpl();
	
	ExecutorService pool = Executors.newFixedThreadPool(FetchMain.defaultThreadPoolSize);

	public DownTask(Queue<UrlPage> queue) {
		this.queue = queue;
	}
	
	public void run() {
		while (true) {
			synchronized (queue) {
				while (queue.isEmpty()) {
					try {
						System.out.println("Queue is empty");
						queue.wait();
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
				
				
				// 开始取出URL，进行下载抓取
				final Queue<UrlPage> finishedQueue = new LinkedList<UrlPage>();
				final int size = queue.size();
				int i=0;
				
				while(!queue.isEmpty()) {
					final UrlPage page = queue.poll();
					i++;
					
					logger.info("url解析进度：" + i + "/" + size);
					pool.execute(new Runnable() {
		
						public void run() {
							down(page);
							
							synchronized (finishedQueue) {
								finishedQueue.add(page);
								
								if(finishedQueue.size() == size) {
									finishedQueue.clear();
									queue.notify();
								}
							}
						}
					});
				}
			}
		}
	}
	
	private void down(UrlPage page) {
		logger.trace("Type1426664271330.run start...");
		
		HashMap<String, UrlPage> resources = downloader.downPage(page.getRefer(), page.getUrl(), page.getLevel());
		
		// 更新 resource 到数据库
		// update profile set status = ? where url=? and level
		try {
			downloadDao.save(resources);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		
		logger.trace("Type1426664271330.run end.");
	}

}
