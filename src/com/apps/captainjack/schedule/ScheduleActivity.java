package com.apps.captainjack.schedule;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.captainjack.R;
import com.apps.captainjack.adapter.ListViewAdapter;
import com.apps.captainjack.domain.Schedule;
import com.apps.captainjack.init.RestUrl;
import com.apps.captainjack.news.NewsActivity;
import com.apps.captainjack.util.Shortcut;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ScheduleActivity extends FragmentActivity {

	private ActionBar actionBar;
	private ListView listView;
	private AsyncHttpClient client;
	private ProgressDialog dialog;
	private ArrayList<Schedule> schedules = new ArrayList<Schedule>();
	private static final String TAG = "ScheduleActivity";
	private ImageButton imageReload;
	private TextView noData;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		actionBar = getActionBar();
		actionBar.hide();
		initComponent();
		requestData();
	}

	private void initComponent() {
		client = new AsyncHttpClient();
		dialog = new ProgressDialog(ScheduleActivity.this);
		dialog.setMessage(getString(R.string.wait));
		dialog.setCancelable(true);
		listView = (ListView) findViewById(R.id.listView);
		noData = (TextView) findViewById(R.id.noData);
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
				ScheduleActivity.this.finish();
			}
		});
	}

	private void requestData() {
		dialog.show();
		client.get(RestUrl.SCHEDULE, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				dialog.dismiss();
				parsingData(response);
			}

			public void onFailure(Throwable error, String content) {
				dialog.dismiss();
				Toast.makeText(ScheduleActivity.this,
						getString(R.string.failed), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void parsingData(String response) {
		schedules.clear();
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONArray jsonArray = jsonObject.getJSONArray("items");
			for (int i = 1; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				int number = object.getInt("no");
				String place = object.getString("place");
				String time = object.getString("time");
				Log.i(TAG, place);

				Schedule schedule = new Schedule();
				schedule.setNo(number);
				schedule.setPlace(place);
				schedule.setTime(time);
				schedules.add(schedule);

			}
			fillData(schedules);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private void fillData(final ArrayList<Schedule> schedules) {
		if (schedules.size() > 0) {
			TextView labelPlace;
			TextView labelDay;
			TextView labelTime;
			TextView labelMonth;
			LayoutInflater inflater = LayoutInflater
					.from(ScheduleActivity.this);
			View view;
			List<View> views = new ArrayList<View>();
			for (int i = 0; i < schedules.size(); i++) {
				view = inflater.inflate(R.layout.item_schedule, null, false);
				if (i % 2 == 1) {
					view.setBackgroundResource(R.drawable.bg_list_coklat);
				} else {
					view.setBackgroundResource(R.drawable.bg_list_putih);
				}
				labelMonth = (TextView) view.findViewById(R.id.textMonth);
				labelDay = (TextView) view.findViewById(R.id.textDay);
				labelTime = (TextView) view.findViewById(R.id.textTime);
				labelPlace = (TextView) view.findViewById(R.id.textPlace);

				String timejson = schedules.get(i).getTime();
				String day = timejson.substring(0, 2);
				String month = timejson.substring(3, 5);
				month = Shortcut.Month(month);
				labelPlace.setText(schedules.get(i).getPlace());
				labelDay.setText(day);
				labelMonth.setText(month);

				String mytime = schedules.get(i).getTime();
				mytime = mytime.substring(10, mytime.length());
				mytime = mytime.trim();
				labelTime.setText(mytime);

				views.add(view);
			}
			listView.setAdapter(new ListViewAdapter(views));

		} else {
			listView.setVisibility(View.GONE);
			noData.setVisibility(View.VISIBLE);
			noData.setText("CaptainJack have not a schedule for this moment");
		}
	}
}
