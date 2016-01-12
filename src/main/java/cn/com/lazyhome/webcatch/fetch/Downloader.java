/**
 * 
 */
package cn.com.lazyhome.webcatch.fetch;

import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

/**
 * ����ģ��
 * @author rainbow
 *
 */
public interface Downloader {
	/**
	 * ��ʼ���������Ļ�������������������Դ�����ݿ��¼�����ݿ�����͵�һ��Ĭ�ϲ�������app.properties
	 */
	void initParam();
	/**
	 * parent-�ϼ���ַ��url-���ص�ַ��level-�㼶��Ĭ��3����ҳ��һ����ͼƬ��Դҳһ����չ����ͼһ����
	 * @param parent �ϼ���ַ
	 * @param url ���ص�ַ
	 */
	HashMap<String, UrlPage> downPage(URL parent, URL url);
	/**
	 * parent-�ϼ���ַ��url-���ص�ַ��level-�㼶��Ĭ��3����ҳ��һ����ͼƬ��Դҳһ����չ����ͼһ����
	 * @param parent �ϼ���ַ
	 * @param url ���ص�ַ
	 * @param level �㼶��Ĭ��3����ҳ��һ����ͼƬ��Դҳһ����չ����ͼһ����
	 * @return 
	 */
	HashMap<String, UrlPage> downPage(URL parent, URL url, int level);
	HashMap<String, UrlPage> downPage(String parent, String url);
	void setPool(ExecutorService pool);
	HashMap<String, UrlPage> downPage(String refer, String url, int level);
}
