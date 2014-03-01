package cn.com.lazyhome.webcatch.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import cn.com.lazyhome.webcatch.CatchMain;
import cn.com.lazyhome.webcatch.Post;

public class PostDaoImp implements PostDao {
	private static ResultSetHandler<Post> handler = new BeanHandler<Post>(Post.class);
	private static ResultSetHandler<List<Post>> listHandler = new BeanListHandler<Post>(Post.class);
	private static final Log logger = LogFactory.getLog(CatchMain.class);

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
		} catch (SQLException e) {
			logger.debug(post);
			logger.error("open conn", e);
		} finally {
			DBAccess.close(conn);
		}
	}

	public void update(Post post) {
		Connection conn = null;
		String sql = "update post set title = ?, content=?, size=? where url=?";
		
		if(post == null) {
			throw new IllegalArgumentException("post is null");
		}
		try {
			conn = DBAccess.getConnection();
			QueryRunner runner = new QueryRunner();
			runner.update(conn, sql, post.getTitle(), post.getContent(), post.getSize(), post.getUrl());
		} catch (SQLException e) {
			logger.debug(post);
			logger.error("open conn", e);
		} finally {
			DBAccess.close(conn);
		}
	}

	public void save(Post post) {
		Post p = get(post.getUrl());
		boolean insert = false;
		
		if(p == null) {
			insert = true;
		}

		save(post, insert);
	}

	public void save(Post post, boolean insert) {
		if(insert) {
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
	

	public Post convert(TagNode node) {
		Post post = new Post();
		
		String xpathp = "//div[@class=\"post_top\"]/div[@class=\"post_bttm\"]/div[@class=\"title\"]/h2/a";
		try {
			TagNode info = (TagNode) node.evaluateXPath(xpathp)[0];
			
			post.setTitle(info.getText().toString());
			post.setUrl(info.getAttributeByName("href"));
			
		} catch (XPatherException e) {
			logger.warn("convert html node to post bean", e);
		}

		
		return post;
	}

}
