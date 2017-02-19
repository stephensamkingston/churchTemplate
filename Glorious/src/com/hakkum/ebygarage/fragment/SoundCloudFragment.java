package com.hakkum.ebygarage.fragment;

import java.net.URISyntaxException;
import java.util.ArrayList;

import com.Glorious.adapter.CloudAdapter;
import com.actionbarsherlock.app.SherlockFragment;
import com.hakkum.eby.R;
import com.hakkum.eby.garageImageFetcher.ImageFetcher;
import com.hakkum.eby.garageImageFetcher.ImageCache.ImageCacheParams;
import com.hakkum.ebygarage.customclasses.ServerApi;
import com.hakkum.ebygarage.customclasses.SoundCloud;
import com.hakkum.ebygarage.customclasses.Utility;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SoundCloudFragment extends SherlockFragment implements
		OnRefreshListener<ListView>, OnCompletionListener {

	private ArrayList<SoundCloud> mp3List = new ArrayList<SoundCloud>();
	private ProgressDialog progressDialog;
	public boolean isAvailability;
	public static PullToRefreshListView ptrlv;
	public static SeekBar songProgressBar;
	public static Button btnMusicPlay;
	private ImageFetcher mImageFetcher;
	private int mImageThumbSize;
	private ImageView cover;
	public static Button changeButton;
	public static Handler mHandler = new Handler();

	public static SoundCloudFragment newInstance() {
		SoundCloudFragment guitarVideos = new SoundCloudFragment();
		return guitarVideos;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.sound_view, container, false);
		songProgressBar = (SeekBar) rootView.findViewById(R.id.songProgressBar);
		btnMusicPlay = (Button) rootView.findViewById(R.id.play);
		ptrlv = (PullToRefreshListView) rootView.findViewById(R.id.listView1);
		cover = (ImageView) rootView.findViewById(R.id.thumb);
		ptrlv.setOnRefreshListener(this);

		// --------Stephen uncomment later

		ImageCacheParams cacheParams = new ImageCacheParams(
				rootView.getContext(), Utility.IMAGE_CACHE_DIR);

		mImageThumbSize = getResources().getDimensionPixelSize(
				R.dimen.photo_thumbnail_size);
		mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
		mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(),
				cacheParams);

		btnMusicPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (CloudAdapter.mPlay.isPlaying()) {
					btnMusicPlay
							.setBackgroundResource(R.drawable.play_btn_selector);
					changeButton
							.setBackgroundResource(R.drawable.play_btn_selector);
					CloudAdapter.mPlay.pause();
				} else {
					CloudAdapter.mPlay.start();
					btnMusicPlay
							.setBackgroundResource(R.drawable.pause_btn_selector);
					changeButton
							.setBackgroundResource(R.drawable.pause_btn_selector);
				}
			}
		});

		songProgressBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						mHandler.removeCallbacks(mUpdateTimeTask);
						int totalDuration = CloudAdapter.mPlay.getDuration();
						int currentPosition = Utility.progressToTimer(
								seekBar.getProgress(), totalDuration);

						// forward or backward to certain seconds
						CloudAdapter.mPlay.seekTo(currentPosition);

						// update timer progress again
						updateProgressBar();
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						mHandler.removeCallbacks(mUpdateTimeTask);
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub

					}
				});
		refreshList();

		Log.d("Hello", "SoundCloudFragment onCrateView()");
		return rootView;
	}

	public static void buttonChanged(Button changed) {
		changeButton = changed;
	}

	public static void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}

	public static Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long totalDuration = CloudAdapter.mPlay.getDuration();
			long currentDuration = CloudAdapter.mPlay.getCurrentPosition();

			// Displaying Total Duration time
			// songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
			// Displaying time completed playing
			// songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));

			// Updating progress bar
			int progress = Utility.getProgressPercentage(currentDuration,
					totalDuration);
			// Log.d("Progress", ""+progress);
			songProgressBar.setProgress(progress);

			// Running this thread after 100 milliseconds
			mHandler.postDelayed(this, 100);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
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
					if (Utility.ReadSoundCloud(getActivity(), "soundCloud") == null) {
						isAvailability = true;
						try {
							Utility.WriteSettings(getActivity(),
									ServerApi.getSoundCloudMp3Stream(),
									"soundCloud");
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				mp3List = Utility.ReadSoundCloud(getActivity(), "soundCloud");
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				Log.v("tag", "videos" + mp3List);
				progressDialog.hide();

				// --------Stephen uncomment later
				if (!isAvailability) {
					Toast.makeText(getActivity(), R.string.check_connectivity,
							Toast.LENGTH_SHORT).show();
				}
				if (mp3List != null) {
					mImageFetcher.loadImage(mp3List.get(0).image, cover);
					CloudAdapter adapter = new CloudAdapter(getActivity(),
							mp3List);
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
				try {
					Utility.WriteSettings(getActivity(),
							ServerApi.getSoundCloudMp3Stream(), "soundCloud");
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			mp3List = Utility.ReadSoundCloud(getActivity(), "soundCloud");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.hide();
			mRefreshedView.onRefreshComplete();
			CloudAdapter adapter = new CloudAdapter(getActivity(), mp3List);
			ptrlv.setAdapter(adapter);
			super.onPostExecute(result);
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}

}
