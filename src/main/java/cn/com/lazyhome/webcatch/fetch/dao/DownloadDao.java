package cn.com.lazyhome.webcatch.fetch.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import cn.com.lazyhome.webcatch.fetch.UrlPage;

public interface DownloadDao {

	/**
	 * �����ݿ��н���Ҫ���صļ�¼
	 * (status: {@link cn.com.lazyhome.webcatch.fetch.UrlPage.STATUS_NEW },
	 * {@link cn.com.lazyhome.webcatch.fetch.UrlPage.STATUS_DOWNLOADING STATUS_DOWNLOADING})
	 * ���Ϊ�������أ������ص�profiles��
	 * @return
	 * @throws SQLException 
	 */
	List<UrlPage> loadDownloaderProfile() throws SQLException;
	/**
	 * ��ȡ����status = STATUS_NEW �ļ�¼
	 * @return
	 */
	List<UrlPage> getDownloaderProfile()  throws SQLException;
	
	/**
	 * ����������url��level)����UrlPage
	 * @param url
	 * @return
	 * @throws SQLException
	 */
	UrlPage find(String url, int level) throws SQLException;
	/**
	 * �������ж������������UrlPage
	 * @param url
	 * @param level
	 * @return
	 * @throws SQLException
	 */
	UrlPage find(UrlPage url) throws SQLException;
	/**
	 * ����һ��url�����ݿ�
	 * @param url
	 * @throws SQLException
	 */
	void addNew(UrlPage url) throws SQLException;
	/**
	 * �޸�һ��url
	 * @param url
	 * @throws SQLException
	 */
	void update(UrlPage url) throws SQLException;

	/**
	 * ������������ resource URL���浽���ݿ⣬�Ѵ��ڲ�������û�еľ�����
	 * @param resources
	 */
	void save(HashMap<String, UrlPage> resources) throws SQLException;

	/**
	 * ���������
	 * @param url
	 */
	void markFetched(String url, int level) throws SQLException;

	/**
	 * ���Ϊ��ʼ������ҳ���ݣ�������������Ҫ��URL
	 * @param url
	 */
	void markAnalyzing(String url, int level) throws SQLException;

	/**
	 * ���Ϊ�ѷ������
	 * @param url
	 */
	void markAnalyzed(String url, int level) throws SQLException;

	/**
	 * ���Ϊ��ʼ����
	 * @param url
	 * @throws SQLException
	 */
	public void markDownloading(String url, int level) throws SQLException;
	public void markStatus(String url, int level, String status) throws SQLException;
	public void initURLstatus() throws SQLException;
}
