package com.apps.captainjack.about;

import com.apps.captainjack.R;
import com.apps.captainjack.news.NewsActivity;
import com.apps.captainjack.util.Shortcut;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutActivity extends FragmentActivity {

	private TextView labelPhone;
	private TextView labelEmail1;
	private TextView labelEmail2;
	private ActionBar actionBar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		actionBar = getActionBar();
		actionBar.hide();
		labelPhone = (TextView) findViewById(R.id.textPhoneNumber);
		labelEmail1 = (TextView) findViewById(R.id.textEmail1);
		labelEmail2 = (TextView) findViewById(R.id.textEmail2);

		labelEmail1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Shortcut.Email(AboutActivity.this, labelEmail1.getText()
						.toString(), "", "");
			}
		});

		labelEmail2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Shortcut.Email(AboutActivity.this, labelEmail2.getText()
						.toString(), "", "");
			}
		});

		labelPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Shortcut.Call(AboutActivity.this, "081328575999");
			}
		});

		LinearLayout layBack = (LinearLayout) findViewById(R.id.layBack);
		layBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AboutActivity.this.finish();
			}
		});
	}

}
