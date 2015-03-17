package cn.com.lazyhome.webcatch.fetch;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


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
		start(args);
	}
	
	public static void start(String[] args) {
		Downloader downloader = new DownloaderImpl();
		String url = "http://freebdsmsexvideos.net/category/all-bdsm/page/1?jjj=1";
		String parent = "http://freebdsmsexvideos.net/category/all-bdsm/page/1";
		
		downloader.initParam();
		try {
			downloader.downPage(parent, url);
		} catch (MalformedURLException e) {
			logger.error("FetchMain.start MalformedURLException", e);
		}
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
