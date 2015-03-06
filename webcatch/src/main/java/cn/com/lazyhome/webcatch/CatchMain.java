package cn.com.lazyhome.webcatch;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.JDomSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.jdom2.Document;

import cn.com.lazyhome.webcatch.util.PostDao;
import cn.com.lazyhome.webcatch.util.PostDaoImp;
import cn.com.lazyhome.webcatch.util.Util;

public class CatchMain {
	private static final Log logger = LogFactory.getLog(CatchMain.class);

	public static void main(String[] args) {
		String uri = "http://freebdsmsexvideos.net/category/all-bdsm/page/1";
		uri = System.getProperty("user.dir") + "/src/test/resources/all-bdsm.htm";
		uri = System.getProperty("user.dir") + "/src/test/resources/all-bdsm-with-category.htm";

		new CatchMain().analyseList(uri);
//		new CatchMain().getlist();
		
//		uri = System.getProperty("user.dir") + "/src/test/resources/post-detail.htm";
		
//		new CatchMain().analysePost(uri);
//		new CatchMain().analysePost();
	}
	
	void getlist() {
		PostDao dao = new PostDaoImp();
		System.out.println(dao.count());
		Util.showList(dao.listAll());
	}

	/**
	 * 启动程序
	 */
	private void start() {
		//取列表线程
		analyse(1, 10);
		
		// 取详细信息线程
		analysePost();
	}
	/**
	 * 循环分析列表
	 * @param start
	 * @param end
	 */
	private void analyse(int start, int end) {
		String baseurl = "http://freebdsmsexvideos.net/category/all-bdsm/page/";
		int i;
		String url;
		
		for(i=start; i<=end; i++) {
			url = baseurl + i;
			analyseList(url);
		}
	}
	/**
	 * 从指定URL解析页面，将页面中各个post的url和title存入数据库
	 * @param url
	 *            http://freebdsmsexvideos.net/category/all-bdsm/page/1
	 *            http://freebdsmsexvideos.net/category/all-bdsm/page/32
	 */
	public List<Post> analyseList(String uri) {
		Vector<Post> posts = new Vector<Post>();
		try {
			String xpathposts = "//*[@id=\"post-230831\"]/div/div/div[1]/h2/a";
//			xpathstr = "//div[@id=\"content\"]/div[contains(@class,\"post\")]";
//			xpathposts = "//div[@id=\"content\"]/div[matches(@id,\"post-\\d+\")]";
			xpathposts = "//div[@id=\"content\"]";
			//*[@id="post-230831"]/div/div/div[1]/h2/a
			
			String xpathp = "//div[@class=\"post_top\"]/div[@class=\"post_bttm\"]/div[@class=\"title\"]/h2/a";
//			xpathp = "//div[@class=\"title\"]";
			//*[@id="post-230831"]/div/div/div[1]/h2/a

			logger.info(uri);
			HtmlCleaner cleaner = new HtmlCleaner();
//			TagNode node = cleaner.clean(new File(uri));
			TagNode node = cleaner.clean(new URL(uri));

			TagNode[] nodes = ((TagNode)node.evaluateXPath(xpathposts)[0]).getChildTags();
			String postname;
			PostDao dao = new PostDaoImp();

			for (int i = 0; i < nodes.length; i++) {
				TagNode postNd = (TagNode) nodes[i];
				
				postname = postNd.getAttributeByName("class");
				
				if(postname != null && postname.contains("post")) {
					logger.debug(i);
//					TagNode info = (TagNode) postNd.evaluateXPath(xpathp)[0];
					
					Post post = dao.convertListNode(postNd);
					logger.debug(post);
					dao.save(post);
//				} else {
//					logger.debug(postname);
				}
			}
			

		} catch (MalformedURLException e) {
			logger.warn("url error", e);
		} catch (IOException e) {
			logger.warn("io error", e);
		} catch (XPatherException e) {
			logger.warn("xpath error", e);
		}  finally {

		}

		return null;
	}
	
	/**
	 * 从数据库取出信息部完整的数据进行更新
	 */
	private void analysePost() {
		PostDao dao = new PostDaoImp();
		List<Post> posts = dao.getUnUpdate();
		
		Iterator<Post> iter = posts.iterator();
		while(iter.hasNext()) {
			Post p = iter.next();
			analysePost(p.getUrl());
		}
	}
	
	/**
	 * 根据uri解析该页面的信息，最终更新数据库中的数据
	 * @param uri
	 * @return
	 */
	public Post analysePost(String uri) {
		PostDao dao = new PostDaoImp();
		
		Post post = new Post();
		post.setUrl(uri);
		try {
			String xpathNode = "//div[@class=\"post_top\"]/div[@class=\"post_bttm\"]";
			

			logger.info(uri);
			HtmlCleaner cleaner = new HtmlCleaner();
			TagNode node = cleaner.clean(new URL(uri));

			node = (TagNode)node.evaluateXPath(xpathNode)[0];
			
			post = dao.convertPostNode(node);
			dao.update(post);
			
//			System.out.println(post.getContent());
//			System.out.println(post);
		} catch (MalformedURLException e) {
			logger.warn("url error", e);
		} catch (IOException e) {
			logger.warn("io error", e);
			
			post.setErr(e.getMessage());
			post.setSize("0");
			post.setCates(new Vector<CateMap>());
			
			dao.update(post);
			dao.recordErr(post);
		} catch (XPatherException e) {
			logger.warn("xpath error", e);
		}  finally {

		}

		return null;
	}

	private List<Post> analysexml(String uri) {
		Vector<Post> posts = new Vector<Post>();
		try {
			String xpathstr = "//*[@id=\"post-230831\"]/div/div/div[1]/h2/a";
			xpathstr = "//div[@id=\"content\"]/div[contains(@class,\"post\")]";
			//*[@id="post-230831"]/div/div/div[1]/h2/a

			logger.info(uri);
			HtmlCleaner cleaner = new HtmlCleaner();
			TagNode node = cleaner.clean(new File(uri));
			
//			URL url = new URL(uri);
//			TagNode node = cleaner.clean(new URL(uri));

		
			JDomSerializer domSerializer = new JDomSerializer(cleaner.getProperties());
			Document doc = domSerializer.createJDom(node);

		} catch (MalformedURLException e) {
			logger.warn("url error", e);
		} catch (IOException e) {
			logger.warn("io error", e);
		}  finally {

		}

		return null;
	}
}
