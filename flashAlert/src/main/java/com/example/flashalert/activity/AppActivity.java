package com.example.flashalert.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidtub.flashalert.R;
import com.example.flashalert.adapter.AppAdapter;
import com.example.flashalert.model.AppInfo;
import com.example.flashalert.service.NotificationService;
import com.example.flashalert.utils.Properties;
import com.example.flashalert.view.AppDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by hanh on 11/10/2015.
 */
public class AppActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private AppAdapter mAdapter;
    private ArrayList<AppInfo> appList;
    private ProgressWheel progressWheel;
    private Toolbar mToolbar;
    private TextView mCancelButton;
    private TextView mOkButton;
    private SharedPreferences fromShared;
    private SharedPreferences toShared;
    private SwitchCompat appSwitch;
    private ImageView backBtn;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_dialog);

        appSwitch = (SwitchCompat)findViewById(R.id.app_switch);
        appSwitch.setChecked(NotificationService.isNotificationAccessEnabled);
        appSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent i = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivityForResult(i, 0);
            }
        });

        backBtn = (ImageView)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressWheel = (ProgressWheel)findViewById(R.id.progress);
        progressWheel.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView)findViewById(R.id.app_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        appList = new ArrayList<AppInfo>();
        mAdapter = new AppAdapter(this, appList);
        new LoadApp().execute();
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new BaseUiClickListener() {
            @Override
            public void onClick(View view, int position) {

                mAdapter.handleClick(view, position);

            }
        }));

        mCancelButton = (TextView)findViewById(R.id.appCancel);
        mOkButton = (TextView)findViewById(R.id.appOK);
        mCancelButton.setOnClickListener(this);
        mOkButton.setOnClickListener(this);
        fromShared = this.getSharedPreferences(com.example.flashalert.utils.Properties.PREF_MAIN_NAME, Context.MODE_PRIVATE);
        toShared = this.getSharedPreferences(Properties.PREF_COPY, Context.MODE_PRIVATE );
        copySharedPrefs(fromShared, toShared);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            this.getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        /*AdView mBottomAddViewApp = (AdView)findViewById(R.id.bottom_ad_banner_app);
        AdRequest adRequest = new AdRequest.Builder().build();
        mBottomAddViewApp.loadAd(adRequest);*/


        /*mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(this.getResources().getString(R.string.full_ad_enter_appscreen));
        requestNewInterstitial();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded(){
                if(mInterstitialAd!= null && mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }
            }


        });*/

    }

    @Override
    protected void onResume(){
        appSwitch.setChecked(NotificationService.isNotificationAccessEnabled);
        super.onResume();
    }

    class LoadApp extends AsyncTask<Void, String, Void> {
        private Integer actualApp;
        private Integer totalApp;

        public LoadApp(){
            actualApp = 0;
        }
        @Override
        protected Void doInBackground(Void... params){

            final PackageManager packageManager = getPackageManager();
            List<PackageInfo> packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
            totalApp = packages.size();
            Collections.sort(packages, new Comparator<PackageInfo>() {
                @Override
                public int compare(PackageInfo p1, PackageInfo p2) {
                    return packageManager.getApplicationLabel(p1.applicationInfo).toString().toLowerCase().compareTo(packageManager.getApplicationLabel(p2.applicationInfo).toString().toLowerCase());
                }
            });

            for(PackageInfo packageInfo : packages){
                if(!(packageManager.getApplicationLabel(packageInfo.applicationInfo).equals("") || packageInfo.packageName.equals(""))){
                    if(packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null){
                        AppInfo tempApp = new AppInfo(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(), packageInfo.packageName, packageManager.getApplicationIcon(packageInfo.applicationInfo));
                        appList.add(tempApp);
                    }
                }
            }

            actualApp++;
            publishProgress(Double.toString((actualApp*100)/totalApp));
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress){
            super.onProgressUpdate(progress);
            progressWheel.setProgress(Float.parseFloat(progress[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            progressWheel.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }

    public interface BaseUiClickListener{
        public void onClick(View view, int position);
    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private BaseUiClickListener clickListener;

        public RecyclerTouchListener(AppCompatActivity mActivity, RecyclerView recyclerView, BaseUiClickListener listener) {
            this.clickListener = listener;
            gestureDetector = new GestureDetector(mActivity, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e){
                    return true;
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View item = rv.findChildViewUnder(e.getX(), e.getY());
            if(item != null && clickListener != null && gestureDetector.onTouchEvent(e)){
                clickListener.onClick(item, rv.getChildPosition(item));
            }
            return false;
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean arg0) {

        }

        @Override
        public void onTouchEvent(RecyclerView arg0, MotionEvent arg1) {

        }

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.appOK:
                onBackPressed();
                break;
            case R.id.appCancel:
                copySharedPrefs(toShared, fromShared);
                onBackPressed();
                break;
        }
    }

    public void copySharedPrefs(SharedPreferences fromShared, SharedPreferences toShared){
        SharedPreferences.Editor editor = toShared.edit();
        for(Map.Entry<String, ?> entry : fromShared.getAll().entrySet()){
            Object v = entry.getValue();
            String key = entry.getKey();
            if(v instanceof Boolean)
                editor.putBoolean(key, ((Boolean) v).booleanValue());
            else if(v instanceof Float)
                editor.putFloat(key, ((Float) v).floatValue());
            else if(v instanceof Integer)
                editor.putInt(key, ((Integer) v).intValue());
            else if(v instanceof Long)
                editor.putLong(key, ((Long) v).longValue());
            else if(v instanceof String)
                editor.putString(key, ((String)v));
        }
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        return true;
    }

    /*private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);

    }*/

}

