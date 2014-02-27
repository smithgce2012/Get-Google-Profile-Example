package com.smit.getgoogleprofiledemo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.smit.common.AlertDialogRadio;
import com.smit.common.AlertDialogRadio.AlertPositiveListener;
import com.smit.common.Config;
import com.smit.common.StaticValue;

public class MainActivity extends Activity implements AlertPositiveListener {

	int position = 0;
	private Button button1 = null;
	AccountManager mAccountManager;
	private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button1 = (Button) findViewById(R.id.button1);

		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(StaticValue.buttonClick);
				loginGmail();
			}
		});
	}

	private void loginGmail() {
		Config.getmycontext(getApplicationContext());
		if (Config.oConnectivityManager.getActiveNetworkInfo() != null
				&& Config.oConnectivityManager.getActiveNetworkInfo()
						.isAvailable()
				&& Config.oConnectivityManager.getActiveNetworkInfo()
						.isConnected()) {

			StaticValue.accountarrs = getAccountNames();
			if (StaticValue.accountarrs.length > 0) {
				// you can set here account for login
				if (StaticValue.accountarrs.length == 1) {
					getTask(MainActivity.this, StaticValue.accountarrs[0],
							SCOPE).execute();
				} else {
					getAccountfromArray();
				}

			} else {
				Builder alert2 = new AlertDialog.Builder(MainActivity.this);
				alert2.setTitle("Sorry!");
				alert2.setCancelable(false);
				alert2.setMessage("These options rely on a Gmail account, but you don't seem to have one configured. Would you like to configure one now?");
				alert2.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								try {
									AccountManager acm = AccountManager
											.get(getApplicationContext());
									acm.addAccount("com.google", null, null,
											null, MainActivity.this, null, null);
								} catch (Exception e) {
									Log.e("Errror:", e.toString());
								}

							}
						});
				alert2.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						});
				AlertDialog dialog = alert2.show();

				// Must call show() prior to fetching text view
				TextView messageView = (TextView) dialog
						.findViewById(android.R.id.message);
				messageView.setGravity(Gravity.CENTER);
				Button yes = (Button) dialog.findViewById(android.R.id.button1);
				Button no = (Button) dialog.findViewById(android.R.id.button2);

				yes.setTextColor(Color.parseColor("#FFFFFF"));
				no.setTextColor(Color.parseColor("#FFFFFF"));

				yes.getBackground().setColorFilter(0xFF69DC05,
						PorterDuff.Mode.MULTIPLY);
				no.getBackground().setColorFilter(0xFFEE0000,
						PorterDuff.Mode.MULTIPLY);

				TextView titleView = (TextView) dialog
						.findViewById(getResources().getIdentifier(
								"alertTitle", "id", "android"));
				if (titleView != null) {
					titleView.setGravity(Gravity.CENTER);
				}

			}
		}

		else {
			StaticValue.networkAlert(MainActivity.this);
		}

	}

	private String[] getAccountNames() {
		mAccountManager = AccountManager.get(this);
		Account[] accounts = mAccountManager
				.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
		String[] names = new String[accounts.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = accounts[i].name;
		}
		return names;
	}

	private void getAccountfromArray() {

		FragmentManager manager = getFragmentManager();

		AlertDialogRadio alert = new AlertDialogRadio();

		Bundle b = new Bundle();

		b.putInt("position", position);

		alert.setArguments(b);

		alert.show(manager, "alert_dialog_radio");

	}

	private AbstractGetLoginNameTask getTask(MainActivity activity,
			String email, String scope) {
		return new Loginprofile(activity, email, scope);

	}

	@Override
	public void onPositiveClick(int position) {
		this.position = position;

		getTask(MainActivity.this, StaticValue.accountarrs[this.position],
				SCOPE).execute();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			// user chose an image from the gallery

			getTask(MainActivity.this, StaticValue.accountarrs[this.position],
					SCOPE).execute();
		}
		if (resultCode == RESULT_CANCELED) {
			// user chose an image from the gallery
			Log.e("", "cancel");

		}

	}
}

