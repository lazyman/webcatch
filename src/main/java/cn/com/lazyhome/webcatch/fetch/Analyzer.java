/**
 * 
 */
package cn.com.lazyhome.webcatch.fetch;

import java.util.List;

/**
 * ����ģ��
 * 
 * @author rainbow
 * 
 */
public interface Analyzer {
	/**
	 * ������ҳ
	 */
	void analyzeHomePage();

	/**
	 * ���������б�
	 * @return
	 */
	List<Thread> analyzeThreadList();

	/**
	 * �����������ŵ�Ԫ
	 * @return
	 */
	Thread analyzeThread();
}
