package com.example.flashalert.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.droidtub.flashalert.R;
import com.example.flashalert.model.AppInfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

	private LayoutInflater inflater;
	private Context mContext;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	private List<AppInfo> appList;

	public AppAdapter(Context context, ArrayList<AppInfo> appList) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		mContext = context;

		prefs = context.getSharedPreferences(com.example.flashalert.utils.Properties.PREF_MAIN_NAME, Context.MODE_PRIVATE);
		editor = prefs.edit();
		editor.commit();
		this.appList = appList;
		//new LoadApp().execute();
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		if(appList!= null)
			return appList.size();
		return 0;
	}

	@Override
	public void onBindViewHolder(final AppViewHolder holder, int position) {
		// TODO Auto-generated method stub
		final AppInfo item = appList.get(position);
		//final ApplicationInfo item = listApp.get(position);
		holder.title.setText(item.getName());
		holder.icon.setImageDrawable(item.getIcon());

		holder.checkbox.setChecked(prefs.getBoolean(item.getPackageName(), false));

		holder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    editor.putBoolean(item.getPackageName(), isChecked);
                    editor.commit();

                }
            }
        });

		holder.checkbox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                    boolean mValue = !prefs.getBoolean(item.getPackageName(), false);
                    holder.checkbox.setChecked(mValue);
                    editor.putBoolean(item.getPackageName(), mValue);
                    editor.commit();

            }
        });
	}

	@Override
	public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.app_row, parent, false);
		AppViewHolder holder = new AppViewHolder(view);
		return holder;
	}

	class AppViewHolder extends RecyclerView.ViewHolder{
		ImageView icon;
		TextView title;
		CheckBox checkbox;

		public AppViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			icon = (ImageView)itemView.findViewById(R.id.app_icon);
			title = (TextView)itemView.findViewById(R.id.app_title);
			checkbox = (CheckBox)itemView.findViewById(R.id.app_checkbox);
		}

	}


	public void handleClick(View view, int position){
		AppInfo item = appList.get(position);
		boolean mValue = !prefs.getBoolean(item.getPackageName(), false);
		CheckBox mCheckbox = (CheckBox)view.findViewById(R.id.app_checkbox);
		mCheckbox.setChecked(mValue);
		editor.putBoolean(item.getPackageName(), mValue);
		editor.commit();

	}
}
