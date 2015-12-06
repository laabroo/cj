package com.apps.captainjack.news;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
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
import com.apps.captainjack.domain.News;
import com.apps.captainjack.init.RestUrl;
import com.apps.captainjack.photos.GridPhotoActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class NewsActivity extends FragmentActivity {

	private ActionBar actionBar;
	private XListView listView;
	private AsyncHttpClient client;
	private ProgressDialog dialog;
	private LinkedList<News> news = new LinkedList<News>();
	private static final String TAG = "NewsActivity";
	private ImageButton imageReload;
	private int pageCount;
	private int pageNumber = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		actionBar = getActionBar();
		actionBar.hide();
		initComponent();
		loadDataAwal();

	}

	private void initComponent() {
		client = new AsyncHttpClient();
		dialog = new ProgressDialog(NewsActivity.this);
		dialog.setMessage(getString(R.string.wait));
		dialog.setCancelable(true);
		listView = (XListView) findViewById(R.id.listView);
		listView.setPullLoadEnable(true);

		imageReload = (ImageButton) findViewById(R.id.imageReload);
		imageReload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadDataAwal();
			}
		});
		LinearLayout layBack = (LinearLayout) findViewById(R.id.layBack);
		layBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				NewsActivity.this.finish();
			}
		});

		listView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				loadDataAwal();
			}

			@Override
			public void onLoadMore() {
				if (pageNumber <= pageCount) {
					Log.i(TAG, "Page Count : " + pageCount);
					Log.i(TAG, "Page Number : " + pageNumber);
					requestMore(pageNumber);
				} else {
					onLoad();
				}
			}
		});
	}

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
	}

	private void loadDataAwal() {
		dialog.show();
		news.clear();
		requestData(1);
		pageNumber = 2;
	}

	private void requestMore(int page) {
		requestData(page);
		pageNumber = page + 1;
	}

	private void requestData(int page) {
		client.get(RestUrl.NEWS + page + "&json=1",
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						// dialog.dismiss();
						parsingData(response);
						onLoad();
					}

					public void onFailure(Throwable error, String content) {
						dialog.dismiss();
						Toast.makeText(NewsActivity.this,
								getString(R.string.failed), Toast.LENGTH_SHORT)
								.show();
					}
				});
	}

	private void parsingData(String response) {
		// news.clear();
		try {
			JSONObject jsonObject = new JSONObject(response);
			pageCount = jsonObject.getInt("pages");
			JSONArray jsonArray = jsonObject.getJSONArray("posts");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				int id = object.getInt("id");
				String title = object.getString("title");
				String url = object.getString("url");
				String date = object.getString("date");

				title = Html.fromHtml(title).toString();

				News newss = new News();
				newss.setId(id);
				newss.setTitle(title);
				newss.setDate(date);
				newss.setUrl(url);
				news.add(newss);
			}
			if (news.size() < 15) {
				// dialog.show();
				requestMore(pageNumber);
			} else {
				fillData(news);
			}
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private void fillData(final LinkedList<News> mnews) {
		if (news.size() > 0) {
			TextView labelTitle;
			TextView labelDate;
			LayoutInflater inflater = LayoutInflater.from(NewsActivity.this);
			View view;
			List<View> views = new ArrayList<View>();
			for (int i = 0; i < mnews.size(); i++) {
				view = inflater.inflate(R.layout.item_news_new, null, false);
				if (i % 2 == 1) {
					view.setBackgroundResource(R.drawable.bg_list_coklat);
				} else {
					view.setBackgroundResource(R.drawable.bg_list_putih);
				}
				labelTitle = (TextView) view.findViewById(R.id.textTitle);
				labelDate = (TextView) view.findViewById(R.id.textDate);
				labelTitle.setText(mnews.get(i).getTitle());
				String date = mnews.get(i).getDate();
				date = date.substring(0, 9);
				labelDate.setText("date: " + date);
				views.add(view);
			}
			listView.setAdapter(new ListViewAdapter(views));
			if (news.size() > 15) {
				dialog.dismiss();
			}
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int pos, long id) {
					int index = pos - 1;
					News newss = mnews.get(index);
					Intent intent = new Intent(NewsActivity.this,
							DetailNewsActivity.class);
					intent.putExtra("news", newss);
					startActivity(intent);
				}
			});

		}
	}
}
