package com.example.hospital;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends Activity {
Context context;
EditText oldpass,newpass,conpass;
Button update,home;
String opass,npass,cpass;
HttpRequestClass htp;
String passUrl=CommonClass.ip+"action/change_doc_pass_action.jsp";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.changepassword);
	   oldpass=(EditText)findViewById(R.id.oldpasseditText1);
	   newpass=(EditText)findViewById(R.id.newpasseditText1);
	   conpass=(EditText)findViewById(R.id.confirmeditText1);
	   update=(Button)findViewById(R.id.Updatebutton1);
	   home=(Button)findViewById(R.id.backhomebutton1);
	   //db=new Database(this);
	   update.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			opass=oldpass.getText().toString();
			npass=newpass.getText().toString();
			cpass=conpass.getText().toString();
	         Log.d("opass", opass);
	         Log.d("npass", npass);
	         Log.d("cpass", cpass);
	
              if(npass.equals(cpass))
				{
				 new Asyncsend().execute(passUrl,opass,npass);
				}
		}
	});
	   home.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent in= new Intent(getApplicationContext(),MainActivity.class);
			startActivity(in);
		}
	});
	}
	public class Asyncsend extends AsyncTask<String, Integer, String>
	{

		@Override
		protected String doInBackground(String... str) {
			// TODO Auto-generated method stub
			String result=null;
			//result=result.replaceAll("\\n||\\r", "");
			//Log.d("hai", result);
			htp=new HttpRequestClass();
			ArrayList< NameValuePair> nvp=new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("p",str[1]));
			nvp.add(new BasicNameValuePair("np",str[2]));
			try {
				result=htp.getsingleinformation(str[0], nvp);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.equals("success"))
			{
				Toast.makeText(getApplicationContext(), "Password Changed Successfully", Toast.LENGTH_LONG).show();
			}
			else
				Toast.makeText(getApplicationContext(), "Something Went Error...", Toast.LENGTH_LONG).show();

		}
		
	}
	

}
