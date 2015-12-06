package com.apps.captainjack.photos;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.apps.captainjack.R;
import com.apps.captainjack.domain.Gallery;
import com.apps.captainjack.news.NewsActivity;
import com.loopj.android.image.SmartImageView;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DetailPhotoActivity extends FragmentActivity {

	private ActionBar actionBar;
	private String respondJson;
	private ArrayList<Gallery> photos = new ArrayList<Gallery>();
	private static final String TAG = "DetailPhotoActivity";
	private ViewPager viewPager;
	private int pos = 0;

	public void onCreate(Bundle savedInstaceState) {
		super.onCreate(savedInstaceState);
		setContentView(R.layout.activity_detail_photo_new);
		actionBar = getActionBar();
		actionBar.hide();
		Intent intent = getIntent();
		respondJson = intent.getStringExtra("json");
		pos = intent.getIntExtra("pos", 0);
		parsingData(respondJson);

		LinearLayout layBack = (LinearLayout) findViewById(R.id.layBack);
		layBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DetailPhotoActivity.this.finish();
			}
		});

		viewPager = (ViewPager) findViewById(R.id.view_pager);
		ImagePagerAdapter adapter = new ImagePagerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(pos);

	}

	private void parsingData(String response) {
		photos.clear();
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONArray jsonArray = jsonObject.getJSONArray("items");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				int number = object.getInt("no");
				String img_thumbnail = object.getString("img_thumbnail");
				String img_large = object.getString("img_large");

				Gallery photo = new Gallery();
				photo.setNo(number);
				photo.setLarge(img_large);
				photo.setThumbnail(img_thumbnail);
				photos.add(photo);

			}

		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private class ImagePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return photos.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageView) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Context context = DetailPhotoActivity.this;
			SmartImageView imageView = new SmartImageView(context);
			int padding = context.getResources().getDimensionPixelSize(
					R.dimen.padding_medium);
			imageView.setPadding(padding, padding, padding, padding);
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			imageView.setImageResource(R.drawable.cj_image_loading);
			imageView.setImageUrl(photos.get(position).getLarge());
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}

}
