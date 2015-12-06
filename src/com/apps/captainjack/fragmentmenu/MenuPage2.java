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
import com.apps.captainjack.music.MusicActivity;
import com.apps.captainjack.photos.PhotoActivity;
import com.apps.captainjack.store.StoreActivity;
import com.apps.captainjack.videos.VideoActivity;

public class MenuPage2 extends Fragment {

	private static final String TAG = "MenuPage2";
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
		View rootView = inflater.inflate(R.layout.layout_menu_2, container,
				false);
		ImageButton btnPhotos = (ImageButton) rootView
				.findViewById(R.id.buttonPhotos);
		ImageButton btnVideo = (ImageButton) rootView
				.findViewById(R.id.buttonVideo);
		ImageButton btnMusic = (ImageButton) rootView
				.findViewById(R.id.buttonMusic);
		ImageButton btnStore = (ImageButton) rootView
				.findViewById(R.id.buttonStore);

		btnMusic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context.getApplicationContext(),
						MusicActivity.class));
			}
		});

		btnVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context.getApplicationContext(),
						VideoActivity.class));
			}
		});

		btnStore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context.getApplicationContext(),
						StoreActivity.class));
			}
		});

		btnPhotos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context.getApplicationContext(),
						PhotoActivity.class));
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
