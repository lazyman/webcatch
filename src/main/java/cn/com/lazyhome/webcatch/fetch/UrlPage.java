package cn.com.lazyhome.webcatch.fetch;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UrlPage {
	private static Log logger = LogFactory.getLog(UrlPage.class);
	
	private String refer;
	private String url;
	private int level;
	private File localFile;
	private String status;
	
	/**
	 * 地址状态：
	 * NEW - 新获取
	 * DOWNLOADING - 正在等待下载
	 * FETCHED - 已抓取
	 * ANALYZING - 正在分析
	 * ANALYZED - 已分析网页内容
	 */
	public static final String STATUS_NEW = "NEW";
	/**
	 * {@link #STATUS_NEW}
	 */
	public static final String STATUS_DOWNLOADING = "DOWNLOADING";
	public static final String STATUS_FETCHED = "FETCHED";
	public static final String STATUS_ANALYZING = "ANALYZING";
	public static final String STATUS_ANALYZED = "ANALYZED";
	
	public static final String STATUS_UNANALYZED = "UNANALYZED";
	public static final String STATUS_UNFETCHED = "UNFETCHED";
	public static final String STATUS_UNKNOWN = "UNKNOWN";
	
	
	public UrlPage() {
		super();
	}


	public UrlPage(String refer, String url, int level) {
		super();
		this.refer = refer;
		this.url = url;
		this.level = level;
	}


	public String getRefer() {
		return refer;
	}
	public void setRefer(String parent) {
		this.refer = parent;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
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
