package cn.com.lazyhome.webcatch.util;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Util {
	private static final Log logger = LogFactory.getLog(Util.class);

	@SuppressWarnings("rawtypes")
	public static void showList(List list) {
		Iterator iter = list.iterator();
		while(iter.hasNext()) {
			logger.info( iter.next());
		}
	}
}
