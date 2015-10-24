package com.example.flashalert.receiver;

import com.example.flashalert.utils.CommonUtils;
import com.example.flashalert.utils.Properties;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneReceiver extends BroadcastReceiver {

	private static final String TAG = PhoneReceiver.class.getName();
	private CommonUtils commonUtils;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (!intent.getAction().equals("android.intent.action.PHONE_STATE")) {
			return;
		}

		// TODO check sharepreferences
		commonUtils = new CommonUtils();
		if (!commonUtils.checkSetup(context.getApplicationContext(), Properties.TYPE_CALL)) {
			Log.e("han.hanh", "PhoneReceiver: return");
			return;
		}

		context.startService(
				new Intent(context, com.example.flashalert.service.CallService.class).setAction(intent.getAction())
						.putExtra(TelephonyManager.EXTRA_INCOMING_NUMBER,
								intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER))
				.putExtra(TelephonyManager.EXTRA_STATE, intent.getStringExtra(TelephonyManager.EXTRA_STATE)));
	}

}
