package cn.com.lazyhome.webcatch.fetch.dao;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import cn.com.lazyhome.webcatch.fetch.UrlPage;

public interface DownloadDao {

	/**
	 * 在数据库中将需要下载的记录
	 * (status: {@link cn.com.lazyhome.webcatch.fetch.UrlPage.STATUS_NEW },
	 * {@link cn.com.lazyhome.webcatch.fetch.UrlPage.STATUS_DOWNLOADING STATUS_DOWNLOADING})
	 * 标记为正在下载，并加载到profiles中
	 * @return
	 * @throws SQLException 
	 */
	List<UrlPage> loadDownloaderProfile() throws SQLException;
	/**
	 * 获取所有status = STATUS_NEW 的记录
	 * @return
	 */
	List<UrlPage> getDownloaderProfile()  throws SQLException;

	/**
	 * 将解析出来的 resource URL保存到数据库
	 * @param resources
	 */
	void analyzed(HashMap<String, UrlPage> resources) throws SQLException;

	/**
	 * 标记已下载
	 * @param url
	 */
	void fetched(URL url) throws SQLException;

	/**
	 * 标记为开始分析网页内容，解析出更多需要的URL
	 * @param url
	 */
	void analyzing(URL url) throws SQLException;

	/**
	 * 标记为已分析完成
	 * @param url
	 */
	void analyzed(URL url) throws SQLException;

}
