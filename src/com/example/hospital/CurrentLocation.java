package com.example.hospital;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class CurrentLocation extends Service implements LocationListener {

	LocationManager mLocationManager;
	Double longitude,latitude;
	String id,doc_name,pat_name;
	HttpRequestClass httpRequest;
	String rep_url=CommonClass.ip+"Hospital_Privacy/action/request.jsp";
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(CurrentLocation.this, "Service Stopped", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(CurrentLocation.this, "Service started", Toast.LENGTH_SHORT).show();
		
		initializeLocationAndStartGpsThread();
		
	}

	private void initializeLocationAndStartGpsThread() {
		// TODO Auto-generated method stub
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		List<String> providers = mLocationManager.getProviders(true);
		Log.i("hai04", "Enabled providers = " + providers.toString());
		String provider = mLocationManager.getBestProvider(new Criteria(),true);
		Log.i("hai05", "Best provider = " + provider);

		setCurrentGpsLocation(null);   
	}

	private void setCurrentGpsLocation(Location location) {
		// TODO Auto-generated method stub
	String bestProvider = "";

	if (location == null) {
		
		mLocationManager =(LocationManager) getSystemService(LOCATION_SERVICE);
                mLocationManager.requestLocationUpdates(
		    LocationManager.GPS_PROVIDER, 1000, 0, this); // Every 10000 msecs	
                System.out.println("GPS_PROVIDER:  ");
		location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);				

	}
    	try {
    		   
    	      
    		longitude = location.getLongitude();
    	 latitude = location.getLatitude();
    	 System.out.println("latttlong"+longitude+""+longitude);
    	
    	
    	  new UpdateWithNewLocation().execute(location);

    } catch (NullPointerException e) {
    	//Log.i("hai02", "Null pointer exception " + longitude + "," + latitude);
    }
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	public class UpdateWithNewLocation extends AsyncTask<Location, Integer, String>

	{

		@Override
		protected String doInBackground(Location... params) {
			String latlong;
			Location loc=params[0];
			if(loc != null)
			{
				double lat=loc.getLatitude();
				double lng=loc.getLongitude();
				latlong="Latitude:"+lat+"\nLongitude:"+lng;
				CommonClass.Latlong=latlong;
				 System.out.println("latttlong"+CommonClass.Latlong);
				//Log.d("sam", latlong);
				String result = CommonClass.executeWithOutNVP("http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=false");
				//Log.d("sam", result);
				return result;
				
			}
			else
			{
				latlong="No loc found";
			}
			
			return latlong;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			result=result.replaceAll("\\n|\\r", "");
			if(result.equals("No loc found"))
			{
				System.out.print("No loc found");
				  CommonClass.LocationName="No loc found";
			}
			else
			{
				try {
					JSONObject jObject = new JSONObject(result);
					JSONArray jArray = jObject.getJSONArray("results");
				    JSONObject oneObject = jArray.getJSONObject(0);
					String address = oneObject.getString("formatted_address");
			        CommonClass.LocationName=address;
			        Log.d("loc_address", CommonClass.Latlong);
			        Log.d("loc_name", CommonClass.LocationName);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("CommonClass item  :"+CommonClass.item);
			new Async_view_report().execute(rep_url,CommonClass.item,CommonClass.doctor_id,CommonClass.LocationName);
			 
		//	new LocationSave().execute(url1,CommonClass.LocationName,CommonClass.uname);
		}
	}
public class Async_view_report extends AsyncTask<String, Integer, String>{
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			httpRequest=new HttpRequestClass();
			ArrayList<NameValuePair>nvp=new ArrayList<NameValuePair>();
		
			nvp.add(new BasicNameValuePair("pat_id", params[1]));
			nvp.add(new BasicNameValuePair("doc_id", params[2]));
			nvp.add(new BasicNameValuePair("locname", params[3]));
		
			//nvp.add(new BasicNameValuePair("time", params[4]));
			//nvp.add(new BasicNameValuePair("date", params[5]));
			String result=null;
			try {
		 result=httpRequest.getsingleinformation(params[0], nvp);
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
		result=result.trim();
		Log.d("3333", result);
		JSONObject json=null;
		

			try {
				json=new JSONObject(result);
				   id=json.getString("result");
				   doc_name=json.getString("doc_name");
				   pat_name=json.getString("pat_name");
				  CommonClass.report=id;
				  
				
				  Log.d("id", id);
				  Intent in=new Intent(CurrentLocation.this,Report.class);
				  in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					in.putExtra("report", CommonClass.report);
					in.putExtra("doc_name",doc_name);
					in.putExtra("pat_name", pat_name);
					 startActivity(in);
				 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		}
	} 

