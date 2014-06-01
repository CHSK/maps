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
		CameraUpdate zoom=CameraUpdateFactory.zoomTo(10);
		
		String keywords = "";
		String checkkey [];
		String types [] = new String [] {"amusement_park","aquarium","art_gallery","beauty_salon","bar","bowling_alley","casino","campground","gym","library","movie_theatre","museum","night_club","park","restaurant","shopping_mall","stadium","spa","zoo"};
		int typeSize = types.length;
		boolean checks[] = getIntent().getBooleanArrayExtra("checks");
		System.out.println(checks[0]);
		for(int i = 0; i < checks.length; i++){
			if(checks[i]){
				switch (i){
					case 0:
						checkkey = new String[] {"aquarium","art_gallery","bar","bowling_alley","casino","gym","library","museum","night_club","shopping_mall","movie_theatre"};
						typeSize = intersection(types,typeSize,checkkey);
						//keywords = keywords + "aquarium%7Cart_gallery%7Cbar%7Cbowling_alley%7Ccasino%7Cgym%7Clibrary%7Cmuseum%7Cnight_club%7Cshopping_mall%7Cmovie_theatre";
						break;
					case 1:
						checkkey = new String [] {"amusement_park","campground","zoo","stadium","park"};
						typeSize = intersection(types,typeSize,checkkey);
						//keywords = keywords + "amusement_park%7Ccampground%7Czoo%7Cstadium%7Cpark%7C";
						break;
					case 2:
						checkkey = new String [] {"gym","night_club","amusement_park","campground","zoo","park","bowling_alley"};
						typeSize = intersection(types,typeSize,checkkey);
						//keywords = keywords + "gym%7Cnight_club%7Camusement_park%7Ccampground%7Czoo%7Cpark%7Cbowling_alley%7C";
						break;
					case 3:
						checkkey = new String [] {"aquarium","art_gallery","bar","casino","spa","beauty_salon","library","movie_theatre","stadium","shopping_mall"};
						typeSize = intersection(types,typeSize,checkkey);
						//keywords = keywords + "aquarium%7Cart_gallery%7Cbar%7Ccasino%7Cspa%7Cbeauty_salon%7Clibrary%7Cmovie_theatre%7Cstadium%7Cshopping_mall%7C";
						break;
					case 4:
						checkkey = new String [] {"park","zoo","amusement_park","bar","casino","gym","movie_theatre","spa","shopping_mall","museum","campground"};
						typeSize = intersection(types,typeSize,checkkey);
						//keywords = keywords + "park%7Czoo%7Camusement_park%7Cbar%7Ccasino%7Cgym%7Cmovie_theatre%7Cspa%7Cshopping_mall%7Cmuseum%7Ccampground%7C";
						break;
					case 5:
						checkkey = new String [] {"bar","art_gallery","campground","casino","bowling_alley","gym","movie_theatre","night_club"};
						typeSize = intersection(types,typeSize,checkkey);
						//keywords = keywords + "bar%7Cart_gallery%7Ccampground%7Ccasino%7Cbowling_alley%7Cgym%7Cmovie_theatre%7Cnight_club%7C";
						break;
					case 6:
						checkkey = new String [] {"restaurant"};
						typeSize = intersection(types,typeSize,checkkey);
						//keywords = keywords + "restaurant%7C";
						break;
					case 7:
						break;
				}
			}
		}
		
		for(int i = 0; i < typeSize; i++){
			keywords = keywords + types[i];
			keywords = keywords + "%7C";
			System.out.println(keywords);
		}
		
		if(keywords.length() == 0){
			keywords = "amusemant_park%7Caquariam%7Cart_gallery%7Cbeauty_salon%7Cbowling_alley%7Ccampground%7Ccasino%7Cgym%7Clibrary%7Cmovie_theatre%7Cmuseum%7Cnight_club%7Cpark%7Crestaurant%7Cshopping_mall%7Cspa%7Cstadium%7Czoo%7C";
		}
		keywords = keywords.substring(0,keywords.length()-3);
		System.out.println(keywords);
		
		System.out.println("hello");

		JSONObject totalPlace = getJSONFromUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
												+ currentLocation.latitude + "," + currentLocation.longitude 
												+ "&radius=32000&types="
												+ keywords + "&sensor=false&key=AIzaSyBBGzY_3gkrw6sNUSEcDHRszYjz-Q99PPI");
		
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
		
		mMap.moveCamera(center);
		mMap.animateCamera(zoom);
	}
	
	public int intersection(String[] a, int size, String[] b){
		int tempSize = size;
		boolean isThere = false;
		for(int i = 0; i < tempSize; i++){
			for(int j = 0; j < b.length; j++){
				if(a[i].equals(b[j])){
					isThere = true;
					break;
				}
			}
			if(!isThere){
				if(i == tempSize - 1){
					tempSize--;
				}
				else{
					a[i] = a[i+1];
					for(int n = i+1; n < tempSize - 1; n++){
						a[n] = a[n+1];
					}
					tempSize--;
					i--;
				}
			}	
			isThere = false;
		}
		return tempSize;
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
