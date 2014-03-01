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
	private String size;
	/**
	 * 出品年份
	 */
	private String releaseYear;
	/**
	 * 工作室
	 */
	private String studio;
	/**
	 * 格式：mp4
	 */
	private String format;
	/**
	 * 长度
	 */
	private String duration;
	/**
	 * 视频参数
	 */
	private String video;
	/**
	 * 音频参数
	 */
	private String audio;
	/**
	 * 风格、题材
	 */
	private String genres;
	private String err;

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
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getReleaseYear() {
		return releaseYear;
	}
	public void setReleaseYear(String releaseYear) {
		this.releaseYear = releaseYear;
	}
	public String getStudio() {
		return studio;
	}
	public void setStudio(String studio) {
		this.studio = studio;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public String getAudio() {
		return audio;
	}
	public void setAudio(String audio) {
		this.audio = audio;
	}
	public String getGenres() {
		return genres;
	}
	public void setGenres(String genres) {
		this.genres = genres;
	}
	public String getErr() {
		return err;
	}
	public void setErr(String err) {
		this.err = err;
	}
}
