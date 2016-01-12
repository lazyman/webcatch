package cn.com.lazyhome.webcatch.util;

import java.net.MalformedURLException;
import java.net.URL;

public class TestURL {
public static void main(String[] args) {
	try {
		URL url = new URL("http://freebdsmsexvideos.net/");
		String url1 = "http://freebdsmsexvideos.net/2016/01/08/she-watches-as-her/1";
		String url2 = "http://freebdsmsexvideos.net/2016/01/08/she-watches-as-her/1#more-382467";
		
		URL u1 = new URL(url, url1);
		URL u2 = new URL(url, url2);
		
		System.out.println(u1);
		System.out.println(u2);
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
