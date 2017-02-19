package com.Glorious.adapter;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hakkum.eby.R;
import com.hakkum.ebygarage.customclasses.SoundCloud;
import com.hakkum.ebygarage.fragment.SoundCloudFragment;


public class CloudAdapter extends BaseAdapter {

	Context context;
	public ArrayList<SoundCloud> mp3Array;
	public int currentSong;
	public static MediaPlayer mPlay;
	public Uri songUri;
	public RelativeLayout progressLayout;
	public ProgressDialog progressDialog;

	public CloudAdapter(Context context, ArrayList<SoundCloud> mp3List) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mp3Array = mp3List;
		progressDialog = ProgressDialog.show(context, null, "Buffering Song..");
		progressDialog.dismiss();
		mPlay = new MediaPlayer();
		currentSong = -1;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mp3Array.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* private view holder class */
	private class ViewHolder {
		TextView trackNumber;
		TextView title;
		TextView desc;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final SoundCloud rowItem = mp3Array.get(position);
		final Button play;
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.cell_layout, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.songTitle);
			holder.desc = (TextView) convertView.findViewById(R.id.songDesc);
			holder.trackNumber = (TextView) convertView
					.findViewById(R.id.trackNumber);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		play = (Button) convertView.findViewById(R.id.play);
		holder.title.setText(rowItem.title);
		holder.desc.setText(rowItem.description);
		holder.trackNumber.setText(Integer.toString(position + 1));
		convertView.setTag(position + "");
		if (position == currentSong)
			play.setBackgroundResource(R.drawable.pause_btn_selector);
		else
			play.setBackgroundResource(R.drawable.play_btn_selector);

		if (position % 2 == 0)
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.list_grey_dark));
		else
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.list_grey_light));
		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				View prevView = SoundCloudFragment.ptrlv
						.findViewWithTag(currentSong + "");

				if (prevView != null) {
					prevView.findViewById(R.id.play).setBackgroundResource(
							R.drawable.play_btn_selector);
					SoundCloudFragment.btnMusicPlay
							.setBackgroundResource(R.drawable.play_btn_selector);
				}
				if (position == currentSong) {
					if (mPlay.isPlaying()) {
						mPlay.pause();
						play.setBackgroundResource(R.drawable.play_btn_selector);
						SoundCloudFragment.btnMusicPlay
								.setBackgroundResource(R.drawable.play_btn_selector);
						SoundCloudFragment.mHandler
								.removeCallbacks(SoundCloudFragment.mUpdateTimeTask);
					} else {
						mPlay.start();
						play.setBackgroundResource(R.drawable.pause_btn_selector);
						SoundCloudFragment.btnMusicPlay
								.setBackgroundResource(R.drawable.pause_btn_selector);
					}
				} else {
					songUri = Uri.parse(rowItem.mp3Url);

					new AsyncTask<Void, Void, Void>() {
						protected void onPreExecute() {
							// do pre execute stuff...
							progressDialog.show();
						}

						protected Void doInBackground(Void... params) {
							// do background stuff...
							try {
								mPlay.reset();
								Log.d("SONG URI", songUri.toString());
								mPlay.setDataSource(songUri.toString());
								mPlay.prepare();
							} catch (IllegalStateException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							return null;
						}

						protected void onPostExecute(Void result) {
							// do post execute stuff...
							mPlay.start();
							SoundCloudFragment.songProgressBar.setProgress(0);
							SoundCloudFragment.songProgressBar.setMax(100);
							SoundCloudFragment.updateProgressBar();
							progressDialog.dismiss();
						}
					}.execute();

					mPlay.start();
					SoundCloudFragment.buttonChanged(play);
					SoundCloudFragment.mHandler
							.removeCallbacks(SoundCloudFragment.mUpdateTimeTask);
					play.setBackgroundResource(R.drawable.pause_btn_selector);
					SoundCloudFragment.btnMusicPlay
							.setBackgroundResource(R.drawable.pause_btn_selector);
					currentSong = position;
				}
			}
		});
		return convertView;
	}

}
