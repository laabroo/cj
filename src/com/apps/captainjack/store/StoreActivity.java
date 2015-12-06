package com.apps.captainjack.store;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.apps.captainjack.R;
import com.apps.captainjack.adapter.ListViewAdapter;
import com.apps.captainjack.domain.Gallery;
import com.apps.captainjack.domain.Photo;
import com.apps.captainjack.domain.Song;
import com.apps.captainjack.domain.Store;
import com.apps.captainjack.init.RestUrl;
import com.apps.captainjack.news.NewsActivity;
import com.apps.captainjack.photos.DetailPhotoActivity;
import com.apps.captainjack.photos.GridPhotoActivity;
import com.apps.captainjack.photos.PhotoActivity;
import com.apps.captainjack.util.Shortcut;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.image.SmartImageView;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class StoreActivity extends FragmentActivity {

	private ActionBar actionBar;
	private GridView gridView;
	private AsyncHttpClient client;
	private ProgressDialog dialog;
	private LinkedList<Store> stores = new LinkedList<Store>();
	private static final String TAG = "GridPhotoActivity";
	private int COUNT_PAGE = 1;
	private int PAGE_NUMBER = 1;
	private int myLastVisiblePos;
	private ImageButton imageReload;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);

		actionBar = getActionBar();
		actionBar.hide();
		initComponent();
		getCountPage();

	}

	private void initComponent() {
		client = new AsyncHttpClient();
		dialog = new ProgressDialog(StoreActivity.this);
		dialog.setMessage(getString(R.string.wait));
		dialog.setCancelable(true);
		gridView = (GridView) findViewById(R.id.gridView);
		imageReload = (ImageButton) findViewById(R.id.imageReload);
		myLastVisiblePos = gridView.getFirstVisiblePosition();

		gridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int currentFirstVisPos = view.getFirstVisiblePosition();
				if (currentFirstVisPos > myLastVisiblePos) {
					// scroll down
					Log.d(TAG, "Scroll Down");
					Log.d(TAG, "currentFirstVisPos : " + currentFirstVisPos);
					Log.d(TAG, "myLastVisiblePos : " + myLastVisiblePos);
					getDada();
				}
				if (currentFirstVisPos < myLastVisiblePos) {
					// scroll up
					Log.d(TAG, "Scroll Up");
				}

				myLastVisiblePos = currentFirstVisPos;
			}
		});

		imageReload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				COUNT_PAGE = 1;
				PAGE_NUMBER = 1;
				stores.clear();
				getCountPage();
			}
		});

		LinearLayout layBack = (LinearLayout) findViewById(R.id.layBack);
		layBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				StoreActivity.this.finish();
			}
		});

	}

	private void getCountPage() {
		dialog.show();
		client.get(RestUrl.COUNT_PAGE_STORE, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				dialog.dismiss();
				parsingCoungPage(response);
			}

			public void onFailure(Throwable error, String content) {
				dialog.dismiss();
				Toast.makeText(StoreActivity.this, getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void parsingCoungPage(String respond) {
		try {
			JSONObject jsonObject = new JSONObject(respond);
			COUNT_PAGE = jsonObject.getInt("count_page");
		} catch (JSONException e) {
			COUNT_PAGE = 1;
			Log.e(TAG, e.getMessage());
		}
		dialog.show();
		getDada();

	}

	private void getDada() {
		if (PAGE_NUMBER <= COUNT_PAGE) {
			requestData(PAGE_NUMBER);
			PAGE_NUMBER = PAGE_NUMBER + 1;
		}
	}

	private void requestDataAwal() {
		dialog.show();
		String url = RestUrl.STORE + 1;
		Log.i(TAG, "request >>> " + url);
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				dialog.dismiss();
				stores.clear();
				parsingData(response);
			}

			public void onFailure(Throwable error, String content) {
				dialog.dismiss();
				Toast.makeText(StoreActivity.this, getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void requestData(int id) {
		// dialog.show();
		final int currentPosition = gridView.getCount();
		String url = RestUrl.STORE + id;
		Log.i(TAG, "request >>> " + url);
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				dialog.dismiss();
				parsingData(response);

				gridView.setSelection(currentPosition - 2);

			}

			public void onFailure(Throwable error, String content) {
				dialog.dismiss();
				Toast.makeText(StoreActivity.this, getString(R.string.failed),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void parsingData(String response) {
		// stores.clear();
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONArray jsonArray = jsonObject.getJSONArray("items");
			if (jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					int number = object.getInt("no");
					String imgThumbnail = object.getString("img_thumbnail");
					String title = object.getString("title");
					String url = object.getString("url");

					Store store = new Store();
					store.setNo(number);
					store.setImgThumbnail(imgThumbnail);
					store.setTitle(title);
					store.setUrl(url);
					stores.add(store);

				}
				fillData(stores);
			}
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private void fillData(final LinkedList<Store> stores) {
		if (stores.size() > 0) {
			TextView labelTitle;
			SmartImageView imageView;
			LayoutInflater inflater = LayoutInflater.from(StoreActivity.this);
			View view;
			List<View> views = new ArrayList<View>();
			for (int i = 0; i < stores.size(); i++) {
				view = inflater.inflate(R.layout.item_store, null, false);
				imageView = (SmartImageView) view.findViewById(R.id.thumbnail);
				labelTitle = (TextView) view.findViewById(R.id.labelTitle);
				String urlThumbnail = stores.get(i).getImgThumbnail();
				urlThumbnail = urlThumbnail.replaceAll("-150x150", "");
				imageView.setImageUrl(urlThumbnail);
				labelTitle.setText(stores.get(i).getTitle());
				views.add(view);
			}
			gridView.setAdapter(new ListViewAdapter(views));

			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int pos, long id) {
					Store store = stores.get(pos);
					Intent intent = new Intent(StoreActivity.this,
							DetailStoreActivity.class);
					intent.putExtra("store", store);
					startActivity(intent);
				}
			});

		}
	}

}
