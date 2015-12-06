package com.apps.captainjack.fragmentmenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.apps.captainjack.R;
import com.apps.captainjack.biography.BiographyActivity;
import com.apps.captainjack.discography.DiscographyActivity;
import com.apps.captainjack.news.NewsActivity;
import com.apps.captainjack.schedule.ScheduleActivity;

public class MenuPage1 extends Fragment {

	private static final String TAG = "MenuPage1";
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Police();

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = getActivity();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.layout_menu_1, container,
				false);
		ImageButton btnNews = (ImageButton) rootView
				.findViewById(R.id.buttonNews);
		ImageButton btnShow = (ImageButton) rootView
				.findViewById(R.id.buttonShows);
		ImageButton btnBiography = (ImageButton) rootView
				.findViewById(R.id.buttonBiography);
		ImageButton btnDiscography = (ImageButton) rootView
				.findViewById(R.id.buttonDiscography);

		btnBiography.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context.getApplicationContext(),
						BiographyActivity.class));
			}
		});

		btnDiscography.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context.getApplicationContext(),
						DiscographyActivity.class));
			}
		});

		btnNews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context.getApplicationContext(),
						NewsActivity.class));
			}
		});

		btnShow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context.getApplicationContext(),
						ScheduleActivity.class));
			}
		});
		return rootView;
	}

	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi", "NewApi" })
	public void Police() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

}
