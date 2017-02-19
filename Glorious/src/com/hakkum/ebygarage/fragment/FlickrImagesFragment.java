package com.hakkum.ebygarage.fragment;

import java.net.URISyntaxException;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Glorious.adapter.CustomListAdapter;
import com.actionbarsherlock.app.SherlockFragment;
import com.hakkum.eby.R;
import com.hakkum.ebygarage.customclasses.Flickr;
import com.hakkum.ebygarage.customclasses.NonSwipeableViewPager;
import com.hakkum.ebygarage.customclasses.ServerApi;
import com.hakkum.ebygarage.customclasses.Utility;
import com.hakkum.horizontalScrollView.CenterLockHorizontalScrollview;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class FlickrImagesFragment extends SherlockFragment {

	public ArrayList<Flickr> flickrArrayList = new ArrayList<Flickr>();

	private ProgressDialog progressDialog;
	static CenterLockHorizontalScrollview centerLockHorizontalScrollview;
	CustomListAdapter customListAdapter;
	MyPagerAdapter adapter;
	public static NonSwipeableViewPager pager;
	public CenterLockHorizontalScrollview scrollThumb;
	public boolean _bool;
	public Animation fadeInAnimation, fadeOutAnimation;
	public TextView txtView;
	public boolean isAvailability;

	DisplayImageOptions options;
	ImageLoader imageLoader;

	@SuppressLint("CutPasteId")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.flicker_layout, container,
				false);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.logo)
				.showImageForEmptyUri(R.drawable.logo)
				.showImageOnFail(R.drawable.logo).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();

		pager = (NonSwipeableViewPager) rootView.findViewById(R.id.pager);
		txtView = (TextView) rootView.findViewById(R.id.textView1);
		scrollThumb = (CenterLockHorizontalScrollview) rootView
				.findViewById(R.id.scrollView);
		fadeInAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.fade_in);
		fadeOutAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.fade_out);

		pager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					Log.v("touch", "touch");
					// if (!_bool) {
					// scrollThumb.setVisibility(View.VISIBLE);
					// scrollThumb.startAnimation(fadeInAnimation);
					// _bool = true;
					// } else {
					// scrollThumb.setVisibility(View.INVISIBLE);
					// scrollThumb.startAnimation(fadeOutAnimation);
					// _bool = false;
					// }
					break;
				case MotionEvent.ACTION_MOVE:
					Log.v("Touch", "swipe");
					// scrollThumb.startAnimation(fadeInAnimation);
					// scrollThumb.setVisibility(View.VISIBLE);
					break;
				}
				return false;
			}
		});
		centerLockHorizontalScrollview = (CenterLockHorizontalScrollview) rootView
				.findViewById(R.id.scrollView);
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
					if (Utility.ReadGuitarVideos(getActivity(), "Flickr") == null) {
						try {
							Utility.WriteSettings(getActivity(),
									ServerApi.getFlickr(), "Flickr");
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}
				flickrArrayList = Utility.ReadFlickrImages(getActivity(),
						"Flickr");
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
				if (flickrArrayList != null) {
					customListAdapter = new CustomListAdapter(getActivity(), 0,
							flickrArrayList);
					centerLockHorizontalScrollview.setAdapter(getActivity(),
							customListAdapter);
					centerLockHorizontalScrollview.setCenter(0);
					adapter = new MyPagerAdapter();
					pager.setAdapter(adapter);
					pager.setOnPageChangeListener(adapter);
					pager.setCurrentItem(0);
				}
			}
		}.execute();

	}

	public static void clickedThumbItem(int pos) {
		pager.setCurrentItem(pos);

	}

	class MyPagerAdapter extends PagerAdapter implements OnPageChangeListener {

		public int getCount() {
			return flickrArrayList.size();
		}

		@Override
		public Object instantiateItem(View collection, int position) {
			final ImageView imgView = new ImageView(getActivity());
			imageLoader.displayImage(flickrArrayList.get(position).image,
					imgView, options, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							// imgView.startAnimation(fadeInAnimation);
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub

						}
					});
			((ViewPager) collection).addView(imgView, 0);
			return imgView;
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);

		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);

		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			centerLockHorizontalScrollview.setCenter(pager.getCurrentItem());
			txtView.setText(flickrArrayList.get(pager.getCurrentItem()).title
					.toUpperCase());
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub

		}

	}

}
