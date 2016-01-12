package cn.com.lazyhome.webcatch.fetch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import cn.com.lazyhome.webcatch.fetch.dao.DownloadDao;
import cn.com.lazyhome.webcatch.fetch.dao.DownloadDaoImpl;
import cn.com.lazyhome.webcatch.fetch.util.ParamUtil;

/**
 * 网页链接有六个状态：等待-waiting，已下载-downloaded，处理中-processing，处理完-processed，分析中-
 * analyzing，分析完-analyzed
 * 
 * @author rainbow
 * 
 */
public class DownloaderImpl implements Downloader {
	private static final Log logger = LogFactory.getLog(DownloaderImpl.class);
	private ExecutorService pool;// = Executors.newFixedThreadPool(5);
	
	/**
	 * 没有指定文件名时，给予的默认文件名
	 */
	public static final String DEFAULT_FILE_NAME = "_.html";
	
	public static Properties config;
	/**
	 * 结尾不带斜杠
	 */
	public static String localDir;
	private String baseDir;

	public HashMap<String, UrlPage> downPage(URL parent, URL url) {
		logger.debug("begin downPage(String parent, String url)");
		// TODO 下载网页
		String level = config.getProperty("app.downloader.level");
		int lev = Integer.parseInt(level);

		return downPage(parent, url, lev);
	}

	public HashMap<String, UrlPage> downPage(URL parent, final URL url, final int level) {
		// 使用map保证url不重复
		HashMap<String, UrlPage> res = new HashMap<String, UrlPage>();
		
		if(logger.isDebugEnabled()) {
			logger.debug("begin downPage(String parent, String url, int level)");
			logger.debug(parent);
			logger.debug(url);
			logger.debug(level);
		}
		
		DownloadDao downloadDao = new DownloadDaoImpl();
		// 开启多线程下载模式
		ExecutorService pool = Executors.newFixedThreadPool(3);
		
		// TODO 下载网页
		Vector<String> resourceXPaths = new Vector<String>();
		// 超链接 <a href="http://freebdsmsexvideos.net/"><img
		// src="http://freebdsmsexvideos.net/wp-content/themes/adult-theme28/images/logo.png"
		// width="557" height="90" border="0" style="padding-top:10px;" /></a>
		String xpath = "//a/@href";
		resourceXPaths.add(xpath);
		// 图片 <img
		// src="http://freebdsmsexvideos.net/wp-content/themes/adult-theme28/images/logo.png"
		// width="557" height="90" border="0" style="padding-top:10px;" />
		xpath = "//img/@src";
		resourceXPaths.add(xpath);
		// 脚本 <script type='text/javascript'
		// src='http://freebdsmsexvideos.net/wp-includes/js/jquery/jquery.js?ver=1.3.2'></script>
		xpath = "//script/@src";
		resourceXPaths.add(xpath);
		// css <link rel="stylesheet"
		// href="http://freebdsmsexvideos.net/wp-content/themes/adult-theme28/style.css"
		// type="text/css" media="screen" />
		xpath = "//script/@src";
		resourceXPaths.add(xpath);

		// 1. 下载入口，downloaded，下载前到数据库查询一遍确认是否已存在
		// 本模块只负责下载，是否已存在由调用程序确认
		try {
			String filename = File.separator + url.getHost() + url.getPath();
			
			File localFile;
			
			//检查path是否带有文件名
			if(filename.endsWith("/")) {
				filename = filename + DEFAULT_FILE_NAME;
			}
			localFile = new File(localDir, filename);
			
			saveUrlFile(url, localFile);
			// 标记已下载
			downloadDao.markFetched(url.toString(), level);
			
			// 如果没有更多的下载层级要求，则不再下载，退出循环
			if(level <= 0) {
				return res;
			}

			// 标记为开始分析网页内容，解析出更多需要的URL
			downloadDao.markAnalyzing(url.toString(), level);
			
			// 2. 处理页面，提取需要下载的链接资源，把没主机的补上。
			HtmlCleaner cleaner = new HtmlCleaner();
			TagNode html = cleaner.clean(localFile);

//			List<String> resources = new Vector<String>();
			
			int urlCount=0;
			int skipUrl=0;
			int sumUrl=0;
			for(String path : resourceXPaths) {
				skipUrl = 0;
				urlCount = 0;
				logger.info("使用" + path + "解析URL");
				Object[] objs = html.evaluateXPath(path);
				
				for(int i=0; i < objs.length; i++) {
					String analyzedUrl = objs[i].toString();
					logger.trace(analyzedUrl);
					urlCount++;
					
					URL u = null;
					try {
						u = new URL(url, analyzedUrl);
					} catch (MalformedURLException e) {
						// 出现不合法URL，自动跳过
						logger.debug("下载模块解析页面URL时,出现不合法URL自动跳过【" + analyzedUrl + "】");
						continue;
					}
					
					String key = u.getHost() + u.getPath();
					if(res.containsKey(key)) {
						logger.trace("skip:" + analyzedUrl);
						skipUrl++;
						continue;
					} else {
						UrlPage value = new UrlPage();
						value.setRefer(url.toString());
						String ustr = u.toString();
						if(ustr.contains("#")) {
							ustr = ustr.substring(0, ustr.indexOf("#"));
						}
						value.setUrl(ustr);
						value.setStatus(UrlPage.STATUS_NEW);
						value.setLevel(level - 1);
						
						res.put(key, value);
							
							// 3. 递归下载新页面
//							pool.execute(new Runnable() {
//								
//								public void run() {
//									logger.trace("Type1426641219818.run start...");
//									
//									downPage(url, value, level-1);
//									
//									logger.trace("Type1426641219818.run end.");
//								}
//							});
					}
				}
				logger.info("urlCount:" + urlCount + "skipUrl:" + skipUrl + "path:" + path);
				sumUrl = sumUrl + urlCount;
				
//				List<String> list = Arrays.asList(objs);
				// 为了去除重复URL，不保证性能
//				resources.removeAll(list);
//				resources.addAll(list);
				
				// 标记为已分析完成
				downloadDao.markAnalyzed(url.toString(), level);
			}
			
			logger.info("sumUrl:" + sumUrl);

			// 3. 递归下载新页面
//			downPage(url, url, level);

		} catch (XPatherException e) {
			logger.warn("DownloaderImpl.downPage XPatherException");
			markStatus(url, level, UrlPage.STATUS_UNANALYZED);
		} catch (IOException e1) {
			logger.warn("DownloaderImpl.downPage IOException");
			markStatus(url, level, UrlPage.STATUS_UNFETCHED);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("DownloaderImpl.downPage SQLException", e);
			
			// 数据已抓取并分析，存入数据库时失败，结果没有保存，相当于白干
			markStatus(url, level, UrlPage.STATUS_UNANALYZED);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("DownloaderImpl.downPage other Exception", e);
			markStatus(url, level, UrlPage.STATUS_UNKNOWN);
		}
		
		return res;
	}

