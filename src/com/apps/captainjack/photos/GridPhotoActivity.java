package com.apps.captainjack.photos;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.apps.captainjack.R;
import com.apps.captainjack.adapter.ListViewAdapter;
import com.apps.captainjack.domain.Gallery;
import com.apps.captainjack.domain.Photo;
import com.apps.captainjack.init.PrefData;
import com.apps.captainjack.init.RestUrl;
import com.apps.captainjack.news.NewsActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.image.SmartImageView;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GridPhotoActivity extends FragmentActivity {

	private ActionBar actionBar;
	private GridView gridView;
	private AsyncHttpClient client;
	private ProgressDialog dialog;
	private ArrayList<Gallery> photos = new ArrayList<Gallery>();
	private static final String TAG = "GridPhotoActivity";
	private ImageButton imageReload;
	private String url;
	private String respondjson;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid_photo);
		Intent intent = getIntent();
		Photo data = (Photo) intent.getSerializableExtra("photo");
		url = data.getUrl();
		actionBar = getActionBar();
		actionBar.hide();
		initComponent();
		requestData(url);

	}

	private void initComponent() {
		client = new AsyncHttpClient();
		dialog = new ProgressDialog(GridPhotoActivity.this);
		dialog.setMessage(getString(R.string.wait));
		dialog.setCancelable(true);
		gridView = (GridView) findViewById(R.id.gridView);
		imageReload = (ImageButton) findViewById(R.id.imageReload);
		imageReload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestData(url);
			}
		});

		LinearLayout layBack = (LinearLayout) findViewById(R.id.layBack);
		layBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GridPhotoActivity.this.finish();
			}
		});
	}

	private void requestData(String param) {
		dialog.show();
		client.get(RestUrl.PHOTOS_GRID + param, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				dialog.dismiss();
				parsingData(response);
				respondjson = response;
			}

			public void onFailure(Throwable error, String content) {
				dialog.dismiss();
				Toast.makeText(GridPhotoActivity.this,
						getString(R.string.failed), Toast.LENGTH_SHORT).show();
			}
		});
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
			fillData(photos);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private void fillData(final ArrayList<Gallery> photos) {
		if (photos.size() > 0) {
			SmartImageView imageView;
			LayoutInflater inflater = LayoutInflater
					.from(GridPhotoActivity.this);
			View view;
			List<View> views = new ArrayList<View>();
			for (int i = 0; i < photos.size(); i++) {
				view = inflater.inflate(R.layout.item_gallery, null, false);
				imageView = (SmartImageView) view.findViewById(R.id.thumbnail);
				imageView.setImageUrl(photos.get(i).getThumbnail());
				views.add(view);
			}
			gridView.setAdapter(new ListViewAdapter(views));
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int pos, long id) {
					Gallery gallery = photos.get(pos);
					Intent intent = new Intent(GridPhotoActivity.this,
							DetailPhotoActivity.class);
					intent.putExtra("gallery", gallery);
					intent.putExtra("pos", pos);
					intent.putExtra("json", respondjson);
					startActivity(intent);
				}
			});

		}
	}

}
