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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class Patient_IdList extends Activity {
	
	ListView lvpatient_id;
	HttpRequestClass httpRequest;
	String result;
	String[] pat_id;
	ArrayAdapter<String>adpt;
	String url=CommonClass.ip+"Hospital_Privacy/action/pat_id_request.jsp";
	String report_url=CommonClass.ip+"Hospital_Privacy/action/first_report.jsp";
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		//startActivity(new Intent(getApplicationContext(),MainActivity.class)); 
		finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_patient);
		
		lvpatient_id=(ListView) findViewById(R.id.listpatient);
		
		new AsyncViewPatient().execute(url);
		lvpatient_id.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String item=pat_id[arg2];
				CommonClass.item=item;
				System.out.println("item" +CommonClass.item);
				
				
				new AsyncCreateReport().execute(report_url,item,CommonClass.doctor_id);
			}
			
			
		});
		
	}
	public class AsyncViewPatient extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			httpRequest=new HttpRequestClass();
			try {
				result=httpRequest.getinformation(params[0]);
				result=result.replaceAll("\\n||\\r", "");
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
			Log.d("result", result);
			JSONObject json=null;
			
			try {
				json=new JSONObject(result);
				   String id=json.getString("result");
				   JSONArray jArray_id=new JSONArray(id);
				    pat_id=new String[ jArray_id.length()];
				   
				   for(int i=0;i<jArray_id.length();i++){
					   pat_id[i]=jArray_id.get(i).toString();
				   }
				   adpt=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,pat_id);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lvpatient_id.setAdapter(adpt);
		}
	}
	
	public class AsyncCreateReport extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			httpRequest=new HttpRequestClass();
			ArrayList<NameValuePair>nvp=new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("pat_id", params[1]));
			nvp.add(new BasicNameValuePair("doc_id", params[2]));
			
			try {
				result=httpRequest.getsingleinformation(params[0],nvp);
				result=result.replaceAll("//n||//t", "");
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
			Log.d("result222", result);
			JSONObject jobj=null;
			try {
				jobj=new JSONObject(result);
				String res=jobj.getString("result");
				if(res.equals("success")){
					startActivity(new Intent(getApplicationContext(),ReportGen.class));
				}
				else{
					Toast.makeText(getApplicationContext(), "Can't generate Report", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
