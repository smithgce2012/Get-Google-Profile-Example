package com.smit.common;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Parcelable;

public final class Config {

	public static ConnectivityManager oConnectivityManager;

	public static SharedPreferences oSharedPreferences = null;
	public static SharedPreferences.Editor prefsEditor = null;
	public static Parcelable[] moduleDataArray = null;
	public static int PAGE = 1;

	public static void getmycontext(final Context ocontext) {

		oConnectivityManager = (ConnectivityManager) ocontext
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);

	}

	public static boolean isInternetReachable() {
		try {
			// make a URL to a known source
			URL url = new URL("http://www.google.com");

			// open a connection to that source
			HttpURLConnection urlConnect = (HttpURLConnection) url
					.openConnection();

			// trying to retrieve data from the source. If there
			// is no connection, this line will fail
			urlConnect.getContent();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