abstract class AbstractGetLoginNameTask extends AsyncTask<Void, String, String> {
	private static final String TAG = "TokenInfoTask";
	protected MainActivity mActivity;

	public static String GOOGLE_USER_DATA = "No_data";
	protected String mScope;
	protected String mEmail;
	protected int mRequestCode;

	protected String fname = "", birthday = "", gender = "", googleid = "",
			profilePic = "", regId = "";

	AbstractGetLoginNameTask(MainActivity activity, String email, String scope) {
		this.mActivity = activity;
		this.mScope = scope;
		this.mEmail = email;

	}

	@Override
	protected String doInBackground(Void... params) {
		String s = "";
		try {
			s = fetchNameFromProfileServer();

		} catch (IOException ex) {
			onError("Following Error occured, please try again. "
					+ ex.getMessage(), ex);
		} catch (JSONException e) {
			onError("Bad response: " + e.getMessage(), e);
		}
		return s;
	}

	@Override
	protected void onPostExecute(String result) {

		if (!result.equals("No_data")) {
			try {

				System.out.println("On Home Page***"
						+ AbstractGetLoginNameTask.GOOGLE_USER_DATA);

				JSONObject profileData = new JSONObject(
						AbstractGetLoginNameTask.GOOGLE_USER_DATA);

				if (profileData.has("id"))
					googleid = profileData.getString("id");
				if (profileData.has("name"))
					fname = profileData.getString("name");

				if (profileData.has("gender"))
					gender = profileData.getString("gender");

				if (profileData.has("birthday"))
					birthday = profileData.getString("birthday");
				if (profileData.has("picture"))
					profilePic = profileData.getString("picture");

			} catch (JSONException e) {
				StaticValue.serverAlert(mActivity);
			}

			if (!googleid.equals(""))
				login_google();
		}
	}

	private void login_google() {
		
		Intent i=new Intent(mActivity,HomeActivity.class);
		i.putExtra("googleid", googleid);
		i.putExtra("fname", fname);
		i.putExtra("gender", gender);
		i.putExtra("birthday", birthday);
		i.putExtra("profilePic", profilePic);
		i.putExtra("email", mEmail);
		mActivity.startActivity(i);
		mActivity.finish();

	}

	protected void onError(String msg, Exception e) {
		if (e != null) {
			Log.e(TAG, "Exception: ", e);
		}
	}

	protected String fetchToken2() {
		try {
			try {
				String s = GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
				return s;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (GooglePlayServicesAvailabilityException playEx) {
			// GooglePlayServices.apk is either old, disabled, or not present.
		} catch (UserRecoverableAuthException userRecoverableException) {
			// Unable to authenticate, but the user can fix this.
			// Forward the user to the appropriate activity.
			mActivity.startActivityForResult(
					userRecoverableException.getIntent(), mRequestCode);
		} catch (GoogleAuthException fatalException) {
			onError("Unrecoverable error " + fatalException.getMessage(),
					fatalException);
		}
		return null;
	}

	private String fetchNameFromProfileServer() throws IOException,
			JSONException {
		String token = fetchToken2();
		URL url = new URL(
				"https://www.googleapis.com/oauth2/v1/userinfo?access_token="
						+ token);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		int sc = con.getResponseCode();
		if (sc == 200) {
			InputStream is = con.getInputStream();
			GOOGLE_USER_DATA = readResponse(is);
			is.close();
			return GOOGLE_USER_DATA;
		} else if (sc == 401) {
			GoogleAuthUtil.invalidateToken(mActivity, token);
			onError("Server auth error, please try again.", null);

			return GOOGLE_USER_DATA;
		} else {
			onError("Server returned the following error code: " + sc, null);
			return GOOGLE_USER_DATA;
		}
	}

	/**
	 * Reads the response from the input stream and returns it as a string.
	 */
	private static String readResponse(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] data = new byte[2048];
		int len = 0;
		while ((len = is.read(data, 0, data.length)) >= 0) {
			bos.write(data, 0, len);
		}
		return new String(bos.toByteArray(), "UTF-8");
	}

}
