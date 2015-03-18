package cn.com.lazyhome.webcatch.fetch;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	
	public static void main(String[] args) {
//		demo();
//		start(args);
		downloading();
	}
	
	public static void start(String[] args) {
		Downloader downloader = new DownloaderImpl();
		String url = "http://freebdsmsexvideos.net/category/all-bdsm/page/1?jjj=1";
		String parent = "http://freebdsmsexvideos.net/category/all-bdsm/page/1";
		
		downloader.initParam();

		try {
			// TODO 在数据库中将需要的记录标记为正在下载，并加载到resource中，线程池并发执行下载任务
//			downloading();
			
			downloader.downPage(parent, url);
			
		} catch (MalformedURLException e) {
			logger.error("FetchMain.start MalformedURLException", e);
		}
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
		final DownloadDao dao = new DownloadDaoImpl();

		ExecutorService pool = Executors.newFixedThreadPool(5);

		// 在数据库中将需要下载的记录标记为正在下载，并加载到profiles中
		List<UrlPage> profiles = dao.loadDownloaderProfile();
		
		do {
			
			// 线程池并发执行下载任务
			for(final UrlPage p : profiles) {
				pool.execute(new Runnable() {
					
					public void run() {
						logger.trace("Type1426664271330.run start...");
						
						HashMap<String, UrlPage> resources = downloader.downPage(p.getParent(), p.getUrl(), p.getLevel());
						
						// 更新 resource 到数据库
						// update profile set status = ? where url=? and level
						try {
							dao.analyzed(resources);
						} catch (SQLException e) {
							logger.trace(e.getMessage(), e);
						}
						
						logger.trace("Type1426664271330.run end.");
					}
				});
			}
			
			profiles = dao.loadDownloaderProfile();
			
		} while(profiles.size() > 0);
		
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
