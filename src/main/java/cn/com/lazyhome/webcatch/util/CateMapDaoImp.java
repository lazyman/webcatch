package cn.com.lazyhome.webcatch.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.TagNode;

import cn.com.lazyhome.webcatch.CateMap;

public class CateMapDaoImp implements CateMapDao {
	private ResultSetHandler<CateMap> handler = new BeanHandler<CateMap>(CateMap.class);
	private static final Log logger = LogFactory.getLog(CateMapDaoImp.class);

	public void insert(CateMap cat) {
		Connection conn = null;
		String sql = "insert into catemap(catename, posturl, cateurl) values(?, ?, ?)";
		
		if(cat == null) {
			throw new IllegalArgumentException("cat is null");
		}
		try {
			conn = DBAccess.getConnection();
			QueryRunner runner = new QueryRunner();
			
			//insert category
			runner.update(conn, sql, cat.getCatename(), cat.getPosturl(), cat.getCateurl());
		} catch (SQLException e) {
			logger.debug(cat);
			logger.error("insert cat", e);
		} finally {
			DBAccess.close(conn);
		}
	}
	public void save(CateMap cat) {
		CateMap ca = get(cat.getPosturl(), cat.getCateurl());
		
		if(ca == null) {
			insert(cat);
		}
	}
	public CateMap get(String posturl, String cateurl) {
		CateMap cat = null;
		Connection conn = null;
		
		try {
			conn = DBAccess.getConnection();
			QueryRunner runner = new QueryRunner();
			cat = runner.query(conn, "select * from catemap c where c.posturl = ? and c.cateurl = ?", handler, posturl, cateurl);
			
		} catch (SQLException e) {
			logger.error("get cat", e);
		} finally {
			DBAccess.close(conn);
		}
		
		return cat;
	}
	public List<CateMap> convertTags(List<TagNode> cateNodes) {
		List<CateMap> cates = new Vector<CateMap>();
		
		for(TagNode node : cateNodes) {
			CateMap cat = new CateMap();
			
			cat.setCatename(node.getText().toString());
			cat.setCateurl(node.getAttributeByName("href"));
			
			cates.add(cat);
		}
		
		return cates;
	}
}
