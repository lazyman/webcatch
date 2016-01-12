package cn.com.lazyhome.webcatch.test;

import java.util.Vector;

public class TestMain {
	
	public static void main(String[] args) {
		Vector<String> vector = new Vector<String>();
		Tongbu tb = new Tongbu(vector);

		Thread t1 = new Thread(tb);
		Thread t2 = new Thread(tb);
		Thread t3 = new Thread(tb);
		Thread t4 = new Thread(tb);
		Thread t5 = new Thread(tb);
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
	}
}
