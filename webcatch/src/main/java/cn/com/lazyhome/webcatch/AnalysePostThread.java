package cn.com.lazyhome.webcatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AnalysePostThread implements Runnable {
	private static final Log logger = LogFactory.getLog(AnalysePostThread.class);
	String uri;
	CatchMain catchTool = new CatchMain();

	public AnalysePostThread(String uri) {
		this.uri = uri;
	}
	
	public void run() {
		logger.info(Thread.currentThread().getName() + uri + "is started.");
		
		catchTool.analysePost(uri);
	}

}
