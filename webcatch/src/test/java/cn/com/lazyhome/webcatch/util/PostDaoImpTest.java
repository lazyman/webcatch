package cn.com.lazyhome.webcatch.util;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cn.com.lazyhome.webcatch.Post;


public class PostDaoImpTest {
	private static final Log logger = LogFactory.getLog(PostDaoImpTest.class);
	TagNode postNd;
	//init post util
	PostDao dao = new PostDaoImp();
	
	String title = "Elise Graves Takes On 10 Inch";
	String url = "http://freebdsmsexvideos.net/2014/02/28/elise-graves-takes-on-10-inch/";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		String uri = System.getProperty("user.dir") + "/src/test/resources/view-source freebdsmsexvideos.net category all-bdsm.htm";

		System.out.println(uri);
		
		String xpathposts = "//*[@id=\"post-230831\"]/div/div/div[1]/h2/a";
		xpathposts = "//div[@id=\"content\"]";
		
		// 加载数据
		logger.info("loading data...");
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode node = cleaner.clean(new File(uri));
		
		TagNode[] nodes = ((TagNode)node.evaluateXPath(xpathposts)[0]).getChildTags();
		
		//找到第一个post，返回用于后面的测试
		String postname;
		for (int i = 0; i < nodes.length; i++) {
			TagNode postNd = (TagNode) nodes[i];
			
			postname = postNd.getAttributeByName("class");
			
			if(postname != null && postname.contains("post")) {
				logger.debug(i);

				this.postNd = postNd;
				break;
			}
		}
		
//		logger.info(postNd.getText());
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInsertAndGet() {
		Post post = dao.convert(postNd);
		
		//insert to db
		int beforeCount = dao.count();
		
		dao.insert(post);
		
		//test get
		Post p2 = dao.get(url);
		assertEquals("post", p2, post);
		logger.info("post equal");
		
		//test list
		List<Post> posts = dao.listAll();
		assertEquals(posts.get(1), post);
		logger.info("list succes");
		
		//test count
		int afterCount = dao.count();
		assertEquals("count", afterCount-beforeCount, 1);
		logger.info("count succes");
//		fail("Not yet implemented");
	}

	@Test
	public void testConvert() {
		Post p = dao.convert(postNd);
		
		assertEquals("title", p.getTitle(), title);
		assertEquals("url", p.getUrl(), url);
	}

}
