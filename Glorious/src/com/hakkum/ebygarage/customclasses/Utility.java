package com.hakkum.ebygarage.customclasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.inputmethod.InputMethodManager;

public class Utility {

	public static final String IMAGE_CACHE_DIR = "MuzikGarage";
	public static final String YOUTUBE_DEVELOPER_KEY = "AIzaSyBzhy8VYSme9SOAOizbPZ0JE0In9PY4d00";
	public static final String SOUNDCLOUD_URL = "http://api.soundcloud.com/users/9232467/tracks.json?client_id=a0511f550ed4110af9098ccba7d139c0";
	public static final String YOUTUBE_URL = "http://gdata.youtube.com/feeds/api/users/QYWPN_nFYL0-V5oLtTv-pw/uploads";
	public static String URL_FACEBOOK = "https://www.facebook.com/groups/183310478390396/?ref=br_tf";
	public static String URL_TWITTER = "https://twitter.com/ibanezebenezer";
	public static String URL_FLICKR = "http://api.flickr.com/services/feeds/photos_public.gne?id=126317758@N03&lang=en-en&format=json&nojsoncallback=1";

	public static boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager connectivityManager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}

	public static String getSearchURL(String searchURL) {
		return "http://gdata.youtube.com/feeds/api/users/D56FaddGnjBakrkErv2lVQ/uploads?q="
				+ searchURL + "&v=2";
	}

	// Write settings
	public synchronized static void WriteSettings(Context ctx,
			ArrayList<?> data, String fileName) {

		FileOutputStream fOut = null;
		ObjectOutputStream osw = null;

		String filename = ctx.getFilesDir().toString() + "/" + fileName
				+ ".boi";

		try {
			fOut = new FileOutputStream(new File(filename));
			osw = new ObjectOutputStream(fOut);

			osw.writeObject(data);

			osw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized static ArrayList<Twitter_Item> ReadTwitterFeeds(
			Context ctx, String fileName) {

		FileInputStream fIn = null;
		ObjectInputStream isr = null;

		ArrayList<Twitter_Item> _list = null;
		String filename = ctx.getFilesDir().toString() + "/" + fileName
				+ ".boi";
		File f = new File(filename);
		if (!f.exists())
			return null;
		try {
			fIn = new FileInputStream(filename);
			isr = new ObjectInputStream(fIn);

			_list = (ArrayList<Twitter_Item>) isr.readObject();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return _list;
	}

	public synchronized static ArrayList<SoundCloud> ReadSoundCloud(
			Context ctx, String fileName) {

		FileInputStream fIn = null;
		ObjectInputStream isr = null;

		ArrayList<SoundCloud> _list = null;
		String filename = ctx.getFilesDir().toString() + "/" + fileName
				+ ".boi";
		File f = new File(filename);
		if (!f.exists())
			return null;
		try {
			fIn = new FileInputStream(filename);
			isr = new ObjectInputStream(fIn);

			_list = (ArrayList<SoundCloud>) isr.readObject();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return _list;
	}

	// Read settings
	@SuppressWarnings("unchecked")
	public synchronized static ArrayList<Guitar_Video> ReadGuitarVideos(
			Context ctx, String fileName) {

		FileInputStream fIn = null;
		ObjectInputStream isr = null;

		ArrayList<Guitar_Video> _list = null;
		String filename = ctx.getFilesDir().toString() + "/" + fileName
				+ ".boi";
		File f = new File(filename);
		if (!f.exists())
			return null;
		try {
			fIn = new FileInputStream(filename);
			isr = new ObjectInputStream(fIn);

			_list = (ArrayList<Guitar_Video>) isr.readObject();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return _list;
	}

	// Read settings
		@SuppressWarnings("unchecked")
		public synchronized static ArrayList<Flickr> ReadFlickrImages(
				Context ctx, String fileName) {

			FileInputStream fIn = null;
			ObjectInputStream isr = null;

			ArrayList<Flickr> _list = null;
			String filename = ctx.getFilesDir().toString() + "/" + fileName
					+ ".boi";
			File f = new File(filename);
			if (!f.exists())
				return null;
			try {
				fIn = new FileInputStream(filename);
				isr = new ObjectInputStream(fIn);

				_list = (ArrayList<Flickr>) isr.readObject();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return _list;
		}
	public static String getFName(String url) {
		return url.replace("http://www.boxofficeindia.co.in/category/", "")
				.replace("/feed", "").replace("/", "_");
	}

	public String milliSecondsToTimer(long milliseconds) {
		String finalTimerString = "";
		String secondsString = "";

		// Convert total duration into time
		int hours = (int) (milliseconds / (1000 * 60 * 60));
		int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
		int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
		// Add hours if there
		if (hours > 0) {
			finalTimerString = hours + ":";
		}

		// Prepending 0 to seconds if it is one digit
		if (seconds < 10) {
			secondsString = "0" + seconds;
		} else {
			secondsString = "" + seconds;
		}

		finalTimerString = finalTimerString + minutes + ":" + secondsString;

		// return timer string
		return finalTimerString;
	}

	public static int progressToTimer(int progress, int totalDuration) {
		int currentDuration = 0;
		totalDuration = (int) (totalDuration / 1000);
		currentDuration = (int) ((((double) progress) / 100) * totalDuration);

		// return current duration in milliseconds
		return currentDuration * 1000;
	}

	/**
	 * Function to get Progress percentage
	 * 
	 * @param currentDuration
	 * @param totalDuration
	 * */
	public static int getProgressPercentage(long currentDuration,
			long totalDuration) {
		Double percentage = (double) 0;

		long currentSeconds = (int) (currentDuration / 1000);
		long totalSeconds = (int) (totalDuration / 1000);

		// calculating percentage
		percentage = (((double) currentSeconds) / totalSeconds) * 100;

		// return percentage
		return percentage.intValue();
	}

	public static Bitmap getBitmapWithReflection(Bitmap originalImage) {

		// The gap we want between the reflection and the original image
		final int reflectionGap = 4;

		// Get your bit map
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		// This will not scale but will flip on the Y axis
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		// Create a Bitmap with the flip matix applied to it.
		// We only want the bottom half of the image
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 3, width, height / 3, matrix, false);

		// Create a new bitmap with same width but taller to fit reflection
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 3), Config.ARGB_8888);

		// Create a new Canvas with the bitmap that's big enough for
		// the image plus gap plus reflection
		Canvas canvas = new Canvas(bitmapWithReflection);
		// Draw in the original image
		canvas.drawBitmap(originalImage, 0, 0, null);
		// Draw in the gap
		Paint deafaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
		// Draw in the reflection
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		// Create a shader that is a linear gradient that covers the reflection
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0,
				originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
						+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		// Set the paint to use this shader (linear gradient)
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		// refturn bitmap with reflection
		return bitmapWithReflection;
	}
	
}
