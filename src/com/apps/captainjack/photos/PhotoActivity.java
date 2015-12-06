package com.apps.captainjack.photos;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.apps.captainjack.R;
import com.apps.captainjack.adapter.ListViewAdapter;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoActivity extends FragmentActivity {

	private ActionBar actionBar;
	private ListView listView;
	private AsyncHttpClient client;
	private ProgressDialog dialog;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private ArrayList<Photo> photos = new ArrayList<Photo>();
	private static final String TAG = "PhotoActivity";
	private ImageButton imageReload;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		actionBar = getActionBar();
		actionBar.hide();
		initComponent();
		initData();

	}

	private void initComponent() {
		client = new AsyncHttpClient();
		dialog = new ProgressDialog(PhotoActivity.this);
		dialog.setMessage(getString(R.string.wait));
		dialog.setCancelable(true);
		listView = (ListView) findViewById(R.id.listView);
		imageReload = (ImageButton) findViewById(R.id.imageReload);
		imageReload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.show();
				requestData();
			}
		});
		
		LinearLayout layBack = (LinearLayout) findViewById(R.id.layBack);
		layBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PhotoActivity.this.finish();
			}
		});
	}

	private void initData() {
		preferences = getSharedPreferences(PrefData.PHOTOS_KEY, 0);
		String data = preferences.getString(PrefData.PHOTOS_VALUE, "");
		if (!data.equals("")) {
			parsingData(data);
		} else {
			dialog.show();
			requestData();
		}
	}

	private void requestData() {
		client.get(RestUrl.PHOTOS, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				dialog.dismiss();
				preferences = getSharedPreferences(PrefData.PHOTOS_KEY, 0);
				editor = preferences.edit();
				editor.putString(PrefData.PHOTOS_VALUE, response);
				parsingData(response);
			}

			public void onFailure(Throwable error, String content) {
				dialog.dismiss();
				Toast.makeText(PhotoActivity.this, getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
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
				String title = object.getString("title");
				title =  title.replaceAll("&#8211;", "-");
				String url = object.getString("url");
				String img_thumbnail = object.getString("img_thumbnail");

				Photo photo = new Photo();
				photo.setNo(number);
				photo.setTitle(title);
				photo.setThumbnail(img_thumbnail);
				photo.setUrl(url);
				photos.add(photo);

			}
			fillData(photos);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private void fillData(final ArrayList<Photo> photos) {
		if (photos.size() > 0) {
			TextView labelTitle;
			SmartImageView imageThumb;
			LayoutInflater inflater = LayoutInflater.from(PhotoActivity.this);
			View view;
			List<View> views = new ArrayList<View>();
			for (int i = 0; i < photos.size(); i++) {
				view = inflater.inflate(R.layout.item_foto_new, null, false);
				if (i % 2 == 1) {
					view.setBackgroundResource(R.drawable.bg_list_coklat);
				} else {
					view.setBackgroundResource(R.drawable.bg_list_putih);
				}
				labelTitle = (TextView) view.findViewById(R.id.labelTitle);
				imageThumb = (SmartImageView)view.findViewById(R.id.thumbnail);
				labelTitle.setText(photos.get(i).getTitle());
				imageThumb.setImageUrl(photos.get(i).getThumbnail());
				views.add(view);
			}
			listView.setAdapter(new ListViewAdapter(views));
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int pos, long id) {
					Photo photo = photos.get(pos);
					Intent intent = new Intent(PhotoActivity.this,
							GridPhotoActivity.class);
					intent.putExtra("photo", photo);
					startActivity(intent);
				}
			});

		}
	}
}
