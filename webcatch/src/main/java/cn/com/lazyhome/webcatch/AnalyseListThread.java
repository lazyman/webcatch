package cn.com.lazyhome.webcatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AnalyseListThread implements Runnable {
	private static final Log logger = LogFactory.getLog(AnalyseListThread.class);
	String uri;
	CatchMain catchTool = new CatchMain();

	public AnalyseListThread(String uri) {
		this.uri = uri;
	}
	
	public void run() {
		logger.info(Thread.currentThread().getName() + uri + "is started.");
		
		catchTool.analyseList(uri);
	}

}
