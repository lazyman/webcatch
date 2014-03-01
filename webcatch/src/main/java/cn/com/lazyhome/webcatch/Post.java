package cn.com.lazyhome.webcatch;

/**
 * 博客
 * @author rainbow
 *
 */
public class Post {
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 
	 */
	private String url;
	/**
	 * 
	 */
	private String content;
	/**
	 * 文件大小
	 */
	private float size;

	/**
	 * @return 
	 * 99.5m  ----  title<br />
	 * 2014/02/28/danielle-trixie-lacey-sheen/
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(size);
		sb.append("  ----  ");
		sb.append(title);
		sb.append("\n");
		sb.append(url);
		
		return sb.toString();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Post) {
			Post p = (Post) obj;
			
			return url.equals(p.getUrl()) && title.equals(p.getTitle());
		} else {
			return false;
		}
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
}
