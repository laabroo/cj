package com.apps.captainjack.news;

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
import android.widget.TextView;
import android.widget.Toast;

import com.apps.captainjack.R;
import com.apps.captainjack.domain.News;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class DetailNewsActivity extends FragmentActivity {

	private ActionBar actionBar;
	private WebView webView;
	private TextView labelTitle;
	private TextView labelDate;
	private AsyncHttpClient client;
	private static final String TAG = "DetailNewsActivity";
	private ProgressDialog dialog;
	private ImageButton imageReload;
	private String url;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_news);
		actionBar = getActionBar();
		actionBar.hide();
		Intent intent = getIntent();
		News news = (News) intent.getSerializableExtra("news");
		url = news.getUrl();
		initComponent();
		requestData(url);

	}

	private void initComponent() {
		client = new AsyncHttpClient();
		dialog = new ProgressDialog(DetailNewsActivity.this);
		dialog.setMessage(getString(R.string.wait));
		dialog.setCancelable(true);
		webView = (WebView) findViewById(R.id.webView);
		labelTitle = (TextView) findViewById(R.id.textTitle);
		labelDate = (TextView) findViewById(R.id.textDate);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(false);
		webView.getSettings().setDefaultFontSize(15);
		webView.setHorizontalScrollBarEnabled(false);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

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
				DetailNewsActivity.this.finish();
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
				Toast.makeText(DetailNewsActivity.this,
						getString(R.string.failed), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void parsingData(String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject postJson = jsonObject.getJSONObject("post");
			String content = postJson.getString("content");
			content = content.replaceAll("“", "\"");
			content = content.replaceAll("”", "\"");
			content = content.replaceAll("‘", "\"");
			content = content.replaceAll("’", "\"");
			String date = postJson.getString("date");
			date = date.substring(0, 9);
			String title = postJson.getString("title");
			labelTitle.setText(title);
			labelDate.setText("date: " + date);
			String html = "<html><head><title>"
					+ title
					+ "</title>"
					+ "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\" /></head>"
					+ "<body style=\"background-color:#5f5d5e; color:white; font-size:13\">"
					+ content + "</body></html>";
			webView.loadData(html, "text/html", "UTF-8");
			webView.setVisibility(View.VISIBLE);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}
}
