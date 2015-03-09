/**
 * 
 */
package cn.com.lazyhome.webcatch.fetch;

/**
 * 下载模块
 * @author rainbow
 *
 */
public interface Downloader {
	/**
	 * parent-上级地址，url-下载地址，level-层级，默认3（本页面一级，图片资源页一级，展开大图一级）
	 * @param parent 上级地址
	 * @param url 下载地址
	 */
	void downPage(String parent, String url);
	/**
	 * parent-上级地址，url-下载地址，level-层级，默认3（本页面一级，图片资源页一级，展开大图一级）
	 * @param parent 上级地址
	 * @param url 下载地址
	 * @param level 层级，默认3（本页面一级，图片资源页一级，展开大图一级）
	 */
	void downPage(String parent, String url, int level);
}
