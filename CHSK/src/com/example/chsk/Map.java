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
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
				mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
					
					@Override
					public View getInfoWindow(Marker arg0) {
						return null;
					}

					@Override
					public View getInfoContents(Marker arg0) {
						View v = getLayoutInflater().inflate(R.layout.custom_info,null);
						
						String reference = arg0.getSnippet();
						
						//String tempTitle = arg0.getTitle();
						
						JSONObject detail = getJSONFromUrl("https://maps.googleapis.com/maps/api/place/details/json?reference=" +
								reference + "&sensor=true&key=AIzaSyBBGzY_3gkrw6sNUSEcDHRszYjz-Q99PPI");
						
						JSONObject temp;
						
						String phone = "",weblink = "",address = "",tempTitle = "";
						try {
							temp = detail.getJSONObject("result");
							tempTitle = temp.getString("name");
							phone = temp.getString("formatted_phone_number");
							weblink = temp.getString("website");
							address = temp.getString("formatted_address");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						arg0.setTitle(weblink);
						
						//String cutchar1 = "%#";
						//String cutchar2 = "%##";
						//String vicinity = tempSnippet.substring(0,tempSnippet.indexOf(cutchar1));
						//String summary = tempSnippet.substring(tempSnippet.indexOf(cutchar1) + 2,tempSnippet.indexOf(cutchar2));
						//String locationUrl = tempSnippet.substring(tempSnippet.indexOf(cutchar2) + 3);
						
						TextView titleView = (TextView) v.findViewById(R.id.title);
						TextView vicinityView = (TextView) v.findViewById(R.id.snippet);
						TextView summaryView = (TextView) v.findViewById(R.id.summary);
						TextView urlView = (TextView) v.findViewById(R.id.weblink);
						
						titleView.setText(tempTitle);
						vicinityView.setText(address);
						summaryView.setText(phone);
						urlView.setText(weblink);
						
						System.out.println("marker call");
						
						return v;
					}
				});
			
				mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){

					@Override
					public void onInfoWindowClick(Marker arg0) {
						//String snippet = arg0.getSnippet();
						//String website = snippet.substring(snippet.indexOf("%##") + 3);
						String website = arg0.getTitle();
						if(!website.equals("")){
							startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(website)));
						}
					}
				});
				
				mMap.setOnMarkerClickListener(new OnMarkerClickListener(){

					@Override
					public boolean onMarkerClick(Marker arg0) {
						System.out.println("crazy taco");
						return false;
					}
					
				});
				
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
		
		String keywords = getIntent().getStringExtra("keywords");
		keywords = keywords.replaceAll(" ","%7C");
		
		String typewords = "";
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
						if(checks[0]){
							typeSize = union(types,typeSize,checkkey);
						}
						else{
							typeSize = intersection(types,typeSize,checkkey);
						}
						//keywords = keywords + "amusement_park%7Ccampground%7Czoo%7Cstadium%7Cpark%7C";
						break;
					case 2:
						checkkey = new String [] {"gym","night_club","amusement_park","campground","zoo","park","bowling_alley"};
						typeSize = intersection(types,typeSize,checkkey);
						//keywords = keywords + "gym%7Cnight_club%7Camusement_park%7Ccampground%7Czoo%7Cpark%7Cbowling_alley%7C";
						break;
					case 3:
						checkkey = new String [] {"aquarium","art_gallery","bar","casino","spa","beauty_salon","library","movie_theatre","stadium","shopping_mall"};
						if(checks[2]){
							typeSize = union(types,typeSize,checkkey);
						}
						else{
							typeSize = intersection(types,typeSize,checkkey);
						}
						//keywords = keywords + "aquarium%7Cart_gallery%7Cbar%7Ccasino%7Cspa%7Cbeauty_salon%7Clibrary%7Cmovie_theatre%7Cstadium%7Cshopping_mall%7C";
						break;
					case 4:
						checkkey = new String [] {"park","zoo","amusement_park","bar","casino","gym","movie_theatre","spa","shopping_mall","museum","campground"};
						typeSize = intersection(types,typeSize,checkkey);
						//keywords = keywords + "park%7Czoo%7Camusement_park%7Cbar%7Ccasino%7Cgym%7Cmovie_theatre%7Cspa%7Cshopping_mall%7Cmuseum%7Ccampground%7C";
						break;
					case 5:
						checkkey = new String [] {"bar","art_gallery","campground","casino","bowling_alley","gym","movie_theatre","night_club"};
						if(checks[4]){
							typeSize = union(types,typeSize,checkkey);
						}
						else{
							typeSize = intersection(types,typeSize,checkkey);
						}
						//keywords = keywords + "bar%7Cart_gallery%7Ccampground%7Ccasino%7Cbowling_alley%7Cgym%7Cmovie_theatre%7Cnight_club%7C";
						break;
					case 6:
						checkkey = new String [] {"restaurant"};
						typeSize = union(types,typeSize,checkkey);
						//keywords = keywords + "restaurant%7C";
						break;
					case 7:
						break;
				}
			}
		}
		
		for(int i = 0; i < typeSize; i++){
			typewords = typewords + types[i];
			typewords = typewords + "%7C";
			System.out.println(typewords);
		}
		
		if(typewords.length() == 0){
			typewords = "amusemant_park%7Caquariam%7Cart_gallery%7Cbeauty_salon%7Cbowling_alley%7Ccampground%7Ccasino%7Cgym%7Clibrary%7Cmovie_theatre%7Cmuseum%7Cnight_club%7Cpark%7Crestaurant%7Cshopping_mall%7Cspa%7Cstadium%7Czoo%7C";
		}
		typewords = typewords.substring(0,typewords.length()-3);
		System.out.println(typewords);
		System.out.println(keywords);
		
		System.out.println("hello");
		
		JSONObject totalPlace;
		if(keywords.length() == 0){
			totalPlace = getJSONFromUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
					+ currentLocation.latitude + "," + currentLocation.longitude 
					+ "&radius=32000&types="
					+ typewords + "&sensor=false&key=AIzaSyBBGzY_3gkrw6sNUSEcDHRszYjz-Q99PPI");
		}
		else{
			totalPlace = getJSONFromUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
					+ currentLocation.latitude + "," + currentLocation.longitude 
					+ "&radius=32000"
					+ "&keyword=" + keywords
					+ "&sensor=false&key=AIzaSyBBGzY_3gkrw6sNUSEcDHRszYjz-Q99PPI");
		}
		
		String title, address;
		LatLng placeLocation;
		JSONObject temp;
		//JSONObject totalPlace = new JSONObject(data);
		arrPlace = totalPlace.getJSONArray("results");
		for(int i = 0; i < arrPlace.length(); i++){
			
			temp = arrPlace.getJSONObject(i);
			address = temp.getString("vicinity");
			title = temp.getString("name");
			String reference = temp.getString("reference");
			temp = temp.getJSONObject("geometry");
			temp = temp.getJSONObject("location");
			placeLocation = new LatLng(temp.getDouble("lat"),temp.getDouble("lng"));
			
			if(i < arrPlace.length()/3){
				
				//JSONObject detail = getJSONFromUrl("https://maps.googleapis.com/maps/api/place/details/json?reference=" +
				//		reference + "&sensor=true&key=AIzaSyBBGzY_3gkrw6sNUSEcDHRszYjz-Q99PPI");
				
				//temp = detail.getJSONObject("result");
				//String phone = temp.getString("formatted_phone_number");
				//String weblink = temp.getString("website");
				
				//String phone = "(111) 222-3333";
				//String weblink = "http://youtube.com";
				mMap.addMarker(new MarkerOptions()
					.position(placeLocation)
					.title(title)
					.snippet(reference)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.greencheck)));
			}
			else if(i < arrPlace.length()/3*2){
				mMap.addMarker(new MarkerOptions()
					.position(placeLocation)
					.title(title)
					.snippet(reference)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowcheck)));
			}
			else{
				mMap.addMarker(new MarkerOptions()
					.position(placeLocation)
					.title(title)
					.snippet(reference)
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
	
	public int union (String [] a, int size, String [] b){
		System.out.println("union");
		//int tempSize = 0;
		boolean isThere = false;
		//String [] temp = new String [size + b.length];
		for(int i = 0; i < b.length; i++){
			for(int j = 0; j < size; j++){
				if(a[j].equals(b[i])){
					isThere = true;
					break;
				}
			}
			if(!isThere){
				a[size] = b[i];
				size++;
			}
		}
		//for(int i = 0; i < b.length; i++){
		//	temp[tempSize] = b[i];
		//	tempSize++;
		//}
		//a = temp;
		for(int i = 0; i < size; i++){
			System.out.println(a[i]);
		}
		return size;
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
