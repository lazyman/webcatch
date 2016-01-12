package cn.com.lazyhome.webcatch.test;

public class TestFilename {

	public static void main(String[] args) {
		new TestFilename().testfilename();
	}
	private void testfilename() {
		String filename = "http://a.com/ff";
		System.out.println(filename);
		System.out.println(filename.length());
		System.out.println(filename.lastIndexOf("/"));
		
		filename = "http://a.com/ff/";
		System.out.println(filename);
		System.out.println(filename.length());
		System.out.println(filename.lastIndexOf("/"));
	}
}
