package com.ebyzgarage.glorious;

import com.hakkum.eby.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends Activity {

	private static long SLEEP_TIME = 3; // Sleep for some time (seconds)

	ImageView logo;
	TextView poweredBy;
	Animation fadeInAnimation;
	public static String URL_MAIN_STORY;
	public static String URL_MOVIE_REVIEWS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes title bar
		setContentView(R.layout.splash_screen);
		logo = (ImageView) findViewById(R.id.logo);
		poweredBy = (TextView) findViewById(R.id.poweredBy);
		fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		// Start timer and launch main activity
		IntentLauncher launcher = new IntentLauncher();
		launcher.execute();

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// set animation
						poweredBy.setVisibility(View.VISIBLE);
						logo.setVisibility(View.VISIBLE);

						poweredBy.startAnimation(fadeInAnimation);
						logo.startAnimation(fadeInAnimation);

					}
				});
			}
		}).start();

	}

	private class IntentLauncher extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			// Imp: savedList (singleTon) initialization
			try {
				Thread.sleep(SLEEP_TIME * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Start main activity
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			SplashActivity.this.startActivity(intent);
			SplashActivity.this.finish();
		}

	}
}
