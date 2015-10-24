package com.example.flashalert.activity;

import android.support.v7.app.ActionBarActivity;

import com.droidtub.flashalert.R;
import com.example.flashalert.controller.SettingController;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

@SuppressWarnings("deprecation")
public class SettingActivity extends ActionBarActivity {

	private SettingController settingController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingController = new SettingController(this);
		settingController.onCreate(savedInstanceState);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
