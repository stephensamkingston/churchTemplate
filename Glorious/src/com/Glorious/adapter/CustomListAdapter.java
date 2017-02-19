package com.Glorious.adapter;

import java.util.ArrayList;
import com.hakkum.eby.R;
import com.hakkum.ebygarage.customclasses.Flickr;
import com.hakkum.ebygarage.fragment.FlickrImagesFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CustomListAdapter extends ArrayAdapter<Flickr> {
	Context context;
	private ArrayList<Flickr> list;
	int layoutId;
	Holder holder;
	public View view;
	public int currPosition = 0;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	public Animation fadeInAnimation;
	public CustomListAdapter(Context context, int textViewResourceId,
			ArrayList<Flickr> list) {
		super(context, textViewResourceId, list);
		this.context = context;
		this.list = list;
		layoutId = textViewResourceId;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.logo)
				.showImageForEmptyUri(R.drawable.logo)
				.showImageOnFail(R.drawable.logo).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		fadeInAnimation = AnimationUtils.loadAnimation(context,
				R.anim.fade_in);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RelativeLayout layout;
		Flickr rowItem = (Flickr) list.get(position);
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.flickr_item, null);
			holder = new Holder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.imgView);
			convertView.setTag(holder);

		} else {
			layout = (RelativeLayout) convertView;
			view = layout;
			holder = (Holder) layout.getTag();
		}

		imageLoader.displayImage(rowItem.thumbImages, holder.imageView,
				options, new ImageLoadingListener() {

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
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub
						//holder.imageView.startAnimation(fadeInAnimation);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub

					}
				});
		holder.imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FlickrImagesFragment.clickedThumbItem(position);
			}
		});
		return convertView;
	}

	private class Holder {
		public ImageView imageView;

	}

	public int getCurrentPosition() {
		return currPosition;
	}
}
