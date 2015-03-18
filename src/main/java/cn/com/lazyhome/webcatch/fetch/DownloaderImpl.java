package cn.com.lazyhome.webcatch.fetch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
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
import cn.com.lazyhome.webcatch.fetch.util.DBUtil;
import cn.com.lazyhome.webcatch.fetch.util.ParamUtil;

/**
 * ��ҳ����������״̬���ȴ�-waiting��������-downloaded��������-processing��������-processed��������-
 * analyzing��������-analyzed
 * 
 * @author rainbow
 * 
 */
public class DownloaderImpl implements Downloader {
	private static final Log logger = LogFactory.getLog(DownloaderImpl.class);
	private ExecutorService pool;// = Executors.newFixedThreadPool(5);
	
	/**
	 * û��ָ���ļ���ʱ�������Ĭ���ļ���
	 */
	public static final String DEFAULT_FILE_NAME = "_";
	
	public static Properties config;
	/**
	 * ��β����б��
	 */
	public static String localDir;
	private String baseDir;

	public HashMap<String, UrlPage> downPage(URL parent, URL url) {
		logger.debug("begin downPage(String parent, String url)");
		// TODO ������ҳ
		String level = config.getProperty("app.downloader.level");
		int lev = Integer.parseInt(level);

		return downPage(parent, url, lev);
	}

	public HashMap<String, UrlPage> downPage(URL parent, final URL url, final int level) {
		if(logger.isDebugEnabled()) {
			logger.debug("begin downPage(String parent, String url, int level)");
			logger.debug(parent);
			logger.debug(url);
			logger.debug(level);
		}
		
		
		// �������߳�����ģʽ
		ExecutorService pool = Executors.newFixedThreadPool(3);
		
		// TODO ������ҳ
		Vector<String> resourceXPaths = new Vector<String>();
		// ������ <a href="http://freebdsmsexvideos.net/"><img
		// src="http://freebdsmsexvideos.net/wp-content/themes/adult-theme28/images/logo.png"
		// width="557" height="90" border="0" style="padding-top:10px;" /></a>
		String xpath = "//a/@href";
		resourceXPaths.add(xpath);
		// ͼƬ <img
		// src="http://freebdsmsexvideos.net/wp-content/themes/adult-theme28/images/logo.png"
		// width="557" height="90" border="0" style="padding-top:10px;" />
		xpath = "//img/@src";
		resourceXPaths.add(xpath);
		// �ű� <script type='text/javascript'
		// src='http://freebdsmsexvideos.net/wp-includes/js/jquery/jquery.js?ver=1.3.2'></script>
		xpath = "//script/@src";
		resourceXPaths.add(xpath);
		// css <link rel="stylesheet"
		// href="http://freebdsmsexvideos.net/wp-content/themes/adult-theme28/style.css"
		// type="text/css" media="screen" />
		xpath = "//script/@src";
		resourceXPaths.add(xpath);

		// 1. ������ڣ�downloaded������ǰ�����ݿ��ѯһ��ȷ���Ƿ��Ѵ���
		// ��ģ��ֻ�������أ��Ƿ��Ѵ����ɵ��ó���ȷ��
		try {
			String filename = File.separator + url.getHost() + url.getPath();
			File localFile;
			
			//���path�Ƿ�����ļ���
			if(filename.lastIndexOf('/') > 0) {
				localFile = new File(localDir + filename, DEFAULT_FILE_NAME);
			} else {
				localFile = new File(localDir, filename);
			}
			
			saveUrlFile(url, localFile);
			// ���������
			fetched(url);
			
			// ���û�и�������ز㼶Ҫ���������أ��˳�ѭ��
			if(level <= 0) {
				return null;
			}

			// ���Ϊ��ʼ������ҳ���ݣ�������������Ҫ��URL
			analyzing(url);
			
			// 2. ����ҳ�棬��ȡ��Ҫ���ص�������Դ����û�����Ĳ��ϡ�
			HtmlCleaner cleaner = new HtmlCleaner();
			TagNode html = cleaner.clean(localFile);

//			List<String> resources = new Vector<String>();
			// ʹ��map��֤url���ظ�
			HashMap<String, UrlPage> res = new HashMap<String, UrlPage>();
			
			for(String path : resourceXPaths) {
				logger.info("ʹ��" + path + "����URL");
				Object[] objs = html.evaluateXPath(path);
				
				for(int i=0; i < objs.length; i++) {
					String key = objs[i].toString();
					if(res.containsKey(key)) {
						continue;
					} else {
						final URL u;
						try {
							u = new URL(key);
							UrlPage value = new UrlPage();
							value.setParent(url);
							value.setUrl(u);
							value.setStatus(UrlPage.STATUS_NEW);
							value.setLevel(level - 1);
							
							res.put(key, value);
							
							// 3. �ݹ�������ҳ��
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
							
						} catch (MalformedURLException e) {
							// ���ֲ��Ϸ�URL���Զ�����
							logger.debug("����ģ�����ҳ��URLʱ,���ֲ��Ϸ�URL�Զ�����" + key, e);
						}
					}
				}
				
//				List<String> list = Arrays.asList(objs);
				// Ϊ��ȥ���ظ�URL������֤����
//				resources.removeAll(list);
//				resources.addAll(list);
				
				// ���Ϊ�ѷ������
				analyzed(url);
				
				return res;
			}

			// 3. �ݹ�������ҳ��
//			downPage(url, url, level);

		} catch (XPatherException e) {
			logger.error("DownloaderImpl.downPage XPatherException", e);
		} catch (IOException e1) {
			logger.error("DownloaderImpl.downPage IOException", e1);
		}
		
		return null;
	}

