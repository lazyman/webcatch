package cn.com.lazyhome.webcatch.util;

import java.util.List;

import org.htmlcleaner.TagNode;

import cn.com.lazyhome.webcatch.Post;

public interface PostDao {

	/**
	 * ��������
	 * @param post
	 */
	void insert(Post post);
	/**
	 * �޸�����
	 * @param post
	 */
	void update(Post post);
	/**
	 * �Զ��жϲ�����޸�����
	 * @param post
	 * @param insert
	 */
	void save(Post post);
	/**
	 * ���ݱ�־�жϲ�����޸�����
	 * @param post ��������
	 * @param insert true: ��������, false: �޸�����
	 */
	void save(Post post, boolean insert);
	/**
	 * ��ʼ����ṹ
	 */
	void init();
	/**
	 * ��ʾ��������
	 * @return
	 */
	List<Post> listAll();
	/**
	 * ����URL��ַȡpost
	 * @param url
	 * @return
	 */
	Post get(String url);
	/**
	 * ������
	 * @return
	 */
	int count();
	/**
	 * �����б��еĽڵ���Ϣ
	 * @param node
	 * @return
	 */
	Post convertListNode(TagNode node);
	/**
	 * �����ڵ����ϸ��Ϣ
	 * @param node
	 * @return
	 */
	Post convertPostNode(TagNode node);
}
