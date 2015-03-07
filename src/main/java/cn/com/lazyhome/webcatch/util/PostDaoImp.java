package cn.com.lazyhome.webcatch.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import cn.com.lazyhome.webcatch.CateMap;
import cn.com.lazyhome.webcatch.Post;

public class PostDaoImp implements PostDao {
	private static ResultSetHandler<Post> handler = new BeanHandler<Post>(Post.class);
	private static ResultSetHandler<List<Post>> listHandler = new BeanListHandler<Post>(Post.class);
	private static final Log logger = LogFactory.getLog(PostDaoImp.class);
	private static CateMapDao cateMapDao = new CateMapDaoImp();

	public static void main(String[] args) {
		PostDao dao = new PostDaoImp();
		dao.init();
		System.out.println(dao.count());
		logger.debug(dao.get(""));
	}


	public void init() {
//		String droptable = "drop table post";
		String posttable = "drop table post; create table post(   title varchar(100) null,   url varchar(1000) null,   content varchar(4000),  size float)";

		Connection conn = null;
		try {
			conn = DBAccess.getConnection();
			
			Statement stmt = conn.createStatement();
			stmt.execute(posttable);
		} catch (SQLException e) {
			logger.error("create table", e);
		} finally {
			DBAccess.close(conn);
		}
	}
	
	public void insert(Post post) {
		Connection conn = null;
		String sql = "insert into post(title, url) values(?, ?)";
		
		if(post == null) {
			throw new IllegalArgumentException("post is null");
		}
		try {
			conn = DBAccess.getConnection();
			QueryRunner runner = new QueryRunner();
			runner.update(conn, sql, post.getTitle(), post.getUrl());
			
			//insert category
			for(CateMap cat : post.getCates()) {
				cat.setPosturl(post.getUrl());
				cateMapDao.save(cat);
			}
		} catch (SQLException e) {
			logger.debug(post);
			logger.error("open conn", e);
		} finally {
			DBAccess.close(conn);
		}
	}

	public void update(Post post) {
		Connection conn = null;
		String sql = "update post set title = ?, content=?, size=?, releaseyear =?, studio =?, format =?, "
				+ " duration =?, video =?, audio =?, genres =?, errmsg=null, filesize=?, unit=? where url=?";
		
		if(post == null) {
			throw new IllegalArgumentException("post is null");
		}
		try {
			conn = DBAccess.getConnection();
			QueryRunner runner = new QueryRunner();
			runner.update(conn, sql, post.getTitle(), post.getContent(), post.getSize(), 
					post.getReleaseYear(), post.getStudio(), post.getFormat(), post.getDuration(), 
					post.getVideo(), post.getAudio(), post.getGenres(), post.getFilesize(), post.getUnit(), post.getUrl());
			
			//insert category
			for(CateMap cat : post.getCates()) {
				cat.setPosturl(post.getUrl());
				cateMapDao.save(cat);
			}
		} catch (SQLException e) {
			post.setErr(e.getMessage());
			recordErr(post);
			
			logger.debug(post);
			logger.error("update post", e);
		} finally {
			DBAccess.close(conn);
		}
	}

	public void save(Post post) {
		Post p = get(post.getUrl());
		
		if(p == null) {
			insert(post);
		} else {
			update(post);
		}
	}

	public List<Post> listAll() {
		List<Post> posts = null;
		Connection conn = null;
		
		try {
			conn = DBAccess.getConnection();
			QueryRunner runner = new QueryRunner();
			posts = runner.query(conn, "select * from post", listHandler);
			
		} catch (SQLException e) {
			logger.error("open conn", e);
		} finally {
			DBAccess.close(conn);
		}
		
		return posts;
	}

	public int count() {
		Connection conn = null;
		String sql = "select count(*) from post";
		
		try {
			conn = DBAccess.getConnection();
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			int size = rs.getInt(1);
			
			return size;
		} catch (SQLException e) {
			logger.error("open conn", e);
		} finally {
			DBAccess.close(conn);
		}
		
		return 0;
	}

	public Post get(String url) {

		Post post = null;
		Connection conn = null;
		
		try {
			conn = DBAccess.getConnection();
			QueryRunner runner = new QueryRunner();
			post = runner.query(conn, "select * from post p where p.url = ?", handler, url);
			
		} catch (SQLException e) {
			logger.error("open conn", e);
		} finally {
			DBAccess.close(conn);
		}
		
		return post;
	}
	