	/**
	 * ���Ϊ�ѷ������
	 * @param url 
	 */
	private void analyzed(URL url) {
		logger.trace("DownloaderImpl.analyzed start...");
		
		DownloadDao dao = new DownloadDaoImpl();
		dao.analyzed(url);
		
		logger.trace("DownloaderImpl.analyzed end.");
	}

	/**
	 * ���Ϊ��ʼ������ҳ���ݣ�������������Ҫ��URL
	 * @param url
	 */
	private void analyzing(URL url) {
		logger.trace("DownloaderImpl.analyzing start...");
		
		DownloadDao dao = new DownloadDaoImpl();
		dao.analyzing(url);
		
		logger.trace("DownloaderImpl.analyzing end.");
	}

	/**
	 * ���������
	 * @param url 
	 */
	private void fetched(URL url) {
		logger.trace("DownloaderImpl.fetched start...");
		
		DownloadDao dao = new DownloadDaoImpl();
		dao.fetched(url);
		
		logger.trace("DownloaderImpl.fetched end.");
	}
	

	// ��ȡ�����ļ���ת�浽fileDes�У�fileDes��Ҫ���ļ���׺��
	public void saveUrlFile(URL url, File fileDes) throws IOException {
		logger.debug("begin saveUrlFile(String fileUrl, File fileDes)");
		
		File pathFile = fileDes.getParentFile();
		
		// ����ļ�·���Ƿ����
		pathFile.mkdirs();
		File toFile = fileDes;
		if (toFile.exists()) {
			// throw new Exception("file exist");
			return;
		}
		toFile.createNewFile();
		FileOutputStream outImgStream = new FileOutputStream(toFile);
		outImgStream.write(getUrlFileData(url));
		outImgStream.close();
	}

	// ��ȡ���ӵ�ַ�ļ���byte����
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
	 * ��ʼ��ϵͳ����
	 */
	public void initParam() {
		// ��ʼ������
		ParamUtil param = new ParamUtil();
		config = param.loadParam();
		String paramDir = config.getProperty("app.downloader.localDir");
		logger.trace("app.downloader.localDir: " + paramDir);
		
		localDir = paramDir;
		File f = new File(localDir);
		
		//�жϲ����Ƿ����·��
		if (f.isAbsolute()) {
			logger.debug("�����ݴ�Ŀ¼��\t" + localDir);
		} else {
			localDir = param.getBaseDir() + File.separatorChar + paramDir;
			
			f = new File(localDir);
			if(f.isAbsolute()) {
				logger.debug("�����ݴ�Ŀ¼��\t" + localDir);
			} else {
				logger.warn("�޷���ȡ�����ݴ�Ŀ¼��\t" + localDir);
			}
		}
		
		
		// ���ݿ���ز���
//		String dbpassword = config.getProperty("config.db.password");
//		String dbusername = config.getProperty("config.db.username");
//		String dburl = config.getProperty("config.db.url");
//		String dbdriver = config.getProperty("config.db.driverClass");
//		
//		DBUtil.getInstance(dbdriver, dburl, dbusername, dbpassword);
	}

	public HashMap<String, UrlPage> downPage(String parent, String url) throws MalformedURLException {
		logger.trace("Downloader.downPage start...");
		
		// TODO Auto-generated method stub
		return downPage(new URL(parent), new URL(url));
		
//		logger.trace("Downloader.downPage end.");
	}

	public ExecutorService getPool() {
		return pool;
	}

	public void setPool(ExecutorService pool) {
		this.pool = pool;
	}

}
