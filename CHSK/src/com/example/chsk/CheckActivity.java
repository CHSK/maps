package com.example.chsk;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class CheckActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.check, menu);
		return true;
	}

	@SuppressLint("NewApi")
	public void goToMap(View view)
	{
		
		Button b1 = (Button) findViewById(R.id.button1);
		
		         b1.setBackground(getResources().getDrawable(R.drawable.rectangleyellow));
		 
		 Intent intent = new Intent(this, Map.class);
		 startActivity(intent);

	}
	@SuppressLint("NewApi")
	public void goToQ1(View view)
	{
		
		Button b1 = (Button) findViewById(R.id.button2);
		
		         b1.setBackground(getResources().getDrawable(R.drawable.rectangleyellow));
		 
		 Intent intent = new Intent(this, Quest1.class);
		 startActivity(intent);

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
			View rootView = inflater.inflate(R.layout.fragment_check,
					container, false);
			return rootView;
		}
	}

}
