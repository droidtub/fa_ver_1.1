package com.example.flashalert.service;

import com.example.flashalert.receiver.SettingsContentObserver;
import com.example.flashalert.utils.CommonUtils;
import com.example.flashalert.utils.Properties;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallService extends Service {

	public static final String TAG = CallService.class.getName();
	
	private String curState = TelephonyManager.EXTRA_STATE_IDLE;
	private Context mContext;
	private SharedPreferences prefs;
	private CommonUtils commonUtils;
	private SettingsContentObserver mSettingsContentObserver;

	@Override
	public IBinder onBind(Intent intent) {
		Log.e(TAG, "onBind");
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.e(TAG, "Create service");
		mContext = this;
		commonUtils = new CommonUtils();
		prefs = mContext.getApplicationContext().getSharedPreferences(Properties.PREF_MAIN_NAME, Context.MODE_PRIVATE);
		mSettingsContentObserver = new SettingsContentObserver(this, new Handler(), commonUtils);
		getApplicationContext().getContentResolver()
				.registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver);
		
		IntentFilter checkFilter = new IntentFilter();
		checkFilter.addAction(Intent.ACTION_SCREEN_OFF);
		checkFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(checkStateReceiver, checkFilter);
	}
	
	private BroadcastReceiver checkStateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                Log.i(TAG, "Screen OFF");
                commonUtils.setFlashEnable(false);
                stopSelf();
            } else if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
				int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				int level = -1;
				if (currentLevel >= 0 && scale > 0) {
					level = (currentLevel * 100) / scale;
				}
				int batteryPercentSave = prefs.getInt(Properties.PREF_BATTERY_VALUE, Properties.PREF_DEFAULT_BATTERY_VALUE);
				if (level < batteryPercentSave) {
					Log.i(TAG, "Battery Low");
					commonUtils.setFlashEnable(false);
	                stopSelf();
				}
            }
		}
		
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onStartCommand");
		//Log.e(TAG, intent.getAction());
		
		if (intent == null || intent.getAction() == null) {
			return super.onStartCommand(intent, flags, startId);
		}
		
		curState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

		if (curState.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)
				|| curState.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

			commonUtils.setFlashEnable(false);
			stopSelf();
		}

		if (curState.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
			// TODO flicker led
			Log.e(TAG, "RING RING");
			int onLength = prefs.getInt(Properties.PREF_CALL_ON_LENGTH_VALUE, Properties.PREF_DEFAULT_LENGTH_FLASH_ON_OFF);
			int offLength = prefs.getInt(Properties.PREF_CALL_OFF_LENGTH_VALUE, Properties.PREF_DEFAULT_LENGTH_FLASH_ON_OFF);
			commonUtils.flickFlash(onLength, offLength);
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		Log.e(TAG, "onDestroy");
		unregisterReceiver(checkStateReceiver);
		getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsContentObserver);
		super.onDestroy();
	}
}