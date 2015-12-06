package com.apps.captainjack.biography;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
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
import com.apps.captainjack.init.RestUrl;
import com.apps.captainjack.news.NewsActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class BiographyActivity extends FragmentActivity {

	private ActionBar actionBar;
	private WebView webView;
	private static final String TAG = "BiographyActivity";
	private TextView labelTitle;
	private ImageButton imageReload;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_biography);
		actionBar = getActionBar();
		actionBar.hide();
		initComponent();
		requestData();
	}

	private void initComponent() {
		webView = (WebView) findViewById(R.id.webView);
		labelTitle = (TextView) findViewById(R.id.labelTitle);
		imageReload = (ImageButton) findViewById(R.id.imageReload);
		webView.setVisibility(View.GONE);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		labelTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestData();
			}
		});

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
				BiographyActivity.this.finish();
			}
		});
	}

	private void requestData() {
		webView.loadUrl("file:///android_asset/web/biografi.html");
		webView.setVisibility(View.VISIBLE);
	}

}
