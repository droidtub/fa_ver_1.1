package com.example.flashalert.view;

import com.droidtub.flashalert.R;
import com.example.flashalert.activity.SettingActivity;
import com.example.flashalert.controller.MainController;
import com.example.flashalert.utils.Properties;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/*import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;*/

public class MainView implements View.OnClickListener{
	private MainController mController;
	private ActionBarActivity mActivity;
	protected Context mContext;
	private ViewGroup mBottomContainer;
	private View mBottomBarUi;
	private SharedPreferences pref;
	private Drawable drawable;
	private String appPackage;

	
	public MainView(MainController controller, ActionBarActivity activity){
		mController = controller;
		mActivity = activity;
        //mContext = activity;
		pref = mActivity.getApplicationContext().getSharedPreferences(Properties.PREF_MAIN_NAME, Context.MODE_PRIVATE);
		appPackage = mActivity.getApplicationContext().getPackageName();
	}
	
	
	
	public void initializeUi(){
		//Remove title bar
		//mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mActivity.setContentView(R.layout.activity_main);
		mBottomContainer = (ViewGroup)mActivity.findViewById(R.id.bottom_container);
		ViewStub bottomBarStub = new ViewStub(mActivity, R.layout.bottombar);
		mBottomContainer.addView(bottomBarStub);
        bottomBarStub.setInflatedId(R.id.bottom_bar);
        mBottomBarUi = bottomBarStub.inflate();
        
        ImageButton powerBtn = (ImageButton)mActivity.findViewById(R.id.power_btn);
        powerBtn.setSelected(pref.getBoolean(Properties.POWER_VALUE, false));
        powerBtn.setOnClickListener(this);
        
        
        TextView shareBtn = (TextView)mActivity.findViewById(R.id.share_btn);
        drawable = mActivity.getResources().getDrawable(R.drawable.bottombar_share_selector);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());    
        shareBtn.setCompoundDrawables(null, drawable, null, null);
        shareBtn.setOnClickListener(this);
        
        TextView likeBtn = (TextView)mActivity.findViewById(R.id.like_btn);
        drawable = mActivity.getResources().getDrawable(R.drawable.bottombar_like_selector);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        likeBtn.setCompoundDrawables(null, drawable, null, null);
        likeBtn.setOnClickListener(this);
        
        TextView setting = (TextView)mActivity.findViewById(R.id.settings_btn);
        drawable = mActivity.getResources().getDrawable(R.drawable.bottombar_setting_selector);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        setting.setCompoundDrawables(null, drawable, null, null);
        setting.setOnClickListener(this);

		/*AdView mBottomAddViewHome = (AdView)mActivity.findViewById(R.id.bottom_ad_banner_main);
		AdRequest adRequest = new AdRequest.Builder().build();
		mBottomAddViewHome.loadAd(adRequest);*/



    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.power_btn:
			if(!pref.getBoolean(Properties.POWER_VALUE, false)){
				SharedPreferences.Editor edit = pref.edit();
				edit.putBoolean(Properties.POWER_VALUE, true );
				edit.apply();
			}
			else{
				SharedPreferences.Editor edit = pref.edit();
				edit.putBoolean(Properties.POWER_VALUE, false);
				edit.apply();
			}	
			v.setSelected(pref.getBoolean(Properties.POWER_VALUE, false));
			break;
			
		case R.id.settings_btn:
			Intent i = new Intent(mActivity, SettingActivity.class);
            //if (mInterstitialAd.isLoaded()) {
            //    mInterstitialAd.show();
           // } else {
                mActivity.startActivity(i);
            //}
			break;
		case R.id.like_btn:
			Toast.makeText(mActivity, "Thank you for rating and comment :)", Toast.LENGTH_SHORT).show();
			Intent like = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackage));
			like.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mActivity.startActivity(like);
			break;
		case R.id.share_btn:
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
		    share.setType("text/plain");
		    share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    Toast.makeText(mActivity, "Thank you for sharing :)", Toast.LENGTH_SHORT).show();
		    share.putExtra(Intent.EXTRA_SUBJECT, "Greate Flash Alert App Ever :)");
		    share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackage);
		    mActivity.startActivity(Intent.createChooser(share, "Share to be share!"));
			break;
		default:
			break;
		}
	}

}
