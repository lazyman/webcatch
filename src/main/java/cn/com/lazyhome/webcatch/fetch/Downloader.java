/**
 * 
 */
package cn.com.lazyhome.webcatch.fetch;

import java.net.MalformedURLException;
import java.net.URL;

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
	void downPage(URL parent, URL url);
	/**
	 * parent-�ϼ���ַ��url-���ص�ַ��level-�㼶��Ĭ��3����ҳ��һ����ͼƬ��Դҳһ����չ����ͼһ����
	 * @param parent �ϼ���ַ
	 * @param url ���ص�ַ
	 * @param level �㼶��Ĭ��3����ҳ��һ����ͼƬ��Դҳһ����չ����ͼһ����
	 */
	void downPage(URL parent, URL url, int level);
	void downPage(String parent, String url) throws MalformedURLException;
}
