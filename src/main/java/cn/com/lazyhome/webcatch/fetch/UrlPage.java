package cn.com.lazyhome.webcatch.fetch;

import java.io.File;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UrlPage {
	private static Log logger = LogFactory.getLog(UrlPage.class);
	
	private URL parent;
	private URL url;
	private int level;
	private File localFile;
	private String status;
	
	/**
	 * ��ַ״̬��
	 * NEW - �»�ȡ
	 * DOWNLOADING - ���ڵȴ�����
	 * FETCHED - ��ץȡ
	 * ANALYZING - ���ڷ���
	 * ANALYZED - �ѷ�����ҳ����
	 */
	public static final String STATUS_NEW = "NEW";
	/**
	 * {@link #STATUS_NEW}
	 */
	public static final String STATUS_DOWNLOADING = "DOWNLOADING";
	public static final String STATUS_FETCHED = "FETCHED";
	public static final String STATUS_ANALYZING = "ANALYZING";
	public static final String STATUS_ANALYZED = "ANALYZED";
	
	public URL getParent() {
		return parent;
	}
	public void setParent(URL parent) {
		this.parent = parent;
	}
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public File getLocalFile() {
		return localFile;
	}
	public void setLocalFile(File localFile) {
		this.localFile = localFile;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
