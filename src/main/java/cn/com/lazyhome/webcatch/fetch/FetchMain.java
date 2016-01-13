package cn.com.lazyhome.webcatch.fetch;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.lazyhome.webcatch.fetch.dao.DownloadDao;
import cn.com.lazyhome.webcatch.fetch.dao.DownloadDaoImpl;


/**
 * 程序启动入口类
 * 
 * @author rainbow
 * 
 */
public class FetchMain {
	private static Log logger = LogFactory.getLog(FetchMain.class);
	private static String defaultUrl = "http://freebdsmsexvideos.net/category/all-bdsm/page/1?jjj=1";
	private static String refer = "http://freebdsmsexvideos.net/category/all-bdsm/page/1";
	private static int defaultLevel = 3;
	
	public final static int defaultThreadPoolSize = 15;
	
	public static void main(String[] args) {
//		demo();
//		start(args);
		
		try {
//			downloading();
			producerConsumerDownload();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
	}
	
	public static void start(String[] args) {
		Downloader downloader = new DownloaderImpl();
		String url = "http://freebdsmsexvideos.net/category/all-bdsm/page/1?jjj=1";
//		String refer = "http://freebdsmsexvideos.net/category/all-bdsm/page/1";
		
		downloader.initParam();

		// TODO 在数据库中将需要的记录标记为正在下载，并加载到resource中，线程池并发执行下载任务
//			downloading();
		
		downloader.downPage(refer, url);
	}

	/**
	 * 使用生产者消费者模式下载抓取
	 * @throws SQLException 
	 */
	private static void producerConsumerDownload() throws SQLException {
		final Downloader downloader = new DownloaderImpl();
		// 初始化数据库连接
		downloader.initParam();
		final DownloadDao downloadDao = new DownloadDaoImpl();
		downloadDao.initURLstatus();
		
		// 定义共享队列
		Queue<UrlPage> pages = new LinkedList<UrlPage>();
		LinkedList<UrlPage> finishedQueue = new LinkedList<UrlPage>();
		// 入口页面
		UrlPage entryPage = new UrlPage(defaultUrl, refer, defaultLevel);
		pages.add(entryPage);
		
		URLFetchTask fetchTask = new URLFetchTask(pages, entryPage, finishedQueue);
		new Thread(fetchTask).start();

		DownTask downTask1 = new DownTask(pages, finishedQueue);
		new Thread(downTask1).start();
//		DownTask downTask2 = new DownTask(pages, finishedQueue);
//		new Thread(downTask2).start();
		
//		for(int i=0; i<defaultThreadPoolSize; i++) {
//			DownTask task = new DownTask(pages, finishedQueue);
//			new Thread(task).start();
//		}
	}
	
	/**
	 * 在数据库中将需要的记录标记为正在下载，并加载到resource中，线程池并发执行下载任务
	 * @throws SQLException 
	 */
	private static void downloading() throws SQLException {
		// TODO 在数据库中将需要的记录标记为正在下载，并加载到resource中，线程池并发执行下载任务
		logger.trace("FetchMain.downloading start...");
		
		final Downloader downloader = new DownloaderImpl();
		downloader.initParam();
		final DownloadDao downloadDao = new DownloadDaoImpl();

		ExecutorService pool = Executors.newFixedThreadPool(defaultThreadPoolSize);

		// 在数据库中将需要下载的记录标记为正在下载，并加载到profiles中
		List<UrlPage> profiles = new ArrayList<UrlPage>();
		UrlPage page = new UrlPage(defaultUrl, refer, defaultLevel);
		profiles.add(page);
		
		do {
			
		synchronized (profiles) {
			
			// 线程池并发执行下载任务
			for(final UrlPage p : profiles) {
				pool.execute(new Runnable() {
					
					public void run() {
						logger.trace("Type1426664271330.run start...");
						
						HashMap<String, UrlPage> resources = downloader.downPage(p.getRefer(), p.getUrl(), p.getLevel());
						
						// 更新 resource 到数据库
						// update profile set status = ? where url=? and level
						try {
							downloadDao.save(resources);
						} catch (SQLException e) {
							logger.error(e.getMessage(), e);
						}
						
						logger.trace("Type1426664271330.run end.");
					}
				});
			}
		}
			
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
			
			synchronized (profiles) {
				profiles = downloadDao.loadDownloaderProfile();
			}
			
			
		} while(profiles.size() > 0);
		
		pool.shutdown();
		try {
			// 准备停止
			logger.info("准备停止");
			if(pool.awaitTermination(20, TimeUnit.SECONDS)) {
//				pool.shutdown();
				logger.info("已经停止");
			}
			logger.info("停了吗？？？");
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		
		logger.trace("FetchMain.downloading end.");
	}

	private static void demo() {
		logger.trace("FetchMain.demo start...");

		// TODO Auto-generated method stub
		String url = "http://ww.freebdsmsexvideos.net/category/all-bdsm/page/1?jjj=1";
		url = "http://freebdsmsexvideos.net/category/all-bdsm/page/";
		try {
			URL u1 = new URL(url);
			System.out.println(u1.getPath());
			System.out.println(u1.getFile());
			System.out.println(u1);
			
			URI u2 = new URI(url);
			System.out.println(u2);
		} catch (MalformedURLException e) {
			// e.printStackTrace();
			logger.error("FetchMain.demo MalformedURLException", e);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.error("FetchMain.demo URISyntaxException", e);
		}

		logger.trace("FetchMain.demo end.");
	}
}
