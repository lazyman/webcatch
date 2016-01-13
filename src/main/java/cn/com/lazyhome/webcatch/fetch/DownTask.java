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
	
	private Queue<UrlPage> taskQueue;
	private Queue<UrlPage> finishedQueue;
	
	final Downloader downloader = new DownloaderImpl();
	final DownloadDao downloadDao = new DownloadDaoImpl();
	
	ExecutorService pool = Executors.newFixedThreadPool(FetchMain.defaultThreadPoolSize);

	public DownTask(Queue<UrlPage> taskQueue, LinkedList<UrlPage> finishedQueue) {
		this.taskQueue = taskQueue;
		this.finishedQueue = finishedQueue;
	}
	
	public void run() {
		run1();
	}
	public void run2() {
		while (true) {
			synchronized (taskQueue) {
				while (taskQueue.isEmpty()) {
					try {
						System.out.println("DownTask2 Queue is empty");
						taskQueue.wait();
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			
				final UrlPage page = taskQueue.poll();
				
				down(page);
				
				taskQueue.notify();
			}
		}
	}
	
	public void run1() {
		while (true) {
			synchronized (taskQueue) {
				while (taskQueue.isEmpty()) {
					try {
						System.out.println("DownTask1 Queue is empty");
						taskQueue.wait();
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
				
				
				// 开始取出URL，进行下载抓取
				final int size = taskQueue.size();
				int i=0;
				
				finishedQueue.addAll(taskQueue);
				while(!taskQueue.isEmpty()) {
					final UrlPage page = taskQueue.poll();
					i++;
					
					logger.info("url解析进度：" + i + "/" + size);
					pool.execute(new Runnable() {
		
						public void run() {
							down(page);
							
							finishedQueue.remove(page);
						}
					});
				}
				
				while(!finishedQueue.isEmpty()) {
					try {
						int finishSize = finishedQueue.size();
						logger.info("finishedQueue.size():" + finishSize + "taskQueue.size:" + size);
						
						if(finishSize<5) {
							Iterator<UrlPage> iterator = finishedQueue.iterator();
							while(iterator.hasNext()) {
								UrlPage page = iterator.next();
								logger.debug(page.getUrl());
							}
						}
						
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
//				while(finishedQueue.size() < size) {
//					try {
//						logger.info("finishedQueue.size():" + finishedQueue.size() + "taskQueue.size:" + size);
//						Thread.sleep(3000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
				
				finishedQueue.clear();
				taskQueue.notify();
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
