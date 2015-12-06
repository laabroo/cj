package com.apps.captainjack.music;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
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

import com.apps.captainjack.R;
import com.apps.captainjack.adapter.ListViewAdapter;
import com.apps.captainjack.domain.Music;
import com.apps.captainjack.domain.Song;
import com.apps.captainjack.init.RestUrl;
import com.apps.captainjack.news.NewsActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.image.SmartImageView;

public class MusicActivity extends FragmentActivity {

	private ActionBar actionBar;
	private ListView listView;
	private AsyncHttpClient client;
	private ProgressDialog dialog;
	private ArrayList<Music> musics = new ArrayList<Music>();
	private ArrayList<Song> songs = new ArrayList<Song>();
	private static final String TAG = "MusicActivity";
	private ImageButton imageReload;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music);
		actionBar = getActionBar();
		actionBar.hide();
		initComponent();
		requestData();

	}

	private void initComponent() {
		client = new AsyncHttpClient();
		dialog = new ProgressDialog(MusicActivity.this);
		dialog.setMessage(getString(R.string.wait));
		dialog.setCancelable(true);
		listView = (ListView) findViewById(R.id.listView);
		imageReload = (ImageButton) findViewById(R.id.imageReload);
		imageReload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestData();
			}
		});

		LinearLayout layBack = (LinearLayout) findViewById(R.id.layBack);
		layBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MusicActivity.this.finish();
			}
		});
	}

	private void requestData() {
		dialog.show();
		client.get(RestUrl.MUSIC, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				dialog.dismiss();
				parsingData(response);
			}

			public void onFailure(Throwable error, String content) {
				dialog.dismiss();
				Toast.makeText(MusicActivity.this, getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void parsingData(String response) {
		// musics.clear();
		musics = new ArrayList<Music>();
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONArray jsonArray = jsonObject.getJSONArray("posts");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				int id = object.getInt("id");
				String title = object.getString("title");
				String url = object.getString("url");
				String thumbnail = object.getString("thumbnail");

				JSONArray attachments = object.getJSONArray("attachments");

				// songs.clear();
				songs = new ArrayList<Song>();
				for (int a = 0; a < attachments.length(); a++) {
					JSONObject item = attachments.getJSONObject(a);
					int idSong = item.getInt("id");
					String urlStremSong = item.getString("url");
					String songTitle = item.getString("title");

					// ///////////////////////

					songTitle = songTitle.replaceAll("\\.", "");
					songTitle = songTitle.replaceAll("[0-9]", "");
					String prefix = String.valueOf((a + 1));
					if (prefix.length() < 2) {
						prefix = "0" + prefix + ". ";
					} else {
						prefix = prefix + ". ";
					}
					String myTitle = prefix + songTitle;
					myTitle = myTitle.replaceAll("_", " ");

					// ///////////////////////

					Song song = new Song();
					song.setId(idSong);
					song.setTitle(myTitle);
					song.setUrlStream(urlStremSong);

					songs.add(song);
				}

				Music music = new Music();
				music.setUrl(url);
				music.setTitle(title);
				music.setThumbnail(thumbnail);
				music.setSongs(songs);
				musics.add(music);

			}
			fillData(musics);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private void fillData(final ArrayList<Music> musicslist) {
		if (musicslist.size() > 0) {
			TextView labelTitle;
			SmartImageView image;
			LayoutInflater inflater = LayoutInflater.from(MusicActivity.this);
			View view;
			List<View> views = new ArrayList<View>();
			for (int i = 0; i < musicslist.size(); i++) {
				view = inflater.inflate(R.layout.item_music, null, false);
				if (i % 2 == 1) {
					view.setBackgroundResource(R.drawable.bg_list_coklat);
				} else {
					view.setBackgroundResource(R.drawable.bg_list_putih);
				}
				labelTitle = (TextView) view.findViewById(R.id.labelTitle);
				image = (SmartImageView) view.findViewById(R.id.thumbnail);
				labelTitle.setText(musicslist.get(i).getTitle());
				String urlImage = musicslist.get(i).getThumbnail();
				image.setImageUrl(urlImage);
				views.add(view);
			}
			listView.setAdapter(new ListViewAdapter(views));
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int pos, long id) {
					Music music = musicslist.get(pos);
					Intent intent = new Intent(MusicActivity.this,
							DetailMusicActivity.class);
					intent.putExtra("music", music);
					startActivity(intent);
				}
			});

		}
	}
}
