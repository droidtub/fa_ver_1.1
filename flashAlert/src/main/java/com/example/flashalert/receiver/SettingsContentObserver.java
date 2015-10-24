package com.example.flashalert.receiver;

import com.example.flashalert.service.CallService;
import com.example.flashalert.utils.CommonUtils;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

public class SettingsContentObserver extends ContentObserver {
	private int previousVolume;
	private Context context;
	private CommonUtils commonUtils;

	public SettingsContentObserver(Context c, Handler handler, CommonUtils commonUtils) {
		super(handler);
		context = c;
		this.commonUtils = commonUtils;
		AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		previousVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	@Override
	public boolean deliverSelfNotifications() {
		return super.deliverSelfNotifications();
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Log.e("CallService", "onChange");
		AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

		int delta = previousVolume - currentVolume;

		if (delta > 0) {
			previousVolume = currentVolume;
		} else if (delta < 0) {
			previousVolume = currentVolume;
		}
		
		commonUtils.setFlashEnable(false);
		Intent i = new Intent(context, CallService.class);
		context.stopService(i);
	}
}
