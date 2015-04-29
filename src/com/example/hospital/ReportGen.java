package com.example.hospital;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ReportGen extends Activity {
	
	EditText report;
	Button gen;
	String rep;
	String res;
	HttpRequestClass httpRequest;
	JSONObject jobj = null;
	String result;
	String url = CommonClass.ip+"action/receive_report.jsp";

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.repgen);

		report = (EditText) findViewById(R.id.etreport);
		gen = (Button) findViewById(R.id.btngenerate);

		gen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rep = report.getText().toString();
				new Async_Repgen().execute(url, CommonClass.doctor_id,
						CommonClass.item, rep);

				startActivity(new Intent(getApplicationContext(),
						MainActivity.class));
			}
		});

	}

	public class Async_Repgen extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			httpRequest = new HttpRequestClass();
			ArrayList<NameValuePair> namevaluepair = new ArrayList<NameValuePair>();
			namevaluepair.add(new BasicNameValuePair("doc_id", params[1]));
			namevaluepair.add(new BasicNameValuePair("pat_id", params[2]));
			namevaluepair.add(new BasicNameValuePair("report", params[3]));
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

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			super.onPostExecute(result);
			result = result.trim();
			Log.d("5555", result);

			try {
				jobj = new JSONObject(result);
				res = jobj.getString("result");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (res.equals("success")) {
				Toast.makeText(getApplicationContext(), "Report Generated",
						Toast.LENGTH_LONG).show();

			} else {
				Toast.makeText(getApplicationContext(), "Report not Generated",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
