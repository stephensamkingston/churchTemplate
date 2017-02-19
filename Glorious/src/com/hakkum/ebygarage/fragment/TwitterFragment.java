package com.hakkum.ebygarage.fragment;

import java.util.ArrayList;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.Glorious.adapter.TwitterListAdapter;
import com.actionbarsherlock.app.SherlockFragment;
import com.hakkum.eby.R;
import com.hakkum.ebygarage.customclasses.ServerApi;
import com.hakkum.ebygarage.customclasses.Twitter_Item;
import com.hakkum.ebygarage.customclasses.Utility;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class TwitterFragment extends SherlockFragment implements
		OnRefreshListener<ListView> {
	ArrayList<Twitter_Item> twitterFeeds;
	private ProgressDialog progressDialog;
	PullToRefreshListView ptrlv;
	TwitterListAdapter adapter;
	public boolean isAvailability;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.twitter_view, container,
				false);
		ptrlv = (PullToRefreshListView) rootView.findViewById(R.id.listView1);
		ptrlv.setOnRefreshListener(this);
		refreshList();
		return rootView;
	}

	public void refreshList() {
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
				if (!Utility.isNetworkAvailable(getActivity())) {
					isAvailability = false;
				} else {
					isAvailability = true;
					if (Utility.ReadTwitterFeeds(getActivity(), "twitterFeeds") == null)
						Utility.WriteSettings(getActivity(),
								ServerApi.getTwitterFeeds(), "twitterFeeds");

				}
				twitterFeeds = Utility.ReadTwitterFeeds(getActivity(),
						"twitterFeeds");
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				progressDialog.hide();

				if (!isAvailability) {
					Toast.makeText(getActivity(), R.string.check_connectivity,
							Toast.LENGTH_SHORT).show();
				}
				if (twitterFeeds != null) {
					adapter = new TwitterListAdapter(getActivity(), 0,
							twitterFeeds);
					ptrlv.setAdapter(adapter);
				}
			}
		}.execute();

	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		new GetDataTask(ptrlv).execute();

	}

	private class GetDataTask extends AsyncTask<Void, Void, Void> {

		PullToRefreshBase<?> mRefreshedView;

		public GetDataTask(PullToRefreshBase<?> refreshedView) {
			mRefreshedView = refreshedView;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			progressDialog = ProgressDialog.show(getActivity(), "Loading..",
					"PleasWait");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Simulates a background job.
			if (!Utility.isNetworkAvailable(getActivity())) {
				Toast.makeText(getActivity(), R.string.check_connectivity,
						Toast.LENGTH_SHORT).show();
			} else {
				Utility.WriteSettings(getActivity(),
						ServerApi.getTwitterFeeds(), "twitterFeeds");
			}
			twitterFeeds = Utility.ReadTwitterFeeds(getActivity(),
					"twitterFeeds");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.hide();
			mRefreshedView.onRefreshComplete();
			adapter = new TwitterListAdapter(getActivity(), 0, twitterFeeds);
			ptrlv.setAdapter(adapter);
			super.onPostExecute(result);
		}
	}

}
