package com.hakkum.ebygarage.fragment;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hakkum.eby.R;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MapViewFragment extends SherlockFragment {

	static final LatLng KIEL = new LatLng(12.9947435,77.6138435);
	private GoogleMap map;
	private View rootView;
	MapView mapView;

	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.map_view, container, false);
		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			Log.e("Address Map", "Could not initialize google play", e);
		}

		switch (GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity())) {
		case ConnectionResult.SUCCESS:
			mapView = (MapView) rootView.findViewById(R.id.map);
			mapView.onCreate(savedInstanceState);
			// Gets to GoogleMap from the MapView and does initialization stuff
			if (mapView != null) {
				map = mapView.getMap();
				 map.addMarker(new MarkerOptions()
				.position(KIEL)
				.title("Frazer town")
				.snippet("Landmark: Near Govt Community Mall")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_launcher)));
				map.getUiSettings().setMyLocationButtonEnabled(false);
				map.setMyLocationEnabled(true);
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(KIEL, 15));
				map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
			}
			break;
		case ConnectionResult.SERVICE_MISSING:
			Toast.makeText(getActivity(), "SERVICE MISSING", Toast.LENGTH_SHORT)
					.show();
			break;
		case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			Toast.makeText(getActivity(), "UPDATE REQUIRED", Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			Toast.makeText(
					getActivity(),
					GooglePlayServicesUtil
							.isGooglePlayServicesAvailable(getActivity()),
					Toast.LENGTH_SHORT).show();
		}
		return rootView;
	}

	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}
}
