package com.example.hospital;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassword extends Activity {

	EditText email,id;
	Button button;
	String url = CommonClass.ip+"action/forgotpassword.jsp";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		
		email  = (EditText)findViewById(R.id.editText1);
		id = (EditText)findViewById(R.id.editText2);
		button = (Button)findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				new Send().execute(url,email.getText().toString(),id.getText().toString());				
			}
		});
		
	}
	
	public class Send extends AsyncTask<String, Integer, String>{

		HttpRequestClass httpRequestClass = new HttpRequestClass();
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if(result!=null){
				if(result.equals("success")){
					Toast.makeText(getApplicationContext(), "New password send to email", Toast.LENGTH_SHORT).show();
				}else
					Toast.makeText(getApplicationContext(), "Username entered is wrong", Toast.LENGTH_SHORT).show();
			}else
				Toast.makeText(getApplicationContext(), "Server is down", Toast.LENGTH_SHORT).show();
		}
		
		
		@Override
		protected String doInBackground(String... params) {
			
			ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
			
			arrayList.add(new BasicNameValuePair("email", params[1]));
			arrayList.add(new BasicNameValuePair("username", params[2]));
			
			try {
				String data = httpRequestClass.getsingleinformation(params[0], arrayList);
				return data;
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
}
