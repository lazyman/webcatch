package cn.com.lazyhome.webcatch.fetch.dao;

import java.net.URL;
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
	 * ������������ resource URL���浽���ݿ�
	 * @param resources
	 */
	void analyzed(HashMap<String, UrlPage> resources) throws SQLException;

	/**
	 * ���������
	 * @param url
	 */
	void fetched(URL url) throws SQLException;

	/**
	 * ���Ϊ��ʼ������ҳ���ݣ�������������Ҫ��URL
	 * @param url
	 */
	void analyzing(URL url) throws SQLException;

	/**
	 * ���Ϊ�ѷ������
	 * @param url
	 */
	void analyzed(URL url) throws SQLException;

}
