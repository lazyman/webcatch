package cn.com.lazyhome.webcatch;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.lazyhome.webcatch.util.PostDao;
import cn.com.lazyhome.webcatch.util.PostDaoImp;

public class Main {
	public static void main(String[] args) {
		new Main().poolStart();
	}

	/**
	 * 以线程池的方式启动任务
	 */
	private void poolStart() {
		ExecutorService pool = Executors.newFixedThreadPool(20);
//		pool = Executors.newCachedThreadPool();
		
		int start=1;
		int end = 3;
		final String baseurl = "http://freebdsmsexvideos.net/category/all-bdsm/page/";
		
		//循环分析列表
		for(int i=start; i<= end; i++) {
			String uri = baseurl + i;
			Runnable t = new AnalyseListThread(uri);
			
			pool.execute(t);
		}
		
		
		boolean running = true;
		while(running) {
			//取详细信息线程
			PostDao dao = new PostDaoImp();
			List<Post> posts = dao.getUnUpdate();
	
			Iterator<Post> iter = posts.iterator();
			while(iter.hasNext()) {
				Post p = iter.next();
				Runnable t = new AnalysePostThread(p.getUrl());
				
				pool.execute(t);
			}
			
			
			running = false;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		pool.shutdown();
	}
}
