/**
 * 
 */
package cn.com.lazyhome.webcatch.fetch;

import java.util.List;

/**
 * 解析模块
 * 
 * @author rainbow
 * 
 */
public interface Analyzer {
	/**
	 * 解析首页
	 */
	void analyzeHomePage();

	/**
	 * 解析新闻列表
	 * @return
	 */
	List<Thread> analyzeThreadList();

	/**
	 * 解析单个新闻单元
	 * @return
	 */
	Thread analyzeThread();
}
