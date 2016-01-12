package cn.com.lazyhome.webcatch.test;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Vector;

public class Tongbu implements Runnable {
	private Vector<String> vector;

	public Tongbu(Vector<String> vector) {
		this.vector = vector;
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			download();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void download() throws InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("this is " + Thread.currentThread().getName());
		
		synchronized (vector) {
			System.out.println(Thread.currentThread().getName()
					+ "synchronized");
			vector.notify();
//			vector.wait();
			
			System.out.println(Thread.currentThread().getName() + "wait");
			
			vector.add("1");
			
			vector.notify();
			System.out.println(Thread.currentThread().getName() + "notify");
		}
	}
}
