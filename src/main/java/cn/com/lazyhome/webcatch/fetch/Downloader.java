/**
 * 
 */
package cn.com.lazyhome.webcatch.fetch;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 下载模块
 * @author rainbow
 *
 */
public interface Downloader {
	/**
	 * 初始化下载器的环境参数，参数内容来源于数据库记录，数据库参数和第一次默认参数存于app.properties
	 */
	void initParam();
	/**
	 * parent-上级地址，url-下载地址，level-层级，默认3（本页面一级，图片资源页一级，展开大图一级）
	 * @param parent 上级地址
	 * @param url 下载地址
	 */
	void downPage(URL parent, URL url);
	/**
	 * parent-上级地址，url-下载地址，level-层级，默认3（本页面一级，图片资源页一级，展开大图一级）
	 * @param parent 上级地址
	 * @param url 下载地址
	 * @param level 层级，默认3（本页面一级，图片资源页一级，展开大图一级）
	 */
	void downPage(URL parent, URL url, int level);
	void downPage(String parent, String url) throws MalformedURLException;
}