	/**
	 * ͨ��xpath��λpost������url��titleȡ����url����ΪΨһ������title��Ҫ������ʾ
	 */
	public Post convertListNode(TagNode node) {
		Post post = new Post();
		
		String xpathp = "//div[@class=\"post_top\"]/div[@class=\"post_bttm\"]/div[@class=\"title\"]/h2/a";
		String xpathTag = "//div[@class=\"post_top\"]/div[@class=\"post_bttm\"]/div[@class=\"tags\"]/div[@class=\"categories\"]";
		try {
			TagNode info = (TagNode) node.evaluateXPath(xpathp)[0];

			post.setTitle(info.getText().toString());
			post.setUrl(info.getAttributeByName("href"));

			
			List<TagNode> cateNodes = ((TagNode)node.evaluateXPath(xpathTag)[0]).getChildTagList();
			
			List<CateMap> cates = cateMapDao.convertTags(cateNodes);
			post.setCates(cates);
		} catch (XPatherException e) {
			logger.warn("convert html node to post bean", e);
		}

		
		return post;
	}


	/**
	 * ͨ��xpath��λ��post������������ʽ�����ݣ�content����ȡ������
	 */
	public Post convertPostNode(TagNode node) {
		Post post = new Post();
		
		//ͨ��xpath��λ��post
		String xpathTitle = "/div[@class=\"title\"]/h2/a";
		String xpathTag = "//div[@class=\"categories\"]";
		
		try {
			String content = node.getText().toString();
			TagNode titleNode = (TagNode)node.evaluateXPath(xpathTitle)[0];
			
			post.setTitle(titleNode.getText().toString());
			post.setUrl(titleNode.getAttributeByName("href"));
			post.setContent(content);
			
			List<TagNode> cateNodes = ((TagNode)node.evaluateXPath(xpathTag)[0]).getChildTagList();
			
			List<CateMap> cates = cateMapDao.convertTags(cateNodes);
			post.setCates(cates);
			
			//����������ʽ�����ݣ�content����ȡ������
			//year
			Pattern pattern = Pattern.compile("Release Year: (\\d+)");
			Matcher matcher = pattern.matcher(content);
			if(matcher.find()) {
				post.setReleaseYear(matcher.group(1));
			}
			//studio
			pattern = Pattern.compile("Studio: (.+)");
			matcher = pattern.matcher(content);
			if(matcher.find()) {
				post.setStudio(matcher.group(1));
			}
			//Genres
			pattern = Pattern.compile("Genres: (.+)\\s+\\w+:");
			matcher = pattern.matcher(content);
			if(matcher.find()) {
				post.setGenres(matcher.group(1));
			}
			//Format
			pattern = Pattern.compile("Format: (.+)");
			matcher = pattern.matcher(content);
			if(matcher.find()) {
				post.setFormat(matcher.group(1));
			}
			//Duration
			pattern = Pattern.compile("\\w+:\\s*([\\d+:]\\d+:\\d+)\\s+");
			matcher = pattern.matcher(content);
			if(matcher.find()) {
				post.setDuration(matcher.group(1));
			}
			//Video
			pattern = Pattern.compile("Video: (.+)");
			matcher = pattern.matcher(content);
			if(matcher.find()) {
				post.setVideo(matcher.group(1));
			}
			//Audio
			pattern = Pattern.compile("Audio: (.+)");
			matcher = pattern.matcher(content);
			if(matcher.find()) {
				post.setAudio(matcher.group(1));
			}
			//File size
			pattern = Pattern.compile("File size:\\s+([\\d\\.]+\\s[M|G]B)");
			matcher = pattern.matcher(content);
			if(matcher.find()) {
				post.setSize(matcher.group(1));
				//200mb 77gb 99.43 mb
				pattern = Pattern.compile("([\\d\\.]+)\\s(\\w+)");
				matcher = pattern.matcher(post.getSize());
				if(matcher.find()) {
					String filesize = matcher.group(1);
					String unit = matcher.group(2);
					post.setUnit(unit);
					post.setFilesize(Float.parseFloat(filesize));
				}
			} else {
				post.setSize("null");
			}
			
			
		} catch (XPatherException e) {
			logger.warn("convert html node to post bean", e);
		}

		
		return post;
	}

	public void recordErr(Post post) {

		Connection conn = null;
		String sql = "update post set errmsg = ? where url=?";
		
		if(post == null) {
			throw new IllegalArgumentException("post is null");
		}
		try {
			conn = DBAccess.getConnection();
			QueryRunner runner = new QueryRunner();
			runner.update(conn, sql, post.getErr(), post.getUrl());
		} catch (SQLException e) {
			logger.debug(post.getErr());
			logger.error("recordErr", e);
		} finally {
			DBAccess.close(conn);
		}
	}

	public List<Post> getUnUpdate() {
		List<Post> posts = null;
		Connection conn = null;
		
		try {
			conn = DBAccess.getConnection();
			QueryRunner runner = new QueryRunner();
			posts = runner.query(conn, "select * from post where size is null", listHandler);
			
		} catch (SQLException e) {
			logger.error("open conn", e);
		} finally {
			DBAccess.close(conn);
		}
		
		return posts;
	}

}
