package cn.com.lazyhome.webcatch.dao;

import java.util.List;

/**
 * ����-Thread���DAO
 * @author rainbow
 *
 */
public interface ThreadDao {
	void save(Thread thread);
	void save(List<Thread> threads);
}
