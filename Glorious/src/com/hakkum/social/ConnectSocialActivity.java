package com.hakkum.social;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ConnectSocialActivity extends SherlockFragmentActivity {

	Button later, btnFb, btnTwt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		setContentView(R.layout.social_connect);
//
//		later = (Button) findViewById(R.id.btnLater);
//
//		btnFb = (Button) findViewById(R.id.fbButton);
//		btnTwt = (Button) findViewById(R.id.twtButton);
//
//		btnFb.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				if (!Social.facebook.isSessionValid()) {
//					Social social = new Social(ConnectSocialActivity.this, ConnectSocialActivity.this
//							.getString(R.string.fb_id));
//					social.loginToFacebook();
//				} else {
//					Toast.makeText(ConnectSocialActivity.this, "Connected to Facebook!", Toast.LENGTH_SHORT).show();
//				}
//
//			}
//		});
//
//		btnTwt.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				Social.postFeedToTwitter(ConnectSocialActivity.this, "");
//
//			}
//		});
//
//		later.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(ConnectSocialActivity.this, MainActivity.class);
//				startActivity(intent);
//				finish();
//
//			}
//		});
//
	}

}