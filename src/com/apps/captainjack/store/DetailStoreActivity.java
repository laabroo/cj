package com.apps.captainjack.store;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apps.captainjack.R;
import com.apps.captainjack.domain.Store;
import com.apps.captainjack.init.RestUrl;
import com.apps.captainjack.news.NewsActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.image.SmartImageView;

public class DetailStoreActivity extends FragmentActivity {

	private ActionBar actionBar;
	private WebView webView;
	private SmartImageView imageView;
	private AsyncHttpClient client;
	private static final String TAG = "DetailStoreActivity";
	private ProgressDialog dialog;
	private String title = "";
	private ImageButton imageReload;
	private String url;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_store);
		actionBar = getActionBar();
		actionBar.hide();
		Intent intent = getIntent();
		Store store = (Store) intent.getSerializableExtra("store");
		url = store.getUrl();
		title = store.getTitle();
		initComponent();
		requestData(url);
	}

	private void initComponent() {
		client = new AsyncHttpClient();
		dialog = new ProgressDialog(DetailStoreActivity.this);
		dialog.setMessage(getString(R.string.wait));
		dialog.setCancelable(true);
		webView = (WebView) findViewById(R.id.webView);
		imageReload = (ImageButton) findViewById(R.id.imageReload);
		imageView = (SmartImageView) findViewById(R.id.thumbnail);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
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
				DetailStoreActivity.this.finish();
			}
		});
	}

	private void requestData(String url) {
		dialog.show();
		client.get(url + "&json=1", new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				dialog.dismiss();
				parsingData(response);
			}

			public void onFailure(Throwable error, String content) {
				dialog.dismiss();
				Toast.makeText(DetailStoreActivity.this,
						getString(R.string.failed), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void parsingData(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject postJson = jsonObject.getJSONObject("post");
			JSONObject thumbJson = postJson.getJSONObject("thumbnail_images");
			JSONObject fullJson = thumbJson.getJSONObject("full");
			String url = fullJson.getString("url");
			imageView.setImageUrl(url);
			String content = postJson.getString("content");
			String html = "<html><head><title>"
					+ title
					+ "</title>"
					+ "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\" /></head>"
					+ "<body style=\"background-color:#5f5d5e; color:white\">" + content + "</body></html>";
			webView.loadData(html, "text/html", "UTF-8");
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}
}
