package com.hakkum.social;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class Social {

	private static final String[] PERMISSIONS = new String[] { "publish_stream" };

	private static final String TOKEN = "access_token";
	private static final String EXPIRES = "expires_in";
	private static final String KEY = "facebook-credentials";

	static Facebook facebook;
	static String toastMessage = "";

	static Runnable updateRunnable;
	static String fbResponse = "";
	static ProgressDialog progressDialog = null;
	static Boolean isAuthenticated;
	private static final Handler myHandler = new Handler();

	public Social(Context ctx, String fbId) {
		facebook = new Facebook(fbId);
		restoreCredentials(ctx, facebook);
	}

	public Social() {

	}

	public boolean saveCredentials(Context ctx, Facebook facebook) {
		Editor editor = ctx.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putString(TOKEN, facebook.getAccessToken());
		editor.putLong(EXPIRES, facebook.getAccessExpires());
		return editor.commit();
	}

	public boolean restoreCredentials(Context ctx, Facebook facebook) {
		SharedPreferences sharedPreferences = ctx.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
		facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
		return facebook.isSessionValid();
	}

	public static void postFeedToTwitter(Context ctx, String messageToPost) {
		new Twitter_Dialog(ctx, "http://twitter.com/?status="
				+ Uri.encode(messageToPost)).show();
	}

	public void shareImageToFb(Context ctx, String messageToPost, int resImageId) {
		if (!facebook.isSessionValid()) {
			loginAndPostToWall(ctx, messageToPost, resImageId);
		} else {
			postToWall(ctx, messageToPost, resImageId);
		}
	}

	public void postFeedToFb(Context ctx, String messageToPost) {
		if (!facebook.isSessionValid()) {
			loginAndPostToWall(ctx, messageToPost, 0);
		} else {
			postToWall(ctx, messageToPost, 0);
		}

	}

	public void loginAndPostToWall(Context ctx, String message, int resImageId) {
		facebook.authorize((Activity) ctx, PERMISSIONS,
				Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener(ctx,
						message, resImageId));
	}

	public void postToWall(final Context ctx, final String message,
			final int resImageId) {

		progressDialog = ProgressDialog.show(ctx, "",
				"Posting on your wall...", true, false);
		new Thread(new Runnable() {

			public void run() {
				Bundle params = new Bundle();
				params.putString(Facebook.TOKEN, facebook.getAccessToken());
				params.putString("message", message);
				String response;
				try {
					facebook.request("me");
					if (resImageId != 0) {
						byte[] data = null;
						Bitmap icon = BitmapFactory.decodeResource(
								ctx.getResources(), resImageId);

						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						icon.compress(Bitmap.CompressFormat.JPEG, 100, baos);
						data = baos.toByteArray();
						params.putByteArray("photo", data);
						response = facebook
								.request("me/photos", params, "POST");
					} else
						response = facebook.request("me/feed", params, "POST");

					Log.d("Tests", "got response: " + response);
					if (response == null || response.equals("")
							|| response.equals("false")) {
						toastMessage = "No response.";
						myHandler.post(updateRunnable);

					} else {
						toastMessage = "posted to your facebook wall!";
						myHandler.post(updateRunnable);
					}
				} catch (Exception e) {
					toastMessage = "Failed to post to wall!";
					myHandler.post(updateRunnable);
					e.printStackTrace();

				}
			}

		}).start();

		updateRunnable = new Runnable() {
			public void run() {
				progressDialog.dismiss();
				showToast(ctx, toastMessage);
			}
		};

	}

	class LoginDialogListener implements DialogListener {
		Context ctx;
		String messageToPost;
		int resImageId;

		public LoginDialogListener(Context ctx, String messageToPost,
				int resImageId) {
			this.ctx = ctx;
			this.messageToPost = messageToPost;
			this.resImageId = resImageId;
		}

		public void onComplete(Bundle values) {
			saveCredentials(ctx, facebook);
			if (messageToPost != null) {
				postToWall(ctx, messageToPost, resImageId);
			}
		}

		public void onFacebookError(FacebookError error) {
			showToast(ctx, "Authentication with Facebook failed!");

		}

		public void onError(DialogError error) {
			showToast(ctx, "Authentication with Facebook failed!");

		}

		public void onCancel() {
			showToast(ctx, "Authentication with Facebook cancelled!");

		}

	}

	private void showToast(Context ctx, String message) {
		Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
	}
}
