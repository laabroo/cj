package com.apps.captainjack.discography;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.captainjack.R;
import com.apps.captainjack.domain.Discografi;
import com.apps.captainjack.news.NewsActivity;

public class DetailDiscographyActivity extends FragmentActivity {

	private ActionBar actionBar;
	private WebView webView;
	private TextView labelTitle;
	private TextView labelSubtitle;
	private ImageView imageView;
	private static final String TAG = "DetailNewsActivity";
	private ImageButton imageReload;
	private Discografi discografi;
	private int thumbnails[] = { R.drawable.captainjack_2012,
			R.drawable.musuhku_dalam_cermin, R.drawable.fall_of_concept,
			R.drawable.somethink_about, R.drawable.unmindless };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_discography);
		actionBar = getActionBar();
		actionBar.hide();
		Intent intent = getIntent();
		discografi = (Discografi) intent.getSerializableExtra("disco");
		initComponent();
		parsingData(discografi);

	}

	private void initComponent() {
		webView = (WebView) findViewById(R.id.webView);
		imageReload = (ImageButton) findViewById(R.id.imageReload);
		labelTitle = (TextView) findViewById(R.id.textTitle);
		labelSubtitle = (TextView) findViewById(R.id.textSubTitle);
		imageView = (ImageView) findViewById(R.id.image);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		imageReload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				parsingData(discografi);
			}
		});
		
		LinearLayout layBack = (LinearLayout) findViewById(R.id.layBack);
		layBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DetailDiscographyActivity.this.finish();
			}
		});
	}

	private void parsingData(Discografi discografi) {
		String content = discografi.getContent();
		String title = discografi.getTitle();
		String subtitle = discografi.getSubtitle();
		labelTitle.setText(title);
		labelSubtitle.setText(subtitle);
		imageView.setImageResource(thumbnails[discografi.getNo()]);
		String html = "<html><head><title>"
				+ title
				+ "</title>"
				+ "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\" /></head>"
				+ "<body style=\"background-color:#5f5d5e; font-size:13; color:white\">"
				+ content + "</body></html>";
		webView.loadData(html, "text/html", "UTF-8");
		webView.setVisibility(View.VISIBLE);

	}
}
