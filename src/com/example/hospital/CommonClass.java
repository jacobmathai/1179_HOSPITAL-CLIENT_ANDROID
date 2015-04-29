package com.example.hospital;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class CommonClass {
	public static String ip="http://172.30.16.234:8084/";
	static String Latlong;
	static String LocationName="No location found";
	protected static String uname;
	public static String doctor_id;
	protected static String item;
	public static String report;
	public static String pre_rep;
	public static String do_name;
	public static String pa_name;
	public static String nex_rep;
	
	public static String nexdo_name;
	public static String nexpa_name;
	
	public static String executeWithOutNVP(String url) {
		String s="fail";	
		System.out.println("url"+url);
		
		 try {
				HttpClient httpclient=new DefaultHttpClient();
				HttpPost httppost=new HttpPost(url);
				HttpResponse res=httpclient.execute(httppost);
				HttpEntity e= res.getEntity();
				 s=EntityUtils.toString(e);
				 System.out.println("asasss"+s);
				return s;
				
			} catch (Exception e) {	
				e.printStackTrace();
			}
		return null;
	}
}
