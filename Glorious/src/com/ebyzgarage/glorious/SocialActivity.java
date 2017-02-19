package com.ebyzgarage.glorious;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockActivity;
import com.hakkum.eby.R;

public class SocialActivity extends SherlockActivity {

	WebView wvSocial;
	String URL;
	ProgressDialog progress;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		URL = getIntent().getStringExtra("URL");

		if (URL.contains("facebook"))
			getSupportActionBar().setTitle("Facebook");
		else
			getSupportActionBar().setTitle("Twitter");

		// init
		wvSocial = (WebView) findViewById(R.id.wvSocial);
		progress = new ProgressDialog(this);
		progress.setMessage("Loading please wait...");
		progress.setCancelable(true);

		progress.show();

		class Callback extends WebViewClient {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url);

				if (!progress.isShowing())
					progress.show();

				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				if (progress.isShowing())
					progress.dismiss();

			}

		}

		wvSocial.getSettings()
				.setUserAgentString(
						"Mozilla/5.0 (Linux; U; Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
		wvSocial.setWebChromeClient(new WebChromeClient());
		wvSocial.setWebViewClient(new Callback());
		wvSocial.loadUrl(URL);

	}

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			// app icon in action bar clicked; finish activity to go home
			finish();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	// To handle "Back" key press event for WebView to go back to previous screen.
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && wvSocial.canGoBack()) {
			wvSocial.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
