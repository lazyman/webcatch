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
	public static final String DEFAULT_FILE_NAME = "_.html";
	
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
		// ʹ��map��֤url���ظ�
		HashMap<String, UrlPage> res = new HashMap<String, UrlPage>();
		
		if(logger.isDebugEnabled()) {
			logger.debug("begin downPage(String parent, String url, int level)");
			logger.debug(parent);
			logger.debug(url);
			logger.debug(level);
		}
		
		DownloadDao downloadDao = new DownloadDaoImpl();
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
			if(filename.endsWith("/")) {
				filename = filename + DEFAULT_FILE_NAME;
			}
			localFile = new File(localDir, filename);
			
			saveUrlFile(url, localFile);
			// ���������
			downloadDao.markFetched(url.toString(), level);
			
			// ���û�и�������ز㼶Ҫ���������أ��˳�ѭ��
			if(level <= 0) {
				return res;
			}

			// ���Ϊ��ʼ������ҳ���ݣ�������������Ҫ��URL
			downloadDao.markAnalyzing(url.toString(), level);
			
			// 2. ����ҳ�棬��ȡ��Ҫ���ص�������Դ����û�����Ĳ��ϡ�
			HtmlCleaner cleaner = new HtmlCleaner();
			TagNode html = cleaner.clean(localFile);

//			List<String> resources = new Vector<String>();
			
			int urlCount=0;
			int skipUrl=0;
			int sumUrl=0;
			for(String path : resourceXPaths) {
				skipUrl = 0;
				urlCount = 0;
				logger.info("ʹ��" + path + "����URL");
				Object[] objs = html.evaluateXPath(path);
				
				for(int i=0; i < objs.length; i++) {
					String analyzedUrl = objs[i].toString();
					logger.trace(analyzedUrl);
					urlCount++;
					
					URL u = null;
					try {
						u = new URL(url, analyzedUrl);
					} catch (MalformedURLException e) {
						// ���ֲ��Ϸ�URL���Զ�����
						logger.debug("����ģ�����ҳ��URLʱ,���ֲ��Ϸ�URL�Զ�������" + analyzedUrl + "��");
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
					}
				}
				logger.info("urlCount:" + urlCount + "skipUrl:" + skipUrl + "path:" + path);
				sumUrl = sumUrl + urlCount;
				
//				List<String> list = Arrays.asList(objs);
				// Ϊ��ȥ���ظ�URL������֤����
//				resources.removeAll(list);
//				resources.addAll(list);
				
				// ���Ϊ�ѷ������
				downloadDao.markAnalyzed(url.toString(), level);
			}
			
			logger.info("sumUrl:" + sumUrl);

			// 3. �ݹ�������ҳ��
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
			
			// ������ץȡ���������������ݿ�ʱʧ�ܣ����û�б��棬�൱�ڰ׸�
			markStatus(url, level, UrlPage.STATUS_UNANALYZED);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("DownloaderImpl.downPage other Exception", e);
			markStatus(url, level, UrlPage.STATUS_UNKNOWN);
		}
		
		return res;
	}

	/**
	 * ��Ǵ���״̬
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
	 *  ��ȡ�����ļ���ת�浽destFile�У�destFile��Ҫ���ļ���׺��
	 * @param url �ɼ�����ҳ
	 * @param destFile ���ر�����ļ�
	 * @throws IOException 
	 */
	public void saveUrlFile(URL url, File destFile) throws IOException {
		logger.debug("begin saveUrlFile(String fileUrl, File destFile)");
		
		File pathFile = destFile.getParentFile();
		
		// ����ļ�·���Ƿ����
		pathFile.mkdirs();
		File toFile = destFile;
		if (toFile.exists()) {
			// TODO �ļ��Ѵ��ڣ��Ƿ���������
			// throw new Exception("file exist");
			return;
		}
		toFile.createNewFile();
		FileOutputStream outImgStream = new FileOutputStream(toFile);
		outImgStream.write(getUrlFileData(url));
		outImgStream.close();
	}

	/**
	 *  ��ȡ���ӵ�ַ�ļ���byte����
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
			f = new File(param.getBaseDir(), paramDir);
			if(f.isAbsolute()) {
				logger.debug("�����ݴ�Ŀ¼��\t" + f.getPath());
			} else {
				logger.warn("�޷���ȡ�����ݴ�Ŀ¼��\t" + f.getPath());
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
