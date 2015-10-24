package com.example.flashalert.view;

import com.droidtub.flashalert.R;
import com.example.flashalert.utils.CommonUtils;
import com.example.flashalert.utils.Properties;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class IncomingTextDialogFragment extends DialogFragment implements OnSeekBarChangeListener, View.OnClickListener{
	
	private int onProgress = 500;
	private int offProgress = 500;
	private int times = 3;
	private int step = 50;
	
	private SharedPreferences pref;
	private SharedPreferences.Editor edit;
	private TextView onLength;
	private TextView offLength;
	private TextView flashTimes;
	private TextView btnOk;
	private TextView btnCancel;
	private TextView btnTest;
	private boolean isEnable = true;
	private CommonUtils utils;
	
	@Override
	public Dialog onCreateDialog(Bundle bundle){
		pref = getActivity().getSharedPreferences(Properties.PREF_MAIN_NAME, Context.MODE_PRIVATE);
		edit = pref.edit();
		utils = new CommonUtils();
		
		AlertDialog.Builder txtBuilder = new AlertDialog.Builder(getActivity());
		LayoutInflater txtInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = txtInflater.inflate(R.layout.txt_dialog_layout, null);
		
		SeekBar mSeekBarOnLength = (SeekBar) view.findViewById(R.id.text_seekbar_on_length);
		mSeekBarOnLength.setOnSeekBarChangeListener(this);
		SeekBar mSeekBarOffLength = (SeekBar)view.findViewById(R.id.text_seekbar_off_length);
		mSeekBarOffLength.setOnSeekBarChangeListener(this);
		SeekBar mSeekBarFlashTimes = (SeekBar)view.findViewById(R.id.text_seekbar_flash_time);
		mSeekBarFlashTimes.setOnSeekBarChangeListener(this);
		
		btnOk = (TextView)view.findViewById(R.id.txtOk);
		btnOk.setOnClickListener(this);
		btnCancel = (TextView)view.findViewById(R.id.txtCancel);
		btnCancel.setOnClickListener(this);
		btnTest = (TextView)view.findViewById(R.id.txtTest);
		btnTest.setOnClickListener(this);
		
		onLength = (TextView)view.findViewById(R.id.text_on_length_ms);
	    offLength = (TextView)view.findViewById(R.id.text_off_length_ms);
	    flashTimes = (TextView)view.findViewById(R.id.text_flash_time);
	    
		mSeekBarOnLength.setProgress(pref.getInt(Properties.PREF_TXT_ON_LENGTH_VALUE, 500) - 50);
		mSeekBarOffLength.setProgress(pref.getInt(Properties.PREF_TXT_OFF_LENGTH_VALUE, 500) - 50);
		mSeekBarFlashTimes.setProgress(pref.getInt(Properties.PREF_TXT_TIMES_VALUE, 3) - 1);
		
		onLength.setText(pref.getInt(Properties.PREF_TXT_ON_LENGTH_VALUE, 500) + " ms");
		offLength.setText(pref.getInt(Properties.PREF_TXT_OFF_LENGTH_VALUE, 500) + " ms");
		flashTimes.setText(pref.getInt(Properties.PREF_TXT_TIMES_VALUE, 3) + " Time(s)");
	
		txtBuilder.setView(view);
				/*.setTitle(R.string.text_title)
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
					}
				})
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						edit.putInt(Properties.PREF_TXT_ON_LENGTH_VALUE, onProgress);
						edit.putInt(Properties.PREF_TXT_OFF_LENGTH_VALUE, offProgress);
						edit.putInt(Properties.PREF_TXT_TIMES_VALUE, times);
						edit.commit();
					}
				})
				.setNeutralButton(R.string.test_on, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
					}
				});*/
		Dialog dialog = txtBuilder.create();
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		switch (seekBar.getId()) {
		case R.id.text_seekbar_on_length:
			onProgress = ((int)Math.round(progress/step ))*step + 50;
			onLength.setText(onProgress + " ms");
			break;
		case R.id.text_seekbar_off_length:
			offProgress = ((int)Math.round(progress/step ))*step + 50;
			offLength.setText(offProgress + " ms");
			break;
		case R.id.text_seekbar_flash_time:
			times = progress + 1;
			flashTimes.setText(times + " Time(s)");
		default:
			break;
		}
		
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	
		
	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtOk:
			edit.putInt(Properties.PREF_TXT_ON_LENGTH_VALUE, onProgress);
			edit.putInt(Properties.PREF_TXT_OFF_LENGTH_VALUE, offProgress);
			edit.putInt(Properties.PREF_TXT_TIMES_VALUE, times);
			edit.commit();
			if(!isEnable)
				utils.setFlashEnable(false);
			this.dismiss();
			break;
		case R.id.txtCancel:
			if(!isEnable)
				utils.setFlashEnable(false);
			this.dismiss();
			break;
		case R.id.txtTest:
			edit.putInt(Properties.PREF_TXT_ON_LENGTH_VALUE, onProgress);
			edit.putInt(Properties.PREF_TXT_OFF_LENGTH_VALUE, offProgress);
			edit.putInt(Properties.PREF_TXT_TIMES_VALUE, times);
			edit.commit();
			if(isEnable){
				btnTest.setText("TEST OFF");
				utils.flickFlash(pref.getInt(Properties.PREF_TXT_ON_LENGTH_VALUE, 50), pref.getInt(Properties.PREF_TXT_OFF_LENGTH_VALUE, 50));
				isEnable = false;
			} else{
				btnTest.setText("TEST ON");
				utils.setFlashEnable(false);
				isEnable = true;
			}
			break;
		default:
			break;
		}
		
	}
}