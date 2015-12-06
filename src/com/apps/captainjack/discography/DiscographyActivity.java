package com.apps.captainjack.discography;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.captainjack.R;
import com.apps.captainjack.adapter.ListViewAdapter;
import com.apps.captainjack.domain.Discografi;
import com.apps.captainjack.domain.News;
import com.apps.captainjack.init.RestUrl;
import com.apps.captainjack.news.NewsActivity;
import com.apps.captainjack.photos.GridPhotoActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class DiscographyActivity extends FragmentActivity {

	private ActionBar actionBar;
	private ListView listView;
	private ProgressDialog dialog;
	private ArrayList<Discografi> discografis = new ArrayList<Discografi>();
	private static final String TAG = "DiscographyActivity";
	private ImageButton imageReload;
	private int thumbnails[] = { R.drawable.captainjack_2012,
			R.drawable.musuhku_dalam_cermin, R.drawable.fall_of_concept,
			R.drawable.somethink_about, R.drawable.unmindless };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discography);
		actionBar = getActionBar();
		actionBar.hide();
		initComponent();
		loadData();

	}

	private void loadData() {
		dialog.show();
		try {
			InputStream is = getAssets().open("json/discografi.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer); // read file
			is.close(); // close file

			dialog.dismiss();
			// Store text file data in the string variable
			String str_data = new String(buffer);
			parseData(str_data);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private void parseData(String data) {
		discografis.clear();
		try {
			JSONObject jsonObject = new JSONObject(data);
			JSONArray items = jsonObject.getJSONArray("items");
			if (items.length() > 0) {
				for (int i = 0; i < items.length(); i++) {
					JSONObject object = items.getJSONObject(i);
					String title = object.getString("title");
					String subtitle = object.getString("subtitle");
					String cover = object.getString("cover");
					String content = object.getString("content");
					int no = object.getInt("no");

					Discografi discografi = new Discografi();
					discografi.setContent(content);
					discografi.setCover(cover);
					discografi.setSubtitle(subtitle);
					discografi.setTitle(title);
					discografi.setNo(no);
					discografis.add(discografi);
					
				}
				fillData(discografis);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void fillData(final ArrayList<Discografi> disco) {
		if (disco.size() > 0) {
			TextView labelTitle;
			ImageView imageThumbnail;
			LayoutInflater inflater = LayoutInflater
					.from(DiscographyActivity.this);
			View view;
			List<View> views = new ArrayList<View>();
			for (int i = 0; i < disco.size(); i++) {
				view = inflater.inflate(R.layout.item_discography, null, false);
				if (i % 2 == 1) {
					view.setBackgroundResource(R.drawable.bg_list_coklat);
				} else {
					view.setBackgroundResource(R.drawable.bg_list_putih);
				}
				labelTitle = (TextView) view.findViewById(R.id.labelTitle);
				imageThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
				labelTitle.setText(disco.get(i).getTitle());
				imageThumbnail.setImageResource(thumbnails[i]);
				views.add(view);
			}
			listView.setAdapter(new ListViewAdapter(views));
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int pos, long id) {
					Discografi discografi = disco.get(pos);
					Intent intent = new Intent(DiscographyActivity.this,
							DetailDiscographyActivity.class);
					intent.putExtra("disco", discografi);
					startActivity(intent);
				}
			});

		}
	}

	private void initComponent() {
		dialog = new ProgressDialog(DiscographyActivity.this);
		dialog.setMessage(getString(R.string.wait));
		dialog.setCancelable(true);
		listView = (ListView) findViewById(R.id.listView);
		imageReload = (ImageButton) findViewById(R.id.imageReload);
		imageReload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadData();
			}
		});
		
		LinearLayout layBack = (LinearLayout) findViewById(R.id.layBack);
		layBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DiscographyActivity.this.finish();
			}
		});
	}

}
