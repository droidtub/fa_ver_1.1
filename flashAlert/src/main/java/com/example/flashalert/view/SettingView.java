package com.example.flashalert.view;

import com.droidtub.flashalert.R;
import com.example.flashalert.adapter.SettingViewPagerAdapter;
import com.example.flashalert.controller.SettingController;
import com.example.flashalert.utils.Properties;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;


public class SettingView {

	private SettingController mController;
	private ActionBarActivity mActivity;
	private Toolbar mToolbar;
	private SettingViewPagerAdapter adapter;
	private ViewPager pager;
	private SlidingTabLayout tabs;
	private CharSequence Titles[] = { "BASIC", "ADVANCE" };
	int Numboftabs = 2;
	private SharedPreferences pref;
	private Context mContext;
	//InterstitialAd mInterstitialAd;

	public SettingView(SettingController controller, ActionBarActivity activity){
		mController = controller;
		mActivity = activity;
		mContext = activity;
		pref = mActivity.getSharedPreferences(Properties.PREF_MAIN_NAME, Context.MODE_PRIVATE);
	}
	
	public void createSettingUi() {
        mActivity.setContentView(R.layout.activity_setting);

        // Creating The Toolbar and setting it as the Toolbar for the activity
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setElevation(0);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new SettingViewPagerAdapter(mActivity.getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) mActivity.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) mActivity.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return mActivity.getResources().getColor(R.color.white);
            }
        });

//        tabs.setSelectedIndicatorColors(R.attr.colorPrimaryDark);

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mActivity.getWindow().setNavigationBarColor(mContext.getResources().getColor(R.color.colorPrimary));
        }

        /*mInterstitialAd = new InterstitialAd(mActivity);
        mInterstitialAd.setAdUnitId(mActivity.getResources().getString(R.string.full_ad_enter_setting));

        requestNewInterstitial();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if(mInterstitialAd!= null && mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }
            }
        });
	}

    public void requestNewInterstitial(){
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID").build();
        mInterstitialAd.loadAd(adRequest);
    }*/
    }
}
