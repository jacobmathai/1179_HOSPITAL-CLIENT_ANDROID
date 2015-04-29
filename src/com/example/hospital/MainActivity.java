package com.example.hospital;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	EditText user, pass;
	Button login, logout, exit,forgotpassword;
	String uname, pas;
	String url = CommonClass.ip+"Hospital_Privacy/action/doc_login_action.jsp";
	HttpRequestClass httpRequest;
	String result = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		user = (EditText) findViewById(R.id.etusername);
		pass = (EditText) findViewById(R.id.etpass);
		login = (Button) findViewById(R.id.mabtnlogin);
		logout = (Button) findViewById(R.id.btnlogout);
		exit = (Button) findViewById(R.id.exitbutton1);
		forgotpassword = (Button)findViewById(R.id.bnforgotpass);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CommonClass.uname = user.getText().toString();
				pas = pass.getText().toString();
				new AsyncLogin().execute(url, CommonClass.uname, pas);
			}
		});

		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopService(new Intent(getApplicationContext(),
						CurrentLocation.class));
			}
		});
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());

			}
		});
		forgotpassword.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,ForgotPassword.class);
				startActivity(intent);
			}
		});
	}

	public class AsyncLogin extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			httpRequest = new HttpRequestClass();

			ArrayList<NameValuePair> namevaluepair = new ArrayList<NameValuePair>();
			namevaluepair.add(new BasicNameValuePair("user", params[1]));
			namevaluepair.add(new BasicNameValuePair("pass", params[2]));
			try {
				result = httpRequest.getsingleinformation(params[0],
						namevaluepair);
				System.out.println("1111   :" + result);
				result = result.replaceAll("\\n||\\r", "");
				System.out.println("result" + result);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// result="true";
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			result = result.trim();
			Log.d("1111", result);
			CommonClass.doctor_id = result;
			System.out.println("doctor_id" + CommonClass.doctor_id);
			if (result.equals("fail")) {
				Toast.makeText(getApplicationContext(), "Not Valid",
						Toast.LENGTH_LONG).show();

			} else {
				Toast.makeText(getApplicationContext(), "Valid",
						Toast.LENGTH_LONG).show();
				startActivity(new Intent(getApplicationContext(), Home.class));
			}
		}
	}
}
