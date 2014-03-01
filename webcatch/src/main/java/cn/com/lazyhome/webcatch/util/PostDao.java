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
	 * 自动判断插入或修改数据
	 * @param post
	 * @param insert
	 */
	void save(Post post);
	/**
	 * 根据标志判断插入或修改数据
	 * @param post 数据内容
	 * @param insert true: 插入数据, false: 修改数据
	 */
	void save(Post post, boolean insert);
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
}
