package com.hakkum.ebygarage.fragment;

import java.util.ArrayList;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.ebyzgarage.glorious.YoutubeVideo;
import com.hakkum.eby.R;
import com.hakkum.ebygarage.customclasses.Guitar_Video;
import com.hakkum.ebygarage.customclasses.IShowedFragment;
import com.hakkum.ebygarage.customclasses.ServerApi;
import com.hakkum.ebygarage.customclasses.Utility;
import com.hakkum.social.Social;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GuitarVideosFragment extends SherlockFragment implements
		IShowedFragment, OnRefreshListener<GridView> {

	private ArrayList<Guitar_Video> videoArrayList = new ArrayList<Guitar_Video>();
	private ProgressDialog progressDialog;
	public static PullToRefreshGridView gridView;
	private ImageAdapter adapter;
	private int mImageThumbSize;
	private int mImageThumbSpacing;
	private int height;
	private int width;
	public boolean isAvailability;
	ImageLoader imageLoader;
	DisplayImageOptions options;

	public static GuitarVideosFragment newInstance() {
		GuitarVideosFragment guitarVideos = new GuitarVideosFragment();
		return guitarVideos;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.main, menu);
		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getActivity()
				.getSystemService(Context.SEARCH_SERVICE);
		final SearchView searchView = (SearchView) menu.findItem(
				R.id.action_settings).getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getActivity().getComponentName()));
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {

				return true;
			}

			@SuppressWarnings("null")
			@Override
			public boolean onQueryTextSubmit(String query) {
				// Do something
				searchView.clearFocus();
				if (query != null)
					searchGuitarVideos(query);
				else if (query.equals("") || query == null)
					refreshList();
				return true;
			}
		});
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		height = displaymetrics.heightPixels;
		width = displaymetrics.widthPixels;

		View rootView = inflater.inflate(R.layout.guitar_video, container,
				false);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.logo)
				.showImageForEmptyUri(R.drawable.logo)
				.showImageOnFail(R.drawable.logo).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		gridView = (PullToRefreshGridView) rootView
				.findViewById(R.id.photoGrid);
		gridView.setOnRefreshListener(this);
		refreshList();
		if (savedInstanceState != null) {
			Log.d("Hello", "GuitarVideosFragment onCrateView()");
		}

		return rootView;
	}

	private Object getWindowManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onShowedFragment() {
		// TODO Auto-generated method stub

	}

	public void navigate(int pos) {
		Intent detailIntent = new Intent(getActivity(), YoutubeVideo.class);
		YoutubeVideo.viewSource = videoArrayList.get(pos).videoUrl.toString();
		detailIntent.putExtra("VideoId",
				videoArrayList.get(pos).videoUrl.toString());
		detailIntent.putExtra("Title",
				videoArrayList.get(pos).songName.toString());
		startActivity(detailIntent);
	}

	public void dialogAlert(final String picture, final int pos,
			final String message) {
		AlertDialog.Builder builder;
		AlertDialog alertDialog;
		Log.v("post", "post" + message);
		View layout = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_view, null);
		ImageView imageBack = (ImageView) layout.findViewById(R.id.imageView1);
		ImageView imageFront = (ImageView) layout.findViewById(R.id.imageView2);
		imageFront.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				navigate(pos);

			}
		});

		imageLoader.displayImage(picture, imageBack, options);

		imageBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				navigate(pos);
			}
		});

		ImageButton facebook = (ImageButton) layout.findViewById(R.id.facebook);

		facebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Social social = new Social(getActivity(), getActivity()
						.getString(R.string.fb_id));
				social.postFeedToFb(getActivity(), message);
			}
		});

		ImageButton twitter = (ImageButton) layout.findViewById(R.id.twitter);

		twitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Social.postFeedToTwitter(getActivity(), message);
			}
		});

		builder = new AlertDialog.Builder(getActivity());
		builder.setView(layout);
		alertDialog = builder.create();
		alertDialog.setInverseBackgroundForced(true);
		alertDialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		alertDialog.show();
	}

	public void searchGuitarVideos(final String searchQuery) {
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

				videoArrayList = ServerApi.getAllProducts(Utility
						.getSearchURL(searchQuery));

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				Log.v("search", "search" + videoArrayList.size());
				progressDialog.hide();
				if (videoArrayList.size() == 0)
					refreshList();
				else
					onSetAdapter();
			}
		}.execute();

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

				if (!Utility.isNetworkAvailable(getActivity()))
					isAvailability = false;
				else {
					if (Utility.ReadGuitarVideos(getActivity(), "guitarVideos") == null) {
						Utility.WriteSettings(getActivity(),
								ServerApi.getAllProducts(Utility.YOUTUBE_URL),
								"guitarVideos");
						isAvailability = true;
					}
				}
				videoArrayList = Utility.ReadGuitarVideos(getActivity(),
						"guitarVideos");
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
				if (videoArrayList != null)
					onSetAdapter();
			}
		}.execute();

	}

	public void onSetAdapter() {

		mImageThumbSize = getActivity().getResources().getDimensionPixelSize(
				R.dimen.photo_thumbnail_size);
		mImageThumbSpacing = getActivity().getResources()
				.getDimensionPixelSize(R.dimen.photo_thumbnail_spacing);
		adapter = new ImageAdapter(videoArrayList, getActivity());
		gridView.setAdapter(adapter);
		// gridView.setFastScrollEnabled(true);

		gridView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						// TODO Auto-generated method stub
						if (width < 600) {
							if (adapter.getNumColumns() == 0) {
								final int numColumns = (int) Math
										.floor(gridView.getWidth()
												/ (mImageThumbSize + mImageThumbSpacing));
								if (numColumns > 0) {
									final int columnWidth = (gridView
											.getWidth() / numColumns)
											- mImageThumbSpacing;
									adapter.setNumColumns(numColumns);
									adapter.setItemHeight(columnWidth);
								}
							}
						} else {
							adapter.setItemHeight(150);
						}
					}
				});

		// albumGrid on item click:
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				// On click
				// Display the zoomed in image in full screen
				Log.v("Hello", "You are a jenius");
				dialogAlert(videoArrayList.get(pos).imageName.toString(), pos,
						"Check this video url:"
								+ videoArrayList.get(pos).postURl);
			}
		});
	}

	@Override
	public void onRefresh(PullToRefreshBase<GridView> refreshView) {
		// TODO Auto-generated method stub
		new GetDataTask(gridView).execute();
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
				Utility.WriteSettings(getActivity(),
						ServerApi.getAllProducts(Utility.YOUTUBE_URL),
						"guitarVideos");
			}
			videoArrayList = Utility.ReadGuitarVideos(getActivity(),
					"guitarVideos");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.hide();
			mRefreshedView.onRefreshComplete();
			onSetAdapter();
			super.onPostExecute(result);
		}
	}

}

