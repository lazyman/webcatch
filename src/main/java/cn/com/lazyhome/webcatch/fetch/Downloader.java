/**
 * 
 */
package cn.com.lazyhome.webcatch.fetch;

/**
 * ����ģ��
 * @author rainbow
 *
 */
public interface Downloader {
	/**
	 * parent-�ϼ���ַ��url-���ص�ַ��level-�㼶��Ĭ��3����ҳ��һ����ͼƬ��Դҳһ����չ����ͼһ����
	 * @param parent �ϼ���ַ
	 * @param url ���ص�ַ
	 */
	void downPage(String parent, String url);
	/**
	 * parent-�ϼ���ַ��url-���ص�ַ��level-�㼶��Ĭ��3����ҳ��һ����ͼƬ��Դҳһ����չ����ͼһ����
	 * @param parent �ϼ���ַ
	 * @param url ���ص�ַ
	 * @param level �㼶��Ĭ��3����ҳ��һ����ͼƬ��Դҳһ����չ����ͼһ����
	 */
	void downPage(String parent, String url, int level);
}
