package com.ebyzgarage.glorious;
import android.os.Bundle;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.hakkum.eby.R;
import com.hakkum.ebygarage.customclasses.Utility;

public class YoutubeVideo extends YouTubeBaseActivity implements
		YouTubePlayer.OnInitializedListener {
	public static String viewSource;
	String videoId, title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_view);

		videoId = getIntent().getStringExtra("VideoId");
		title = getIntent().getStringExtra("Title");

		YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubeplayerview);
		youTubePlayerView.initialize(Utility.YOUTUBE_DEVELOPER_KEY, this);

	}

	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer player,
			boolean wasRestored) {
		// TODO Auto-generated method stub
		if (!wasRestored) {
			player.cueVideo(videoId);
		}

	}

//	@Override
//	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
//		switch (item.getItemId()) {
//
//		case android.R.id.home:
//			// app icon in action bar clicked; finish activity to go home
//			finish();
//			return true;
//
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
//	// To handle "Back" key press event for WebView to go back to previous screen.
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if ((keyCode == KeyEvent.KEYCODE_BACK) && wvSocial.canGoBack()) {
//			wvSocial.goBack();
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
}