// //////////////////////////////////////////////////////////

// ///////////////// ADAPTER ////////////////////////////////
class ImageAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private int mItemHeight = 0;
	private int mNumColumns = 0;
	private ArrayList<Guitar_Video> _feed;
	private RelativeLayout.LayoutParams mImageViewLayoutParams;
	ImageLoader imageLoader;
	DisplayImageOptions options;

	public ImageAdapter(ArrayList<Guitar_Video> videoArrayList, Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageViewLayoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.logo)
				.showImageForEmptyUri(R.drawable.logo)
				.showImageOnFail(R.drawable.logo).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		_feed = videoArrayList;
	}

	public int getCount() {
		return _feed.size();
	}

	public void setNumColumns(int numColumns) {
		mNumColumns = numColumns;
	}

	public int getNumColumns() {
		return mNumColumns;
	}

	public void setItemHeight(int height) {
		if (height == mItemHeight) {
			return;
		}
		mItemHeight = height;
		mImageViewLayoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, mItemHeight);
		notifyDataSetChanged();
	}

	public Guitar_Video getItem(int position) {
		return _feed.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {

		ViewHolder holder;

		if (view == null) {
			holder = new ViewHolder();
			view = mInflater.inflate(R.layout.grid_item, null);
			holder.cover = (ImageView) view.findViewById(R.id.cover);
			holder.title = (TextView) view.findViewById(R.id.title);
			view.setTag(holder);
		} else
			holder = (ViewHolder) view.getTag();

		holder.cover.setLayoutParams(mImageViewLayoutParams);

		// Check the height matches our calculated column width
		if (holder.cover.getLayoutParams().height != mItemHeight) {
			holder.cover.setLayoutParams(mImageViewLayoutParams);
		}

		Guitar_Video video = getItem(position);
		imageLoader.displayImage(video.imageName, holder.cover, options);
		holder.title.setText(video.songName);

		return view;
	}
}

class ViewHolder {
	ImageView cover;
	TextView title;
}
