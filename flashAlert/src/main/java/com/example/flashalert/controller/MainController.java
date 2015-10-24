package com.example.flashalert.controller;

import com.example.flashalert.view.MainView;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class MainController {

	private ActionBarActivity mActivity;
	private MainView mUi;
	
	public MainController(ActionBarActivity activity){
		mActivity = activity;
		mUi = new MainView(this, mActivity);
	}
	
	public void onCreate(Bundle savedInstanceState){
		//mUi.initializeUi(mActivity, mActivity.getWindow().getDecorView());
		mUi.initializeUi();
	}
}
