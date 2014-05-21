package com.example.chsk;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class Map extends FragmentActivity {

	private GoogleMap mMap;
	private JSONArray arrPlace;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		try {
			setUpMap();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void onResume() {
		super.onResume();
		try {
			setUpMap();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setUpMap() throws Exception {
		if(mMap == null){
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			if (mMap != null)
				setUpAllMap();
		}
	}
	
	@SuppressLint("NewApi") public JSONObject getJSONFromUrl(String url) {
		if (android.os.Build.VERSION.SDK_INT > 9) 
		{
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
	    InputStream is = null;
		// Making HTTP request
	    try {
	      // defaultHttpClient
	      DefaultHttpClient httpClient = new DefaultHttpClient();
	      HttpPost httpPost = new HttpPost(url);
	      HttpResponse httpResponse = httpClient.execute(httpPost);
	      HttpEntity httpEntity = httpResponse.getEntity();
	      is = httpEntity.getContent();
	    } catch (UnsupportedEncodingException e) {
	      e.printStackTrace();
	    } catch (ClientProtocolException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    String json = null;
		try {
	      BufferedReader reader = new BufferedReader(new InputStreamReader(
	          is, "iso-8859-1"), 8);
	      StringBuilder sb = new StringBuilder();
	      String line = null;
	      while ((line = reader.readLine()) != null) {
	        sb.append(line);
	      }
	      is.close();
	      json = sb.toString();
	    } catch (Exception e) {
	      Log.e("Buffer Error", "Error converting result " + e.toString());
	    }
	    JSONObject jObj = null;
		// try parse the string to a JSON object
	    try {
	      jObj = new JSONObject(json);
	    } catch (JSONException e) {
	      Log.e("JSON Parser", "Error parsing data " + e.toString());
	    }
	    // return JSON String
	    return jObj;
	  }
	
	private void setUpAllMap() throws Exception {
		Bundle cLocation = getIntent().getParcelableExtra("location");
		LatLng currentLocation = cLocation.getParcelable("currentLocation");
		CameraUpdate center=
		        CameraUpdateFactory.newLatLng(currentLocation);
		CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
		
		System.out.println("hello");
		JSONObject totalPlace = getJSONFromUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=currentLocation.latitude,currentLocation.longitude&keyword=rock%20climbing&radius=32000&sensor=false&key=AIzaSyBBGzY_3gkrw6sNUSEcDHRszYjz-Q99PPI");
		
		String title, address;
		LatLng placeLocation;
		JSONObject temp;
		//JSONObject totalPlace = new JSONObject(data);
		arrPlace = totalPlace.getJSONArray("results");
		for(int i = 0; i < arrPlace.length(); i++){
			
			temp = arrPlace.getJSONObject(i);
			address = temp.getString("vicinity");
			title = temp.getString("name");
			temp = temp.getJSONObject("geometry");
			temp = temp.getJSONObject("location");
			placeLocation = new LatLng(temp.getDouble("lat"),temp.getDouble("lng"));
			
			if(i < arrPlace.length()/3){
				mMap.addMarker(new MarkerOptions()
					.position(placeLocation)
					.title(title)
					.snippet(address)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.greencheck)));
			}
			else if(i < arrPlace.length()/3*2){
				mMap.addMarker(new MarkerOptions()
					.position(placeLocation)
					.title(title)
					.snippet(address)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowcheck)));
			}
			else{
				mMap.addMarker(new MarkerOptions()
					.position(placeLocation)
					.title(title)
					.snippet(address)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.redcheck)));
			}
		}
		mMap.addMarker(new MarkerOptions()
				.position(currentLocation)
				.title("Green")
				.snippet("This is the green Marker")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.greencheck)));
		mMap.addMarker(new MarkerOptions()
				.position(new LatLng((currentLocation.latitude - 0.005),currentLocation.longitude))
				.title("Yellow")
				.snippet("This is the yellow Marker")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowcheck)));
		mMap.addMarker(new MarkerOptions()
				.position(new LatLng((currentLocation.latitude + 0.005),currentLocation.longitude))
				.title("Red")
				.snippet("This is the red Marker")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.redcheck)));
		
		mMap.moveCamera(center);
		mMap.animateCamera(zoom);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_map, container,
					false);
			return rootView;
		}
	}

}
