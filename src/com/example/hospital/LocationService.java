package com.example.hospital;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class LocationService extends Service {
	HttpRequestClass htp;
	 //String url="http://192.168.1.12:8084/Droid_Spector/latlng.jsp";
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 10;
	private static final long MIN_TIME_FOR_UPDATE = 60000;
	String id, doc_name, pat_name;
	HttpRequestClass httpRequest;

	String rep_url = CommonClass.ip + "Hospital_Privacy/action/request.jsp";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d("hai", "service created");
		turnGPSOn(); // method to turn on the GPS if its in off state.
		getMyCurrentLocation();

	}

	public void turnGPSOn() {
		try {
			Log.d("hai", "service1");
			String provider = Settings.Secure.getString(getContentResolver(),
					Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

			Log.d("hai", "service2");
			if (!provider.contains("gps")) { // if gps is disabled
				Log.d("hai", "service3");
				final Intent poke = new Intent();
				poke.setClassName("com.android.settings",
						"com.android.settings.widget.SettingsAppWidgetProvider");
				Log.d("hai", "service3");
				poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
				poke.setData(Uri.parse("3"));
				sendBroadcast(poke);
				Log.d("hai", "service4");
			} else {
				getMyCurrentLocation();
			}
		} catch (Exception e) {

		}
	}

	public void turnGPSOff() {
		Log.d("hai", "service5");
		String provider = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		Log.d("hai", "service6");
		if (provider.contains("gps")) { // if gps is enabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			sendBroadcast(poke);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("hai", "service10");
		turnGPSOff();
	}

	void getMyCurrentLocation() {

		Log.d("hai", "service11");
		LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener locListener = new MyLocationListener();
		Log.d("hai", "service12");

		try {
			gps_enabled = locManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = locManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}
		Log.d("hai", "service13");
		// don't start listeners if no provider is enabled
		// if(!gps_enabled && !network_enabled)
		// return false;

		if (gps_enabled) {
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
					0, locListener);
			Log.d("hai", "service14");
		}

		if (gps_enabled) {
			location = locManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			Log.d("hai", "service15");

		}

		if (network_enabled && location == null) {
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					0, 0, locListener);
			Log.d("hai", "service16");
		}

		if (network_enabled && location == null) {
			location = locManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			Log.d("hai", "service17");
		}

		if (location != null) {
			Log.d("hai", "service18");
			MyLat = location.getLatitude();
			MyLong = location.getLongitude();

		} else {
			Location loc = getLastKnownLocation(this);
			if (loc != null) {
				Log.d("hai", "service19");
				MyLat = loc.getLatitude();
				MyLong = loc.getLongitude();

			}
		}
		locManager.removeUpdates(locListener); // removes the periodic updates
												// from location listener to
												// //avoid battery drainage. If
												// you want to get location at
												// the periodic intervals call
												// this method using //pending
												// intent.

		try {
			// Getting address from found locations.
			Geocoder geocoder;

			List<Address> addresses;
			geocoder = new Geocoder(this, Locale.getDefault());
			addresses = geocoder.getFromLocation(MyLat, MyLong, 1);

			StateName = addresses.get(0).getAdminArea();
			CityName = addresses.get(0).getLocality();
			CountryName = addresses.get(0).getCountryName();
			// you can get more details other than this . like country code,
			// state code, etc.
			CommonClass.LocationName = CityName;

			System.out.println(" StateName " + StateName);
			System.out.println(" CityName " + CityName);
			System.out.println(" CountryName " + CountryName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// new
		// Async_view_report().execute(rep_url,CommonClass.item,CommonClass.doctor_id,"ernakulam");
		// textView2.setText(""+MyLat);
		// textView3.setText(""+MyLong);
		// textView1.setText(" StateName " + StateName +" CityName " + CityName
		// +" CountryName " + CountryName);
	}

	public class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location location) {
			if (location != null) {
				// new sendloc().execute(url,CityName);

			}
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}

	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	Location location;

	Double MyLat, MyLong;
	String CityName = "";
	String StateName = "";
	String CountryName = "";

	// below method to get the last remembered location. because we don't get
	// locations all the times .At some instances we are unable to get the
	// location from GPS. so at that moment it will show us the last stored
	// location.

	public static Location getLastKnownLocation(Context context) {
		Location location = null;
		LocationManager locationmanager = (LocationManager) context
				.getSystemService("location");
		List list = locationmanager.getAllProviders();
		boolean i = false;
		Iterator iterator = list.iterator();
		do {
			// System.out.println("---------------------------------------------------------------------");
			if (!iterator.hasNext())
				break;
			String s = (String) iterator.next();
			// if(i != 0 && !locationmanager.isProviderEnabled(s))
			if (i != false && !locationmanager.isProviderEnabled(s))
				continue;
			// System.out.println("provider ===> "+s);
			Location location1 = locationmanager.getLastKnownLocation(s);
			if (location1 == null)
				continue;
			if (location != null) {
				// System.out.println("location ===> "+location);
				// System.out.println("location1 ===> "+location);
				float f = location.getAccuracy();
				float f1 = location1.getAccuracy();
				if (f >= f1) {
					long l = location1.getTime();
					long l1 = location.getTime();
					if (l - l1 <= 600000L)
						continue;
				}
			}
			location = location1;
			// System.out.println("location  out ===> "+location);
			// System.out.println("location1 out===> "+location);
			i = locationmanager.isProviderEnabled(s);
			// System.out.println("---------------------------------------------------------------------");
		} while (true);
		return location;
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

			// nvp.add(new BasicNameValuePair("time", params[4]));
			// nvp.add(new BasicNameValuePair("date", params[5]));
			String result = null;
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
				CommonClass.report = id;

				Log.d("id", id);
				Intent in = new Intent(LocationService.this, Report.class);
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				in.putExtra("report", CommonClass.report);
				in.putExtra("doc_name", doc_name);
				in.putExtra("pat_name", pat_name);
				startActivity(in);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
