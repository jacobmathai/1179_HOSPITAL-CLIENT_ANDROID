package com.example.hospital;

import java.io.IOException;

import org.apache.http.ParseException;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Report extends Activity {
	TextView report;
	Button newrep;
	Button h;
	 String rep,d_name,p_name,details,do_name,pa_name,pre_rep,pre_disp,nex_disp,page,pdisease;
	 
	 HttpRequestClass httpRequest;
	 String result=null;
	 String previousurl=CommonClass.ip+"Hospital_Privacy/action/prev.jsp";
	 String nexturl=CommonClass.ip+"Hospital_Privacy/action/next.jsp";
	 
	 MenuItem new_report;
	 
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
		setContentView(R.layout.report);
		h=(Button) findViewById(R.id.hm);
		report=(TextView) findViewById(R.id.txtreport);
		newrep=(Button) findViewById(R.id.btnnewreport);
		newrep.setVisibility(-1);
	  rep=getIntent().getStringExtra("report");
	  d_name=getIntent().getStringExtra("doc_name");
	  p_name=getIntent().getStringExtra("pat_name");
	  page=getIntent().getStringExtra("pat_age");
	  pdisease=getIntent().getStringExtra("disease");
	  h.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i=new Intent(Report.this,Home.class);
			startActivity(i);
		}
	});
	  
	  if(rep.equals("No Reports available")){
		  report.setText("No report Available");
	  }
	  else{
	  details="Doctor Name   :  \t"+d_name+"\n Patient Name  : \t"+p_name+"\n Report   :   \t"+rep+"\n Age           : \t"+page+"\n Disease       : \t"+pdisease;
	   report.setText(details);
	  }
	   /*if(rep.equals("No permission")){
		   newrep.setVisibility(-1);
	   }
	   else{
		   newrep.setVisibility(1);
	   }
	   newrep.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			startActivity(new Intent(getApplicationContext(),ReportGen.class));
			
		}
	});*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.newreport, menu);
		 new_report = menu.findItem(R.id.new_rep);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.new_rep:
			
			if(rep.equals("No permission")){
				Toast.makeText(getApplicationContext(), "No permission to generate report", Toast.LENGTH_SHORT).show();
				new_report.setVisible(false);
			}
			else{
				new_report.setVisible(true);
				startActivity(new Intent(getApplicationContext(),ReportGen.class));
		}
			
			break;
			
		case R.id.previous:
			
			new AsyncPrevious().execute(previousurl);
			
			break;
			
		case R.id.next:
			
			new AsyncNext().execute(nexturl);
			

		default:
			break;
		} 
		return super.onOptionsItemSelected(item);
	}

	
	public class AsyncPrevious extends AsyncTask<String, Integer, String>{
		
		
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			httpRequest=new HttpRequestClass();
			try {
				result=httpRequest.getinformation(params[0]);
				result=result.replaceAll("//n||//r", "");
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
			result=result.trim();
			Log.d("114", result);
			
			JSONObject json=null;
			
			try {
				json=new JSONObject(result);
				   CommonClass.pre_rep=json.getString("result");
				   CommonClass.do_name=json.getString("doc_name");
				   CommonClass.pa_name=json.getString("pat_name");
				   pre_disp="Doctor Name   :  \t"+CommonClass.do_name+"\n Patient Name  : \t"+CommonClass.pa_name+"\n Report   :   \t"+CommonClass.pre_rep;
						report.setText(pre_disp);
				  
				 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public class AsyncNext extends AsyncTask<String, Integer, String>{
		
		

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			httpRequest=new HttpRequestClass();
			try {
				result=httpRequest.getinformation(params[0]);
				result=result.replaceAll("//n||//r", "");
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
			result=result.trim();
			Log.d("224", result);
			
			
			JSONObject json=null;
			
			try {
				json=new JSONObject(result);
				   CommonClass.nex_rep=json.getString("result");
				   CommonClass.nexdo_name=json.getString("doc_name");
				   CommonClass.nexpa_name=json.getString("pat_name");
				  

				nex_disp="Doctor Name   :  \t"+CommonClass.nexdo_name+"\n Patient Name  : \t"+CommonClass.nexpa_name+"\n Report   :   \t"+CommonClass.nex_rep;
						report.setText(nex_disp);
				 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
		
}
