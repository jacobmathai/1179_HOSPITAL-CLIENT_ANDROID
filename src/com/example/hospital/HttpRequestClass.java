package com.example.hospital;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpRequestClass {
	HttpClient httpclient;

	public String getinformation(String url) throws ParseException, IOException {
		httpclient = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		try {
			HttpResponse respnse = httpclient.execute(request);
			int status = respnse.getStatusLine().getStatusCode();
			if (status == 200) {
				HttpEntity e = respnse.getEntity();
				String data = EntityUtils.toString(e);
				return data;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public String getsingleinformation(String url,
			ArrayList<NameValuePair> namevaluepair) throws ParseException,
			IOException {
		httpclient = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		request.setEntity(new UrlEncodedFormEntity(namevaluepair));
		try {
			HttpResponse respnse = httpclient.execute(request);
			int status = respnse.getStatusLine().getStatusCode();

			if (status == 200) {
				HttpEntity e = respnse.getEntity();
				String data = EntityUtils.toString(e);
				return data;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
}
