package cn.com.lazyhome.webcatch;

public class Category {
	/**
	 * key
	 */
	private String id;
	/**
	 * 类名
	 */
	private String catename;
	/**
	 * 类的URL
	 */
	private String url;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCatename() {
		return catename;
	}
	public void setCatename(String catename) {
		this.catename = catename;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
