package com.smit.getgoogleprofiledemo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author manish
 * 
 */
public class HomeActivity extends Activity {
	ImageView imageProfile;
	TextView textViewName, textViewEmail, textViewGender, textViewBirthday,
			textViewGoogleid;
	String googleid, textName, textEmail, textGender, textBirthday,
			userImageUrl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		imageProfile = (ImageView) findViewById(R.id.imageView1);
		textViewName = (TextView) findViewById(R.id.textViewNameValue);
		textViewEmail = (TextView) findViewById(R.id.textViewEmailValue);
		textViewGender = (TextView) findViewById(R.id.textViewGenderValue);
		textViewBirthday = (TextView) findViewById(R.id.textViewBirthdayValue);
		textViewGoogleid = (TextView) findViewById(R.id.textViewGoogleidValue);
		/**
		 * get user email using intent
		 */
		try {
			Intent intent = getIntent();
			googleid = intent.getStringExtra("googleid") + "";
			textName = intent.getStringExtra("fname") + "";
			textEmail = intent.getStringExtra("email") + "";
			textGender = intent.getStringExtra("gender") + "";
			textBirthday = intent.getStringExtra("birthday") + "";
			userImageUrl = intent.getStringExtra("profilePic") + "";

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		textViewEmail.setText(textEmail);
		textViewName.setText(textName);
		textViewGender.setText(textGender);
		textViewBirthday.setText(textBirthday);
		textViewGoogleid.setText(googleid);
		/**
		 * get user data from google account
		 */
		new GetImageFromUrl().execute(userImageUrl);
	}

	public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... urls) {
			Bitmap map = null;
			for (String url : urls) {
				map = downloadImage(url);
			}
			return map;
		}

		// Sets the Bitmap returned by doInBackground
		@Override
		protected void onPostExecute(Bitmap result) {
			imageProfile.setImageBitmap(result);
		}

		// Creates Bitmap from InputStream and returns it
		private Bitmap downloadImage(String url) {
			Bitmap bitmap = null;
			InputStream stream = null;
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inSampleSize = 1;

			try {
				stream = getHttpConnection(url);
				bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
				stream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return bitmap;
		}

		// Makes HttpURLConnection and returns InputStream
		private InputStream getHttpConnection(String urlString)
				throws IOException {
			InputStream stream = null;
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();

			try {
				HttpURLConnection httpConnection = (HttpURLConnection) connection;
				httpConnection.setRequestMethod("GET");
				httpConnection.connect();

				if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					stream = httpConnection.getInputStream();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return stream;
		}
	}
}