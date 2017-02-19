package com.ebyzgarage.glorious;

import com.Glorious.adapter.CloudAdapter;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.hakkum.eby.R;
import com.hakkum.ebygarage.customclasses.Utility;
import com.hakkum.ebygarage.fragment.AboutUsFragment;
import com.hakkum.ebygarage.fragment.FlickrImagesFragment;
import com.hakkum.ebygarage.fragment.GuitarVideosFragment;
import com.hakkum.ebygarage.fragment.MapViewFragment;
import com.hakkum.ebygarage.fragment.SoundCloudFragment;
import com.hakkum.ebygarage.fragment.TwitterFragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends SherlockFragmentActivity implements
		OnClickListener {

	private DrawerLayout mDrawerLayout;
	private RelativeLayout mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String fragmentTag;
	public String postion;
	public Button changeButton;
	// SLIDING MENU
	RelativeLayout rlVideos;
	RelativeLayout rlSoundCloud;
	RelativeLayout rlTwitterFeeds;
	RelativeLayout rlMapView;
	RelativeLayout rlGallery;
	RelativeLayout rlMore;

	ImageView ivMenuFacebook;
	ImageView ivMenuTwitter;
	ImageView ivMenuShare;
	Bundle savedState;

	public static boolean isUpdateHomeCollections = true;
	FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_main);
		savedState = savedInstanceState;
		fragmentManager = getSupportFragmentManager();
		initSlidingMenu();

		mTitle = mDrawerTitle = "Muzik Garage";
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerList = (RelativeLayout) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		// mDrawerList.setAdapter(new ArrayAdapter<String>(this,
		// R.layout.drawer_list_item, mPlanetTitles));
		// mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu(); // creates call to
												// onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(mDrawerTitle);
				supportInvalidateOptionsMenu(); // creates call to
												// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		if (savedInstanceState == null) {
			supportInvalidateOptionsMenu();
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, new GuitarVideosFragment())
					.commit();
			setTitle("Videos");
			setSelected(rlVideos);
		}
	}

	private void initSlidingMenu() {

		rlVideos = (RelativeLayout) findViewById(R.id.guitarVideos);
		rlSoundCloud = (RelativeLayout) findViewById(R.id.soundCloud);
		rlTwitterFeeds = (RelativeLayout) findViewById(R.id.rlTwitterFeeds);
		rlGallery = (RelativeLayout) findViewById(R.id.rlGallery);
		rlMapView = (RelativeLayout) findViewById(R.id.mapView);
		rlMore = (RelativeLayout) findViewById(R.id.rlMore);
		ivMenuFacebook = (ImageView) findViewById(R.id.ivMenuFacebook);
		ivMenuTwitter = (ImageView) findViewById(R.id.ivMenuTwitter);
		ivMenuShare = (ImageView) findViewById(R.id.ivMenuShare);

		rlVideos.setOnClickListener(this);
		rlSoundCloud.setOnClickListener(this);
		rlTwitterFeeds.setOnClickListener(this);
		rlMapView.setOnClickListener(this);
		rlGallery.setOnClickListener(this);
		rlMore.setOnClickListener(this);

		ivMenuFacebook.setOnClickListener(this);
		ivMenuTwitter.setOnClickListener(this);
		ivMenuShare.setOnClickListener(this);

	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}
		return true;

	}
	
	public void changeButton(Button button)
	{
		changeButton = button;
	}

	@Override
	public void onClick(View v) {

		// update the main content by replacing fragments
		Fragment newContent = null;
		if (v.getId() == R.id.guitarVideos) {
			// HOME
			postion = Integer.toString(v.getId());
			newContent = GuitarVideosFragment.newInstance();
			setTitle("Videos");
			setSelected(rlVideos);
			fragmentTag = "Videos";
		} else if (v.getId() == R.id.soundCloud) {
			// NextAttraction
			postion = Integer.toString(v.getId());
			newContent = SoundCloudFragment.newInstance();
			setTitle("Sound Cloud");
			setSelected(rlSoundCloud);
			fragmentTag = "soundCloud";
		} else if (v.getId() == R.id.rlTwitterFeeds) {
			// FEATURES
			postion = Integer.toString(v.getId());
			newContent = new TwitterFragment();
			setTitle("Tweets");
			setSelected(rlTwitterFeeds);
			fragmentTag = "twitterFeeds";
		} else if (v.getId() == R.id.mapView) {

			// COLUMNS
			postion = Integer.toString(v.getId());
			newContent = new MapViewFragment();
			setTitle("Location");
			setSelected(rlMapView);
			fragmentTag = "mapView";
		} else if (v.getId() == R.id.rlGallery) {
			// COLUMNS
			postion = Integer.toString(v.getId());
			newContent = new FlickrImagesFragment();
			setTitle("Gallery");
			setSelected(rlGallery);
			fragmentTag = "Gallery";

		} else if (v.getId() == R.id.rlMore) {
			// COLUMNS
			postion = Integer.toString(v.getId());
			newContent = new AboutUsFragment();
			setTitle("About Us");
			setSelected(rlMore);
			fragmentTag = "More";

		}
		// SHARE
		else if (v.getId() == R.id.ivMenuFacebook) {
			// FACEBOOK
			Intent fbIntent = new Intent(MainActivity.this,
					SocialActivity.class);
			fbIntent.putExtra("URL", Utility.URL_FACEBOOK);

			startActivity(fbIntent);

		} else if (v.getId() == R.id.ivMenuTwitter) {
			// TWITTER
			Intent twtIntent = new Intent(MainActivity.this,
					SocialActivity.class);
			twtIntent.putExtra("URL", Utility.URL_TWITTER);

			startActivity(twtIntent);

		} else if (v.getId() == R.id.ivMenuShare) {
			// SHARE
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, "Muzik Garage");
			intent.putExtra(Intent.EXTRA_TEXT,
					"I have installed Muzik Garage App: " + "URL");

			startActivity(Intent.createChooser(intent, "Share via"));

		}

		if (CloudAdapter.mPlay != null) {
			if (CloudAdapter.mPlay.isPlaying()) {
				CloudAdapter.mPlay.stop();
				SoundCloudFragment.btnMusicPlay
				.setBackgroundResource(R.drawable.play_btn_selector);
				SoundCloudFragment.changeButton
				.setBackgroundResource(R.drawable.pause_btn_selector);
			}
		}

		if (newContent != null
				&& fragmentManager.findFragmentByTag(fragmentTag) == null) {
			switchFragment(newContent, fragmentTag);
		} else
			mDrawerLayout.closeDrawer(mDrawerList);
	}

	private void setSelected(RelativeLayout rl) {

		rlVideos.setSelected(false);
		rlSoundCloud.setSelected(false);
		rlTwitterFeeds.setSelected(false);
		rlMapView.setSelected(false);
		rlGallery.setSelected(false);
		rlMore.setSelected(false);
		rl.setSelected(true);
	}

	// switching fragment
	private void switchFragment(final Fragment fragment, String tag) {

		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment, tag).commit();
		// fragmentManager.beginTransaction().addToBackStack("hello");
		supportInvalidateOptionsMenu();
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private Toast toast;
	private long lastBackPressTime = 0;

	@Override
	public void onBackPressed() {

		// show message to exit if user presses back two times
		if (this.lastBackPressTime < System.currentTimeMillis() - 2000) {
			toast = Toast.makeText(this, "Press back again Exit",
					Toast.LENGTH_LONG);
			toast.show();
			this.lastBackPressTime = System.currentTimeMillis();
		} else {
			if (toast != null) {
				toast.cancel();
			}
			super.onBackPressed();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putString(fragmentTag, postion);
		super.onSaveInstanceState(outState);
	}

}
