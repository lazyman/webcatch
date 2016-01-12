package cn.com.lazyhome.webcatch.fetch.dao;

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
	 * 根据主键（url，level)查找UrlPage
	 * @param url
	 * @return
	 * @throws SQLException
	 */
	UrlPage find(String url, int level) throws SQLException;
	/**
	 * 根据已有对象的主键查找UrlPage
	 * @param url
	 * @param level
	 * @return
	 * @throws SQLException
	 */
	UrlPage find(UrlPage url) throws SQLException;
	/**
	 * 新增一个url到数据库
	 * @param url
	 * @throws SQLException
	 */
	void addNew(UrlPage url) throws SQLException;
	/**
	 * 修改一个url
	 * @param url
	 * @throws SQLException
	 */
	void update(UrlPage url) throws SQLException;

	/**
	 * 将解析出来的 resource URL保存到数据库，已存在不做处理，没有的就新增
	 * @param resources
	 */
	void save(HashMap<String, UrlPage> resources) throws SQLException;

	/**
	 * 标记已下载
	 * @param url
	 */
	void markFetched(String url, int level) throws SQLException;

	/**
	 * 标记为开始分析网页内容，解析出更多需要的URL
	 * @param url
	 */
	void markAnalyzing(String url, int level) throws SQLException;

	/**
	 * 标记为已分析完成
	 * @param url
	 */
	void markAnalyzed(String url, int level) throws SQLException;

	/**
	 * 标记为开始下载
	 * @param url
	 * @throws SQLException
	 */
	public void markDownloading(String url, int level) throws SQLException;
	public void markStatus(String url, int level, String status) throws SQLException;
	public void initURLstatus() throws SQLException;
}
