package com.example.flashalert.view;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements OnTimeSetListener {

	private int mId;
	private TimePickerDialogListener mListener;

	public static TimePickerFragment newInstance(int id) {
		Bundle args = new Bundle();
		args.putInt("picker_id", id);
		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		mId = getArguments().getInt("picker_id");
		mListener = (TimePickerDialogListener) getTargetFragment();

		return new TimePickerDialog(getActivity(), this, hour, minute, false);
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		if (mListener != null)
			mListener.onTimeSet(mId, view, hourOfDay, minute);
	}

	public static interface TimePickerDialogListener {
		public void onTimeSet(int id, TimePicker view, int hourOfDay, int minute);
	}

}
