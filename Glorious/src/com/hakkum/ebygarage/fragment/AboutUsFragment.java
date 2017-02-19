package com.hakkum.ebygarage.fragment;
import com.actionbarsherlock.app.SherlockFragment;
import com.hakkum.eby.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutUsFragment extends SherlockFragment{
 TextView txtView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.aboutus, container, false);
		txtView = (TextView) rootView.findViewById(R.id.textView1);
//		Typeface myTypeface = Typeface.createFromAsset(
//                getActivity().getAssets(),
//                "HelveticaNeue.ttf");
//          txtView.setTypeface(myTypeface);
		return rootView;
	}
}