	/**
	 * 标记处理状态
	 * @param url 
	 * @throws SQLException 
	 */
	private void markStatus(URL url, int level, String status) {
		logger.trace("DownloaderImpl.markStatus start...");
		
		DownloadDao dao = new DownloadDaoImpl();
		try {
			dao.markStatus(url.toString(), level, status);
		} catch(SQLException e) {
			logger.error("DownloaderImpl.markStatus SQLException", e);
		}
		
		logger.trace("DownloaderImpl.markStatus end.");
	}
	

	/**
	 *  获取网络文件，转存到destFile中，destFile需要带文件后缀名
	 * @param url 采集的网页
	 * @param destFile 本地保存的文件
	 * @throws IOException 
	 */
	public void saveUrlFile(URL url, File destFile) throws IOException {
		logger.debug("begin saveUrlFile(String fileUrl, File destFile)");
		
		File pathFile = destFile.getParentFile();
		
		// 检查文件路径是否存在
		pathFile.mkdirs();
		File toFile = destFile;
		if (toFile.exists()) {
			// TODO 文件已存在，是否重新下载
			// throw new Exception("file exist");
			return;
		}
		toFile.createNewFile();
		FileOutputStream outImgStream = new FileOutputStream(toFile);
		outImgStream.write(getUrlFileData(url));
		outImgStream.close();
	}

	/**
	 *  获取链接地址文件的byte数据
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public byte[] getUrlFileData(URL url) throws IOException  {
		logger.debug("begin getUrlFileData(String fileUrl)");
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.connect();
		InputStream cin = httpConn.getInputStream();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = cin.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		cin.close();
		byte[] fileData = outStream.toByteArray();
		outStream.close();
		return fileData;
	}

	/**
	 * 初始化系统参数
	 */
	public void initParam() {
		// 初始化参数
		ParamUtil param = new ParamUtil();
		config = param.loadParam();
		String paramDir = config.getProperty("app.downloader.localDir");
		logger.trace("app.downloader.localDir: " + paramDir);
		
		localDir = paramDir;
		File f = new File(localDir);
		
		//判断参数是否绝对路径
		if (f.isAbsolute()) {
			logger.debug("本地暂存目录：\t" + localDir);
		} else {
			f = new File(param.getBaseDir(), paramDir);
			if(f.isAbsolute()) {
				logger.debug("本地暂存目录：\t" + f.getPath());
			} else {
				logger.warn("无法获取本地暂存目录：\t" + f.getPath());
			}
		}
		
		
		// 数据库相关参数
//		String dbpassword = config.getProperty("config.db.password");
//		String dbusername = config.getProperty("config.db.username");
//		String dburl = config.getProperty("config.db.url");
//		String dbdriver = config.getProperty("config.db.driverClass");
//		
//		DBUtil.getInstance(dbdriver, dburl, dbusername, dbpassword);
	}

	public HashMap<String, UrlPage> downPage(String parent, String url) {
		logger.trace("Downloader.downPage start...");
		
		// TODO Auto-generated method stub
		try {
			return downPage(new URL(parent), new URL(url));
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		
//		logger.trace("Downloader.downPage end.");
	}
	public HashMap<String, UrlPage> downPage(String parent, String url, final int level) {
		logger.trace("Downloader.downPage start...");
		HashMap<String, UrlPage> res = new HashMap<String, UrlPage>();
		
		// TODO Auto-generated method stub
		try {
			if(parent == null) {
				return downPage(null, new URL(url), level);
			} else {
				return downPage(new URL(parent), new URL(url), level);
			}
		} catch (MalformedURLException e) {
			logger.warn("parent:" + parent);
			logger.warn("url:" + url);
			logger.warn("level:" + level);
			
			return res;
		}
		
//		logger.trace("Downloader.downPage end.");
	}

	public ExecutorService getPool() {
		return pool;
	}

	public void setPool(ExecutorService pool) {
		this.pool = pool;
	}

}
