package com.example.hospital;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class Home extends Activity {
	Button chngpas, vew_rep;
	HttpRequestClass httpRequest;
	String result;
	ListView lv;
	String id, doc_name, pat_name,age,disease;
	ArrayAdapter<String> adpt;
	String[] pat_id;
	String url = CommonClass.ip + "Hospital_Privacy/action/pat_id_request.jsp";
	String rep_url = CommonClass.ip + "Hospital_Privacy/action/request.jsp";

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		startService(new Intent(getApplicationContext(), LocationService.class));
		// vew_rep=(Button) findViewById(R.id.btnview);
		lv = (ListView) findViewById(R.id.list_pat_id);

		/*
		 * vew_rep.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * new AsyncViewPatient().execute(url); } });
		 */

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String item = pat_id[arg2];
				CommonClass.item = item;
				System.out.println("item" + CommonClass.item);

				// String location="ernakulam";
				new Async_view_report().execute(rep_url, item,
						CommonClass.doctor_id, CommonClass.LocationName);
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case R.id.viewrep:
			
			new AsyncViewPatient().execute(url);
			break;
			
		case R.id.submit:

			startActivity(new Intent(getApplicationContext(),
					Patient_IdList.class));
			break;
		case R.id.changepass:
			startActivity(new Intent(getApplicationContext(),
					ChangePassword.class));
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public class AsyncViewPatient extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("doc_id", CommonClass.doctor_id));
			
			httpRequest = new HttpRequestClass();
			try {
				result = httpRequest.getsingleinformation(params[0],nvp);
				result = result.replaceAll("\\n||\\r", "");
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
			Log.d("result", result);
			JSONObject json = null;

			try {
				json = new JSONObject(result);
				String id = json.getString("result");
				JSONArray jArray_id = new JSONArray(id);
				pat_id = new String[jArray_id.length()];

				for (int i = 0; i < jArray_id.length(); i++) {
					pat_id[i] = jArray_id.get(i).toString();
				}
				adpt = new ArrayAdapter<String>(getApplicationContext(),
						android.R.layout.simple_list_item_1, pat_id);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lv.setAdapter(adpt);
		}
	}

	public class Async_view_report extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			httpRequest = new HttpRequestClass();
			ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("pat_id", params[1]));
			nvp.add(new BasicNameValuePair("doc_id", params[2]));
			nvp.add(new BasicNameValuePair("locname", params[3]));
			try {
				result = httpRequest.getsingleinformation(params[0], nvp);
				result = result.replaceAll("\\n||\\r", "");
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
			Log.d("3333", result);
			JSONObject json = null;

			try {
				json = new JSONObject(result);
				id = json.getString("result");
				doc_name = json.getString("doc_name");
				pat_name = json.getString("pat_name");
				age=json.getString("pat_age");
				disease=json.getString("disease");
			    
				CommonClass.report = id;
				Log.d("id", id);
				Intent in = new Intent(getApplicationContext(), Report.class);
				in.putExtra("report", CommonClass.report);
				in.putExtra("doc_name", doc_name);
				in.putExtra("pat_name", pat_name);
				in.putExtra("pat_age", age);
				in.putExtra("disease", disease);
				startActivity(in);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
