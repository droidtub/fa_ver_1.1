package com.example.flashalert.service;

import com.example.flashalert.utils.CommonUtils;
import com.example.flashalert.utils.Properties;
import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.view.accessibility.AccessibilityEvent;

public class NotificationService extends NotificationListenerService {

	private Context mContext;
	private SharedPreferences prefs;
	private CommonUtils utils;
	public static boolean isNotificationAccessEnabled = false;

	@Override
	public void onCreate(){
		mContext = getApplicationContext();
		prefs = mContext.getSharedPreferences(com.example.flashalert.utils.Properties.PREF_MAIN_NAME, Context.MODE_PRIVATE);
		utils = new CommonUtils();
	}

    @Override
    public IBinder onBind(Intent mIntent){
        IBinder iBinder = super.onBind(mIntent);
        isNotificationAccessEnabled = true;
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent mIntent){
        boolean mOnUnBind = super.onUnbind(mIntent);
        isNotificationAccessEnabled = false;
        return mOnUnBind;
    }

    @Override
    public void onDestroy(){
        isNotificationAccessEnabled = false;
        super.onDestroy();
    }

    @Override
    public void onRebind(Intent mIntent){
        isNotificationAccessEnabled = true;
        super.onRebind(mIntent);
    }

	@Override
	public void onNotificationPosted(StatusBarNotification sbn){
		String packageName = sbn.getPackageName().toString();
		if(utils.checkSetup(mContext, "") && prefs.getBoolean(packageName, false)){
            int onTime = prefs.getInt(Properties.PREF_TXT_ON_LENGTH_VALUE, 500);
            int offTime = prefs.getInt(Properties.PREF_TXT_OFF_LENGTH_VALUE, 500);
            int times = prefs.getInt(Properties.PREF_TXT_TIMES_VALUE, 3);
            utils.flickFlash(onTime, offTime, times);
		}
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn){

	}

}
