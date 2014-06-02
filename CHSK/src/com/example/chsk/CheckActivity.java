package com.example.chsk;

import java.io.IOException;
import java.util.List;
import java.util.zip.CheckedInputStream;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Build;

public class CheckActivity extends ActionBarActivity implements 
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {
	//options is the amount of stuff we can search for
	//boolean checks is to keep track of the checkboxes that are checked
	int options = 8;
	String areaCode = "";
	boolean zipcode = false;
	boolean checks[] = new boolean[options];
	CheckBox checkbox[] = new CheckBox[options];
	LocationClient mLocationClient;
	Location currentLocation;
	Geocoder geocoder = new Geocoder(this);
	double latitude;
	double longitude;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		mLocationClient = new LocationClient(this, this, this);

	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.check, menu);
		//returns the btn to its normal color
		getMenuInflater().inflate(R.menu.login, menu);
		Button b1 = (Button) findViewById(R.id.quick);
		//Button b2 = (Button) findViewById(R.id.button2);
		 b1.setBackground(getResources().getDrawable(R.drawable.rectangle));
		 //b2.setBackground(getResources().getDrawable(R.drawable.rectangle));
		return true;
	}



//this handles entering an area code
	public void enterArea(final View view)
	{
		 
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(2, 2, 2, 2);

        TextView tv = new TextView(this);
        tv.setText("Enter an Area Code");
        tv.setPadding(40, 40, 40, 40);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);

        final EditText et = new EditText(this);
        
       

        LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv1Params.bottomMargin = 5;
       
        layout.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        alertDialogBuilder.setView(layout);
        alertDialogBuilder.setTitle("Area Code");
        // alertDialogBuilder.setMessage("Input Student ID");
        alertDialogBuilder.setCustomTitle(tv);

      

        // Setting Negative "Cancel" Button
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        // Setting Positive "Yes" Button
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	try{
            		areaCode = et.getText().toString();
            	
            	}
            	catch(Exception e)
            	{
            		//invaild area code
            		areaCode = "";
            	//	 Log.v("bye", "blah blah");
            	}
            	if(!areaCode.equals(""))
            	{
            		zipcode = true;
            		goToMap(view);
            	}
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        try {
            alertDialog.show();
        } catch (Exception e) {
            // WindowManager$BadTokenException will be caught and the app would
            // not display the 'Force Close' message
            e.printStackTrace();
        }
			}
		
	
	@SuppressLint("NewApi")
	public void goToMap(View view)
	{
		if(zipcode) {
			try {
				List<Address> addresses = geocoder.getFromLocationName(areaCode, 1);
				if(addresses != null && !addresses.isEmpty()) {
					Address address = addresses.get(0);
					latitude = address.getLatitude();
					longitude = address.getLongitude();
				}
			} catch (IOException e) {}
			zipcode = false;
		}
		else {
			currentLocation = mLocationClient.getLastLocation();
			latitude = currentLocation.getLatitude();
			longitude = currentLocation.getLongitude();
		}
		LatLng cLocation = new LatLng(latitude, longitude);
		Bundle currentLocation = new Bundle();
		currentLocation.putParcelable("currentLocation", cLocation);

		
		//tv.setText(test);
		checkbox[0] = (CheckBox) findViewById(R.id.CheckIndoors);
		checkbox[1] = (CheckBox) findViewById(R.id.CheckOutdoors);
		checkbox[2] = (CheckBox) findViewById(R.id.CheckActive);
		checkbox[3] = (CheckBox) findViewById(R.id.CheckInactive);
		checkbox[4] = (CheckBox) findViewById(R.id.CheckDay);
		checkbox[5] = (CheckBox) findViewById(R.id.CheckNight);
		checkbox[6] = (CheckBox) findViewById(R.id.CheckFood);
		checkbox[7] = (CheckBox) findViewById(R.id.CheckFood);


		//determines which boxes are checked
		for(int i = 0; i < options; i ++)
		{
			checks[i] = checkbox[i].isChecked();
		//	tv.append(" " + checks[i]);
		}

		Button b1 = (Button) findViewById(R.id.quick);
		b1.setBackground(getResources().getDrawable(R.drawable.rectangleyellow));

		 Intent intent = new Intent(this, Map.class);
		 TextView tv = (TextView) findViewById(R.id.keywords);
		 String s = tv.getText().toString();
			
		 intent.putExtra("keywords", s);
		 
		 intent.putExtra("areaCode", areaCode);
		 
		 intent.putExtra("checks", checks);
		 
		 intent.putExtra("location", currentLocation);
				 //this is the line you need to retrieve the checks in the next intent
			//	 boolean checks[] = getIntent().getBooleanArrayExtra("checks");
		 startActivity(intent);
		
	}

	protected void onStart() {
		super.onStart();
		mLocationClient.connect();
	}
	@Override
	public void onConnected(Bundle dataBundle) {}
	@Override
	public void onDisconnected() {}
	public void onConnectionFailed(ConnectionResult connectionResult) {}

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
			View rootView = inflater.inflate(R.layout.fragment_check,
					container, false);
			return rootView;
		}
	}

}
