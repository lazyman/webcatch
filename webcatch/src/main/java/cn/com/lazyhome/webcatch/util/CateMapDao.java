package cn.com.lazyhome.webcatch.util;

import java.util.List;

import org.htmlcleaner.TagNode;

import cn.com.lazyhome.webcatch.CateMap;

public interface CateMapDao {
	public void insert(CateMap cat);
	public void save(CateMap cat);
	public CateMap get(String posturl, String cateurl);
	public List<CateMap> convertTags(List<TagNode> cateNodes);
}
