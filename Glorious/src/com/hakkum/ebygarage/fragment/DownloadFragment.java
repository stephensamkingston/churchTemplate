package com.hakkum.ebygarage.fragment;


import java.util.ArrayList;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.Glorious.adapter.DownloadAdapter;
import com.actionbarsherlock.app.SherlockFragment;
import com.hakkum.eby.R;
import com.hakkum.ebygarage.customclasses.SoundCloudDownload;
public class DownloadFragment extends SherlockFragment {
	
	private ArrayList<SoundCloudDownload> mp3List = new ArrayList<SoundCloudDownload>();
	private ProgressDialog progressDialog;
	ListView listView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.sound_view, container, false);
		listView = (ListView) rootView.findViewById(R.id.listView1);
		refreshList();
		return rootView;
	}
	
	
	private void refreshList() {
		// TODO Auto-generated method stub
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				progressDialog = ProgressDialog.show(getActivity(),
						"Loading..", "PleasWait");
			}

			@Override
			protected Void doInBackground(Void... params) {
		
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				Log.v("tag", "videos" + mp3List);
				progressDialog.hide();
				DownloadAdapter adapter = new DownloadAdapter(getActivity(), mp3List);
				listView.setAdapter(adapter);
			}
		}.execute();
	}
}
