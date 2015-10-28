package com.example.flashalert.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;

public class CommonUtils {

	private SharedPreferences prefs;
	private boolean enable = true;
	private Camera cam;
	private Camera.Parameters params;

	public static boolean hasKitKat() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT;
	}

	public CommonUtils() {
		enable = true;
	}

	public boolean isFlashEnable() {
		return enable;
	}

	public void setFlashEnable(boolean enable) {
		this.enable = enable;
	}

	public void flickFlash(int onLength, int offLength) {
		flickFlash(onLength, offLength, 300);
	}

	public void flickFlash(final int onLength, final int offLength, final int count) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					releaseCameraAndPreview();
					cam = Camera.open();
					params = cam.getParameters();
					cam.startPreview();
				} catch (Exception e) {
					Log.e("CallService", e.toString());
				}

				for (int i = 0; i < count; i++) {
					if (!enable) {
						break;
					}
					try {
						flipFlash(cam, params, false);
					} catch (Exception e) {
						// TODO: handle exception
					}

					try {
						Thread.sleep(onLength);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					try {
						flipFlash(cam, params, true);
					} catch (Exception e) {
						// TODO: handle exception
					}

					try {
						Thread.sleep(offLength);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				enable = true;
				try {
					cam.stopPreview();
					cam.release();
					cam = null;
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}).start();
	}

	private void flipFlash(Camera cam, Camera.Parameters params, boolean flashIsOn) {
		if (flashIsOn) {
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			cam.setParameters(params);
			flashIsOn = false;
		} else {
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
			cam.setParameters(params);
			flashIsOn = true;
		}
	}

	private void releaseCameraAndPreview() {
		if (cam != null) {
			cam.release();
			cam = null;
		}
	}

	public boolean checkSetup(Context context, String type) {
		prefs = context.getSharedPreferences(Properties.PREF_MAIN_NAME, Context.MODE_PRIVATE);
		AudioManager am = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
		int indexProfile = am.getRingerMode();

		boolean valuePower = prefs.getBoolean(Properties.POWER_VALUE, true);
		boolean valueIncomingCall = prefs.getBoolean(Properties.PREF_INCOMING_CALL_VALUE, true);
		boolean valueIncomingSMS = prefs.getBoolean(Properties.PREF_INCOMING_TEXT_VALUE, true);
		boolean valueNormal = prefs.getBoolean(Properties.PREF_NORMAL_MODE_VALUE, true);
		boolean valueVibrate = prefs.getBoolean(Properties.PREF_VIBRATE_MODE_VALUE, false);
		boolean valueSilent = prefs.getBoolean(Properties.PREF_SILENT_MODE_VALUE, false);
		boolean valueHourTurnOn = prefs.getBoolean(Properties.PREF_SET_TIME_VALUE, false);
		int hourOn = prefs.getInt(Properties.PREF_START_HOUR_VALUE, 0);
		int minuteOn = prefs.getInt(Properties.PREF_START_MINUTE_VALUE, 0);
		int hourOff = prefs.getInt(Properties.PREF_END_HOUR_VALUE, 0);
		int minuteOff = prefs.getInt(Properties.PREF_END_MINUTE_VALUE, 0);


		if (!valuePower) {
			return false;
		}

		if (!valueIncomingCall && type.equals(Properties.TYPE_CALL)) {
			return false;
		} else if (!valueIncomingSMS && type.equals(Properties.TYPE_SMS)) {
			return false;
		}

		if (indexProfile == AudioManager.RINGER_MODE_NORMAL) {
			if (!valueNormal) {
				return false;
			}
		} else if (indexProfile == AudioManager.RINGER_MODE_VIBRATE) {
			if (!valueVibrate) {
				return false;
			}
		} else if (indexProfile == AudioManager.RINGER_MODE_SILENT) {
			if (!valueSilent) {
				return false;
			}
		}

		if (valueHourTurnOn) {

			Calendar cal = Calendar.getInstance();
			int current_minute = cal.get(Calendar.MINUTE);
			//12 hour format
			int current_hour_12 = cal.get(Calendar.HOUR);
			//24 hour format
			int current_hour_24 = cal.get(Calendar.HOUR_OF_DAY);

			String startTime = hourOn + ":" + minuteOn;
			String endTime = hourOff + ":" + minuteOff;
			String nowTime = current_hour_24 + ":" + current_minute;
			Log.e("CallService", "date:" + startTime + "  " + endTime + "  " + nowTime);
			try {
				Date endDate =  new SimpleDateFormat("HH:mm").parse(endTime);
				Date startDate = new SimpleDateFormat("HH:mm").parse(startTime);
				Date nowDate =  new SimpleDateFormat("HH:mm").parse(nowTime);
				if (hourOn <= hourOff) {
					if (nowDate.after(startDate) && nowDate.before(endDate)) {
						Log.e("CallService", "date in");
					} else {
						Log.e("CallService", "date out");
						return false;
					}
				} else {
					if (nowDate.before(startDate) && nowDate.after(endDate)) {
						Log.e("CallService", "date in");
						return false;
					} else {
						Log.e("CallService", "date out");
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	public static void setSwitchChangeColor(SwitchCompat sw) {
		if (sw.isChecked()) {
			sw.getThumbDrawable().setColorFilter(Color.parseColor("#6ea51d"), Mode.MULTIPLY);
			sw.getTrackDrawable().setColorFilter(Color.parseColor("#806ea51d"), Mode.MULTIPLY);
		} else {
			sw.getThumbDrawable().setColorFilter(Color.parseColor("#8f8e8e"), Mode.MULTIPLY);
			sw.getTrackDrawable().setColorFilter(Color.parseColor("#608f8e8e"), Mode.MULTIPLY);
		}
	}

	

}