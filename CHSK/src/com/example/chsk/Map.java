package com.example.chsk;

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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class Map extends FragmentActivity {

	private GoogleMap mMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		setUpMap();
	}

	protected void onResume() {
		super.onResume();
		setUpMap();
	}
	
	private void setUpMap() {
		if(mMap == null){
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			if (mMap != null)
				setUpAllMap();
		}
	}
	
	private void setUpAllMap() {
		Bundle cLocation = getIntent().getParcelableExtra("location");
		LatLng currentLocation = cLocation.getParcelable("currentLocation");
		CameraUpdate center=
		        CameraUpdateFactory.newLatLng(currentLocation);
		CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
		
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
