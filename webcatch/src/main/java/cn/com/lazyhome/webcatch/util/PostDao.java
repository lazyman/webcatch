package cn.com.lazyhome.webcatch.util;

import java.util.List;

import org.htmlcleaner.TagNode;

import cn.com.lazyhome.webcatch.Post;

public interface PostDao {

	/**
	 * 插入数据
	 * @param post
	 */
	void insert(Post post);
	/**
	 * 修改数据
	 * @param post
	 */
	void update(Post post);
	/**
	 * 自动判断插入或修改数据，当有重复数据时，只更新url和title
	 * @param post
	 */
	void save(Post post);
	/**
	 * 初始化表结构
	 */
	void init();
	/**
	 * 显示所有数据
	 * @return
	 */
	List<Post> listAll();
	/**
	 * 获取未更新的post
	 * @return
	 */
	List<Post> getUnUpdate();
	/**
	 * 根据URL地址取post
	 * @param url
	 * @return
	 */
	Post get(String url);
	/**
	 * 数据量
	 * @return
	 */
	int count();
	/**
	 * 分析列表中的节点信息
	 * @param node
	 * @return
	 */
	Post convertListNode(TagNode node);
	/**
	 * 分析节点的详细信息
	 * @param node
	 * @return
	 */
	Post convertPostNode(TagNode node);
	/**
	 * 将错误信息记入数据库
	 * @param post
	 */
	void recordErr(Post post);
}
