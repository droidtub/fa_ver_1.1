package com.example.flashalert.receiver;

import com.example.flashalert.utils.CommonUtils;
import com.example.flashalert.utils.Properties;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class MessagingReceiver extends WakefulBroadcastReceiver {

	private static final String TAG = MessagingReceiver.class.getName();
	private SharedPreferences prefs;
	private CommonUtils commonUtils;
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.e(TAG, "OnReceive");
    	if (intent == null || intent.getAction().equals("android.intent.action.PHONE_STATE")) {
			return;
		}
    	commonUtils = new CommonUtils();

    	if (!commonUtils.checkSetup(context.getApplicationContext(), Properties.TYPE_SMS)) {
			return;
		}
    	
    	prefs = context.getSharedPreferences(Properties.PREF_MAIN_NAME, Context.MODE_PRIVATE);
    	int onLength = prefs.getInt(Properties.PREF_TXT_ON_LENGTH_VALUE, Properties.PREF_DEFAULT_LENGTH_FLASH_ON_OFF);
		int offLength = prefs.getInt(Properties.PREF_TXT_OFF_LENGTH_VALUE, Properties.PREF_DEFAULT_LENGTH_FLASH_ON_OFF);
		int countBlink = prefs.getInt(Properties.PREF_TXT_TIMES_VALUE, Properties.PREF_DEFAULT_TXT_TIMES_VALUE);
		commonUtils.flickFlash(onLength, offLength, countBlink);
		
		completeWakefulIntent(intent);
    	
    }
}
