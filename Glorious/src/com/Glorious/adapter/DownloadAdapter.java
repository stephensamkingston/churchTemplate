package com.Glorious.adapter;

import java.io.IOException;
import java.util.ArrayList;

import com.hakkum.eby.R;
import com.hakkum.ebygarage.customclasses.SoundCloudDownload;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class DownloadAdapter extends BaseAdapter {
	Context context;
	public ArrayList<SoundCloudDownload> mp3Array;
	public int currentSong;
	public MediaPlayer mPlay;
	public Uri songUri;
	public Button play;
	public LayoutInflater mInflater;
	public ViewHolder holder;
	public int position;
	SoundCloudDownload rowItem;

	public DownloadAdapter(Context context, ArrayList<SoundCloudDownload> mp3List) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mp3Array = mp3List;
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
		holder = null;
		this.position = position;
		rowItem = mp3Array.get(position);

		mInflater = (LayoutInflater) context
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

		showView(convertView);
		return convertView;
	}

	void showView(View convertView) {
		play = (Button) convertView.findViewById(R.id.play);
		holder.title.setText(rowItem.title);
		holder.desc.setText(rowItem.description);
		holder.trackNumber.setText(Integer.toString(position + 1));

		if (position == currentSong)
			play.setBackgroundResource(R.drawable.pause_btn_selector);
		else
			play.setBackgroundResource(R.drawable.play_btn_selector);
		// convertView.setTag(position + "");

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

				if (position == currentSong) {
					if (mPlay.isPlaying()) {
						mPlay.pause();
						play.setBackgroundResource(R.drawable.play_btn_selector);
						currentSong = -2;
						// stop the timer thread

					} else {
						mPlay.start();
						play.setBackgroundResource(R.drawable.pause_btn_selector);
						// check for the play time if it is more or equal to 30
						// sec pause it and show alert

					}
				} else {
					mPlay.reset();
					play.setBackgroundResource(R.drawable.pause_btn_selector);
					try {
						mPlay.setDataSource(rowItem.mp3Url);
						mPlay.setOnPreparedListener(new OnPreparedListener() {

							@Override
							public void onPrepared(MediaPlayer mp) {
								// TODO Auto-generated method stub
								mp.start();
							}
						});
						mPlay.prepareAsync();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					currentSong = position;

				}

			}
		});
	}

}
