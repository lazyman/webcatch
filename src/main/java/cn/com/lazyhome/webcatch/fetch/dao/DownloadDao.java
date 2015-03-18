package cn.com.lazyhome.webcatch.fetch.dao;

import java.net.URL;
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
	 */
	List<UrlPage> loadDownloaderProfile();
	/**
	 * ��ȡ����status = STATUS_NEW �ļ�¼
	 * @return
	 */
	List<UrlPage> getDownloaderProfile() ;

	/**
	 * ������������ resource URL���浽���ݿ�
	 * @param resources
	 */
	void analyzed(HashMap<String, UrlPage> resources);

	/**
	 * ���������
	 * @param url
	 */
	void fetched(URL url);

	/**
	 * ���Ϊ��ʼ������ҳ���ݣ�������������Ҫ��URL
	 * @param url
	 */
	void analyzing(URL url);

	/**
	 * ���Ϊ�ѷ������
	 * @param url
	 */
	void analyzed(URL url);

}
