package com.example.flashalert.view;


import com.droidtub.flashalert.R;
import com.example.flashalert.adapter.AppAdapter;
import com.example.flashalert.model.AppInfo;
import com.pnikosis.materialishprogress.ProgressWheel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AppDialog extends DialogFragment{
	private RecyclerView recyclerView;
	private AppAdapter mAdapter;
	private ArrayList<AppInfo> appList;
    private ProgressWheel progressWheel;

	@Override
	public Dialog onCreateDialog(Bundle bundle){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = inflater.inflate(R.layout.app_dialog, null);
        progressWheel = (ProgressWheel)view.findViewById(R.id.progress);
        progressWheel.setVisibility(View.VISIBLE);
		recyclerView = (RecyclerView)view.findViewById(R.id.app_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setHasFixedSize(true);
        appList = new ArrayList<AppInfo>();
        mAdapter = new AppAdapter(getActivity(), appList);
		new LoadApp().execute();
		mAdapter.notifyDataSetChanged();
		recyclerView.setAdapter(mAdapter);
		
		recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),recyclerView, new BaseUiClickListener(){
			@Override
            public void onClick(View view, int position) {

                mAdapter.handleClick(view, position);
                
            }
		}));
		
		builder.setView(view);
		AlertDialog dialog = builder.create();
		//dialog.show();
		return dialog;
		
	}

    class LoadApp extends AsyncTask<Void, String, Void>{
        private Integer actualApp;
        private Integer totalApp;

        public LoadApp(){
            actualApp = 0;
        }
        @Override
        protected Void doInBackground(Void... params){

            final PackageManager packageManager = getActivity().getPackageManager();
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
                       //AppInfo tempApp = new AppInfo(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(), packageManager.getApplicationIcon(packageInfo.applicationInfo));
                       //appList.add(tempApp);
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
        
		public RecyclerTouchListener(Activity mActivity, RecyclerView recyclerView, BaseUiClickListener listener) {
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
}