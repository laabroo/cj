package com.apps.captainjack.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

public class Shortcut {

	public static String Month(String month) {
		if (month.equals("01")) {
			month = "JAN";
		} else if (month.equals("02")) {
			month = "FEB";
		} else if (month.equals("03")) {
			month = "MAR";
		}

		else if (month.equals("04")) {
			month = "APR";
		} else if (month.equals("05")) {
			month = "MAY";
		}

		else if (month.equals("06")) {
			month = "JUN";
		} else if (month.equals("07")) {
			month = "JUL";
		}

		else if (month.equals("08")) {
			month = "AUG";
		} else if (month.equals("09")) {
			month = "SEP";
		}

		else if (month.equals("10")) {
			month = "OCT";
		} else if (month.equals("11")) {
			month = "NOV";
		}

		else if (month.equals("12")) {
			month = "DES";
		} else {
			month = "MON";
		}
		return month;
	}

	public static void Call(Context context, String number) {
		Intent call = new Intent(Intent.ACTION_DIAL);
		call.setData(Uri.parse("tel:" + number));
		context.startActivity(call);
	}

	public static void Email(Context context, String subject, String message,
			String title) {
		Intent mailIntent = new Intent();
		mailIntent.setAction(Intent.ACTION_SEND);
		mailIntent.setType("message/rfc822");
		mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
		mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		mailIntent.putExtra(Intent.EXTRA_TEXT, message);
		context.startActivity(Intent.createChooser(mailIntent, title));
	}

	public static boolean isNetworkOnline(Context context) {
		boolean status = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getNetworkInfo(0);
			if (netInfo != null
					&& netInfo.getState() == NetworkInfo.State.CONNECTED) {
				status = true;
			} else {
				netInfo = cm.getNetworkInfo(1);
				if (netInfo != null
						&& netInfo.getState() == NetworkInfo.State.CONNECTED)
					status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return status;

	}

	public static void ToastLong(Context context, String message) {
		android.widget.Toast.makeText(context, message,
				android.widget.Toast.LENGTH_LONG).show();
	}

	public static void ToastShort(Context context, String message) {
		android.widget.Toast.makeText(context, message,
				android.widget.Toast.LENGTH_SHORT).show();
	}
}
