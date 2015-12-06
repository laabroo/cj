package com.apps.captainjack.videos;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.apps.captainjack.R;
import com.apps.captainjack.adapter.ListViewAdapter;
import com.apps.captainjack.domain.Photo;
import com.apps.captainjack.domain.Video;
import com.apps.captainjack.init.PrefData;
import com.apps.captainjack.init.RestUrl;
import com.apps.captainjack.news.NewsActivity;
import com.apps.captainjack.photos.GridPhotoActivity;
import com.apps.captainjack.photos.PhotoActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.image.SmartImageView;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class VideoActivity extends FragmentActivity {
	private ActionBar actionBar;
	private ListView listView;
	private AsyncHttpClient client;
	private ProgressDialog dialog;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private ArrayList<Video> videos = new ArrayList<Video>();
	private static final String TAG = "VideoActivity";
	private ImageButton imageReload;
	private int widthScreen = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		widthScreen = metrics.widthPixels;
		actionBar = getActionBar();
		actionBar.hide();
		initComponent();
		initData();

	}

	private void initComponent() {
		client = new AsyncHttpClient();
		dialog = new ProgressDialog(VideoActivity.this);
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
				VideoActivity.this.finish();
			}
		});
	}

	private void initData() {
		preferences = getSharedPreferences(PrefData.VIDEOS_KEY, 0);
		String data = preferences.getString(PrefData.VIDEOS_VALUE, "");
		Log.i(TAG, data);
		if (!data.equals("")) {
			parsingData(data);
		} else {
			dialog.show();
			requestData();
		}
	}

	private void requestData() {
		client.get(RestUrl.VIDEOS + 25, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				dialog.dismiss();
				preferences = getSharedPreferences(PrefData.VIDEOS_KEY, 0);
				editor = preferences.edit();
				editor.putString(PrefData.VIDEOS_VALUE, response);
				parsingData(response);
			}

			public void onFailure(Throwable error, String content) {
				dialog.dismiss();
				Toast.makeText(VideoActivity.this, getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void parsingData(String response) {
		videos.clear();
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONArray jsonArray = jsonObject.getJSONArray("items");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				JSONObject idJson = object.getJSONObject("id");
				String youtubeId = idJson.getString("videoId");

				JSONObject snippetJson = object.getJSONObject("snippet");
				String title = snippetJson.getString("title");
				String publishAt = snippetJson.getString("publishedAt");

				JSONObject thumbnailJson = snippetJson
						.getJSONObject("thumbnails");

				JSONObject hightJson = thumbnailJson.getJSONObject("high");
				String imageHight = hightJson.getString("url");

				JSONObject defaultJson = thumbnailJson.getJSONObject("default");
				String imageDefault = defaultJson.getString("url");

				JSONObject mediumJson = thumbnailJson.getJSONObject("medium");
				String imageMedium = mediumJson.getString("url");

				Video video = new Video();
				video.setPublishAt(publishAt);
				video.setImageHight(imageHight);
				video.setImageDefault(imageDefault);
				video.setImageMedium(imageMedium);
				video.setTitle(title);
				video.setYoutubeId(youtubeId);
				videos.add(video);

			}
			fillData(videos);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private void fillData(final ArrayList<Video> videos) {
		if (videos.size() > 0) {
			TextView labelTitle;
			SmartImageView imageView;
			LayoutInflater inflater = LayoutInflater.from(VideoActivity.this);
			View view;
			List<View> views = new ArrayList<View>();
			for (int i = 0; i < videos.size(); i++) {
				view = inflater.inflate(R.layout.item_video, null, false);
				labelTitle = (TextView) view.findViewById(R.id.labelTitle);
				imageView = (SmartImageView) view.findViewById(R.id.thumbnail);
				labelTitle.setText(videos.get(i).getTitle());
				imageView.setImageUrl(videos.get(i).getImageHight());
				imageView.scale(widthScreen);
				views.add(view);
			}
			listView.setAdapter(new ListViewAdapter(views));
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int pos, long id) {
					Video video = videos.get(pos);
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse("vnd.youtube://" + video.getYoutubeId()));
					startActivity(intent);
				}
			});

		}
	}
}
