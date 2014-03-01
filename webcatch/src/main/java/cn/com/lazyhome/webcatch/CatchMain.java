package cn.com.lazyhome.webcatch;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
		String uri = "http://freebdsmsexvideos.net/category/all-bdsm/";
		uri = System.getProperty("user.dir") + "/src/test/resources/view-source freebdsmsexvideos.net category all-bdsm.htm";

		new CatchMain().analyseList(uri);
		new CatchMain().getlist();
	}
	
	void getlist() {
		PostDao dao = new PostDaoImp();
		System.out.println(dao.count());
		Util.showList(dao.listAll());
	}

	/**
	 * 
	 * @param url
	 *            http://freebdsmsexvideos.net/category/all-bdsm
	 */
	private List<Post> analyseList(String uri) {
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
			TagNode node = cleaner.clean(new File(uri));
			
//			URL url = new URL(uri);
//			TagNode node = cleaner.clean(new URL(uri));

			TagNode[] nodes = ((TagNode)node.evaluateXPath(xpathposts)[0]).getChildTags();
			String postname;
			PostDao dao = new PostDaoImp();

			for (int i = 0; i < nodes.length; i++) {
				TagNode postNd = (TagNode) nodes[i];
				
				postname = postNd.getAttributeByName("class");
				
				if(postname != null && postname.contains("post")) {
					logger.debug(i);
//					TagNode info = (TagNode) postNd.evaluateXPath(xpathp)[0];
					
					Post post = dao.convert(postNd);
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
