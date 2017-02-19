package com.Glorious.adapter;
import java.util.ArrayList;
import com.hakkum.eby.R;
import com.hakkum.ebygarage.customclasses.Twitter_Item;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TwitterListAdapter extends ArrayAdapter<Twitter_Item> {

	Context context;
	public ArrayList<Twitter_Item> twitterFeeds;
	public int currentSong;
	public Uri songUri;
	DisplayImageOptions options;
	ImageLoader imageLoader;

	public TwitterListAdapter(Context context, int resourceId,ArrayList<Twitter_Item> twitterFeeds) {
		// TODO Auto-generated constructor stub
		super(context, resourceId, twitterFeeds);
		this.context = context;
		this.twitterFeeds = twitterFeeds;
		
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.logo).displayer(new RoundedBitmapDisplayer(50)).showImageForEmptyUri(R.drawable.logo).showImageOnFail(R.drawable.logo).cacheInMemory(true).cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	/*private view holder class*/
	private class ViewHolder {
		ImageView image;
		TextView title;
		//TextView date;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Twitter_Item rowItem = (Twitter_Item) twitterFeeds.get(position);
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.twitter_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.image = (ImageView)convertView.findViewById(R.id.ivImage);
		   // holder.date = (TextView) convertView.findViewById(R.id.tvDate);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		    
		holder.title.setText(rowItem.title);
		//holder.date.setText(rowItem.publishDate);
		imageLoader.displayImage(rowItem.image,holder.image, options);
		
		if (position % 2 == 0)
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.list_grey_dark));
		else
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.list_grey_light));
		
		return convertView;
	}

}
